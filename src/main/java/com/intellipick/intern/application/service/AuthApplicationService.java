package com.intellipick.intern.application.service;

import com.intellipick.intern.application.dto.CustomerDto;
import com.intellipick.intern.domain.model.AuthorityEntity;
import com.intellipick.intern.domain.model.Customer;
import com.intellipick.intern.domain.repository.CustomerRepository;
import com.intellipick.intern.domain.service.CustomerService;
import com.intellipick.intern.domain.type.UserRole;
import com.intellipick.intern.presentaion.request.RequestSignUpDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthApplicationService {

    private final PasswordEncoder passwordEncoder;
    private final CustomerService customerService;
    private final CustomerRepository CustomerRepository;

    @Transactional
    public CustomerDto createUser(RequestSignUpDto requestSignUpDto) {
        try {
            String password = passwordEncoder.encode(requestSignUpDto.getPassword());
            return customerService.saveCustomer(requestSignUpDto, password);
        } catch (RuntimeException e) {
            // 예외를 로깅하거나 처리하는 로직
            log.error("회원 가입 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("회원 가입 중 오류가 발생했습니다.", e); // 예외 던지기
        }
    }
}
