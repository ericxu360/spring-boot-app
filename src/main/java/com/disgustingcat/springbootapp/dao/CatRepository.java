package com.disgustingcat.springbootapp.dao;

import com.disgustingcat.springbootapp.entity.Cat;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface CatRepository extends JpaRepository<Cat, Long>{
    
    List<Cat> findByUserId(@Param("userId") String userId);
    Optional<Cat> findByIdAndUserId(@Param("id") Long id, @Param("userId") String userId);
    Page<Cat> findByNameContaining(@Param("name") String name, Pageable pageable);
}
