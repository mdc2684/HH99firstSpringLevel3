package com.sparta.testlevel1.controller;

import com.sparta.testlevel1.dto.MsgResponseDto;
import com.sparta.testlevel1.security.UserDetailsImpl;
import com.sparta.testlevel1.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikesController {

    private final LikesService likesService;

    //게시글좋아요
    @PostMapping("/api/board/like/{id}")
    public ResponseEntity<MsgResponseDto> likeBoard(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        System.out.println(userDetails);
        return likesService.likeBoard(id, userDetails.getUser());
    }

    //댓글좋아요
    @PostMapping("/comment/like/{commentId}")
    public ResponseEntity<MsgResponseDto> likeComment(@PathVariable Long commentId,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likesService.likeComment(commentId, userDetails.getUser());
    }
}
