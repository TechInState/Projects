package com.cowin.project.client.service.sessiondataservice;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import com.cowin.project.client.service.dtos.StateDistrictDTO;
import com.cowin.project.client.service.dtos.StatesDTO;

@Service
@RefreshScope
public class ClientSessionService {
	
	//@Value("${sessionservice.get_states_url_ss}")
	private final String get_states_url_ss = "http://localhost:9191/api/SessionService/getStates";
	
	//@Value("${sessionservice.get_districts_url_ss}")
	private final String get_districts_url_ss = "http://localhost:9191/api/SessionService/getDistrictsForState";

	//@Value("${sessionservice.sessionbydistrict_url}")
	private final String get_sessions_district_url = "http://localhost:9191/api/SessionService/getSessionsForDistrict";	

    public List<StatesDTO> getAllStates_SessionService(RestTemplate restTemplate) {

    	ResponseEntity<StatesDTO[]> states_response = restTemplate.getForEntity(get_states_url_ss, StatesDTO[].class);

    	if(states_response.getStatusCode() == HttpStatus.OK) {
    		List<StatesDTO> states_list = Arrays.asList(states_response.getBody());
    		return states_list;
    	}

    	return null;
    }

    public List<StateDistrictDTO> getDistrictsForState_SessionService(RestTemplate restTemplate, int state_id) {
		//Format the URL
		String state_distric_url = String.format("%s%c%d", get_districts_url_ss, '/', state_id);

		//Get districts from data service
		ResponseEntity<StateDistrictDTO[]> districts_response = restTemplate.getForEntity(state_distric_url, StateDistrictDTO[].class);
		if (districts_response.getStatusCode() == HttpStatus.OK) {

			//Get the districts into a list
			List<StateDistrictDTO> district_list = Arrays.asList(districts_response.getBody());

			return district_list;
		}

    	return null;
    }
    
    public String getSessionsForDistrict_SessionService(RestTemplate restTemplate, int district_id, String session_date) {

		//Format the URL
		String sessions_district_url = String.format("%s%s%d%s%s", get_sessions_district_url, "?district_id=", district_id, "&date=", session_date);

		//Get districts from data service
		ResponseEntity<String> districts_response = restTemplate.getForEntity(sessions_district_url, String.class);
		if (districts_response.getStatusCode() == HttpStatus.OK) {
			return districts_response.getBody();
		}

    	return null;
    }
}
