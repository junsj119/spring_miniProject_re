package com.hanghae.spring_miniProject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class FindAllPostRequestDto {
    public List<PostResponseDto> postResponseDto;
    public String username;

    public FindAllPostRequestDto(List<PostResponseDto> postResponseDto, String username) {
        this.postResponseDto = postResponseDto;
        this.username = username;
    }
}
