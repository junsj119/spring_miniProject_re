package com.hanghae.spring_miniProject.dto;

import com.hanghae.spring_miniProject.model.Modifiedstamped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

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
