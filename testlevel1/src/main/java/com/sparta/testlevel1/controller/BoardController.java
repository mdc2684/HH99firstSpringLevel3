// Client <-Dto-> Controller <-Dto-> Service <-Dto-> Repository <-Entity-> DB
package com.sparta.testlevel1.controller;

import com.sparta.testlevel1.dto.BoardRequestDto;
import com.sparta.testlevel1.dto.BoardResponseDto;
import com.sparta.testlevel1.dto.MsgResponseDto;
import com.sparta.testlevel1.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 게시글 작성하기
    @PostMapping("/api/board")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto boardrequestDto, HttpServletRequest request) {  // Dto로 받아올꺼니까 여기서 Dto작성하러Go
        // 서비스개발하러Go
        return boardService.createBoard(boardrequestDto, request); // 클라에서 가져온 dto 사용
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
    public BoardResponseDto updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto boardRequestDto, HttpServletRequest request) {
        return boardService.update(id, boardRequestDto, request);
    }

    //삭제하기
    @DeleteMapping("/api/board/{id}")
    public ResponseEntity<MsgResponseDto> deleteBoard(@PathVariable Long id,  HttpServletRequest request) {
        return boardService.deleteBoard(id,request);
    }
}
