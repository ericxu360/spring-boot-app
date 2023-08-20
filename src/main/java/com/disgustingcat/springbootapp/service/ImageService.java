package com.disgustingcat.springbootapp.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.disgustingcat.springbootapp.document.Image;

@Service
public interface ImageService {
    public String addImage(MultipartFile file) throws IOException;
    public Image getImage(String imageId);
}
