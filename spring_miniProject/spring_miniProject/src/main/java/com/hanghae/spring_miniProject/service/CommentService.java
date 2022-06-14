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


    //create
    @Transactional
    public void addComment(Long postId, CommentRequestDto requestDto, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        //회원번호가 일치하지 않을 때
        Optional<User> found = userRepository.findById(user.getId());
        if(found.isEmpty()){
            throw new IllegalArgumentException("없는 회원입니다.");
        }

        //게시글 번호가 일치하지 않을 때
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new IllegalArgumentException("게시글이 없습니다."));


        //댓글창이 "" 일 때
        if(requestDto.getComment().equals(""))
            throw new IllegalArgumentException("댓글 내용을 입력해주세요.");

        //추가

        String getComment = requestDto.getComment();
        Comment comment = new Comment(post, getComment, user);
        commentRepository.save(comment);
    }


    // Read
    @Transactional
    public List<Comment> getComment(Long postId) {
        return  commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId);
    }


    //Update
    @Transactional
    public void update_Comment(Long commentId, CommentRequestDto requestDto, UserDetailsImpl userDetails) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NullPointerException("댓글이 없습니다.")
        );

        //06/13 : 01:47
        //연관관계      requestDto로 받아도 될듯?
        String username = comment.getUser().getUsername();

        //방금 저장한 이름이랑 수정을 신청한 이름이랑 같다면
        if(username.equals(userDetails.getUsername())){
            //set을 지양하고 싶다면 생성자를 만들면 된다. 리팩토링 때 하기
           comment.setComment(requestDto.getComment());
           commentRepository.save(comment);
        }
        else{
            throw new IllegalArgumentException("본인이 작성한 댓글만 수정할 수 있습니다.");
        }
    }


}
