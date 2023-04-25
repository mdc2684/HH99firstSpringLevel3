package com.sparta.testlevel1.Exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ErrorResponse {
    private final String message;
    private final int statusCode;

    // 에러 반환 형식
    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .message(errorCode.getMessage())
                        .statusCode(errorCode.getHttpStatus().value())
                        .build()
                );
    }
//
//    // 에러 반환 형식
//    public static ResponseEntity<ErrorResponse> toResponseEntityValid(String errorCode, HttpStatus httpStatus) {
//        return ResponseEntity
//                .status(httpStatus.value())
//                .body(ErrorResponse.builder()
//                        .message(errorCode)
//                        .statusCode(httpStatus.value())
//                        .build()
//                );
//    }
}