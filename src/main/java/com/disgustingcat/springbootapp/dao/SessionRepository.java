package com.disgustingcat.springbootapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.disgustingcat.springbootapp.entity.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {
    
}
