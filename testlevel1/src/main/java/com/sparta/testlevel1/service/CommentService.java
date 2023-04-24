package com.sparta.testlevel1.service;


import com.sparta.testlevel1.dto.BoardRequestDto;
import com.sparta.testlevel1.dto.BoardResponseDto;
import com.sparta.testlevel1.dto.CommentRequestDto;
import com.sparta.testlevel1.dto.CommentResponseDto;
import com.sparta.testlevel1.entity.Board;
import com.sparta.testlevel1.entity.Comment;
import com.sparta.testlevel1.entity.User;
import com.sparta.testlevel1.jwt.JwtUtil;
import com.sparta.testlevel1.repository.BoardRepository;
import com.sparta.testlevel1.repository.CommentRepository;
import com.sparta.testlevel1.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final BoardRepository boardRepository;
    private final CommentResponseDto commentResponseDto;
    private final JwtUtil jwtUtil;


    @Transactional
    public CommentResponseDto write(Long boardId, CommentRequestDto commentRequestDto, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request); // request 토큰값 찾기
        Claims claims;

        // 토큰이 있는경우에만 댓글작성 가능
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                //토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {  throw new IllegalArgumentException("Token Error!!!!!! ");   }

            // 게시글 존재여부 확인.
            Board board = boardRepository.findById(boardId).orElseThrow(
                    () -> new IllegalArgumentException("존재하지 않는 게시글입니다.")
            );



            //Comment comment = commentRepository.save(new Comment(commentRequestDto, board));

           // return new CommentResponseDto(comment);
        }
        return commentResponseDto;

    }
//
//    @Transactional
//    public BoardResponseDto update(Long id, CommentRequestDto commentRequestDto, HttpServletRequest request) {
//        // Request에서 Token 가져오기
//        String token = jwtUtil.resolveToken(request); // request 토큰값 찾기
//        Claims claims;
//
//        // 토큰이 있는경우에만 댓글수정 가능
//        if (token != null) {
//            if (jwtUtil.validateToken(token)) {
//                //토큰에서 사용자 정보 가져오기
//                claims = jwtUtil.getUserInfoFromToken(token);  // Token으로 user정보 얻고 user정보를 claims에 저장
//            } else {
//                throw new IllegalArgumentException("Token Error");
//            }
//
//            // 토큰에서 가져온 사용자 정보를 사용하여 DB조회
//            User user = userRepository.findUserByUsername(claims.getSubject()).orElseThrow(
//                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다")
//            );
//
//            // 수정할 데이터가 존재하는지 확인하는 과정 먼저 필요
//            Board board = boardRepository.findById(id).orElseThrow(
//                    () -> new IllegalArgumentException("게시판이 존재하지 않습니다")
//            );
//
//            if (!board.getUsername().equals(claims.getSubject())) {
//                throw new IllegalArgumentException("수정 권한이 없습니다");
//            }
//
//            comment.update(commentRequestDto);
//
//            return new BoardResponseDto(board); // throw 어케?
//        } else {
//            throw new NullPointerException("토큰이없습니다");
//        }
//    }

}
