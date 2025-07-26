package com.sparta.barointern.dto.response;

import com.sparta.barointern.exception.ErrorCode;

public record ErrorResponse(
	String errorCode,
	String message
) {
	public static ErrorResponse from(ErrorCode errorCode) {
		return new ErrorResponse(
			errorCode.name(),
			errorCode.getMessage()
		);
	}
}