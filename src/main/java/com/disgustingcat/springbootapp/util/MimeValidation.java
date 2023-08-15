package com.disgustingcat.springbootapp.util;

import java.io.IOException;

import org.apache.tika.Tika;
import org.apache.tika.mime.MediaType;
import org.springframework.web.multipart.MultipartFile;

public class MimeValidation {
    public static String getMimeType(MultipartFile file) {
        Tika tika = new Tika();
        try {
            String mediaType = tika.detect(file.getInputStream());
            return mediaType;
        } catch (IOException e) {
            return MediaType.OCTET_STREAM.toString();
        }
    }
}
