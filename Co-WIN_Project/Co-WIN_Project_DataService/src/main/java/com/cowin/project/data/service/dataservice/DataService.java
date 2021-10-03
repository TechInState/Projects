package com.cowin.project.data.service.dataservice;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.cowin.project.data.service.entities.StateDistrictEntity;
import com.cowin.project.data.service.entities.StatesEntity;

public interface DataService {
	
	/*read operations*/
	
	/* States Entity*/
	public List<StatesEntity> getAllStates();	
	public Optional<Integer> getStateIdByName(String state_name) throws InterruptedException, ExecutionException;	
	public Optional<String> getStateNameById(Integer state_id) throws InterruptedException, ExecutionException;
	
	
	public List<StateDistrictEntity> getAllStatesDistricts();	
	public List<Integer> getDistrictIdsByStateId(Integer state_id) throws InterruptedException, ExecutionException;	
	public Optional<String> getDistrictNameByDistrictId(Integer district_id) throws InterruptedException, ExecutionException;	
	public Optional<Integer> getDistrictIdByDistrictName(String district_name) throws InterruptedException, ExecutionException;
	
	
	/* Add or Save operations */
	
	/* StateDistrict Entity*/
	public Long addToStates(StatesEntity states);
	public Long addToStateDistrict(StateDistrictEntity statedistrict);

}
