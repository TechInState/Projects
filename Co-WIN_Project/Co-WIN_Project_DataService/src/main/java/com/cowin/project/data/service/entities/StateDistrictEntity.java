package com.cowin.project.data.service.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

/*
 * Districts in States
 * 
*/

@Entity
@Table(name="states_districts", uniqueConstraints = {@UniqueConstraint(columnNames = {"district_id"})})
public class StateDistrictEntity {
	
    public StateDistrictEntity() {
		super();
		this.creationTime = LocalDateTime.now();
		this.modificationTime = LocalDateTime.now();
	}

	public StateDistrictEntity(Long id, long version, LocalDateTime creationTime, LocalDateTime modificationTime,
			Integer state_id, Integer district_id, String district_name) {
		super();
		this.id = id;
		this.version = version;
		this.creationTime = LocalDateTime.now();
		this.modificationTime = LocalDateTime.now();
		this.state_id = state_id;
		this.district_id = district_id;
		this.district_name = district_name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}

	public LocalDateTime getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(LocalDateTime modificationTime) {
		this.modificationTime = modificationTime;
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

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Version
    private long version;
 
    @Column(name = "creation_time", nullable = false)
    private LocalDateTime creationTime;
    
    
    @Column(name = "modification_time")
    private LocalDateTime modificationTime;
 
    @Column(name = "state_id", nullable = false)
    private Integer state_id;
    
    @Column(name = "district_id")
    private Integer district_id;
    
    @Column(name = "district_name", length = 500)
    private String district_name;

}
