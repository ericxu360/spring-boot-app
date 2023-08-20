package com.disgustingcat.springbootapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.disgustingcat.springbootapp.dao.CatRepository;
import com.disgustingcat.springbootapp.entity.Cat;
import com.disgustingcat.springbootapp.entity.Session;
import com.disgustingcat.springbootapp.service.SessionService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class CatController {
    @Autowired
    private CatRepository catRepository;
    @Autowired
    private SessionService sessionService;

    @PostMapping("/api/cats")
    private ResponseEntity<Cat> postCat(@RequestBody Cat c, HttpServletRequest request) {
        if (request.getCookies() == null) {
            return new ResponseEntity<Cat>(null, null, 403);
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("AppsessionID")) {
                Session session = sessionService.updateSession(Long.parseLong(cookie.getValue()));
                c.setUserId(session.getUserId());
                return new ResponseEntity<Cat>(catRepository.save(c), null, 200);
            }
        }
        return new ResponseEntity<Cat>(null, null, 403);
    }

    @GetMapping("/api/cats/getByUserId")
    private ResponseEntity<List<Cat>> getByUserId(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return new ResponseEntity<List<Cat>>(null, null, 403);
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("AppsessionID")) {
                Session session = sessionService.updateSession(Long.parseLong(cookie.getValue()));
                return new ResponseEntity<List<Cat>>(catRepository.findByUserId(session.getUserId()), null, 200);
            }
        }
        return new ResponseEntity<List<Cat>>(null, null, 403);
    }
}
