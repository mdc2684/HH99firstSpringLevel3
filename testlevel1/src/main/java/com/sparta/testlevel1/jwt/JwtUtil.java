package com.sparta.testlevel1.jwt;


import com.sparta.testlevel1.dto.RefreshTokenRepository;
import com.sparta.testlevel1.dto.TokenDto;
import com.sparta.testlevel1.entity.RefreshToken;
import com.sparta.testlevel1.entity.UserRoleEnum;
import com.sparta.testlevel1.security.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    public static final String AUTHORIZATION_KEY = "auth";
    private static final String BEARER_PREFIX = "Bearer ";

    public static final String ACCESS_KEY = "ACCESS_KEY";
    public static final String REFRESH_KEY = "REFRESH_KEY";
    private static final long ACCESSTOKEN_TIME = 60 * 60 * 1000L;
    private static final long REFRESHTOKEN_TIME = 7*24*60 * 60 * 1000L;


    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public TokenDto createAllToken(String username, UserRoleEnum userRoleEnum) {
        return new TokenDto(createToken(username, userRoleEnum, ACCESS_KEY), createToken(username, userRoleEnum, REFRESH_KEY));
    }

    // header 토큰을 가져오기
    public String resolveToken(HttpServletRequest request,String token) {
        String tokenName = token.equals(ACCESS_KEY) ? ACCESS_KEY : REFRESH_KEY;
        String bearerToken = request.getHeader(tokenName);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 생성
    public String createToken(String username, UserRoleEnum role, String tokenName) {
        Date date = new Date();
        long time = tokenName.equals(ACCESS_KEY) ? ACCESSTOKEN_TIME : REFRESHTOKEN_TIME;

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime() + time))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        } catch (NullPointerException e) {
            log.info("JWT 토큰이 없습니다!");
        }
        return false;
    }

    // 인증객체 생성  이 메서드는 jwtauthfilter에 넣어도 된다함.
    public Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        // 인증객체를 만듦. 이 인증객체는 SecurityContextHolder에 세팅됨.
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }  //principal 사용자식별,UserDetails의 인스턴스   credentials 주로비밀번호,사용자인증에 사용하고 비운다  authorities 사용자에게 부여한 권한을 GrantedAuthority로 추상화하여 사용한다.
    //리프레시 토큰 검증
    public Boolean refreshTokenValidation(String token) {
        // 1차 토큰 검증
        if (!validateToken(token)) return false;

        // DB에 저장한 토큰 비교
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUsername(getUserInfoFromToken(token));

        return refreshToken.isPresent() && token.equals(refreshToken.get().getRefreshToken());
    }

    // username을 토큰에서 추출
    public String getUserInfoFromToken(String token) {
        // 매개변수로 받은 token을 키를 사용해서 복호화 (디코딩)
        // 복호화된 토큰의 payload에서 subject에 담긴 것을 가져옴
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
    }

    //액세스 토큰 헤더 설정
    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader(ACCESS_KEY, accessToken);
    }

}