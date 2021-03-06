package com.cowin.project.data.service.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cowin.project.data.service.dataservice.DataService;
import com.cowin.project.data.service.dataservice.DataServiceImpl;
import com.cowin.project.data.service.dtos.StateDistrictDTO;
import com.cowin.project.data.service.dtos.StatesDTO;
import com.cowin.project.data.service.entities.StateDistrictEntity;
import com.cowin.project.data.service.entities.StatesEntity;

@RestController
@RequestMapping("/api/DataService")
public class DataServiceController {
	
	@Autowired
	private DataService dataService;
	
	/*
	public DataServiceController(DataServiceImpl service) {
		this.dataService = service;
	}
	*/
	
	@Autowired
	private ModelMapper modelMapper;

    @GetMapping(value = "/getAllStates", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatesDTO[]> getAllStates() {

    	if (dataService.getAllStates().size() > 0)
    	{
    		// Send the DTO List converted from Entity
    		List<StatesDTO> listStates = dataService.getAllStates().stream().map(state -> modelMapper.map(state, StatesDTO.class))
    				.collect(Collectors.toList());

    		// Remove nulls before sending
    		return new ResponseEntity<StatesDTO[]>(listStates.stream()
    				.filter(Objects::nonNull)
    				.toArray(StatesDTO[]::new), HttpStatus.FOUND);
    	}
    	else
    		return new ResponseEntity<StatesDTO[]>(new ArrayList<StatesDTO>().toArray(StatesDTO[]::new), HttpStatus.NOT_FOUND);

    }
    
    @GetMapping(value = "/getAllStateNames", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String[]> getAllStateNames() {

    	if (dataService.getAllStates().size() > 0)
    	{
    		// Send the DTO List converted from Entity
    		List<String> listStates = dataService.getAllStateNames();

    		// Remove nulls before sending
    		return new ResponseEntity<String[]>(listStates.stream().filter(Objects::nonNull).toArray(String[]::new), HttpStatus.FOUND);
    	}
    	else
    		return new ResponseEntity<String[]>(new ArrayList<String>().toArray(String[]::new), HttpStatus.NOT_FOUND);
    }
    
    @GetMapping(value = "/getStateIdForStateName/{state_name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getStateIdForStateName(@PathVariable("state_name") String state_name) throws InterruptedException, ExecutionException {
    	Optional<Integer> state_id = dataService.getStateIdByName(state_name);
		return new ResponseEntity<Integer>(state_id.get(), HttpStatus.FOUND);
    }

    @GetMapping(value = "/getAllStateDistricts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StateDistrictDTO[]> getAllStateDistricts() {

    	// Send the DTO List converted from Entity
    	List<StateDistrictDTO> listStateDistricts = dataService.getAllStatesDistricts().stream().map(state -> modelMapper.map(state, StateDistrictDTO.class))
    			.collect(Collectors.toList());

    	if (listStateDistricts.size() > 0) {
    		// Remove nulls before sending
    		return new ResponseEntity<StateDistrictDTO[]>(listStateDistricts.stream()
    				.filter(Objects::nonNull)
    				.toArray(StateDistrictDTO[]::new), HttpStatus.FOUND);
    	}
    	else
    		return new ResponseEntity<StateDistrictDTO[]>(new ArrayList<StateDistrictDTO>().toArray(StateDistrictDTO[]::new), HttpStatus.NOT_FOUND);
    }
    
    @GetMapping(value = "/getDistrictsForState/{state_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer[]> getDistrictsForState(@PathVariable("state_id") int state_id) throws InterruptedException, ExecutionException {

    	// Get districts for the state from database
    	List<Integer> list_districts = dataService.getDistrictIdsByStateId(state_id);
    	
		// Remove nulls before sending
		return new ResponseEntity<Integer[]>(list_districts.stream()
				.filter(Objects::nonNull)
				.toArray(Integer[]::new), HttpStatus.FOUND);
    }    
    
    @GetMapping(value = "/getDistrictNamesForState/{state_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String[]> getDistrictNamesForState(@PathVariable("state_id") int state_id) throws InterruptedException, ExecutionException {

    	// Get districts for the state from database
    	List<String> list_districts = dataService.getDistrictNamesByStateId(state_id);
    	
		// Remove nulls before sending
		return new ResponseEntity<String[]>(list_districts.stream()
				.filter(Objects::nonNull)
				.toArray(String[]::new), HttpStatus.FOUND);
    }
    
    @GetMapping(value = "/getDistrictIdForDistrictName/{district_name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getDistrictIdForDistrictName(@PathVariable("district_name") String district_name) throws InterruptedException, ExecutionException {
    	Optional<Integer> district_id = dataService.getDistrictIdByDistrictName(district_name);
		return new ResponseEntity<Integer>(district_id.get(), HttpStatus.FOUND);
    }    

    @PostMapping(value="addState", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> addState(@RequestBody StatesDTO stateDTO) {
    	
    	// Convert DTO to Entity
    	StatesEntity state = modelMapper.map(stateDTO, StatesEntity.class);

    	Long id = dataService.addToStates(state);

        return new ResponseEntity<String>(String.format("State %s successfully added with Id: %d", state.getState_name(), id), HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/addStatesList", method = RequestMethod.POST)    
    @Transactional(timeout = 300)
    public ResponseEntity<String> addStatesList(@RequestBody StatesDTO[] states) {
    	
    	System.out.println(String.format("Total states received: %d", states.length));
    	
    	//Check if we have already updated states in the database
    	if (dataService.getAllStates().stream().count() <= 0)
    	{
    		for(StatesDTO state: states)
    		{
    			// Convert DTO to Entity
    			StatesEntity stateEntity = modelMapper.map(state, StatesEntity.class);
    			Long id = dataService.addToStates(stateEntity);
    			System.out.println(String.format("Data Added for State: %d, %s", stateEntity.getState_id(), stateEntity.getState_name()));
    		}

    		System.out.println(String.format("All states added: %d", states.length));

    		return new ResponseEntity<String>(String.format("States successfully added with count: %d",  states.length), HttpStatus.CREATED);
    	}
    	else
    		return new ResponseEntity<String>(String.format("States already added"), HttpStatus.CREATED);
    }    
    
    @RequestMapping(value = "/addStateDistricts", method = RequestMethod.POST)
    public ResponseEntity<String> addStateDistricts(@RequestBody StateDistrictDTO[] districts) throws InterruptedException, ExecutionException {
    	
    	System.out.println(String.format("Total districts received: %d", districts.length));
    	
    	for(StateDistrictDTO district: districts)
    	{
        	// Convert DTO to Entity
        	StateDistrictEntity state_district = modelMapper.map(district, StateDistrictEntity.class);
        	//if (dataService.getDistrictIdsByStateId(state_district.getState_id()).stream().count() <= 0)
        	{
        		Long id = dataService.addToStateDistrict(state_district);

        		System.out.println(String.format("District data added for state: %d, %s with id: %d", state_district.getDistrict_id(), 
        				state_district.getDistrict_name(), id));
        	}
    	}

    	return new ResponseEntity<String>(String.format("Districts successfully added with count: %d",  districts.length), HttpStatus.CREATED);
    }
}
