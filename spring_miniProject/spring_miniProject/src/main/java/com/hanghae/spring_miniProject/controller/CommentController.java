package com.hanghae.spring_miniProject.controller;

import com.hanghae.spring_miniProject.dto.CommentRequestDto;
import com.hanghae.spring_miniProject.model.Comment;
import com.hanghae.spring_miniProject.model.Post;
import com.hanghae.spring_miniProject.repository.CommentRepository;
import com.hanghae.spring_miniProject.repository.PostRepository;
import com.hanghae.spring_miniProject.security.UserDetailsImpl;
import com.hanghae.spring_miniProject.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentRepository commentRepository;
    private final CommentService commentService;
    private final PostRepository postRepository;

    //댓글 조회
    @GetMapping("api/post/{postId}/comment")
    public ResponseEntity<List<CommentRequestDto>> findComment(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        String username = userDetails.getUsername();
        return new ResponseEntity<>(commentService.findComment(postId, username), HttpStatus.OK);
    }


    // 댓글 작성
    @PostMapping("/api/post/{postId}/comment")
    public ResponseEntity<String> addComment(@PathVariable Long postId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        //로그인 체킹은 시큐리티에서 해줘서 뺐습니다.
        try{
            commentService.addComment(postId, requestDto, userDetails);        //requestDto -> comment, username  / postId, userId
            return new ResponseEntity<>("댓글 작성 완료하였습니다.", HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 댓글 삭제
    @DeleteMapping("/api/comment/{postId}/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {

        Optional<Post> fByPostId = postRepository.findById(postId);
        if (fByPostId.isEmpty()) {
            throw new IllegalArgumentException("게시글이 존재하지 않습니다.");
        }

        Optional<Comment> fByCommentId = commentRepository.findById(commentId);
        if (fByCommentId.isEmpty()) {
            throw new IllegalArgumentException("댓글이 존재하지 않습니다.");
        }
        commentRepository.deleteById(commentId);
        return new ResponseEntity<>("댓글 삭제에 성공하셨습니다.", HttpStatus.OK);
    }

    //댓글 수정
    //requestDto로 안받고 String comment 로 받아도 된다. username은 userDetails에서 꺼내면 되기 때문이다.
    @PutMapping("/api/comment/{id}")
    public ResponseEntity<String> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        Long commentId = id;
        try{
            commentService.update_Comment(commentId, requestDto, userDetails);
            return new ResponseEntity<>("댓글 수정에 성공하셨습니다.", HttpStatus.CREATED);
        }catch(IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}