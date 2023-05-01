package com.sparta.testlevel1.repository;

import com.sparta.testlevel1.entity.Board;
import com.sparta.testlevel1.entity.BoardLikes;
import com.sparta.testlevel1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikesRepository extends JpaRepository<BoardLikes, Long> {
    BoardLikes findByUserAndBoard(User user, Board board);
}
