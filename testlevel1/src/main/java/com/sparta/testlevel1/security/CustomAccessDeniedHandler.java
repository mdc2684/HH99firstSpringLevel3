//package com.sparta.testlevel1.security;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.sparta.testlevel1.exception.ErrorCode;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class CustomAccessDeniedHandler implements AccessDeniedHandler {
//   //403
//
//    @Override
//    public void handle(HttpServletRequest request, HttpServletResponse response,
//                       AccessDeniedException accessDeniedException) throws IOException{
//
//        response.setContentType("application/json; charset=utf8");
//
//        String json = new ObjectMapper().writeValueAsString(ErrorCode.INVALID_USER);
//        response.getWriter().write(json);
//
//        }
//    }
