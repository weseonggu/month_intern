package com.intellipick.intern.presentaion.controller;

import com.intellipick.intern.application.dto.CustomerDto;
import com.intellipick.intern.application.service.AuthApplicationService;
import com.intellipick.intern.presentaion.request.RequestSignUpDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthApplicationService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody RequestSignUpDto requestSignUpDto){
        CustomerDto dto = authService.createUser(requestSignUpDto);
        return ResponseEntity.ok().body(dto);
    }
}
