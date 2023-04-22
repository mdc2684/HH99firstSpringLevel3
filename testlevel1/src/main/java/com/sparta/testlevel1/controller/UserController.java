package com.sparta.testlevel1.controller;


import com.sparta.testlevel1.dto.LoginRequestDto;
import com.sparta.testlevel1.dto.MsgResponseDto;
import com.sparta.testlevel1.dto.SignupRequestDto;
import com.sparta.testlevel1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController // ***************************
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<MsgResponseDto> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        userService.signUp(signupRequestDto); //ResponseEntity: 결과값, 상태코드, 헤더값을 모두 프론트에 넘겨줄 수 있고, 에러코드 또한 섬세하게 설정해서 보내줄 수 있음
        return ResponseEntity.ok(new MsgResponseDto("회원가입완료", HttpStatus.OK.value()));
    }

    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<MsgResponseDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {  // 만들어준 Token을 넣어주기위해
        userService.login(loginRequestDto, response);
        return ResponseEntity.ok(new MsgResponseDto("로그인완료", HttpStatus.OK.value()));
    }
}
