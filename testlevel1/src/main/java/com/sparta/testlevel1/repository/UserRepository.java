package com.sparta.testlevel1.repository;

import com.sparta.testlevel1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);  // 왜 옵셔널해야하는지 모르겠음

}
