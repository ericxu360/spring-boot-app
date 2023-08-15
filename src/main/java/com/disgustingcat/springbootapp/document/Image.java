package com.disgustingcat.springbootapp.document;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "image")
@Data
public class Image {
    @Id
    private String id;

    private Binary image;

}
