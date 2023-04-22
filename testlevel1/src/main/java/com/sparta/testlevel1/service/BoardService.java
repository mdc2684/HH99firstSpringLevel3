package com.sparta.testlevel1.service;

import com.sparta.testlevel1.dto.BoardRequestDto;
import com.sparta.testlevel1.dto.BoardResponseDto;
import com.sparta.testlevel1.dto.MsgResponseDto;
import com.sparta.testlevel1.entity.Board;
import com.sparta.testlevel1.entity.User;
import com.sparta.testlevel1.jwt.JwtUtil;
import com.sparta.testlevel1.repository.BoardRepository;
import com.sparta.testlevel1.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Repository 데이터베이스에 잘 연결해주면되는 역할

@Service //전부 비지니스로직?을 수행하는 Bean(객체)로 등록,
@RequiredArgsConstructor // final 찾아서 주입
public class BoardService {

    //데이터베이스와 연결을 해야하기 때문에 BoardRepository를 사용할수있도록 추가
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional  //?
    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request); // request 토큰값 찾기
        Claims claims;

        // 토큰이 있는경우에만 게시글작성 가능
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                //토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error!!!!!! ");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB조회
            User user = userRepository.findUserByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다")
            );

            // 요청받은 DTO로 DB에 저장할 객체 만들기
            Board board = boardRepository.save(new Board(boardRequestDto, user));

            return new BoardResponseDto(board);
        } else {
            return null;
        }
    }

    //게시글 전체 조회
    @Transactional(readOnly = true)
    public List<BoardResponseDto> getBoardList() {
        //최근순서로하기위해 repository로가서 findAllByOrderByModifiedAtDesc()메서드만들기
        List<Board> boardList = boardRepository.findAllByOrderByCreatedAtDesc();
        List<BoardResponseDto> boardResponseDto = new ArrayList<>();

        for (Board board : boardList) {
            boardResponseDto.add( new BoardResponseDto(board));
        }
        return boardResponseDto;
    }

    // 게시글 수정하기
    @Transactional
    public BoardResponseDto update(Long id, BoardRequestDto boardRequestDto, HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request); // request 토큰값 찾기
        Claims claims;

        // 토큰이 있는경우에만 게시글작성 가능
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                //토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);  // Token으로 user정보 얻고 user정보를 claims에 저장
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB조회
            User user = userRepository.findUserByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다")
            );

            // 수정할 데이터가 존재하는지 확인하는 과정 먼저 필요
            Board board = boardRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("게시판이 존재하지 않습니다")
            );

            if (!board.getUsername().equals(claims.getSubject())) {
                throw new IllegalArgumentException("수정 권한이 없습니다");
            }

            board.update(boardRequestDto);

            return new BoardResponseDto(board); // throw 어케?
        } else {
            throw new NullPointerException("토큰이없습니다");
        }
    }

    //게시글삭제하기
    @Transactional
    public ResponseEntity<MsgResponseDto> deleteBoard(Long id, HttpServletRequest request) {

        String token = jwtUtil.resolveToken(request); // request 토큰값 찾기
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                //토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);  // Token으로 user정보 얻고 user정보를 claims에 저장  ,  claims.getSubject()  :  해당 username
            } else {
                throw new IllegalArgumentException("Token Error");
            }
            // 토큰에서 가져온 사용자 정보를 사용하여 DB조회
            User user = userRepository.findUserByUsername(claims.getSubject()).orElseThrow(
                    () -> new NullPointerException("토큰이 없습니다.")
            );
            // 삭제할 데이터가 존재하는지 확인하는 과정 먼저 필요
            Board board = boardRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("게시판이 존재하지 않습니다")
            );

            if (!board.getUsername().equals(claims.getSubject())) {
                throw new IllegalArgumentException("삭제 권한이 없습니다");
            }

            boardRepository.delete(board);

            return ResponseEntity.ok(new MsgResponseDto("삭제완료!", HttpStatus.OK.value()));
       } else {
            throw new IllegalArgumentException("잘못된 토큰입니다");
        }

    }

    // 게시글 하나만 조회하기
    @Transactional
    public BoardResponseDto getBoardone(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new NullPointerException("아이디가 존재하지 않습니다")
        ); // (id) -> 클라에서 받아온 id  + // orelse``` 추가
        return new BoardResponseDto(board);
    }
}
