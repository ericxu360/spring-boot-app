package com.disgustingcat.springbootapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.disgustingcat.springbootapp.entity.User;


public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername(String username);
}
