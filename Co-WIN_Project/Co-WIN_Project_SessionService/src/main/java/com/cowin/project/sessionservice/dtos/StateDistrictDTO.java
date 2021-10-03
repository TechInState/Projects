package com.cowin.project.sessionservice.dtos;

public class StateDistrictDTO {
	
	public StateDistrictDTO() {

	}

	public StateDistrictDTO(Integer state_id, Integer district_id, String district_name) {
		this.state_id = state_id;
		this.district_id = district_id;
		this.district_name = district_name;
	}
	
	public Integer getState_id() {
		return state_id;
	}
	public void setState_id(Integer state_id) {
		this.state_id = state_id;
	}
	public Integer getDistrict_id() {
		return district_id;
	}
	public void setDistrict_id(Integer district_id) {
		this.district_id = district_id;
	}
	public String getDistrict_name() {
		return district_name;
	}
	public void setDistrict_name(String district_name) {
		this.district_name = district_name;
	}
	
	private Integer state_id;
	private Integer district_id;
	private String  district_name;

}
