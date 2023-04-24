package com.sparta.testlevel1.dto;

import com.sparta.testlevel1.entity.UserRoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter //인스턴스 변수를 반환하기위해
@Setter // 인스턴스 변수를 수정하기위해
@NoArgsConstructor

public class SignupRequestDto {

    private String username;

    @NotNull(message = "비밀번호를 입력해주세요.")
    private String password;
    private boolean admin = false;  // 왜 false를 해야하는지??
    private String adminToken;
}
