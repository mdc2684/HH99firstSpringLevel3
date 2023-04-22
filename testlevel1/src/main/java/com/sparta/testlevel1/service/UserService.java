package com.sparta.testlevel1.service;

import com.sparta.testlevel1.dto.LoginRequestDto;
import com.sparta.testlevel1.dto.SignupRequestDto;
import com.sparta.testlevel1.entity.User;
import com.sparta.testlevel1.entity.UserRoleEnum;
import com.sparta.testlevel1.jwt.JwtUtil;
import com.sparta.testlevel1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor // 이게있어야 final 에러 사라진다 왜일까?
public class UserService {

    //Repository랑 연결필요
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;  // JwtUtill에서 @Component를 해줬기때문에 의존성주입이 가능하다.

    @Transactional //왜 쓰지?
    public void signUp(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        // 회원이름 중복확인필요
        Optional<User> usernamefound = userRepository.findUserByUsername(username);
        if (usernamefound.isPresent()) {
            throw new IllegalArgumentException("이미 회원이름이 존재합니다.");
        }

        User user = new User(username, password); // ?
        userRepository.save(user);

    }
    @Transactional
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 사용자 있는지 확인
        User user = userRepository.findUserByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );
        // 비밀번호 확인
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername()));

        //response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user));  // ****
        //addHeader 헤더에 넣어줄수있음.  createToken 토큰만들건데 User user를 가져왔기때문에 이름과 권한을 넣어줄수있음.

    }
}
