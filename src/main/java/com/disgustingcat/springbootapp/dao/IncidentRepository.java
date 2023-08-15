package com.disgustingcat.springbootapp.dao;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.disgustingcat.springbootapp.entity.Cat;
import com.disgustingcat.springbootapp.entity.Incident;

@CrossOrigin("http://localhost:4200")
@RepositoryRestResource
public interface IncidentRepository extends JpaRepository<Incident, Long>{
    Page<Incident> findByCatId(@Param("id") Long id, Pageable pageable);
    Page<Incident> findByCatUser(@Param("catUser") String catUser, Pageable pageable);
    Page<Incident> findByDateBetween(Date start, Date end, Pageable pageable);
    Page<Incident> findByCatIn(Cat[] cats, Pageable pageable);
}
