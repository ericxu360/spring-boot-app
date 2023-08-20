package com.disgustingcat.springbootapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.disgustingcat.springbootapp.dao.CatRepository;
import com.disgustingcat.springbootapp.entity.Cat;
import com.disgustingcat.springbootapp.entity.Session;
import com.disgustingcat.springbootapp.service.SessionService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;

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
                if (session == null) {
                    return new ResponseEntity<Cat>(null, null, 403);
                }
                c.setUserId(session.getUserId());
                return new ResponseEntity<Cat>(catRepository.save(c), null, 200);
            }
        }
        return new ResponseEntity<Cat>(null, null, 403);
    }

    @PatchMapping("/api/cats/{id}")
    private ResponseEntity<Cat> patchCat(@PathParam("id") Long catId, @RequestBody Cat patchCat, HttpServletRequest request) {
        if (request.getCookies() == null) {
            return new ResponseEntity<Cat>(null, null, 403);
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("AppsessionID")) {
                Session session = sessionService.updateSession(Long.parseLong(cookie.getValue()));
                if (session == null) {
                    return new ResponseEntity<Cat>(null, null, 403);
                }
                Optional<Cat> _cat = catRepository.findByIdAndUserId(catId, session.getUserId());
                if (!_cat.isPresent()) {
                    return new ResponseEntity<Cat>(null, null, 404);
                }
                Cat cat = _cat.get();
                cat.setUserId(session.getUserId());
                cat.setName(patchCat.getName());
                return new ResponseEntity<Cat>(catRepository.save(cat), null, 200);
            }
        }
        return new ResponseEntity<Cat>(null, null, 403);
    }

    @DeleteMapping("/api/cats/{id}")
    private ResponseEntity<Void> deleteCat(@PathParam("id") Long catId, HttpServletRequest request) {
        // TODO: David implement me
        return new ResponseEntity<Void>(null, null, 405);
    }

    @GetMapping("/api/cats/getByUserId")
    private ResponseEntity<List<Cat>> getByUserId(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return new ResponseEntity<List<Cat>>(null, null, 403);
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("AppsessionID")) {
                Session session = sessionService.updateSession(Long.parseLong(cookie.getValue()));
                if (session == null) {
                    return new ResponseEntity<List<Cat>>(null, null, 403);
                }
                return new ResponseEntity<List<Cat>>(catRepository.findByUserId(session.getUserId()), null, 200);
            }
        }
        return new ResponseEntity<List<Cat>>(null, null, 403);
    }
}
