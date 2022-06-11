package com.hanghae.spring_miniProject.repository;

import com.hanghae.spring_miniProject.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
