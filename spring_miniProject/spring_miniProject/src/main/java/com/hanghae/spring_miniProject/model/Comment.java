package com.hanghae.spring_miniProject.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hanghae.spring_miniProject.dto.CommentRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(
        name = "COMMENT_A",
        sequenceName = "COMMENT_B",
        initialValue = 1, allocationSize = 50)
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "COMMENT_A")
    @Column(name = "COMMENT_ID")
    private Long id;

    @Column
    private String comment;



    @JsonBackReference // 순환참조 방지
    @ManyToOne
    @JoinColumn(name ="POST_ID")
    private Post post;

    public Comment(CommentRequestDto requestDto, String username, Long userId) {
        super();
    }
}
