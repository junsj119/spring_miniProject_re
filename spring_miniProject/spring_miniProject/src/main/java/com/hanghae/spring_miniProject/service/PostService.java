package com.hanghae.spring_miniProject.service;

import com.hanghae.spring_miniProject.dto.*;
import com.hanghae.spring_miniProject.model.Comment;
import com.hanghae.spring_miniProject.model.Post;
import com.hanghae.spring_miniProject.model.User;
import com.hanghae.spring_miniProject.repository.PostRepository;
import com.hanghae.spring_miniProject.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



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

        User user = post.getUser();
        Long userId = user.getId();

        if(!(userId.equals(userDetails.getUser().getId()))) {
            throw new IllegalArgumentException("본인이 작성한 글만 수정할 수 있습니다.");
        }

        post.update(requestDto);
    }

    //게시글 전체 조회
    public List<FindAllPostRequestDto> findAll(UserDetailsImpl userDetails) {
        List<FindAllPostRequestDto> returnFindPost = new ArrayList<>();

        String username = userDetails.getUsername();
        List<Post> findAllPost = postRepository.findAll();

        List<PostResponseDto> postResponseDtoList = findAllPost.stream()
                .map((o) -> new PostResponseDto(o.getId(), o.getTitle(), o.getImageUrl(), o.getCategory(), o.getContent(), username,
                        o.getCreatedAt(), o.getModifiedAt(), o.getLikeCnt()))
                .collect(Collectors.toList());

        FindAllPostRequestDto findAllPostRequestDto = new FindAllPostRequestDto(postResponseDtoList);
        returnFindPost.add(findAllPostRequestDto);
        return returnFindPost;
    }

    //게시글 상세 조회     PostResponseDto + commentRequestDto(list)
    @Transactional
    public PostDetailsResponseDto findPostDetails(Long postId, UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        //게시글을 찾는다.
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new NullPointerException("게시글이 존재하지 않습니다.")
        );

        //리턴 타입에 맞게 데이터를 가져와 생성자에 넣어준다.(post 관련)
        PostResponseDto postResponseDto = new PostResponseDto(postId, post.getTitle(), post.getImageUrl(), post.getCategory(), post.getContent(),
                username, post.getCreatedAt(), post.getModifiedAt());

        return new PostDetailsResponseDto(postResponseDto);
    }
}
