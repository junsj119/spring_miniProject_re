package com.hanghae.spring_miniProject.controller;

import com.hanghae.spring_miniProject.security.UserDetailsImpl;
import com.hanghae.spring_miniProject.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class HeartController {
    private final HeartService heartService;

    //좋아요 클릭
    @PostMapping("/api/like/{postId}")
    public ResponseEntity<Boolean> IsLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        Long userId = userDetails.getUser().getId();
        return new ResponseEntity<>(heartService.clickToLike(postId, userId), HttpStatus.OK);
    }

}
