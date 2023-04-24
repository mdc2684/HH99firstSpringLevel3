package com.sparta.testlevel1.dto;


import com.sparta.testlevel1.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDto {
    private Long boardId;
    private String content;


    public CommentRequestDto(String content, Long boardId) {
        this.boardId = boardId;
        this.content = content;
    }
}
