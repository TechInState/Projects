package com.cowin.project.client.service.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.cowin.project.client.service.dtos.StateDistrictDTO;
import com.cowin.project.client.service.dtos.StatesDTO;
import com.cowin.project.client.service.sessiondataservice.ClientDataService;
import com.cowin.project.client.service.sessiondataservice.ClientSessionService;

import org.springframework.ui.Model;

@RestController
@RefreshScope
public class ClientServiceController {
	
	//@Value("${sessionservice.get_states_url_ss}")
	private String get_states_url_ss = "http://localhost:9191/session/getStates";
	
	//@Value("${sessionservice.get_districts_url_ss}")
	private String get_districts_url_ss = "http://localhost:9191/session/getDistrictsForState";

	//@Value("${dataservice.get_states_url_ds}")
	private String get_states_url_ds = "http://localhost:9192/api/dataservice/getAllStates";
	
	//@Value("${dataservice.get_districts_url_ds}")
	private String get_districts_url_ds = "http://localhost:9192/api/dataservice/getDistrictsForState";
	
	//@Value("${dataservice.post_states_url_ds}")
	private String post_states_url_ds = "http://localhost:9192/api/dataservice/addStatesList";
	
	//@Value("${dataservice.post_districts_url_ds}")
	private String post_districts_url_ds = "http://localhost:9192/api/dataservice/addStateDistricts";
	
	/*
	@Value("${cowin.sessionbypin_url}")
	private String sessionsbypin_url;
	
	@Value("${cowin.sessionbydistrict_url}")
	private String sessionsbydistrict_url;
	*/	

	@Autowired
	private ClientDataService clientDataService;

	@Autowired
	private ClientSessionService clientSessionService;

	//Build the RestTemplate for this service with necessary configurations
	private RestTemplate restTemplate;
	@Autowired
	public ClientServiceController(RestTemplateBuilder builder) {
	    this.restTemplate = builder
	    		.setConnectTimeout(Duration.ofSeconds(300))
	            .setReadTimeout(Duration.ofSeconds(300))
	    		.build();
	}


    /* *
     * Initialize States and State District data, use data service to update the data, that will be used by session service
     * 1. Get data using session service for States and State District
     * 2. Update Data to H2 database, using data service
     * 3. Fetch data using data service for verification
     * */
    @GetMapping(value = "clientservice/initialize_data", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> initialize_data() {

    	//1. Get States list from Session Service
    	ResponseEntity<StatesDTO[]> states_response = restTemplate.getForEntity(get_states_url_ss, StatesDTO[].class);
    	if(states_response.getStatusCode() == HttpStatus.OK)
    	{
    		//Get the list from the response
    		List<StatesDTO> states_list = Arrays.asList(states_response.getBody());

    		//2. Update states to database using Data Service
    		ResponseEntity<String> states_post_response = restTemplate.postForEntity(post_states_url_ds, states_response.getBody(), String.class);
    		
    		//We are successful in saving the districts to the database
    		if(states_post_response.getStatusCode() == HttpStatus.CREATED || states_post_response.getStatusCode() == HttpStatus.FOUND)
    		{
            	//3. Get Districts for each state from Session Service
            	for(StatesDTO state: states_list) {

            		//Format the URL
            		String state_distric_url = String.format("%s%c%d", get_districts_url_ss, '/' ,state.getState_id());

            		//Get districts from data service
            		ResponseEntity<StateDistrictDTO[]> districts_response = restTemplate.getForEntity(state_distric_url, StateDistrictDTO[].class);
            		if (districts_response.getStatusCode() == HttpStatus.OK) {

            			//Get the districts into a list
            			List<StateDistrictDTO> district_list = Arrays.asList(districts_response.getBody());

            			ResponseEntity<String> response_district_post = restTemplate.postForEntity(post_districts_url_ds, districts_response.getBody(), String.class);
            			if(response_district_post.getStatusCode() == HttpStatus.CREATED || response_district_post.getStatusCode() == HttpStatus.FOUND)
            			{
            				System.out.println(String.format("State and Districts, state id: %d, total number of districts: %d", state.getState_id(), district_list.size()));
            			}
            		}
            	}
    		}
    	}

    	return new ResponseEntity<String>("All fine", HttpStatus.OK);
    }

    @GetMapping(value = "clientservice/get_all_states_ds", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatesDTO[]> get_all_states_ds() {

    	ResponseEntity<StatesDTO[]> states_response = restTemplate.getForEntity(get_states_url_ds, StatesDTO[].class);

    	if (states_response.getStatusCode() == HttpStatus.FOUND) {
    		return new ResponseEntity<StatesDTO[]>(states_response.getBody(), HttpStatus.OK);
    	}

    	return null;
    }    

    @GetMapping(value = "clientservice/get_district_ids_for_state_ds/{state_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer[]> get_district_Ids_for_state_ds(@PathVariable("state_id") int state_id) {

    	ResponseEntity<Integer[]> districts_response = null;
		
		String state_distric_url = String.format("%s%c%d", get_districts_url_ds, '/' , state_id);

    	districts_response = restTemplate.getForEntity(state_distric_url, Integer[].class);

    	if (districts_response.getStatusCode() == HttpStatus.OK) {
    		return new ResponseEntity<Integer[]>(districts_response.getBody(), HttpStatus.OK);
    	}

    	return null;
    }
}
