// Client <-Dto-> Controller <-Dto-> Service <-Dto-> Repository <-Entity-> DB
package com.sparta.testlevel1.controller;

import com.sparta.testlevel1.dto.BoardRequestDto;
import com.sparta.testlevel1.dto.BoardResponseDto;
import com.sparta.testlevel1.dto.MsgResponseDto;
import com.sparta.testlevel1.security.UserDetailsImpl;
import com.sparta.testlevel1.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 게시글 작성하기
    @PostMapping("/api/board")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto boardrequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.createBoard(boardrequestDto, userDetails.getUser());
    }

    //모든 게시글 조회하기
    @GetMapping("/api/board")
    public List<BoardResponseDto> getBoard() { // request안에 들어있는 Token값을 가져와야하기때문에
        return boardService.getBoardList();
    }

    //// 선택 게시글 조회하기
    @GetMapping("/api/board/{id}")
    public BoardResponseDto getBoardone(@PathVariable Long id) {

        return boardService.getBoardone(id);
    }

    //게시글 수정하기
    @PutMapping("/api/board/{id}")
    public BoardResponseDto updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto boardRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.update(id, boardRequestDto, userDetails.getUser());
    }

    //삭제하기
    @DeleteMapping("/api/board/{id}")
    public ResponseEntity<MsgResponseDto> deleteBoard(@PathVariable Long id,  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.deleteBoard(id,userDetails.getUser());
    }

    //좋아요
    @PostMapping("/api/board/like/{id}")
    public ResponseEntity<MsgResponseDto> likeBoard(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        System.out.println(userDetails);
        return boardService.likeBoard(id, userDetails.getUser());
    }
}
