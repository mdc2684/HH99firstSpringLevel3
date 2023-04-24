package com.sparta.testlevel1.controller;


import com.sparta.testlevel1.dto.*;
import com.sparta.testlevel1.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;



@RestController
@RequestMapping("/comment/")
@RequiredArgsConstructor
public class CommentController  {

    private final CommentService commentService;

    // 댓글 작성하기
    @PostMapping("/{boardId}")
    public CommentResponseDto commentWrite(@PathVariable Long boardId, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) {
        return commentService.write(boardId,commentRequestDto,request);

    }
//
//    // 댓글 수정하기
//    @PutMapping("/{id}")
//    public CommentRequestDto commentUpdate(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) {
//        return commentService.update(id, commentRequestDto, request);
//    }
//
//    // 댓글 삭제하기
//    @DeleteMapping("/api/board/{id}")
//    public ResponseEntity<MsgResponseDto> deleteBoard(@PathVariable Long id, HttpServletRequest request) {
//        commentService.deleteBoard(id,request);
//        return ResponseEntity.ok(new MsgResponseDto("삭제완료!", HttpStatus.OK.value()));
//    }


}
