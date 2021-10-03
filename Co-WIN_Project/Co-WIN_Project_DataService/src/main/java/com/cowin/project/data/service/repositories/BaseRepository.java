package com.cowin.project.data.service.repositories;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

/*
 * Base repositories with common query functions 
*/

@NoRepositoryBean
interface BaseRepository<T, ID extends Serializable> extends CrudRepository<T, ID> {

	List<T> findAll();
 
    //T save(T persisted);
    
    void delete(T deleted);
}