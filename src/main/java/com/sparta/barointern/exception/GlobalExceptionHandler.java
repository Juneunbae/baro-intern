package com.sparta.barointern.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sparta.barointern.dto.exception.ErrorResponseDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(GlobalException.class)
	public ResponseEntity<ErrorResponseDto> handleGlobalException(GlobalException ex) {
		ErrorCode errorCode = ex.getErrorCode();
		log.error(errorCode.getMessage());

		ErrorResponseDto errorResponseDto = new ErrorResponseDto(errorCode.getCode(), errorCode.getMessage());
		return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponseDto);
	}
}