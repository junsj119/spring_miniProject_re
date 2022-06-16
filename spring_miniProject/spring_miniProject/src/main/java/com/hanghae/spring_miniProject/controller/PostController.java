package com.hanghae.spring_miniProject.controller;

import com.hanghae.spring_miniProject.dto.*;
import com.hanghae.spring_miniProject.model.Post;
import com.hanghae.spring_miniProject.model.User;
import com.hanghae.spring_miniProject.repository.PostRepository;
import com.hanghae.spring_miniProject.security.UserDetailsImpl;
import com.hanghae.spring_miniProject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController{

    private final PostRepository postRepository;
    private final PostService postService;

    //게시글 등록
    //게시글 등록
    @PostMapping("/api/post")
    public ResponseEntity<createPostResponseDto> createPost(PostRequest2Dto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return new ResponseEntity<>(postService.createPost(userDetails, requestDto), HttpStatus.CREATED);
    }
//    @PostMapping("/api/post")
//    public ResponseEntity<createPostResponseDto> createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
//        return new ResponseEntity<>(postService.createPost(userDetails, requestDto), HttpStatus.CREATED);
//    }

    //게시글 수정
    @PutMapping("/api/post/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable Long postId, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try{
            postService.update(postId, requestDto, userDetails);
            return new ResponseEntity<>("수정에 성공하셨습니다.", HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //restcontroller advice/ exceptionhandler
    //게시글 삭제
    @DeleteMapping("/api/post/{postId}")
    public ResponseEntity<String> DeletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new NullPointerException("게시글이 존재하지 않습니다.")
        );

        if(post.getUser().getId().equals(userDetails.getUser().getId())){
            postRepository.deleteById(postId);
        }
        else {
            return new ResponseEntity<>("본인의 작성한 게시글만 삭제할 수 있습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("삭제를 성공하셨습니다.", HttpStatus.OK);
    }

    //게시글 전체 조회
    @GetMapping("/api/posts")
    public ResponseEntity<List<PostResponseDto>> findAllPost(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return new ResponseEntity<>(postService.findAll(userDetails), HttpStatus.OK);
    }

    //게시글 상세 조회     PostResponseDto + commentRequestDto(list)
    @GetMapping("/api/post/{postId}")
    public ResponseEntity<PostDetailsResponseDto> FindPostDetails(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return new ResponseEntity<>(postService.findPostDetails(postId, userDetails), HttpStatus.OK);
    }
}
