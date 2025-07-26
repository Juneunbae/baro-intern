package com.sparta.barointern.service;

import org.springframework.stereotype.Component;

import com.sparta.barointern.dto.request.SignupRequestDto;
import com.sparta.barointern.exception.ErrorCode;
import com.sparta.barointern.exception.GlobalException;
import com.sparta.barointern.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserValidation {
	private final UserRepository userRepository;

	public void signupValid(SignupRequestDto request) {
		if (request == null) {
			throw new GlobalException(ErrorCode.INVALID_INPUT_VALUE);
		}

		if (request.username() == null || request.password() == null || request.nickname() == null) {
			throw new GlobalException(ErrorCode.INVALID_INPUT_VALUE);
		}

		if (request.username().isEmpty() || request.password().isEmpty() || request.nickname().isEmpty()) {
			throw new GlobalException(ErrorCode.INVALID_INPUT_VALUE);
		}

		if (request.username().length() < 5) {
			throw new GlobalException(ErrorCode.USERNAME_LENGTH_5_NOT_OVER);
		}

		if (userRepository.findByUsername(request.username()).isPresent()) {
			throw new GlobalException(ErrorCode.ALREADY_EXISTS);
		}
	}
}