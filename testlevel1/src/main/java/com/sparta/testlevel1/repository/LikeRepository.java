package com.sparta.testlevel1.repository;

import com.sparta.testlevel1.entity.Board;
import com.sparta.testlevel1.entity.Comment;
import com.sparta.testlevel1.entity.Likes;
import com.sparta.testlevel1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    Likes findByUserAndBoard(User user, Board board);
    Likes findByUserAndComment(User user, Comment comment);
}
