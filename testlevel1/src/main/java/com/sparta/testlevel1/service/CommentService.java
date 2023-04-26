package com.sparta.testlevel1.service;

import com.sparta.testlevel1.Exception.CustomException;
import com.sparta.testlevel1.dto.CommentRequestDto;
import com.sparta.testlevel1.dto.CommentResponseDto;
import com.sparta.testlevel1.dto.MsgResponseDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import static com.sparta.testlevel1.Exception.ErrorCode.*;
import static com.sparta.testlevel1.entity.UserRoleEnum.ADMIN;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public CommentResponseDto write(Long id, CommentRequestDto commentRequestDto, HttpServletRequest request) {

        User user = checkToken(request);

        // 게시글 유무 확인.
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));

        // 게시글 id의 댓글목록들.


        // 게시글이 있다면 댓글저장하고 댓글반환하기.
        Comment comment = commentRepository.save(new Comment(commentRequestDto, user, board));
        return new CommentResponseDto(comment);
    }


    @Transactional
    public CommentResponseDto update(Long id, CommentRequestDto commentRequestDto, HttpServletRequest request) {
        User user = checkToken(request);  // user는 현재접속중인 유저정보

        //  댓글 유무 확인.
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));


        // 사용자가 작성한 댓글 or ADMIN 만 수정가능
        if (comment.getUser().getUsername().equals(user.getUsername()) || user.getRole() == ADMIN ) {

            comment.update(commentRequestDto);
        }

        return new CommentResponseDto(comment); // throw 어케?
    }

    // 댓글삭제
    @Transactional
    public ResponseEntity<MsgResponseDto> delete(Long id, HttpServletRequest request) {
        User user = checkToken(request);  // user는 현재접속중인 유저정보

        //  댓글 유무 확인.
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));

        // 사용자가 작성한 댓글 or ADMIN 만 삭제가능
        if (comment.getUser().getUsername().equals(user.getUsername()) || user.getRole() == ADMIN ) {
            commentRepository.delete(comment);
        }

        return ResponseEntity.ok(new MsgResponseDto("삭제완료!", HttpStatus.OK.value()));
        }

    /////////토큰체크/////////
    public User checkToken(HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request); // request 토큰값 찾기
        Claims claims;

        // 토큰이 있는경우에만 댓글작성 가능
        if (token != null) {
            if (jwtUtil.validateToken(token)) {  // 토큰 유효 검사
                claims = jwtUtil.getUserInfoFromToken(token);   //토큰에서 사용자 정보 가져오기
            } else {
                throw new CustomException(INVALID_AUTH_TOKEN);
            }
            User user = userRepository.findUserByUsername(claims.getSubject())
                    .orElseThrow(  () -> new CustomException(UNAUTHORIZED_USER));

            return user;
        }
        return null;
    }
}
