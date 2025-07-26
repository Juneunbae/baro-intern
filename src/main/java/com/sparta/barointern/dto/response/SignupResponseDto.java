package com.sparta.barointern.dto.response;

import com.sparta.barointern.entity.Role;

public record SignupResponseDto(
	Long id,
	String username,
	String password,
	String nickname,
	Role role
) {
}