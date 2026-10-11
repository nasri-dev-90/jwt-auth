package com.example.jwtToken.exception;

import com.example.jwtToken.responsedtos.ResponseDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseDto> handleBadCredentialsException(BadCredentialsException ex) {
        ResponseDto response = new ResponseDto(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                LocalDateTime.now(),
                null,
                List.of("نام کاربری یا رمز عبور اشتباه است.")
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseDto> handleAccessDeniedException(AccessDeniedException ex) {

        ResponseDto response = new ResponseDto(
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                LocalDateTime.now(),
                null,
                List.of("شما دسترسی لازم برای انجام این عملیات را ندارید.")
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }


    @ExceptionHandler({ExpiredJwtException.class, JwtException.class})
    public ResponseEntity<ResponseDto> handleJwtException(Exception ex) {

        ResponseDto response = new ResponseDto(
                HttpStatus.UNAUTHORIZED.value(),
                "Invalid Token",
                LocalDateTime.now(),
                null,
                List.of("توکن شما منقضی یا نامعتبر است. لطفا دوباره وارد شوید.")
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseDto> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = "خطا در ثبت اطلاعات. ممکن است داده تکراری باشد.";
        if (ex.getMostSpecificCause().getMessage().contains("duplicate key")) {
            message = "اطلاعات وارد شده (مانند کد ملی یا شماره تلفن) از قبل در سیستم وجود دارد.";
        }

        ResponseDto response = new ResponseDto(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                LocalDateTime.now(),
                null,
                List.of(message)
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(fieldName+" : "+ errorMessage);
        });

        ResponseDto response = new ResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error =داده های ارسالی نامعتبر است.",
                LocalDateTime.now(),
                null,
                errors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> handleGlobalException(Exception ex) {
        ResponseDto response = new ResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                LocalDateTime.now(),
                null,
                List.of(ex.getMessage())
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
