package com.sparta.testlevel1.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.testlevel1.dto.SecurityExceptionDto;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    //protexted?
        String token = jwtUtil.resolveToken(request);

        if(token != null) {  // token null인지
            if(!jwtUtil.validateToken(token)){
                jwtExceptionHandler(response, "Token Error!!!!!!!", HttpStatus.UNAUTHORIZED.value());
                return;
           }
            Claims info = jwtUtil.getUserInfoFromToken(token);  //  info : 토큰으로 찾은 유저의정보
            setAuthentication(info.getSubject());
        }
        filterChain.doFilter(request,response);
    }

    // 시큐리티로 인증을 한 사용자의 상세 정보를 저장한다.
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtUtil.createAuthentication(username); // Authentication 인증 객체를 넣음.
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 토큰에 대한 오류발생시, Client로 커스터마이징해서 Exception처리값을 알려준다.
    public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new SecurityExceptionDto(statusCode, msg));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}