package com.sparta.testlevel1.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@Getter, @Setter: 필드에 선언시 자동으로 get, set 메소드 생성. 클래스에서 선언시 모든 필드에 접근자와 설정자가 자동으로 생성
@Getter
//모든 필드값을 파라미터로 받는 생성자 생성
@AllArgsConstructor
//파라미터가 없는 기본생성자를 생성
@NoArgsConstructor
//해당 클래스에 자동으로 빌더를 추가
@Builder
public class MsgResponseDto {
    private String msg;
    private int statusCode;

    //@AllArgsConstructor 덕분에, 모든 필드값을 파라미터로 받는 생성자를 굳이 생성해주지 않아도 됨
//    public MsgResponseDto(String msg, int statusCode) {
//        this.msg = msg;
//        this.statusCode = statusCode;
//    }

}
