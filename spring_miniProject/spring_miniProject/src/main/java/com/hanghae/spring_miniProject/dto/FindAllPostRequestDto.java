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

    public FindAllPostRequestDto(List<PostResponseDto> postResponseDto) {
        this.postResponseDto = postResponseDto;
    }
}
