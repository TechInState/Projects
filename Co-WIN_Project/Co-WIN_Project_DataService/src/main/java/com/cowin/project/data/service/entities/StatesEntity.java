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

@Entity
@Table(name="states", uniqueConstraints = {@UniqueConstraint(columnNames = {"state_id"})})
public class StatesEntity {
	
    public StatesEntity() {
		super();
		this.creationTime = LocalDateTime.now();
		this.modificationTime = LocalDateTime.now();		
	}

	public StatesEntity(Long id, long version, LocalDateTime creationTime, LocalDateTime modificationTime,
			Integer state_id, String state_name) {
		super();
		this.id = id;
		this.version = version;
		this.creationTime = LocalDateTime.now();
		this.modificationTime = LocalDateTime.now();
		this.state_id = state_id;
		this.state_name = state_name;
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

	public String getState_name() {
		return state_name;
	}

	public void setState_name(String state_name) {
		this.state_name = state_name;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Version
    private long version;
 
    @Column(name = "creation_time", nullable = false)
    //@Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentZonedDateTime")
    //private ZonedDateTime creationTime;
    private LocalDateTime creationTime;
    
    @Column(name = "modification_time")
    private LocalDateTime modificationTime;
    
    @Column(name = "state_id", nullable = false)
    private Integer state_id;
 
    @Column(name = "state_name", length = 500)
    private String state_name;
}
