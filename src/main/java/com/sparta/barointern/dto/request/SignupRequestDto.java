package com.sparta.barointern.dto.request;

public record SignupRequestDto(
	String username,
	String password,
	String nickname
) {
}