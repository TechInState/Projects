package com.cowin.project.client.service.sessiondataservice;

import java.util.Arrays;
import java.util.List;

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
public class ClientDataService {
	
	//@Value("${dataservice.get_states_url_ds}")
	private String get_states_url_ds = "http://localhost:9192/api/DataService/getAllStates";
	
	//@Value("${dataservice.get_state_names_url_ds}")
	private String get_states_names_url_ds = "http://localhost:9192/api/DataService/getAllStateNames";
	
	//@Value("${dataservice.get_state_names_url_ds}")
	private String get_stateid_for_name_url_ds = "http://localhost:9192/api/DataService/getStateIdForStateName";
	
	//@Value("${dataservice.get_districts_url_ds}")
	private String get_districts_url_ds = "http://localhost:9192/api/DataService/getDistrictsForState";
	
	//@Value("${dataservice.get_districts_url_ds}")
	private String get_district_names_url_ds = "http://localhost:9192/api/DataService/getDistrictNamesForState";
	
	//@Value("${dataservice.get_districtid_names_url_ds}")
	private String get_districtid_for_name_url_ds = "http://localhost:9192/api/DataService/getDistrictIdForDistrictName";	
	
	//@Value("${dataservice.post_states_url_ds}")
	private final String post_states_url_ds = "http://localhost:9192/api/DataService/addStatesList";

	//@Value("${dataservice.post_districts_url_ds}")
	private final String post_districts_url_ds = "http://localhost:9192/api/DataService/addStateDistricts";


    public List<StatesDTO> getAllStates_DataService(RestTemplate restTemplate) {

    	ResponseEntity<StatesDTO[]> states_response = restTemplate.getForEntity(get_states_url_ds, StatesDTO[].class);

    	if(states_response.getStatusCode() == HttpStatus.FOUND) {
    		List<StatesDTO> states_list = Arrays.asList(states_response.getBody());
    		return states_list;
    	}

    	return null;
    }
    
    public List<String> getAllStateNames_DataService(RestTemplate restTemplate) {

    	try {
    		ResponseEntity<String[]> states_response = restTemplate.getForEntity(get_states_names_url_ds, String[].class);

    		if(states_response.getStatusCode() == HttpStatus.FOUND) {
    			List<String> states_list = Arrays.asList(states_response.getBody());
    			return states_list;
    		}
    	}
    	catch(Exception ex) {
    		System.out.println(ex.getMessage());
    	}

    	return null;
    }
    
    public Integer getStateIdForStateName(RestTemplate restTemplate, String state_name) {

		String state_id_url = String.format("%s%c%s", get_stateid_for_name_url_ds, '/' , state_name);

		ResponseEntity<Integer> state_id_response = restTemplate.getForEntity(state_id_url, Integer.class);

		if (state_id_response.getStatusCode() == HttpStatus.FOUND) {
			Integer state_id = state_id_response.getBody();
			return state_id;
		}

    	return null;
    }    

    public List<Integer> getDistrictIdsForState(RestTemplate restTemplate, int state_id) {

		String state_distric_url = String.format("%s%c%d", get_districts_url_ds, '/' , state_id);

		ResponseEntity<Integer[]> districts_response = restTemplate.getForEntity(state_distric_url, Integer[].class);

		if (districts_response.getStatusCode() == HttpStatus.FOUND) {

			//Get the districts into a list
			List<Integer> district_list = Arrays.asList(districts_response.getBody());
			
			return district_list;
		}

    	return null;
    }
    
    public List<String> getDistrictNamesForState(RestTemplate restTemplate, int state_id) {

		String state_distric_url = String.format("%s%c%d", get_district_names_url_ds, '/' , state_id);

		ResponseEntity<String[]> districts_response = restTemplate.getForEntity(state_distric_url, String[].class);

		if (districts_response.getStatusCode() == HttpStatus.FOUND) {

			//Get the districts into a list
			List<String> district_list = Arrays.asList(districts_response.getBody());
			
			return district_list;
		}

    	return null;
    }
    
    public Integer getDistrictIdForDistrictName(RestTemplate restTemplate, String district_name) {

		String district_id_url = String.format("%s%c%s", get_districtid_for_name_url_ds, '/' , district_name);

		ResponseEntity<Integer> district_id_response = restTemplate.getForEntity(district_id_url, Integer.class);

		if (district_id_response.getStatusCode() == HttpStatus.FOUND) {
			Integer district_id = district_id_response.getBody();
			return district_id;
		}

    	return null;
    }
    
    public ResponseEntity<String> updateStatesToDatabase(RestTemplate restTemplate, List<StatesDTO> states_list){    	
    	return restTemplate.postForEntity(post_states_url_ds, states_list.toArray(), String.class);    	
    }
    
    public ResponseEntity<String> updateDistrictsForStateToDatabase(RestTemplate restTemplate, List<StateDistrictDTO> districts_list){
    	return restTemplate.postForEntity(post_districts_url_ds, districts_list.toArray(), String.class);
    }
}
