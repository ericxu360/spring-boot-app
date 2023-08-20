package com.disgustingcat.springbootapp.service;

import org.springframework.stereotype.Service;

import com.disgustingcat.springbootapp.entity.Session;

@Service
public interface SessionService {
    public Session updateSession(Long id);
    public Session createSession(String username);
}
