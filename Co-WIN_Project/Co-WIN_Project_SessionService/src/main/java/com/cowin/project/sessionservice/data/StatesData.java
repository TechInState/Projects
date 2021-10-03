package com.cowin.project.sessionservice.data;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "states")
public class StatesData {
	
	public Integer state_id;
	public String state_name;
	
	/*
	private List<Map<String, Integer>> lstStates_Id;
	private Integer ttl;
	
	public List<Map<String, Integer>> getLstStates_Id() {
		return lstStates_Id;
	}
	public void setLstStates_Id(List<Map<String, Integer>> lstStates_Id) {
		this.lstStates_Id = lstStates_Id;
	}
	
	public Integer getTtl() {
		return ttl;
	}
	public void setTtl(Integer ttl) {
		this.ttl = ttl;
	}
	*/
	
}
