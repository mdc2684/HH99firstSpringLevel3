package com.sparta.testlevel1.service;

import com.sparta.testlevel1.dto.MsgResponseDto;
import com.sparta.testlevel1.entity.*;
import com.sparta.testlevel1.exception.CustomException;
import com.sparta.testlevel1.repository.BoardLikesRepository;
import com.sparta.testlevel1.repository.BoardRepository;
import com.sparta.testlevel1.repository.CommentLikesRepository;
import com.sparta.testlevel1.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.testlevel1.exception.ErrorCode.BOARD_NOT_FOUND;
import static com.sparta.testlevel1.exception.ErrorCode.COMMENT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final CommentLikesRepository commentLikesRepository;
    private final BoardLikesRepository boardLikesRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;


    //게시글 좋아요
    @Transactional
    public ResponseEntity<MsgResponseDto> likeBoard(Long id, User user) {
        // 게시글 존재확인.
        Board board = boardRepository.findById(id).orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));

        if (boardLikesRepository.findByUserAndBoard(user, board) == null) {
            board.plusLiked();
            boardLikesRepository.save(new BoardLikes(user, board));
            return ResponseEntity.ok(new MsgResponseDto("좋아요!!", HttpStatus.OK.value()));

        } else {
            BoardLikes boardLikes = boardLikesRepository.findByUserAndBoard(user, board);
            board.minusLiked();
            boardLikesRepository.delete(boardLikes);
            return ResponseEntity.ok(new MsgResponseDto("좋아요취소ㅜㅜ", HttpStatus.OK.value()));
        }
    }

    //댓글 좋아요
    @Transactional
    public ResponseEntity<MsgResponseDto> likeComment(Long commentId, User user) {
        // 댓글 존재확인.
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));

        if (commentLikesRepository.findByUserAndComment(user, comment) == null) {
            comment.plusLiked();
            commentLikesRepository.save(new CommentLikes(user, comment));
            return ResponseEntity.ok(new MsgResponseDto("좋아요!!", HttpStatus.OK.value()));

        } else {
            CommentLikes commentLikes = commentLikesRepository.findByUserAndComment(user, comment);
            comment.minusLiked();
            commentLikesRepository.delete(commentLikes);
            return ResponseEntity.ok(new MsgResponseDto("좋아요취소ㅜㅜ", HttpStatus.OK.value()));
        }
    }
}
