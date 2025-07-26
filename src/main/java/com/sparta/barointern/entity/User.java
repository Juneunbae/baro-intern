package com.sparta.barointern.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
	private Long id;

	private String username;

	private String password;

	private String nickname;

	private Role role;

	public void updateRole(Role role) {
		this.role = role;
	}
}