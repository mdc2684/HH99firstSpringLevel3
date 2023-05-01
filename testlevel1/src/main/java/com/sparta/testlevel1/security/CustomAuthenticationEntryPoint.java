//package com.sparta.testlevel1.security;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.sparta.testlevel1.exception.ErrorResponse;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//import static com.sparta.testlevel1.exception.ErrorCode.INVALID_AUTH_TOKEN;
//
//@Component
//public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
//
//    @Override
//    public void commence(HttpServletRequest request,
//                         HttpServletResponse response,
//                         AuthenticationException authenticationException) throws IOException {
//
//        response.setContentType("application/json; charset=utf8");
//
//        //*********
//        String json = new ObjectMapper().writeValueAsString(ErrorResponse.toResponseEntity(INVALID_AUTH_TOKEN).getBody());  // getbody로 되긴함.
//        // 위에코드 이해불가.
//        response.getWriter().write(json);
//
//        }
//    }
