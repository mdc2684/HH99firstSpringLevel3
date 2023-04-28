package com.sparta.testlevel1.service;

import com.sparta.testlevel1.Exception.CustomException;
import com.sparta.testlevel1.Exception.ErrorCode;
import com.sparta.testlevel1.dto.LoginRequestDto;
import com.sparta.testlevel1.dto.MsgResponseDto;
import com.sparta.testlevel1.dto.SignupRequestDto;
import com.sparta.testlevel1.entity.User;
import com.sparta.testlevel1.entity.UserRoleEnum;
import com.sparta.testlevel1.jwt.JwtUtil;
import com.sparta.testlevel1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    //Repository랑 연결필요
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;  // JwtUtill에서 @Component를 해줬기때문에 의존성주입이 가능하다.
    private final PasswordEncoder passwordEncoder;

    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional //왜 쓰지?
    public ResponseEntity<MsgResponseDto> signUp(SignupRequestDto signupRequestDto) {

        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        // 유효성검사.
        if (!isValidUsername(username)) {
            throw new CustomException(ErrorCode.INVALID_USERNAME);
        }
        if(!isValidPassword(signupRequestDto.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_USER_PASSWORD);
        }

        // 회원이름 중복확인필요
        Optional<User> usernamefound = userRepository.findUserByUsername(username);
        if (usernamefound.isPresent()) {
            throw new CustomException(ErrorCode.INVALID_USER_EXISTENCE);
        }

        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isAdmin()) {
            if(!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)){
                throw new IllegalArgumentException("관리자 암호가 틀립니다.");
            } else {
                role = UserRoleEnum.ADMIN;
            }
        }

        User user = new User(username, password, role); // ?

        userRepository.save(user);  // DB에 회원가입정보 저장.

        return ResponseEntity.ok(new MsgResponseDto("회원가입완료", HttpStatus.OK.value()));
    }

    // 로그인
    @Transactional
    public ResponseEntity<MsgResponseDto> login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 사용자 있는지 확인
        User user = userRepository.findUserByUsername(username).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_USER_PASSWORD);
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));

        return ResponseEntity.ok(new MsgResponseDto("로그인완료", HttpStatus.OK.value()));

        //response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user));  // ****
        //addHeader 헤더에 넣어줄수있음.  createToken 토큰만들건데 User user를 가져왔기때문에 이름과 권한을 넣어줄수있음.

    }
    private boolean isValidUsername(String username) {
        String usernamePattern = "^[a-z0-9]{4,10}$";
        return username.matches(usernamePattern);
    }
    private boolean isValidPassword(String password) {
        String passwordPattern =  "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!%*?&])[A-Za-z0-9$@$!%*?&]{8,15}$";
        return password.matches(passwordPattern);
    }
}
