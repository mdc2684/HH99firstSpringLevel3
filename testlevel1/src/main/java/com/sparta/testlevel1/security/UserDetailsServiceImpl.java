package com.sparta.testlevel1.security;

import com.sparta.testlevel1.entity.User;
import com.sparta.testlevel1.exception.CustomException;
import com.sparta.testlevel1.exception.ErrorCode;
import com.sparta.testlevel1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    //사용자를 조회하고 검증한 후 UserDetails를 반환
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return new UserDetailsImpl(user, user.getUsername()); //DB에서 찾아온 user객체와 username을 담아서 userDetailsImpl을 반환.
    }

}