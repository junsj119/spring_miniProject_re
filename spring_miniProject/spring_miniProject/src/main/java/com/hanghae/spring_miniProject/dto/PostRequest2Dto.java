package com.hanghae.spring_miniProject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class PostRequest2Dto{
    private String title;
    private MultipartFile imageUrl;
    private String category;
    private String content;


    public PostRequest2Dto(String title, MultipartFile imageUrl, String category, String content) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.category = category;
        this.content = content;
    }
}