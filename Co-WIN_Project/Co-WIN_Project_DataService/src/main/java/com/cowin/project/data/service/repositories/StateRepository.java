package com.cowin.project.data.service.repositories;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cowin.project.data.service.entities.StatesEntity;

@Repository
public interface StateRepository extends BaseRepository<StatesEntity, Serializable>{

	@Async
    @Query("SELECT state_name FROM StatesEntity")
    List<String> findAllStateNames();
    
	@Async
    @Query("SELECT s.state_name FROM StatesEntity s where s.state_id = :state_id") 
    Future<Optional<String>> findStateNameByStateId(@Param("state_id") Integer state_id);    

	@Async
    @Query("SELECT s.state_id FROM StatesEntity s where s.state_name = :state_name")
	Future<Optional<Integer>> findStateIdByStateName(@Param("state_name") String state_name);
	
    Optional<StatesEntity> findById(Long id);
	
    @Transactional
    @Modifying
    @Query("DELETE FROM StatesEntity")
	void deleteAllStates();
}
