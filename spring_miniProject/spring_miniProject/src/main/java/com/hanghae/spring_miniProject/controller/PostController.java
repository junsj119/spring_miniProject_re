package com.hanghae.spring_miniProject.controller;

import com.hanghae.spring_miniProject.dto.FindAllPostRequestDto;
import com.hanghae.spring_miniProject.dto.PostRequestDto;
import com.hanghae.spring_miniProject.dto.PostResponseDto;
import com.hanghae.spring_miniProject.dto.createPostResponseDto;
import com.hanghae.spring_miniProject.model.Post;
import com.hanghae.spring_miniProject.repository.PostRepository;
import com.hanghae.spring_miniProject.security.UserDetailsImpl;
import com.hanghae.spring_miniProject.service.PostService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostRepository postRepository;
    private final PostService postService;

    //게시글 등록
    @PostMapping("/api/post")
    public createPostResponseDto createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.createPost(userDetails, requestDto);
    }

    //게시글 수정
    @PutMapping("/api/post/{postId}")
    public String updatePost(@PathVariable Long postId, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try{
            postService.update(postId, requestDto, userDetails);
            return "Success";
        }catch (Exception e){
            return e.getMessage();
        }
    }

    //게시글 삭제
    @DeleteMapping("/api/post/{postId}")
    public String DeletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new NullPointerException("게시글이 존재하지 않습니다.")
        );

        if(post.getUser().getId().equals(userDetails.getUser().getId())){
            postRepository.deleteById(postId);
        }
        else {
            return "본인의 작성한 게시글만 삭제할 수 있습니다.";
        }
        return "게시글 삭제를 완료하셨습니다.";
    }

    //게시글 전체 조회
    @GetMapping("/api/posts")
    public List<FindAllPostRequestDto> findAllPost(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.findAll(userDetails);
    }
}
