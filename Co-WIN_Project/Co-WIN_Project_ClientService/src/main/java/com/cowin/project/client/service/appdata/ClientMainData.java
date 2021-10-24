package com.cowin.project.client.service.appdata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

import com.cowin.project.client.service.dtos.StateDistrictDTO;
import com.cowin.project.client.service.dtos.StatesDTO;

public class ClientMainData implements Serializable {

	private static final long serialVersionUID = 1L;

	public List<StatesDTO> states;

	//For form mapping
	public List<String> state_names;
	//Maintain the map for quicker access to id for state name from the form
	public Map<String, Integer> map_state_name_id;

	public List<StateDistrictDTO> districts;
	public List<String> district_names;
	public List<SessionData> sessions;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
    public Date session_date;

	public ClientMainData() {
		state_names 		= new ArrayList<String>();
		district_names 		= new ArrayList<String>();
		map_state_name_id 	= new HashMap<String, Integer>();
		session_date 		= new Date();
		sessions			= new ArrayList<>();
	}
	
	public ClientMainData(List<StatesDTO> states, List<StateDistrictDTO> districts, List<String> state_names, 
			List<String> district_names, Date session_date, List<SessionData> sessions) {
		this.states 		= states;
		this.districts 		= districts;
		this.state_names 	= state_names;
		this.district_names = district_names;
		this.session_date 	= session_date;
		this.sessions		= sessions;
	}

	/* Getters and Setters */

	public List<StatesDTO> getStates() {
		return states;
	}
	public void setStates(List<StatesDTO> states) {
		this.states = states;
	}
	
	public List<String> getState_names() {
		return state_names;
	}

	public void setState_names(List<String> state_names) {
		this.state_names = state_names;
	}
	
	public void add_StateName(String state_name) {
		state_names.add(state_name);
	}
	
	public void add_StateName_Id(String state_name, Integer Id) {
		map_state_name_id.put(state_name, Id);
	}

	public Integer get_StateIdForName(String state_name) {
		return map_state_name_id.get(state_name);
	}	

	public List<StateDistrictDTO> getDistricts() {
		return districts;
	}
	
	public void setDistricts(List<StateDistrictDTO> districts) {
		this.districts = districts;
	}
	
	public List<String> getDistrict_names() {
		return district_names;
	}

	public void setDistrict_names(List<String> district_names) {
		this.district_names = district_names;
	}
	
	public Date getSession_date() {
		return session_date;
	}

	public void setSession_date(Date session_date) {
		this.session_date = session_date;
	}
	
    public List<SessionData> getSessions() {
		return sessions;
	}

	public void setSessions(List<SessionData> sessions) {
		this.sessions = sessions;
	}	
}
