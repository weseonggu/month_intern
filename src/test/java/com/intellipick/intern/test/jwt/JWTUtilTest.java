package com.intellipick.intern.test.jwt;

import com.intellipick.intern.domain.type.UserRole;
import com.intellipick.intern.security.JWTUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("dev")
public class JWTUtilTest {

    @Autowired
    private JWTUtil jwtUtil;

    private String username = "testUser";
    private Long userId = 1L;
    private String userRole = UserRole.ROLE_USER.getValue();

    private String accessToken;
    private String refreshToken;

    @BeforeEach
    public void setUp() {
        // 테스트용 토큰 생성
        accessToken = jwtUtil.createAccessToken(userId, username, userRole);
        refreshToken = jwtUtil.createRefreshToken(userId, username, userRole);
    }

    @Test
    public void testCreateAccessToken() {
        // accessToken이 null이 아니어야 한다.
        assertNotNull(accessToken);

        // 토큰 앞에 "Bearer "가 포함되어 있어야 한다.
        assertTrue(accessToken.startsWith(JWTUtil.BEARER_PREFIX));
    }

    @Test
    public void testCreateRefreshToken() {
        // refreshToken이 null이 아니어야 한다.
        assertNotNull(refreshToken);

        // 토큰 앞에 "Bearer "가 포함되어 있어야 한다.
        assertTrue(refreshToken.startsWith(JWTUtil.BEARER_PREFIX));
    }

    @Test
    public void testValidateAccessToken() {
        // 유효한 accessToken이기 때문에 예외가 발생하지 않아야 한다.
        assertDoesNotThrow(() -> jwtUtil.validateAccessToken(accessToken.substring(JWTUtil.BEARER_PREFIX.length())));
    }

    @Test
    public void testValidateRefreshToken() {
        // 유효한 refreshToken이기 때문에 예외가 발생하지 않아야 한다.
        assertDoesNotThrow(() -> jwtUtil.validateRefreshToken(refreshToken.substring(JWTUtil.BEARER_PREFIX.length())));
    }

    @Test
    public void testGetUserInfoFromAccessToken() {
        Claims claims = jwtUtil.getUserInfoFromAccessToken(accessToken.substring(JWTUtil.BEARER_PREFIX.length()));

        // 토큰에서 가져온 사용자 정보가 예상한 값과 일치하는지 확인
        assertEquals(userId, claims.get("id", Long.class));
        assertEquals(username, claims.get("username"));
        assertEquals(userRole, claims.get("role"));
    }

    @Test
    public void testGetUserInfoFromRefreshToken() {

        Claims claims = jwtUtil.getUserInfoFromRefreshToken(refreshToken.substring(JWTUtil.BEARER_PREFIX.length()));

        // 토큰에서 가져온 사용자 정보가 예상한 값과 일치하는지 확인
        assertEquals(userId, claims.get("id", Long.class));
        assertEquals(username, claims.get("username"));
        assertEquals(userRole, claims.get("role"));
    }



}
