package com.disgustingcat.springbootapp.service;

import java.io.IOException;
import java.util.Optional;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.disgustingcat.springbootapp.dao.ImageRepository;
import com.disgustingcat.springbootapp.document.Image;

import jakarta.transaction.Transactional;

@Service
public class ImageServiceImpl implements ImageService {
    private ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository userImageRepository) {
        this.imageRepository = userImageRepository;
    }

    @Override
    @Transactional
    public String addImage(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        return imageRepository.save(image).getId();
    }

    @Override
    @Transactional
    public Image getImage(String imageId) {
        Optional<Image> _image = imageRepository.findById(imageId);
        if (_image.isPresent()) {
            return _image.get();
        } else {
            return null;
        }
    }
}
