package com.disgustingcat.springbootapp.service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.disgustingcat.springbootapp.dao.ImageRepository;
import com.disgustingcat.springbootapp.dao.IncidentRepository;
import com.disgustingcat.springbootapp.document.Image;

import jakarta.transaction.Transactional;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class AudioServiceImpl implements AudioService {

    
    private S3Client s3Client;

    public AudioServiceImpl(){
        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
        Region region = Region.US_EAST_2;
        
        s3Client = S3Client.builder()
            .credentialsProvider(credentialsProvider)
            .region(region)
            .build();
    }

    @Override
    @Transactional
    public String addAudio(MultipartFile file) throws IOException {
        UUID rand = UUID.randomUUID();
        PutObjectRequest request = PutObjectRequest.builder().bucket("disgustingcataudio").key(rand.toString()).build();
        s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
        return rand.toString();
    }

    @Override
    public byte[] getAudio(String filename) {
        GetObjectRequest objRequest = GetObjectRequest.builder().bucket("disgustingcataudio").key(filename).build();
        ResponseBytes<GetObjectResponse> responseResponseBytes = s3Client.getObjectAsBytes(objRequest);
        byte[] data = responseResponseBytes.asByteArray();
        return data;
    }
}
