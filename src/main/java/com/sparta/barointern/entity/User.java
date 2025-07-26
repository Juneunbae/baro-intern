package com.sparta.barointern.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
public class User {
	@Setter
	private Long id;

	private String username;

	private String password;

	private String nickname;

	private Role role;

	public User(String username, String password, String nickname, Role role) {
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.role = role;
	}

	public void updateRole(Role role) {
		this.role = role;
	}
}