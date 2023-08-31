package com.disgustingcat.springbootapp.service;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.disgustingcat.springbootapp.dao.SessionRepository;
import com.disgustingcat.springbootapp.entity.Session;

import jakarta.transaction.Transactional;

@Service
public class SessionServiceImpl implements SessionService {
    @Autowired
    SessionRepository sessionRepository;

    @Override
    @Transactional
    public Session updateSession(Long id) {
        Optional<Session> s = sessionRepository.findById(id);
        if (!s.isPresent()) {
            return null;
        }
        Session session = s.get();
        if (session.getExpiresOn().before(new Date(System.currentTimeMillis()))) {
            return null;
        }
        session.setLastUsed(Date.from(java.time.Clock.systemDefaultZone().instant()));
        session.setExpiresOn(new Date(session.getLastUsed().getTime() + TimeUnit.MINUTES.toMillis(10)));
        return sessionRepository.save(session);
    }

    @Override
    @Transactional
    public Session createSession(String username) {
        Session s = new Session();
        s.setUserId(username);
        s = sessionRepository.save(s);
        s.setExpiresOn(new Date(s.getLastUsed().getTime() + TimeUnit.MINUTES.toMillis(10)));
        return sessionRepository.save(s);
    }

    @Override
    @Transactional
    public boolean expireSession(Long id) {
        Optional<Session> s = sessionRepository.findById(id);
        if (!s.isPresent()) {
            return false;
        }
        Session session = s.get();
        if (session.getExpiresOn().before(new Date(System.currentTimeMillis()))) {
            return false;
        }
        session.setLastUsed(Date.from(java.time.Clock.systemDefaultZone().instant()));
        session.setExpiresOn(new Date(session.getLastUsed().getTime()));
        sessionRepository.save(session);
        return true;
    }


}
