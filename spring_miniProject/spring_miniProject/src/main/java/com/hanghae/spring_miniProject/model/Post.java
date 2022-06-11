package com.hanghae.spring_miniProject.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hanghae.spring_miniProject.dto.PostRequestDto;
import com.hanghae.spring_miniProject.dto.PostResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor

@SequenceGenerator(
        name = "POST_A",
        sequenceName = "POST_B",
        initialValue = 1, allocationSize = 50)
public class Post extends Timestamped{
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "POST_A")
    @Column(name = "POST_ID")
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String imageUrl;
    @Column
    private String category;
    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User user;

    @JsonManagedReference // 직렬화 허용 어노테이션
    @OneToMany(mappedBy = "post", orphanRemoval = true) // orpahRemanal = true 부모 삭제시 자식도 삭제
    private List<Comment> comments;


    public Post(User user, PostRequestDto postRequestDto){
        this.user = user;
        this.title = postRequestDto.getTitle();
        this.imageUrl = postRequestDto.getImageUrl();
        this.category = postRequestDto.getCategory();
        this.content = postRequestDto.getContent();
    }


    public void update(PostRequestDto requestDto) {
        this.content = requestDto.getContent();
        this.title = requestDto.getTitle();
        this.category = requestDto.getCategory();
        this.imageUrl = requestDto.getImageUrl();
    }
}
