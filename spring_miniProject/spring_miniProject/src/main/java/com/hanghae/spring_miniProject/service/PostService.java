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


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.hanghae.spring_miniProject.dto.*;
import com.hanghae.spring_miniProject.model.Comment;
import com.hanghae.spring_miniProject.model.Post;
import com.hanghae.spring_miniProject.model.User;
import com.hanghae.spring_miniProject.repository.PostRepository;
import com.hanghae.spring_miniProject.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    //????????? ??????
    @Transactional
    public createPostResponseDto createPost(UserDetailsImpl userDetails, PostRequest2Dto requestDto) {

        User user = userDetails.getUser();

        String title = requestDto.getTitle();
        String category = requestDto.getCategory();
        MultipartFile imageUrl = requestDto.getImageUrl();
        String content = requestDto.getContent();

        String imgUrl;

        String fileName = createFileName(imageUrl.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(imageUrl.getSize());
        objectMetadata.setContentType(imageUrl.getContentType());

        System.out.println(bucket);

        try(InputStream inputStream = imageUrl.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch(IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "????????? ???????????? ??????????????????.");
        }
        imgUrl = amazonS3.getUrl(bucket, fileName).toString();

        PostRequestDto postRequestDto = new PostRequestDto(title, imgUrl, category, content);


        Post post = new Post(user, postRequestDto);

        postRepository.save(post);
        Long postId = post.getId();


        return new createPostResponseDto(postId, title, imgUrl, category, content);
    }

    public void deleteImage(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "????????? ????????? ??????(" + fileName + ") ?????????.");
        }
    }

    //????????? ??????
    @Transactional
    public void update(Long id, PostRequestDto requestDto, UserDetailsImpl userDetails) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("???????????? ???????????? ????????????.")
        );

        User user = post.getUser();
        Long userId = user.getId();

        if (!(userId.equals(userDetails.getUser().getId()))) {
            throw new IllegalArgumentException("????????? ????????? ?????? ????????? ??? ????????????.");
        }

        post.update(requestDto);
    }

    //????????? ?????? ??????
    public List<PostResponseDto> findAll(UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        List<Post> findAllPost = postRepository.findAll();
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();


        for(Post post : findAllPost){

            Long id = post.getId();
            String title = post.getTitle();
            String imgUrl = post.getImageUrl();
            String category = post.getCategory();
            String content = post.getContent();
            LocalDateTime createdAt = post.getCreatedAt();
            LocalDateTime modifiedAt = post.getModifiedAt();
            int likeCnt = post.getLikeCnt();

            PostResponseDto postResponseDto = new PostResponseDto(id, title, imgUrl, category, content, username, createdAt, modifiedAt, likeCnt);
            postResponseDtoList.add(postResponseDto);
        }

        return postResponseDtoList;
    }

    //????????? ?????? ??????     PostResponseDto + commentRequestDto(list)
    @Transactional
    public PostDetailsResponseDto findPostDetails(Long postId, UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        //???????????? ?????????.
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new NullPointerException("???????????? ???????????? ????????????.")
        );

        //?????? ????????? ?????? ???????????? ????????? ???????????? ????????????.(post ??????)
        PostResponseDto postResponseDto = new PostResponseDto(postId, post.getTitle(), post.getImageUrl(), post.getCategory(), post.getContent(),
                username, post.getCreatedAt(), post.getModifiedAt());

        return new PostDetailsResponseDto(postResponseDto);
    }
}
