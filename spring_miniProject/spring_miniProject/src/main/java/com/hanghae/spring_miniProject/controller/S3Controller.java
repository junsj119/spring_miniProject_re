package com.hanghae.spring_miniProject.controller;

import com.hanghae.spring_miniProject.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    // AWS S3에 이미지 업로드
    @PostMapping("/api/post/image")
    public ResponseEntity<?> uploadImage(@RequestPart(value = "images") MultipartFile multipartFiles) {
        return new ResponseEntity<>(s3Service.uploadImage(multipartFiles), HttpStatus.OK);
    }

    // AWS S3에 이미지 업로드 된 파일을 삭제
    @DeleteMapping("/api/post/image")
    public ResponseEntity<?> deleteImage(@RequestParam String fileName) {
        s3Service.deleteImage(fileName);
        return null;
    }

}
