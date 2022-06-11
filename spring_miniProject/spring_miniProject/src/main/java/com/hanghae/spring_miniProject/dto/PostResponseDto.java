package com.hanghae.spring_miniProject.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class PostResponseDto{
    private Long id;
    private String title;
    private String imageUrl;
    private String category;
    private String content;

    //어노테이션을 붙여야 하나??? service단에서 바꿔줘야한다.
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public PostResponseDto(Long id, String title, String imageUrl, String category, String content, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.category = category;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
    public PostResponseDto(Long id, String title, String imageUrl, String category, String content) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.category = category;
        this.content = content;
    }
}
