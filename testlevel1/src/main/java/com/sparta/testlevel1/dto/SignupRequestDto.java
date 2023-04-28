package com.sparta.testlevel1.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter //인스턴스 변수를 반환하기위해
@Setter // 인스턴스 변수를 수정하기위해
@Component
public class SignupRequestDto {

    private String username;

    @NotNull(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[#?!@$%^&*-]).{8,15}")
    private String password;

    private boolean admin = false;  // 왜 false를 해야하는지??
    private String adminToken = "";
}
