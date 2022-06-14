package com.hanghae.spring_miniProject.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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

    private String username;

    //어노테이션을 붙여야 하나??? service단에서 바꿔줘야한다.
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    
    private int likeCnt;

    //게시글 상세조회
    public PostResponseDto(Long id, String title, String imageUrl, String category, String content,
                           String username, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.category = category;
        this.content = content;
        this.username = username;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
    
    //게시글 조회 ( @AllArgs 어노테이션으로 생략 가능)
    public PostResponseDto(Long id, String title, String imageUrl, String category, String content, String username,
                           LocalDateTime createdAt, LocalDateTime modifiedAt, int likeCnt) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.category = category;
        this.content = content;
        this.username = username;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.likeCnt = likeCnt;
    }

}
