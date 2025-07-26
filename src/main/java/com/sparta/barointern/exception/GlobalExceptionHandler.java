package com.sparta.barointern.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sparta.barointern.dto.exception.ErrorResponseDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(GlobalException.class)
	public ResponseEntity<ErrorResponseDto> handleAlreadyExists(ErrorCode e) {
		ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getCode(), e.getMessage());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDto);
	}
}