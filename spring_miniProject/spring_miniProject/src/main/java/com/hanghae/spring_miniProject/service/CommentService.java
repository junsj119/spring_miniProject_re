package com.hanghae.spring_miniProject.service;

import com.hanghae.spring_miniProject.dto.CommentRequestDto;
import com.hanghae.spring_miniProject.model.Comment;
import com.hanghae.spring_miniProject.model.Post;
import com.hanghae.spring_miniProject.model.User;
import com.hanghae.spring_miniProject.repository.CommentRepository;
import com.hanghae.spring_miniProject.repository.PostRepository;
import com.hanghae.spring_miniProject.repository.UserRepository;
import com.hanghae.spring_miniProject.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    private final PostRepository postRepository;

    // create
    @Transactional
    public void addComment(CommentRequestDto requestDto, String username, Long userId) {
        Optional<User> found = userRepository.findByUsername(username);
        if(!found.isPresent()){
            throw new IllegalArgumentException("없는 회원입니다.");
        }

        Post post = postRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        Comment comment = new Comment(requestDto, username, userId);

        commentRepository.save(comment);
    }

    // Read
    public List<Comment> getComment(Long postId) {
        return  commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId);
    }

//    // Delete
//    public void deleteComment(Long id, UserDetailsImpl userDetails) throws IllegalAccessError {
//        Comment comment = commentRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
//
//        if (!comment.getPost().getId().equals(userDetails.getUser().getId())) {
//            throw new IllegalAccessError("로그인된 유저의 댓글이 아닙니다.");
//        }
//
//        commentRepository.delete(comment);
//    }

//    public String deleteComment(Long commentId, Long userId) {
//        Long writerId = commentRepository.findById(commentId).orElseThrow(
//                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")).getId();
//        if (Objects.equals(writerId, userId)) {
//            commentRepository.deleteById(commentId);
//            return "댓글 삭제 완료";
//        }
//        return "작성한 유저가 아닙니다.";
//    }


}
