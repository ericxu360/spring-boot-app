package com.disgustingcat.springbootapp.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.disgustingcat.springbootapp.dao.CatRepository;
import com.disgustingcat.springbootapp.dao.IncidentRepository;
import com.disgustingcat.springbootapp.entity.Cat;
import com.disgustingcat.springbootapp.entity.Incident;
import com.disgustingcat.springbootapp.entity.Session;
import com.disgustingcat.springbootapp.service.AudioService;
import com.disgustingcat.springbootapp.service.SessionService;
import com.disgustingcat.springbootapp.util.MimeValidation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class IncidentController {
    @Autowired
    private IncidentRepository incidentRepository;
    @Autowired
    private CatRepository catRepository;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private AudioService audioService;
    
    @GetMapping("/api/incidents")
    private ResponseEntity<List<Incident>> getIncidents(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return new ResponseEntity<List<Incident>>(null, null, 403);
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("AppsessionID")) {
                Session session = sessionService.updateSession(Long.parseLong(cookie.getValue()));
                if (session == null) {
                    return new ResponseEntity<List<Incident>>(null, null, 403);
                }
                return new ResponseEntity<List<Incident>>(incidentRepository.findByCatUser(session.getUserId()), null, 200);
            }
        }
        return new ResponseEntity<List<Incident>>(null, null, 403);
    }

    @PostMapping("/api/incidents")
    private ResponseEntity<Incident> postIncident(@RequestBody Incident incident, HttpServletRequest request) {
        if (request.getCookies() == null) {
            return new ResponseEntity<Incident>(null, null, 403);
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("AppsessionID")) {
                Session session = sessionService.updateSession(Long.parseLong(cookie.getValue()));
                if (session == null) {
                    return new ResponseEntity<Incident>(null, null, 403);
                }
                Optional<Cat> _cat = catRepository.findById(incident.getId());
                if (!_cat.isPresent()) {
                    return new ResponseEntity<Incident>(null, null, 404);
                }
                Cat cat = _cat.get();
                if (!cat.getUserId().equals(session.getUserId())) {
                    return new ResponseEntity<Incident>(null, null, 404);
                }
                incident.setCatUser(session.getUserId());
                incident.setCatName(cat.getName());
                return new ResponseEntity<Incident>(incidentRepository.save(incident), null, 200);
            }
        }
        return new ResponseEntity<Incident>(null, null, 403);
    }
    
    @PostMapping("/api/incidents/fileUpload")
    private ResponseEntity<Void> addAudio(@RequestParam("incidentId") Long incidentId, @RequestParam("audio") MultipartFile audio,
            HttpServletRequest request) {
        if (request.getCookies() == null) {
            return new ResponseEntity<>(null, null, 403);
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("AppsessionID")) {
                Session session = sessionService.updateSession(Long.parseLong(cookie.getValue()));
                if (session == null) {
                    return new ResponseEntity<>(null, null, 403);
                }
                Optional<Incident> _incdt = incidentRepository.findByCatUserAndId(session.getUserId(), incidentId);
                if (!_incdt.isPresent()) {
                    return new ResponseEntity<>(null, null, 404);
                }
                Incident incdt = _incdt.get();
                String mediaType = MimeValidation.getMimeType(audio);
                System.out.print(mediaType);
                if (!Arrays.asList("audio/wav").contains(mediaType)) {
                    //return new ResponseEntity<>(null, null, 415);
                }
                try {
                    String audioId  = audioService.addAudio(audio);
                    incdt.setAudioUrl(audioId);
                    incidentRepository.save(incdt);
                } catch (IOException e) {
                    return new ResponseEntity<>(null, null, 500);
                }
                return new ResponseEntity<Void>(null, null, 204);
            }
        }
        return new ResponseEntity<>(null, null, 403);
    }

    @GetMapping("/api/incidents/{id}")
    private ResponseEntity<Incident> getIncidentById(@PathVariable("id") Long catId, HttpServletRequest request) {
        if (request.getCookies() == null) {
            return new ResponseEntity<Incident>(null, null, 403);
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("AppsessionID")) {
                Session session = sessionService.updateSession(Long.parseLong(cookie.getValue()));
                if (session == null) {
                    return new ResponseEntity<Incident>(null, null, 403);
                }
                Optional<Incident> _incident = incidentRepository.findByCatUserAndId(session.getUserId(), catId);
                if (!_incident.isPresent()) {
                    return new ResponseEntity<Incident>(null, null, 403);
                }
                return new ResponseEntity<Incident>(_incident.get(), null, 200);
            }
        }
        return new ResponseEntity<Incident>(null, null, 403);
    }
}