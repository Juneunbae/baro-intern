package com.sparta.barointern.entity;

import lombok.Getter;

@Getter
public enum Role {
	USER("사용자"),
	ADMIN("관리자");

	private final String description;

	Role(String description) {
		this.description = description;
	}
}