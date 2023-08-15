package com.disgustingcat.springbootapp.controller;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.disgustingcat.springbootapp.document.Image;
import com.disgustingcat.springbootapp.service.ImageService;
import com.disgustingcat.springbootapp.util.MimeValidation;

@RepositoryRestController
public class UserImageController {

    private ImageService imageService;

    public UserImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/user-image")
    private void addImage(@RequestParam("userId") String userId, @RequestParam("image") MultipartFile image) {
        // TODO: authorize the user
        String mediaType = MimeValidation.getMimeType(image);
        if (!Arrays.asList("image/jpeg", "image/png", "image/gif").contains(mediaType)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File must be JPG, PNG, or GIF");
        }
        try {
            String imageId = imageService.addImage(image);
            // TODO: Add the picture to the user via Okta
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The server could not process the request.");
        }
    }

    @GetMapping("/user-image/{id}")
    private Image getImage(@PathVariable("id") String id) {
        // TODO: fix this
        return imageService.getImage(id);
    }
}
