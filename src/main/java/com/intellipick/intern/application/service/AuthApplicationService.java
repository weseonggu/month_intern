package com.intellipick.intern.application.service;

import com.intellipick.intern.application.dto.CustomerDto;
import com.intellipick.intern.application.dto.Tokens;
import com.intellipick.intern.domain.model.AuthorityEntity;
import com.intellipick.intern.domain.model.Customer;
import com.intellipick.intern.domain.repository.CustomerRepository;
import com.intellipick.intern.domain.service.CustomerService;
import com.intellipick.intern.presentaion.request.RequestLogInDto;
import com.intellipick.intern.presentaion.request.RequestSignUpDto;
import com.intellipick.intern.security.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthApplicationService {

    private final PasswordEncoder passwordEncoder;
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final JWTUtil jwtUtil;

    @Transactional
    public CustomerDto createUser(RequestSignUpDto requestSignUpDto) {
        try {

            if(customerRepository.existsByUsername(requestSignUpDto.getUsername())){
                throw new IllegalArgumentException("중복된 아이디 입니다.");
            }

            String password = passwordEncoder.encode(requestSignUpDto.getPassword());
            return customerService.saveCustomer(requestSignUpDto, password);
        }catch (IllegalArgumentException e){
         throw e;
        }catch (RuntimeException e) {
            // 예외를 로깅하거나 처리하는 로직
            log.error("회원 가입 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("회원 가입 중 오류가 발생했습니다.", e); // 예외 던지기
        }
    }
    @Transactional(readOnly = true)
    public Tokens attemptLogIn(RequestLogInDto requestLogInDto) {
        Customer userInfo = customerRepository.findByUsername(requestLogInDto.getUsername())
                .orElseThrow(()-> new IllegalArgumentException("아이디나 비밀번호가 틀렸습니다."));
        if(passwordEncoder.matches(requestLogInDto.getPassword(), userInfo.getPassword())){
            throw new IllegalArgumentException("아이디나 비밀번호가 틀렸습니다.");
        }

        String access = jwtUtil.createAccessToken(userInfo.getId(), userInfo.getUsername(), roleToStrign(userInfo.getAuthorities()));
        String refresh = jwtUtil.createRefreshToken(userInfo.getId(), userInfo.getUsername(), roleToStrign(userInfo.getAuthorities()));

        return new Tokens(access, refresh);
    }

    private String roleToStrign(Set<AuthorityEntity> authorities) {
        StringBuilder roles = new StringBuilder();
        for(AuthorityEntity authorityEntity : authorities){
            roles.append(authorityEntity.getRole().getValue() + ",");
        }
        return roles.toString();
    }
}
