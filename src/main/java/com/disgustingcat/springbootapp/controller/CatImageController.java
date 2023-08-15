package com.disgustingcat.springbootapp.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.apache.tika.mime.MediaType;
import org.bson.types.Binary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.disgustingcat.springbootapp.dao.CatRepository;
import com.disgustingcat.springbootapp.document.Image;
import com.disgustingcat.springbootapp.entity.Cat;
import com.disgustingcat.springbootapp.service.ImageService;
import com.disgustingcat.springbootapp.util.MimeValidation;
import com.nimbusds.jose.util.IOUtils;

import lombok.var;

@RestController
public class CatImageController {
    private ImageService imageService;
    private CatRepository catRepository;

    public CatImageController(ImageService imageService, CatRepository catRepository) {
        this.imageService = imageService;
        this.catRepository = catRepository;
    }


    @GetMapping(value = "/api/cat-image/{catId}", produces = org.springframework.http.MediaType.IMAGE_JPEG_VALUE)
    private @ResponseBody byte[] getImage(@PathVariable("catId") Long catId) throws IOException {
        // TODO: authorize the user
        System.out.println(catId);
        Optional<Cat> _cat = catRepository.findById(catId);

        if (!_cat.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cat with " + catId + " does not exist");
        }
        System.out.println("First print");
        Cat cat = _cat.get();
        if (cat.getImageUrl() == null) {
            return null;
        }
        System.out.println("First print");
        return imageService.getImage(cat.getImageUrl()).getImage().getData();
    }

    @PostMapping("/api/cat-image")
    private void addImage(@RequestParam("catId") Long catId, @RequestParam("image") MultipartFile image) {
        System.out.println(image.getSize() + " " + catId);


        // TODO: authorize the user
        Optional<Cat> _cat = catRepository.findById(catId);
        if (!_cat.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cat with " + catId + " does not exist");
        }
        Cat cat = _cat.get();
        String mediaType = MimeValidation.getMimeType(image);
        if (!Arrays.asList("image/jpeg", "image/png", "image/gif").contains(mediaType)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File must be JPG, PNG, or GIF");
        }
        try {
            String imageId = imageService.addImage(image);
            cat.setImageUrl(imageId);
            catRepository.save(cat);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The server could not process the request.");
        }
    }

    
}
