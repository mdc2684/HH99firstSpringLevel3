package com.sparta.testlevel1.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor
@Entity(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true)
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z]).{4,10}" , message = "영문자와 숫자로 이루어진 4~10자여야합니다 ")
    private String username;

    @Column(nullable = false)
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[#?!@$%^&*-]).{8,15}", message = "비밀번호는 특수문자,영문자,숫자를 포함하여 8~15자로 입력해주세요")
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    public User(String username, String password, UserRoleEnum role) { // 초기화
        this.username = username;
        this.password = password;
        this.role = role;

    }

}
