package com.sparta.testlevel1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter //인스턴스 변수를 반환하기위해
@Setter // 인스턴스 변수를 수정하기위해
public class SignupRequestDto {

    private String username;
    private String password;
    private boolean admin = false;  // 왜 false를 해야하는지??
    private String adminToken = "";
}
