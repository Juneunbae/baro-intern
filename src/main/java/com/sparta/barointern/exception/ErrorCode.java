package com.sparta.barointern.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	ALREADY_EXISTS("ALREADY_EXISTS", "이미 존재하는 사용자입니다.", HttpStatus.CONFLICT),
	USERNAME_LENGTH_5_NOT_OVER("USERNAME_LENGTH_5_NOT_OVER", "유저 아이디가 5글자 미만입니다.", HttpStatus.BAD_REQUEST),
	INVALID_INPUT_VALUE("INVALID_INPUT_VALUE", "필수 입력 값이 누락되었거나 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
	INVALID_CREDENTIALS("INVALID_CREDENTIALS", "아이디 또는 비밀번호가 일치하지 않아 인증에 실패했습니다.", HttpStatus.BAD_REQUEST),
	INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "서버 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR),
	NOT_EXISTS_USER("NOT_EXISTS_USER", "존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND),
	ACCESS_DENIED("ACCESS_DENIED", "관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다.", HttpStatus.FORBIDDEN);

	private final String code;
	private final String message;
	private final HttpStatus httpStatus;
}