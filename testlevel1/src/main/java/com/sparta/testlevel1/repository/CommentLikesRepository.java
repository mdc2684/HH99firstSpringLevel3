package com.sparta.testlevel1.repository;

import com.sparta.testlevel1.entity.Board;
import com.sparta.testlevel1.entity.Comment;
import com.sparta.testlevel1.entity.CommentLikes;
import com.sparta.testlevel1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikesRepository extends JpaRepository<CommentLikes, Long> {

    CommentLikes findByUserAndComment(User user, Comment comment);
}
