package com.sparta.testlevel1.dto;

import com.sparta.testlevel1.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BoardResponseDto {
    private String title;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private List<CommentResponseDto> commentlist;   // 순환참조?

    public BoardResponseDto(Board board) {
        this.title = board.getTitle();
        this.content = board.getContent();
        this.username = board.getUser().getUsername();
        this.createdAt = board.getCreatedAt();
        this.commentlist = board.getCommentList().stream().map(CommentResponseDto::new).sorted(Comparator.comparing(CommentResponseDto::getCreatedAt).reversed()).collect(Collectors.toList());

        //  아래도가능.
        //this.commentlist = board.getCommentList().stream().map(CommentResponseDto::new).sorted(Comparator.comparing(CommentResponseDto::getCreatedAt, Comparator.reverseOrder())).collect(Collectors.toList());


    }
}
