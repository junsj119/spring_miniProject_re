package com.hanghae.spring_miniProject.repository;

import com.hanghae.spring_miniProject.model.Heart;
import com.hanghae.spring_miniProject.model.Post;
import com.hanghae.spring_miniProject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByPostAndUser(Post post, User user);

    int countByPost(Post post);

    Long deleteByPost(Post post);

    void deleteAllByPost(Post post);
}
