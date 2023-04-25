package com.sparta.testlevel1.repository;


import com.sparta.testlevel1.dto.CommentResponseDto;
import com.sparta.testlevel1.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends   JpaRepository<Comment, Long> {
    List <CommentResponseDto> findByBoard_Id(Long board_id);

}
