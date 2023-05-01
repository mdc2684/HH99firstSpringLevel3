package com.sparta.testlevel1.controller;


import com.sparta.testlevel1.dto.CommentRequestDto;
import com.sparta.testlevel1.dto.CommentResponseDto;
import com.sparta.testlevel1.dto.MsgResponseDto;
import com.sparta.testlevel1.security.UserDetailsImpl;
import com.sparta.testlevel1.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/comment/")
@RequiredArgsConstructor
public class CommentController  {

    private final CommentService commentService;

    // 댓글 작성하기
    @PostMapping("/{boardId}")
    public CommentResponseDto commentWrite(@PathVariable Long boardId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.write(boardId,commentRequestDto,userDetails.getUser());
    }

    // 댓글 수정하기
    @PutMapping("/{commentId}")
    public CommentResponseDto commentUpdate(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.update(commentId, commentRequestDto, userDetails.getUser());
    }

    // 댓글 삭제하기
    @DeleteMapping("/{id}")
    public ResponseEntity<MsgResponseDto> commentDelete(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.delete(id,userDetails.getUser());
    }

    //좋아요
    @PostMapping("/like/{boardId}/{commentId}")
    public ResponseEntity<MsgResponseDto> likeComment(@PathVariable Long boardId, @PathVariable Long commentId,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.likeComment(boardId, commentId, userDetails.getUser());
    }
}
