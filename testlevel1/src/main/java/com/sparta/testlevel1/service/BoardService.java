package com.sparta.testlevel1.service;

import com.sparta.testlevel1.dto.BoardRequestDto;
import com.sparta.testlevel1.dto.BoardResponseDto;
import com.sparta.testlevel1.dto.MsgResponseDto;
import com.sparta.testlevel1.entity.Board;
import com.sparta.testlevel1.entity.User;
import com.sparta.testlevel1.exception.CustomException;
import com.sparta.testlevel1.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.sparta.testlevel1.entity.UserRoleEnum.ADMIN;
import static com.sparta.testlevel1.exception.ErrorCode.BOARD_NOT_FOUND;
import static com.sparta.testlevel1.exception.ErrorCode.INVALID_USER;

// Repository 데이터베이스에 잘 연결해주면되는 역할

@Service //전부 비지니스로직?을 수행하는 Bean(객체)로 등록,
@RequiredArgsConstructor // final 찾아서 주입
public class BoardService {

    //데이터베이스와 연결을 해야하기 때문에 BoardRepository를 사용할수있도록 추가
    private final BoardRepository boardRepository;

    //게시글 작성
    @Transactional  //?
    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto, User user) {

         // 요청받은 DTO로 DB에 저장할 객체 만들기
        Board board = boardRepository.save(new Board(boardRequestDto, user));

        return new BoardResponseDto(board);
        }

    //게시글 전체 조회
    @Transactional(readOnly = true)
    public List<BoardResponseDto> getBoardList(Pageable pageable) {
        //최근순서로하기위해 repository로가서 findAllByOrderByModifiedAtDesc()메서드만들기
        List <BoardResponseDto> boards = boardRepository.findAll(pageable).stream().map(BoardResponseDto::new).toList();;
        if (boards.isEmpty()) {
            throw new CustomException(BOARD_NOT_FOUND);
        }
        return boards;
    }

    // 게시글 수정하기
    @Transactional(readOnly = true)
    public BoardResponseDto update(Long id, BoardRequestDto boardRequestDto, User user) {

        // 수정할 데이터가 존재하는지 확인하는 과정 먼저 필요
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );
        // 권한확인
        if (!board.getUser().getUsername().equals(user.getUsername()) || user.getRole() == ADMIN) {
            throw new CustomException(INVALID_USER);
        }

        board.update(boardRequestDto);

        return new BoardResponseDto(board);
        }

    //게시글삭제하기
    @Transactional(readOnly = true)
    public ResponseEntity<MsgResponseDto> deleteBoard(Long id, User user) {

        // 삭제할 데이터가 존재하는지 확인하는 과정 먼저 필요
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        );

        if (!board.getUser().getUsername().equals(user.getUsername())) {
            throw new CustomException(INVALID_USER);
        }

        boardRepository.delete(board);

        return ResponseEntity.ok(new MsgResponseDto("삭제완료!", HttpStatus.OK.value()));
       }

    // 게시글 하나만 조회하기
    @Transactional(readOnly = true)
    public BoardResponseDto getBoardone(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new CustomException(BOARD_NOT_FOUND)
        ); // (id) -> 클라에서 받아온 id  + // orelse``` 추가
        return new BoardResponseDto(board);
    }

    @Transactional(readOnly = true)
    public List<BoardResponseDto> search(String keyword) {
        return boardRepository.findByTitleContainingOrderByCreatedAtDesc(keyword).stream().map(BoardResponseDto::new).toList();
    }



    ///////토큰체크/////////
//    public User checkToken(HttpServletRequest request) {
//        // Request에서 Token 가져오기
//        String token = jwtUtil.resolveToken(request); // request 토큰값 찾기
//        Claims claims;
//
//        // 토큰이 있는경우에만 작성 가능
//        if (token != null) {
//            if (jwtUtil.validateToken(token)) {  // 토큰 유효 검사
//                claims = jwtUtil.getUserInfoFromToken(token);   //토큰에서 사용자 정보 가져오기
//            } else {
//                throw new CustomException(INVALID_TOKEN);
//            }
//            User user = userRepository.findUserByUsername(claims.getSubject())
//                    .orElseThrow(  () -> new CustomException(UNAUTHORIZED_USER));
//
//            return user;
//            }
//        return null;
//    }
}
