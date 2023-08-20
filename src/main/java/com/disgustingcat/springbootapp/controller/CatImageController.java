package com.disgustingcat.springbootapp.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.disgustingcat.springbootapp.dao.CatRepository;
import com.disgustingcat.springbootapp.document.Image;
import com.disgustingcat.springbootapp.entity.Cat;
import com.disgustingcat.springbootapp.entity.Session;
import com.disgustingcat.springbootapp.service.ImageService;
import com.disgustingcat.springbootapp.service.SessionService;
import com.disgustingcat.springbootapp.util.MimeValidation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class CatImageController {
    @Autowired
    private ImageService imageService;
    @Autowired
    private CatRepository catRepository;
    @Autowired
    private SessionService sessionService;

    @GetMapping(value = "/api/cat-image/{catId}", produces = org.springframework.http.MediaType.IMAGE_JPEG_VALUE)
    private ResponseEntity<byte[]> getImage(@PathVariable("catId") Long catId, HttpServletRequest request)
            throws IOException {
        if (request.getCookies() == null) {
            return new ResponseEntity<byte[]>(null, null, 403);
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("AppsessionID")) {
                Session session = sessionService.updateSession(Long.parseLong(cookie.getValue()));
                if (session == null) {
                    return new ResponseEntity<byte[]>(null, null, 403);
                }
                Optional<Cat> _cat = catRepository.findByIdAndUserId(catId, session.getUserId());
                if (!_cat.isPresent()) {
                    return new ResponseEntity<byte[]>(null, null, 404);
                }
                Cat cat = _cat.get();
                if (cat.getImageUrl() == null) {
                    return new ResponseEntity<byte[]>(null, null, 200);
                }
                Image image = imageService.getImage(cat.getImageUrl());
                if (image == null) {
                    cat.setImageUrl(null);
                    return new ResponseEntity<byte[]>(null, null, 200);
                }
                return new ResponseEntity<byte[]>(image.getImage().getData(), null, 200);
            }
        }
        return new ResponseEntity<byte[]>(null, null, 403);

    }

    @PostMapping("/api/cat-image")
    private ResponseEntity<Void> addImage(@RequestParam("catId") Long catId, @RequestParam("image") MultipartFile image,
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
                Optional<Cat> _cat = catRepository.findByIdAndUserId(catId, session.getUserId());
                if (!_cat.isPresent()) {
                    return new ResponseEntity<>(null, null, 404);
                }
                Cat cat = _cat.get();
                String mediaType = MimeValidation.getMimeType(image);
                if (!Arrays.asList("image/jpeg", "image/png", "image/gif").contains(mediaType)) {
                    return new ResponseEntity<>(null, null, 415);
                }
                try {
                    String imageId = imageService.addImage(image);
                    cat.setImageUrl(imageId);
                    catRepository.save(cat);
                } catch (IOException e) {
                    return new ResponseEntity<>(null, null, 500);
                }
                return new ResponseEntity<Void>(null, null, 204);
            }
        }
        return new ResponseEntity<>(null, null, 403);
    }
}
