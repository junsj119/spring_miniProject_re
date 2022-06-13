package com.hanghae.spring_miniProject.service;

import com.hanghae.spring_miniProject.dto.HeartDto;
import com.hanghae.spring_miniProject.model.Heart;
import com.hanghae.spring_miniProject.model.Post;
import com.hanghae.spring_miniProject.model.User;
import com.hanghae.spring_miniProject.repository.HeartRepository;
import com.hanghae.spring_miniProject.repository.PostRepository;
import com.hanghae.spring_miniProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class HeartService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final HeartRepository heartRepository;

    //좋아요 누르기
    @Transactional
    public boolean ClickToLike(Long postId, Long userId) {
        Post post = getPost(postId);
        User user = getUser(userId);

        //서버에 반환해줄 불리언
        boolean toggleLike;

        HeartDto heartDto = new HeartDto(post, user);
        Heart heart = new Heart(heartDto);
        int likeCnt = heart.getPost().getLikeCnt();

        //지금 로그인 되어있는 사용자가 해당 포스트에 좋아요를 누른적이 있냐 없냐.
        Heart findHeart = heartRepository.findByPostAndUser(post, user).orElse(null);

        if(findHeart == null){
            heart.getPost().setLikeCnt(likeCnt+1);

            heartRepository.save(heart);
            toggleLike = true;
        }
        else{
            heart.getPost().setLikeCnt(likeCnt-1);

            heartRepository.deleteById(findHeart.getId());
            toggleLike = false;
        }
        return toggleLike;
    }


    private User getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("사용자 정보가 존재하지 않습니다.")
        );
        return user;
    }

    private Post getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()->new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        return post;
    }
}
