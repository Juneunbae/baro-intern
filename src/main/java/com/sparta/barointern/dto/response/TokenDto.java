package com.sparta.barointern.dto.response;

public record TokenDto(
	String grantType,
	String accessToken
) {
}