package com.sparta.barointern.dto.exception;

public record ErrorResponseDto(
	String errorCode,
	String message
) {
}