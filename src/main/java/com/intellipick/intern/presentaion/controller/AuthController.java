package com.intellipick.intern.presentaion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.intellipick.intern.application.dto.CustomerDto;
import com.intellipick.intern.application.dto.Tokens;
import com.intellipick.intern.application.service.AuthApplicationService;
import com.intellipick.intern.presentaion.request.RequestLogInDto;
import com.intellipick.intern.presentaion.request.RequestSignUpDto;
import com.intellipick.intern.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthApplicationService authService;
    private final ObjectMapper objectMapper;

    @Operation(summary = "회원 가입 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "가입 성공"),
            @ApiResponse(responseCode = "400", description = "아이디 중복"),
            @ApiResponse(responseCode = "500", description = "가입 중 실패")
    })
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody RequestSignUpDto requestSignUpDto){
        CustomerDto dto = authService.createUser(requestSignUpDto);
        return ResponseEntity.ok().body(dto);
    }

    @Operation(summary = "입출금 상품 등록 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "아이디 비밀번호 틀림"),
            @ApiResponse(responseCode = "500", description = "로그인 실패")
    })
    @PostMapping("/sign")
    public ResponseEntity<?> login(@Valid @RequestBody RequestLogInDto requestLogInDto){
        Tokens tokens = authService.attemptLogIn(requestLogInDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", tokens.getRefresh_token());

        SimpleFilterProvider filters = new SimpleFilterProvider().addFilter(
                "TokenFilter",
                SimpleBeanPropertyFilter.serializeAllExcept("refresh_token")
        );
        objectMapper.setFilterProvider(filters);
        return new ResponseEntity<>(tokens, headers, HttpStatus.OK);
    }

    

    @GetMapping("/test")
    public ResponseEntity<?> test(@AuthenticationPrincipal CustomUserDetails userDetails){
        log.info(userDetails.getUsername());
        log.info(userDetails.getAuthorities().toString());
        return ResponseEntity.ok().body(userDetails);
    }


}
