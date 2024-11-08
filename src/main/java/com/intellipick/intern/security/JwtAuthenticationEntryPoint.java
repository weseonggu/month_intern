package com.intellipick.intern.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
/**
 * 필터에서 발생한 예외 Servlet으로 넘기는 코드
 */
@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final HandlerExceptionResolver resolver;
 
    public JwtAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }
 

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        // 예외 객체가 null인지 확인
        Exception exception = (Exception) request.getAttribute("exception");
        if (exception == null) {
            exception = authException; // 기본적으로 AuthenticationException 사용
        }

        // 예외 처리
        resolver.resolveException(request, response, null, exception);
    }
}


