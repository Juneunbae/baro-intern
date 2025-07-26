package com.sparta.barointern.entity;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
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

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; // 계정 만료 여부 (항상 true로 설정, 과제 조건에 따름)
	}

	@Override
	public boolean isAccountNonLocked() {
		return true; // 계정 잠금 여부 (항상 true로 설정)
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true; // 자격 증명(비밀번호) 만료 여부 (항상 true로 설정)
	}

	@Override
	public boolean isEnabled() {
		return true; // 계정 활성화 여부 (항상 true로 설정)
	}

	public void updateRole(Role role) {
		this.role = role;
	}
}