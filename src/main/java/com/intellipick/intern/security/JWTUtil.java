package com.intellipick.intern.security;



import com.intellipick.intern.domain.type.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {
	// Header KEY 값
	public static final String AUTHORIZATION_HEADER = "Authorization";
	// 사용자 권한 값의 KEY
	// Token 식별자
	public static final String BEARER_PREFIX = "Bearer ";
	// 토큰 만료시간
	private long ACCESS_TOKEN_TIME;

	private long REFESH_TOKEN_TIME;


	private Key AKey;
	private Key RKey;
	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	// 로그 설정
	public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");


	public JWTUtil(@Value("${jwt.access-key}")String accessKey, @Value("${jwt.refresh-key}")String refreshKey
			     , @Value("${jwt.a-time}")long ACCESS_TOKEN_TIME, @Value("${jwt.r-time}")long REFESH_TOKEN_TIME){
		this.AKey = Keys.hmacShaKeyFor(accessKey.getBytes(StandardCharsets.UTF_8));
		this.RKey = Keys.hmacShaKeyFor(refreshKey.getBytes(StandardCharsets.UTF_8));
		this.ACCESS_TOKEN_TIME = ACCESS_TOKEN_TIME;
		this.REFESH_TOKEN_TIME = REFESH_TOKEN_TIME;
	}

	// 토큰 생성
	public String createAccessToken(Long id, String username, String role) {
		Date date = new Date();

		return BEARER_PREFIX +
				Jwts.builder()
						.claim("id",id)
						.claim("username", username)
						.claim("role", role)
						.setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
						.setIssuedAt(date)
						.signWith(AKey, signatureAlgorithm)
						.compact();
	}

	public String createRefreshToken(Long id, String username, String role) {
		Date date = new Date();

		return BEARER_PREFIX +
				Jwts.builder()
						.claim("id",id)
						.claim("username", username)
						.claim("role", role)
						.setExpiration(new Date(date.getTime() + REFESH_TOKEN_TIME))
						.setIssuedAt(date)
						.signWith(RKey, signatureAlgorithm)
						.compact();
	}


	/**
	 * JWT 토큰 substring
	 */
	public String substringToken(String tokenValue) throws UnsupportedEncodingException {
		if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
			return tokenValue.substring(7);
		}
		logger.error("Not Found Token");
		throw new UnsupportedJwtException("Not Found Token");
	}


	/**
	 * 토큰 검증
	 * @param token
	 * @return
	 */
	public void validateAccessToken(String token) throws SecurityException, MalformedJwtException, SignatureException, ExpiredJwtException,UnsupportedJwtException, IllegalArgumentException
	{

		Jwts.parserBuilder().setSigningKey(AKey).build().parseClaimsJws(token);

	}
	public void validateRefreshToken(String token) throws SecurityException, MalformedJwtException, SignatureException, ExpiredJwtException,UnsupportedJwtException, IllegalArgumentException
	{

		Jwts.parserBuilder().setSigningKey(RKey).build().parseClaimsJws(token);

	}

	/**
	 * 토큰에서 사용자 정보 가져오기
	 * @param token
	 * @return
	 */
	public Claims getUserInfoFromAccessToken(String token) {
		return Jwts.parserBuilder().setSigningKey(AKey).build().parseClaimsJws(token).getBody();
	}
	public Claims getUserInfoFromRefreshToken(String token) {
		return Jwts.parserBuilder().setSigningKey(RKey).build().parseClaimsJws(token).getBody();
	}


	/**
	 * 요청에서 토큰 정보 빼기
	 * @param req
	 * @return
	 */
	public String getTokenFromRequest(HttpServletRequest req) {
		Cookie[] cookies = req.getCookies();
		if(cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
					try {
						return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
					} catch (UnsupportedEncodingException e) {
						return null;
					}
				}
			}
		}
		return null;
	}
}
