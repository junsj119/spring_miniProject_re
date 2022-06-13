package com.hanghae.spring_miniProject.dto;

import com.hanghae.spring_miniProject.model.Post;
import com.hanghae.spring_miniProject.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeartDto {
    private Post post;
    private User user;
}
