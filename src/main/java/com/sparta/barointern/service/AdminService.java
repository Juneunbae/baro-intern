package com.sparta.barointern.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sparta.barointern.dto.response.GiveAdminRoleResponseDto;
import com.sparta.barointern.dto.response.SignupResponseDto;
import com.sparta.barointern.entity.Role;
import com.sparta.barointern.entity.User;
import com.sparta.barointern.exception.ErrorCode;
import com.sparta.barointern.exception.GlobalException;
import com.sparta.barointern.mapper.UserMapper;
import com.sparta.barointern.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
	private final UserMapper userMapper;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	private int adminSequence = 1;

	public GiveAdminRoleResponseDto giveAdminRole(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new GlobalException(ErrorCode.NOT_EXISTS_USER));

		user.updateRole(Role.ADMIN);
		log.debug("User: {}, 관리자 부여 성공", user.getId());

		return userMapper.toGiveAdminRoleResponseDto(user.getUsername(), user.getNickname(), user.getRole());
	}

	public SignupResponseDto createAdmin() {
		String adminUsername = "admin" + adminSequence;
		String password = "12345";
		String adminNickname = "admin" + adminSequence;

		User newAdmin = new User(
			adminUsername,
			passwordEncoder.encode(password),
			adminNickname,
			Role.ADMIN
		);
		User savedAdmin = userRepository.save(newAdmin);
		adminSequence++;

		return userMapper.toSignupResponseDto(
			savedAdmin.getId(), savedAdmin.getUsername(), savedAdmin.getPassword(), savedAdmin.getNickname(),
			savedAdmin.getRole()
		);
	}
}