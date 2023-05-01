package com.sparta.testlevel1.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.testlevel1.dto.SecurityExceptionDto;
import com.sparta.testlevel1.entity.User;
import com.sparta.testlevel1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    //protexted?
        String access_token = jwtUtil.resolveToken(request, jwtUtil.ACCESS_KEY);
        String refresh_token = jwtUtil.resolveToken(request, jwtUtil.REFRESH_KEY);

//        if(access_token == null) {
//            jwtExceptionHandler(response,"토큰이 없습니다", 400);
//        }

        if(access_token != null) {  // token null인지
            if(jwtUtil.validateToken(access_token)){  // 액세스토큰 유효성검사
                setAuthentication(jwtUtil.getUserInfoFromToken(access_token));

           } else if (refresh_token != null && jwtUtil.validateToken(refresh_token)){  //액세스토큰만료,유효한 리프레시토큰 존재.
                String username = jwtUtil.getUserInfoFromToken(refresh_token);
                User user = userRepository.findUserByUsername(username).orElseThrow();
                String newAccessToken = jwtUtil.createToken(username, user.getRole(), "Access");
                jwtUtil.setHeaderAccessToken(response, newAccessToken);
                setAuthentication(username);

           } else if (refresh_token  == null) {
                jwtExceptionHandler(response, "Access Token Expired", HttpStatus.OK.value());
           } else {
                jwtExceptionHandler(response, "Refresh Token Expired", HttpStatus.OK.value());
                return;
           }

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