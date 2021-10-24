package com.cowin.project.data.service.dataservice;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cowin.project.data.service.entities.StateDistrictEntity;
import com.cowin.project.data.service.entities.StatesEntity;
import com.cowin.project.data.service.repositories.StateDistrictRepository;
import com.cowin.project.data.service.repositories.StateRepository;

@Transactional
@Service
public class DataServiceImpl implements DataService {
	
	@Autowired
	private StateRepository states_repository;
	
	@Autowired
	private StateDistrictRepository state_dist_repository;
	
	/* Search/Find operations */
	
	
	public List<StatesEntity> getAllStates() {
		List<StatesEntity> states =  states_repository.findAll();
		return states;
	}
	
	@Override
	public List<String> getAllStateNames() {
		return states_repository.findAllStateNames();
	}	

	@Override
	public Optional<Integer> getStateIdByName(String state_name) throws InterruptedException, ExecutionException {
		return states_repository.findStateIdByStateName(state_name).get();
	}
	
	@Override
	public Optional<String> getStateNameById(Integer state_id) throws InterruptedException, ExecutionException {
		return states_repository.findStateNameByStateId(state_id).get();
	}
	
	@Override
	public List<StateDistrictEntity> getAllStatesDistricts(){
		return state_dist_repository.findAll();
	}

	@Override
	public List<Integer> getDistrictIdsByStateId(Integer state_id) throws InterruptedException, ExecutionException {
		return state_dist_repository.findDistrictIdsByStateId(state_id).get();
	}
	
	@Override
	public List<String> getDistrictNamesByStateId(Integer state_id) throws InterruptedException, ExecutionException {
		return state_dist_repository.findDistrictNamesByStateId(state_id).get();
	}	
	
	@Override
	public Optional<String> getDistrictNameByDistrictId(Integer district_id) throws InterruptedException, ExecutionException {
		return state_dist_repository.findDistrictNameByDistrictId(district_id).get();
	}
	
	@Override
	public Optional<Integer> getDistrictIdByDistrictName(String district_name) throws InterruptedException, ExecutionException {
		return state_dist_repository.findDistrictIdByDistrictName(district_name).get();
	}
	
	
	/* Add/Save and Delete operations */

	@Override
	public Long addToStates(StatesEntity states) {
		return states_repository.save(states).getId();
	}

	@Override
	public Long addToStateDistrict(StateDistrictEntity statedistrict) {
		return state_dist_repository.save(statedistrict).getId();
	}

	@Override
	public void deleteAllStates() {
		states_repository.deleteAllStates();
	}

	@Override
	public void deleteAllStateDistricts() {
		state_dist_repository.deleteAllStateDistricts();
	}
}
