package com.sparta.testlevel1.Config;

import com.sparta.testlevel1.jwt.JwtAuthFilter;
import com.sparta.testlevel1.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured 어노테이션 활성화
public class WebSecurityConfig {
    private final JwtUtil jwtUtil;
    private final JwtAuthFilter jwtAuthFilter;

    // 비밀번호 암호화기능 등록
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {  //아래 securityFilterChain보다 우선적으로 걸림
        // h2-console 사용 및 resources 접근 허용 설정
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console())
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors();

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                        .antMatchers(HttpMethod.POST, "/api/user/**").permitAll()
                        .antMatchers(HttpMethod.GET,"/api/board/**").permitAll()
//                .antMatchers(
//                        "/swagger-resources/**",
//                        "/swagger-ui.html",
//                        "/v2/api-docs",
//                        "/webjars/**")
//                .permitAll()
                        .anyRequest().authenticated();     //  permitAll()해준 이외의 url들은 Authentication 인증된 사용자한테만 허용한다.


        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
                // JWT 인증/인가를 사용하기 위한 설정 , UsernamePasswordAuthenticationFilter보다 JwtAuthFilter 먼저 실행
//
//        // 예외 핸들링 메서드
//        http.exceptionHandling()
//
//                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())  // 인증 예외 핸들러 지정
//                .accessDeniedHandler(new CustomAccessDeniedHandler());  // 인가 예외 핸들러 지정

        return http.build();
    }
}