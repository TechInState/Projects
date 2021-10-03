package com.cowin.project.data.service.repositories;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import com.cowin.project.data.service.entities.StateDistrictEntity;

@Repository
public interface StateDistrictRepository extends BaseRepository<StateDistrictEntity, Serializable> {

	@Async
    @Query("SELECT s.district_id FROM StateDistrictEntity s where s.state_id = :state_id")
    Future<List<Integer>> findDistrictIdsByStateId(@Param("state_id") Integer state_id);

	@Async
    @Query("SELECT s.district_name FROM StateDistrictEntity s where s.state_id = :state_id")
    Future<List<String>> findDistrictNamesByStateId(@Param("state_id") Integer state_id);

	@Async
    @Query("SELECT s.district_name FROM StateDistrictEntity s where s.district_id = :district_id")
    Future<Optional<String>> findDistrictNameByDistrictId(@Param("district_id") Integer district_id);
	
	@Async
    @Query("SELECT s.district_id FROM StateDistrictEntity s where s.district_name = :district_name")
    Future<Optional<Integer>> findDistrictIdByDistrictName(@Param("district_name") String district_name);
	
	Optional<StateDistrictEntity> findById(Long id);
}
