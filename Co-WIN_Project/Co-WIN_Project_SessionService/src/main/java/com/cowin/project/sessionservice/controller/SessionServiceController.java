package com.cowin.project.sessionservice.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cowin.project.sessionservice.dtos.StateDistrictDTO;
import com.cowin.project.sessionservice.dtos.StatesDTO;

import org.json.*;

/*
base = datetime.datetime.today().date()
#URL = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id={}&date={}".format(411057, base)
URL_STATES = "https://cdn-api.co-vin.in/api/v2/admin/location/states"
URL_DISTRICT = "https://cdn-api.co-vin.in/api/v2/admin/location/districts/{}".format(21)
# "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?pincode=110010&date=06-05-2021"
URL_GETSESBYPIN = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?pincode={}&date={}".format(411057, "06-05-2021")
#https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByDistrict?district_id=140&date=06-05-2021
URL_GETSESBYDISTRICT = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByDistrict?district_id={}&date={}".format(395, "10-10-2021")
*/


@RestController
@RefreshScope
@RequestMapping(value="/api/SessionService")
public class SessionServiceController {
	
	@Value("${cowin.states_url}")
	private String states_url;
	
	@Value("${cowin.districts_url}")
	private String districts_url;
	
	@Value("${cowin.sessionbypin_url}")
	private String sessionsbypin_url;
	
	@Value("${cowin.sessionbydistrict_url}")
	private String sessionsbydistrict_url;
	
	private static final ExecutorService executorService = Executors.newFixedThreadPool(5);
	
	private static final HttpClient httpClient = HttpClient.newBuilder()
				.executor(executorService)
	            .version(HttpClient.Version.HTTP_2)
	            .connectTimeout(Duration.ofSeconds(10))
	            .build();
	
	private ResponseEntity<String> getForSession(String for_url){

		java.net.http.HttpRequest req = HttpRequest.newBuilder(URI.create(for_url))
				.GET()
				.build();

		CompletableFuture<HttpResponse<String>> response = httpClient.sendAsync(req, BodyHandlers.ofString());

		try {
			switch(response.get().statusCode()) {
			case 400:
				System.out.println("Bad Request");
				return new ResponseEntity<String>("Error: " + "Bad Request", HttpStatus.BAD_REQUEST);
			case 401:
				System.out.println("Unauthenticated access");
				return new ResponseEntity<String>("Error: " + "Unauthenticated access", HttpStatus.UNAUTHORIZED);
			case 500:
				System.out.println("Internal Server Error");
				return new ResponseEntity<String>("Error: " + "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
			case 200: //Success
				String resultJSON = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
				return new ResponseEntity<String>(resultJSON, HttpStatus.OK);
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (TimeoutException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<String>("No Content", HttpStatus.NO_CONTENT);
	}
	
	@GetMapping(value = "/getStates", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StatesDTO[]> getStates() throws IOException{

		ResponseEntity<String> response = getForSession(states_url);

		List<StatesDTO> states_list = new ArrayList<StatesDTO>();

		if(response.getStatusCode() == HttpStatus.OK)
		{
			//send a copy of of data in in-memory database/cache
			String states_data_json = response.getBody();
			System.out.println(states_data_json);

			JSONObject obj = new JSONObject(states_data_json);
			//Integer ttl = obj.getInt("ttl");

			JSONArray arr = obj.getJSONArray("states");
			for (int i = 0; i < arr.length(); i++)
			{
			    Integer state_id = arr.getJSONObject(i).getInt("state_id");
			    String state_name = arr.getJSONObject(i).getString("state_name");
			    
			    System.out.println("state_id: " + state_id.toString());
			    System.out.println("state_name: " + state_name);
			    
			    states_list.add(new StatesDTO(state_id, state_name));
			}
		}

		return new ResponseEntity<StatesDTO[]>(states_list.toArray(StatesDTO[]::new), HttpStatus.OK);
	}

	@GetMapping(value="/getDistrictsForState/{state_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<StateDistrictDTO[]> getDistricts(@PathVariable("state_id") int state_id){
		
		String state_district_url = String.format("%s%c%d", districts_url, '/', state_id);

		ResponseEntity<String> response = getForSession(state_district_url);

		List<StateDistrictDTO> state_districts_list = new ArrayList<StateDistrictDTO>();

		if(response.getStatusCode() == HttpStatus.OK)
		{
			//send a copy of of data in in-memory database/cache
			String states_data_json = response.getBody();
			System.out.println(states_data_json);

			JSONObject obj = new JSONObject(states_data_json);

			JSONArray arr = obj.getJSONArray("districts");
			for (int i = 0; i < arr.length(); i++)
			{
			    Integer district_id = arr.getJSONObject(i).getInt("district_id");
			    String district_name = arr.getJSONObject(i).getString("district_name");
			    
			    System.out.println("district_id: " + district_id.toString());
			    System.out.println("district_name: " + district_name);
			    
			    state_districts_list.add(new StateDistrictDTO(state_id, district_id, district_name));
			}
		}

		return new ResponseEntity<StateDistrictDTO[]>(state_districts_list.toArray(StateDistrictDTO[]::new), HttpStatus.OK);
	}

	@GetMapping(value="/getSessionsForPin/{pin_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getSessionsForPin(@PathVariable("pin_id") int pin_id){

		return new ResponseEntity<String>("sessionsbypin_url = " + sessionsbypin_url, HttpStatus.OK);
	}

	@GetMapping(value="/getSessionsForDistrict", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getSessionsForDistrict(@RequestParam(name = "district_id") int district_id, 
			@RequestParam(name = "date") String date){
		
		//http://localhost:9191/session/getDistrictsForState?district_id=1&date=04-10-2021

		String session_district_url = String.format("%s?district_id=%d&date=%s", sessionsbydistrict_url, district_id, date);
		
		//https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByDistrict/104-10-2021
		//https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByDistrict?district_id={}&date={}".format(395, "10-10-2021")
		//https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByDistrict?district_id=395&date=04-10-2021

		ResponseEntity<String> response = getForSession(session_district_url);

		if(response.getStatusCode() == HttpStatus.OK)
		{
			return new ResponseEntity<String>(response.getBody(), HttpStatus.OK);
		}

		return new ResponseEntity<String>("sessionsbydistrict_url = " + sessionsbypin_url, HttpStatus.OK);
	}
}
