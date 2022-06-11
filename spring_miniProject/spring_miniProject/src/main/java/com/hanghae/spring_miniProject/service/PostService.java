package com.hanghae.spring_miniProject.service;

import com.hanghae.spring_miniProject.dto.FindAllPostRequestDto;
import com.hanghae.spring_miniProject.dto.PostRequestDto;
import com.hanghae.spring_miniProject.dto.PostResponseDto;
import com.hanghae.spring_miniProject.dto.createPostResponseDto;
import com.hanghae.spring_miniProject.model.Post;
import com.hanghae.spring_miniProject.model.User;
import com.hanghae.spring_miniProject.repository.PostRepository;
import com.hanghae.spring_miniProject.repository.UserRepository;
import com.hanghae.spring_miniProject.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityListeners;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;


    //게시글 등록
    @Transactional
    public createPostResponseDto createPost(UserDetailsImpl userDetails, PostRequestDto requestDto) {

        User user = userDetails.getUser();

        Post post = new Post(user, requestDto);

        postRepository.save(post);
        Long postId = post.getId();

        String title = requestDto.getTitle();
        String category = requestDto.getCategory();
        String imageUrl = requestDto.getImageUrl();
        String content = requestDto.getContent();

        return new createPostResponseDto(postId, title, imageUrl, category, content);
    }

    //게시글 수정
    @Transactional
    public void update(Long id, PostRequestDto requestDto, UserDetailsImpl userDetails) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("게시글이 존재하지 않습니다.")
        );
        if(post.getUser().getId().equals(userDetails.getUser().getId())){
            post.update(requestDto);
        }
        else{
            throw new IllegalArgumentException("본인이 작성한 글만 수정할 수 있습니다.");
        }
    }

    //게시글 전체 조회
    public List<FindAllPostRequestDto> findAll(UserDetailsImpl userDetails) {
        List<FindAllPostRequestDto> returnFindPost = new ArrayList<>();

        String username = userDetails.getUsername();
        List<Post> findAllPost = postRepository.findAll();

        List<PostResponseDto> postResponseDtoList = findAllPost.stream()
                .map((o) -> new PostResponseDto(o.getId(), o.getTitle(), o.getImageUrl(), o.getCategory(), o.getContent(), o.getCreatedAt(), o.getModifiedAt()))
                .collect(Collectors.toList());

        FindAllPostRequestDto findAllPostRequestDto = new FindAllPostRequestDto(postResponseDtoList, username);
        returnFindPost.add(findAllPostRequestDto);
        return returnFindPost;
    }
}
