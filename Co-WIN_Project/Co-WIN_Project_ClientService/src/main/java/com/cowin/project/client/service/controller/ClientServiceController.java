package com.cowin.project.client.service.controller;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import com.cowin.project.client.service.appdata.ClientMainData;
import com.cowin.project.client.service.appdata.SessionData;
import com.cowin.project.client.service.dtos.StateDistrictDTO;
import com.cowin.project.client.service.dtos.StatesDTO;
import com.cowin.project.client.service.sessiondataservice.ClientDataService;
import com.cowin.project.client.service.sessiondataservice.ClientSessionService;
import org.json.*;

import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;


@RestController
@ControllerAdvice
@RefreshScope
@RequestMapping(value="ClientService")
public class ClientServiceController {

	/* Session Service handle, for API calls on SessionService */
	@Autowired
	private ClientSessionService clientSessionService;

	/* Data Service handle, for API calls on DataService */
	@Autowired
	private ClientDataService clientDataService;

	/* Build the RestTemplate for this service with necessary configurations */
	private RestTemplate restTemplate;

	@Autowired
	public ClientServiceController(RestTemplateBuilder builder) {
		this.restTemplate = builder
				.setConnectTimeout(Duration.ofSeconds(300))
				.setReadTimeout(Duration.ofSeconds(300))
				.build();
	}
	
	/* *
	 * Initialize Database with States and Districts data, use data service to update the data, that will be used by session service
	 * 1. Get data using session service for States and State District
	 * 2. Update Data to H2 database, using data service
	 * 3. Fetch data using data service for verification
	 * */
	public ResponseEntity<String> initializeDataService() {
		
		/*
			Raw check if the DB is already updated. 
			TODO: Need to modify this condition to check presence of all states and districts for each state
		*/ 
		if ((clientDataService.getAllStateNames_DataService(restTemplate) != null) && (clientDataService.getAllStateNames_DataService(restTemplate).stream().count() > 0))
			return new ResponseEntity<String>("All fine", HttpStatus.OK);

		//1. Get States list from Session Service
		List<StatesDTO> states_list = clientSessionService.getAllStates_SessionService(restTemplate);

		//2. Update states to database using Data Service
		ResponseEntity<String> states_post_response = clientDataService.updateStatesToDatabase(restTemplate, states_list);

		//We are successful in saving the districts to the database using Data Service
		if(states_post_response.getStatusCode() == HttpStatus.CREATED || states_post_response.getStatusCode() == HttpStatus.FOUND)
		{
			//3. Get Districts for each state from Session Service
			for(StatesDTO state: states_list) {

				//Get districts from data service and get the districts into a list using Session Service
				List<StateDistrictDTO> districts_list = clientSessionService.getDistrictsForState_SessionService(restTemplate, state.getState_id());

				//4. Update districts for the state to database using Data Service
				ResponseEntity<String> response_district_post = clientDataService.updateDistrictsForStateToDatabase(restTemplate, districts_list);
				if(response_district_post.getStatusCode() == HttpStatus.CREATED || response_district_post.getStatusCode() == HttpStatus.FOUND)
				{
					System.out.println(String.format("State and Districts, state id: %d, total number of districts: %d", state.getState_id(), districts_list.size()));
				}
			}
		}

		return new ResponseEntity<String>("All fine", HttpStatus.OK);
	}	

	@ModelAttribute
	public void addAttributes(Model model) {
	}

	@RequestMapping("/getSessions")
	public ModelAndView  getSessions(Model model) {

		//Initialize database using DataService
		initializeDataService();

		//Initially populate with all states
		ClientMainData loc_data = new ClientMainData();
		loc_data.setState_names(clientDataService.getAllStateNames_DataService(restTemplate));

		model.addAttribute("client_sessions_data", loc_data);

		//return "redirect:/sessions";
		return new ModelAndView("sessions");
	}


	/* API to get districts info, once a state is selected */
	@RequestMapping(value="/getInfo", method=RequestMethod.POST, params="action=districtsinfo")
	public ModelAndView getdistricts(@ModelAttribute ClientMainData model, BindingResult result, ModelMap modelmap)
	{
		ClientMainData data = model;

		//Get the first selected state
		int state_id = clientDataService.getStateIdForStateName(restTemplate, data.state_names.get(0));
		data.district_names = clientDataService.getDistrictNamesForState(restTemplate, state_id);

		modelmap.addAttribute("client_sessions_data", data);

		return new ModelAndView("sessions");
	}


	/* API to get sessions info, once state and district selected */
	@RequestMapping(value="/getInfo", method=RequestMethod.POST, params="action=sessionsinfo")
	public ModelAndView getSessions(@ModelAttribute ClientMainData model, BindingResult result, ModelMap modelmap)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String session_date = formatter.format(model.session_date);

		String district_name 		= model.district_names.get(0);
		Integer district_id 		= clientDataService.getDistrictIdForDistrictName(restTemplate, district_name);
		String available_sessions 	= clientSessionService.getSessionsForDistrict_SessionService(restTemplate, district_id, session_date);

		org.json.JSONObject obj = new JSONObject(available_sessions);
		JSONArray arr = obj.getJSONArray("sessions");

		for (int i = 0; i < arr.length(); i++)
		{
			SessionData session = new SessionData();

			session.center_id 		= arr.getJSONObject(i).getInt("center_id");
			session.center_name 	= arr.getJSONObject(i).getString("name");
			session.address 		= arr.getJSONObject(i).getString("address");
			session.state_name 		= arr.getJSONObject(i).getString("state_name");
			session.district_name	= arr.getJSONObject(i).getString("district_name");
			session.pincode 		= arr.getJSONObject(i).getInt("pincode");
			session.vaccine			= arr.getJSONObject(i).getString("vaccine");
			session.min_age_limit   = arr.getJSONObject(i).getInt("min_age_limit");
			session.avail_capacity	= arr.getJSONObject(i).getInt("available_capacity");
			
			model.sessions.add(session);
		}

		modelmap.addAttribute("client_sessions_data", model);

		return new ModelAndView("sessions");
	}
}
