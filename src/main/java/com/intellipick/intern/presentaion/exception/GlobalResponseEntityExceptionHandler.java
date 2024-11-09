package com.intellipick.intern.presentaion.exception;

import com.intellipick.intern.presentaion.dto.FailMessage;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {


    /**
     * 유효성 검증 실패시 예외처리
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        List<String> errorMessages = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            errorMessages.add(fieldError.getDefaultMessage());
        }
        FailMessage message = new FailMessage(LocalDateTime.now(), request.getDescription(false), errorMessages);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<FailMessage> handleRuntimeException(RuntimeException ex, WebRequest request) {
        log.warn(ex.getMessage());
        FailMessage message = new FailMessage(LocalDateTime.now(), request.getDescription(false), List.of("잠시후 다시 시도해주세요"));
        return new ResponseEntity<FailMessage>(message,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<FailMessage> handleIllegalArgumentException(RuntimeException ex, WebRequest request) {
        FailMessage message = new FailMessage(LocalDateTime.now(), request.getDescription(false), List.of(ex.getMessage()));
        return new ResponseEntity<FailMessage>(message,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<FailMessage> handleSecurityException(RuntimeException ex, WebRequest request) {
        FailMessage message = new FailMessage(LocalDateTime.now(), request.getDescription(false), List.of(ex.getMessage()));
        return new ResponseEntity<FailMessage>(message,HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<FailMessage> handleJwtException(RuntimeException ex, WebRequest request) {
        FailMessage message = new FailMessage(LocalDateTime.now(), request.getDescription(false), List.of(ex.getMessage()));
        return new ResponseEntity<FailMessage>(message,HttpStatus.UNAUTHORIZED);
    }

}