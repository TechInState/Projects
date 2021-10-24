package com.cowin.project.client.service.appdata;

public class SessionData {
	
    public Integer 	center_id;
    public String 	center_name;
    public String 	address;
    public String 	state_name;
    public String 	district_name;
    public Integer 	pincode;
    public String 	vaccine;
    public Integer 	min_age_limit;
    public Integer 	avail_capacity;

    public SessionData() {
	}

	public SessionData(Integer center_id, String center_name, String address, String state_name, String district_name, Integer pincode,
			String vaccine, Integer min_age_limit, Integer avail_capacity) {
		
		this.center_id = center_id;
		this.center_name = center_name;
		this.address = address;
		this.state_name = state_name;
		this.district_name = district_name;
		this.pincode = pincode;
		this.vaccine = vaccine;
		this.min_age_limit = min_age_limit;
		this.avail_capacity = avail_capacity;
	}   
    
	public Integer getCenter_id() {
		return center_id;
	}
	public void setCenter_id(Integer center_id) {
		this.center_id = center_id;
	}
	public String getCenter_name() {
		return center_name;
	}
	public void setCenter_name(String center_name) {
		this.center_name = center_name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getState_name() {
		return state_name;
	}
	public String getDistrict_name() {
		return district_name;
	}
	public void setDistrict_name(String district_name) {
		this.district_name = district_name;
	}	
	public void setState_name(String state_name) {
		this.state_name = state_name;
	}
	public Integer getPincode() {
		return pincode;
	}
	public void setPincode(Integer pincode) {
		this.pincode = pincode;
	}
	public String getVaccine() {
		return vaccine;
	}
	public void setVaccine(String vaccine) {
		this.vaccine = vaccine;
	}
	public Integer getMin_age_limit() {
		return min_age_limit;
	}
	public void setMin_age_limit(Integer min_age_limit) {
		this.min_age_limit = min_age_limit;
	}
	public Integer getAvail_capacity() {
		return avail_capacity;
	}
	public void setAvail_capacity(Integer avail_capacity) {
		this.avail_capacity = avail_capacity;
	}
}
