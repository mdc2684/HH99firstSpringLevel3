package com.sparta.testlevel1.dto;

import lombok.Getter;

// Dto : 데이터를 어리저리 옮겨다니는 바구니 (Data Trans~ Object)
// entity board는 원본데이터니까 안정성을 위해 바로 쓰지말고 Dto로 따로 저장해서 사용

@Getter
public class BoardRequestDto {
    private String title;  // ID는 우리가 정하기때문에 클라가 입력할 필요없음.
    private String content;

}
