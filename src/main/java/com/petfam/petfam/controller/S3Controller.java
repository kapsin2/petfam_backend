package com.petfam.petfam.controller;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class S3Controller {
  @Autowired
  private AmazonS3 amazonS3;

  @Value("${cloud.aws.s3.bucket}")
  private String bucketName;

  @PostMapping("/upload")
  public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType(file.getContentType());
    metadata.setContentLength(file.getSize());
    amazonS3.putObject(bucketName, file.getOriginalFilename(), file.getInputStream(), metadata);
    Date expiration = new Date(System.currentTimeMillis() +  518400000L); // URL expiration time (1 month from now)
    GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, file.getOriginalFilename())
        .withMethod(HttpMethod.GET)
        .withExpiration(expiration);
    URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
    return ResponseEntity.status(HttpStatus.OK)
        .body(url.toString());
  }

  @DeleteMapping("/delete/{fileName:.+}")
  public String deleteFile(@PathVariable String fileName) {
    amazonS3.deleteObject(bucketName, fileName);
    return "File deleted successfully";
  }

  @GetMapping("/image/{fileName:.+}")
  public  ResponseEntity<String> getUrlFile(@PathVariable String fileName) {
    Date expiration = new Date(System.currentTimeMillis() + 3600000); // URL expiration time (1 hour from now)
    GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileName)
        .withMethod(HttpMethod.GET)
        .withExpiration(expiration);
    URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
    return ResponseEntity.status(HttpStatus.OK)
        .body(url.toString());
  }


}

