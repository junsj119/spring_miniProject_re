package com.hanghae.spring_miniProject.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class PostRequestDto{
    private String title;
    private String imageUrl;
    private String category;
    private String content;


    public PostRequestDto(String title, String imageUrl, String category, String content) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.category = category;
        this.content = content;
    }
}
