package com.hanghae.spring_miniProject.model;


import com.hanghae.spring_miniProject.dto.HeartDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Heart {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "Heart_A")
    @Column(name = "HEART_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    public Heart(HeartDto heartDto) {
        this.user = heartDto.getUser();
        this.post = heartDto.getPost();
    }
}
