package com.disgustingcat.springbootapp.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.disgustingcat.springbootapp.document.Image;

public interface ImageService {
    public String addImage(MultipartFile file) throws IOException;
    public Image getImage(String userId);
}
