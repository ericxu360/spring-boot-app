package com.disgustingcat.springbootapp.dao;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.disgustingcat.springbootapp.document.Image;

public interface ImageRepository extends MongoRepository<Image, String> {
}
