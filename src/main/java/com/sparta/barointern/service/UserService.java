package com.sparta.barointern.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sparta.barointern.config.Security.JwtUtil;
import com.sparta.barointern.dto.request.LoginRequestDto;
import com.sparta.barointern.dto.request.SignupRequestDto;
import com.sparta.barointern.dto.response.SignupResponseDto;
import com.sparta.barointern.dto.response.TokenDto;
import com.sparta.barointern.entity.Role;
import com.sparta.barointern.entity.User;
import com.sparta.barointern.exception.ErrorCode;
import com.sparta.barointern.exception.GlobalException;
import com.sparta.barointern.mapper.UserMapper;
import com.sparta.barointern.repository.UserRepository;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	private final JwtUtil jwtUtil;
	private final UserMapper userMapper;
	private final UserRepository userRepository;
	private final UserValidation userValidation;
	private final PasswordEncoder passwordEncoder;
	private final UserDetailsService userDetailsService;
	private final AuthenticationManager authenticationManager;

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

	public TokenDto login(@RequestBody LoginRequestDto request) {
		userValidation.loginValid(request);

		setAuthentication(request);

		UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());

		User jwtUser = (User)userDetails;

		return jwtUtil.generateToken(jwtUser);
	}

	private void setAuthentication(LoginRequestDto request) {
		try {
			Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.username(), request.password())
			);

			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (BadCredentialsException e) {
			throw new GlobalException(ErrorCode.INVALID_CREDENTIALS);
		} catch (Exception e) {
			throw new GlobalException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
}