package com.hanghae.spring_miniProject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor

@Getter
@Setter
public class PostDetailsResponseDto {
    public PostResponseDto postResponseDto;
    public List<CommentRequestDto> commentRequestDtoList;

    public PostDetailsResponseDto(PostResponseDto postResponseDto, List<CommentRequestDto> commentRequestDtoList) {
        this.postResponseDto = postResponseDto;
        this.commentRequestDtoList = commentRequestDtoList;
    }
}
