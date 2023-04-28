package com.sparta.testlevel1.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */
    INVALID_USERNAME(BAD_REQUEST, "username은 길이가 4자 이상 10자 이하로 구성되어야 합니다."),
    NULL_TOKEN(BAD_REQUEST, "토큰이 없습니다 ( Null Token Error"),
    INVALID_TOKEN(BAD_REQUEST, "토큰이 유효하지 않습니다.."),
    INVALID_USER(BAD_REQUEST, "작성자만 삭제/수정할 수 있습니다."),
    INVALID_USER_EXISTENCE(BAD_REQUEST, "중복된 username 입니다."),
    INVALID_USER_PASSWORD(BAD_REQUEST, "비밀번호는 특수문자,영문자,숫자를 포함하여 8~15자로 입력해주세요."),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "권한 정보가 없는 토큰입니다"),
    UNAUTHORIZED_USER(UNAUTHORIZED, "현재 내 계정 정보가 존재하지 않습니다"),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    USER_NOT_FOUND(NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),
    BOARD_NOT_FOUND(NOT_FOUND, "게시글을 찾을 수 없습니다"),
    COMMENT_NOT_FOUND(NOT_FOUND, "댓글을 찾을 수 없습니다"),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(CONFLICT, "데이터가 이미 존재합니다");

    private final HttpStatus httpStatus;
    private final String message;
}