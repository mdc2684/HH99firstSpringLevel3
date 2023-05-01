package com.sparta.testlevel1.entity;

import com.sparta.testlevel1.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;

    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn
    private Board board;

    @Column
    @ColumnDefault("0")
    private int liked;

    public Comment(CommentRequestDto commentRequestDto, User user, Board board) {
        this.board = board;
        this.user = user;
        this.content = commentRequestDto.getContent();
    }
    public void plusLiked() {
        liked +=  1;
    }
    public void minusLiked() {
        liked -= 1;
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();
    }
}

