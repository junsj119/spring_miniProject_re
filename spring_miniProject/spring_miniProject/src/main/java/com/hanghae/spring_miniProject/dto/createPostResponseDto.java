package com.hanghae.spring_miniProject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Getter
@Setter

public class createPostResponseDto {
    private Long id;
    private String title;
    private String imageUrl;
    private String category;
    private String content;

    public createPostResponseDto(Long id, String title, String imageUrl, String category, String content) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.category = category;
        this.content = content;
    }
}
