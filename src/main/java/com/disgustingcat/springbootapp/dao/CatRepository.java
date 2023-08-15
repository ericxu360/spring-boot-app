package com.disgustingcat.springbootapp.dao;

import com.disgustingcat.springbootapp.entity.Cat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("http://localhost:4200")
@RepositoryRestResource
public interface CatRepository extends JpaRepository<Cat, Long>{
    Page<Cat> findByUserId(@Param("userId") String userId, Pageable pageable);
    Page<Cat> findByNameContaining(@Param("name") String name, Pageable pageable);
}
