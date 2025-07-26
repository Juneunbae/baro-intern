package com.sparta.barointern.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sparta.barointern.dto.request.SignupRequestDto;
import com.sparta.barointern.dto.response.SignupResponseDto;
import com.sparta.barointern.entity.Role;
import com.sparta.barointern.entity.User;
import com.sparta.barointern.mapper.UserMapper;
import com.sparta.barointern.repository.UserRepository;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	private final UserMapper userMapper;
	private final UserValidation userValidation;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public SignupResponseDto signup(@RequestBody SignupRequestDto request) {
		userValidation.signupValid(request);

		User newUser = new User(
			request.username(),
			passwordEncoder.encode(request.password()),
			request.nickname(),
			Role.USER
		);
		User savedUser = userRepository.save(newUser);

		return userMapper.toSignupResponseDto(
			savedUser.getId(), savedUser.getUsername(), savedUser.getPassword(), savedUser.getNickname(),
			savedUser.getRole()
		);
	}
}