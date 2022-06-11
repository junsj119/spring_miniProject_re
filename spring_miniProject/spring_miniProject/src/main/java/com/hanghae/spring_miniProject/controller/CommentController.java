package com.hanghae.spring_miniProject.controller;

import com.hanghae.spring_miniProject.dto.CommentRequestDto;
import com.hanghae.spring_miniProject.model.Comment;
import com.hanghae.spring_miniProject.model.Post;
import com.hanghae.spring_miniProject.repository.CommentRepository;
import com.hanghae.spring_miniProject.repository.PostRepository;
import com.hanghae.spring_miniProject.security.UserDetailsImpl;
import com.hanghae.spring_miniProject.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentRepository commentRepository;
    private final CommentService commentService;
    private final PostRepository postRepository;

    // 댓글 작성
    @PostMapping("/api/post/{postId}/comment")
    public String addComment(@PathVariable Long postId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 로그인 되어 있는 ID
        if (userDetails != null) {
            Long userId = userDetails.getUser().getId();
            String username = userDetails.getUser().getUsername();
            commentService.addComment(requestDto, username, userId);        //requestDto -> comment, username  / postId, userId
            return "댓글 작성 완료";
        }
        return "로그인이 필요합니다.";
    }

    // 댓글 삭제
    @DeleteMapping("/api/comment/{postId}/{commentId}")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
//        // 로그인 되어 있는 ID
//        if (userDetails != null) {
//            CommentService.deleteComment(commentId, userDetails);
//            // return commentService.deleteComment(commentId, userId);
//
//        }
//        return "로그인이 필요합니다.";

        Optional<Post> fByPostId = postRepository.findById(postId);
        if (fByPostId.isEmpty()) {
            throw new IllegalArgumentException("게시글이 존재하지 않습니다.");
        }

        Optional<Comment> fByCommentId = commentRepository.findById(commentId);
        if (fByCommentId.isEmpty()) {
            throw new IllegalArgumentException("댓글이 존재하지 않습니다.");
        }
        return "Success";
    }
}
