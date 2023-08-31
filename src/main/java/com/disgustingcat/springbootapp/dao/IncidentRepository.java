package com.disgustingcat.springbootapp.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    List<Incident> findByCatId(@Param("id") Long id);
    List<Incident> findByCatUser(@Param("catUser") String catUser);
    List<Incident> findByDateBetween(Date start, Date end);
    Optional<Incident> findByCatUserAndId(@Param("catUser") String catUser, @Param("id") long id);
}
