package com.sparta.barointern.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	ALREADY_EXISTS("ALREADY_EXISTS", "이미 존재하는 사용자입니다.");

	private final String code;
	private final String message;
}