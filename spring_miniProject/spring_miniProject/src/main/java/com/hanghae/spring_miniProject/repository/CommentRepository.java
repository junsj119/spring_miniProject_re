package com.hanghae.spring_miniProject.repository;

import com.hanghae.spring_miniProject.model.Comment;
import com.hanghae.spring_miniProject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPostIdOrderByCreatedAtDesc(Long postId);
    int countByPostId(Long postId);

}
