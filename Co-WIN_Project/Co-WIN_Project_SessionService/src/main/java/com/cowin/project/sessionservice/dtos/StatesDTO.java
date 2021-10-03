package com.cowin.project.sessionservice.dtos;

public class StatesDTO {
	
	public StatesDTO(Integer state_id, String state_name) {
		this.state_id = state_id;
		this.state_name = state_name;
	}
	public Integer getState_id() {
		return state_id;
	}
	public void setState_id(Integer state_id) {
		this.state_id = state_id;
	}
	public String getState_name() {
		return state_name;
	}
	public void setState_name(String state_name) {
		this.state_name = state_name;
	}

	private Integer state_id;
	private String  state_name;

}
