package com.sparta.barointern.dto.response;

import com.sparta.barointern.entity.Role;

public record GiveAdminRoleResponseDto(
	String username,
	String nickname,
	Role role
) {
}