package com.sparta.barointern.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.barointern.dto.request.LoginRequestDto;
import com.sparta.barointern.dto.request.SignupRequestDto;
import com.sparta.barointern.dto.response.SignupResponseDto;
import com.sparta.barointern.dto.response.TokenDto;
import com.sparta.barointern.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "사용자")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
	private final UserService userService;

	@Operation(summary = "회원가입", description = "서비스 이용을 위해 회원을 등록합니다.")
	@ApiResponse(responseCode = "200", description = "회원가입 성공")
	@ApiResponse(responseCode = "409", description = "이미 가입된 사용자입니다.")
	@PostMapping("/signup")
	public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto request) {
		SignupResponseDto responseDto = userService.signup(request);
		return ResponseEntity.ok(responseDto);
	}

	@Operation(summary = "로그인", description = "로그인을 위한 API")
	@ApiResponse(responseCode = "200", description = "로그인 성공")
	@ApiResponse(responseCode = "400", description = "로그인 실패")
	@PostMapping("/login")
	public ResponseEntity<TokenDto> login(@RequestBody LoginRequestDto request) {
		TokenDto responseDto = userService.login(request);
		return ResponseEntity.ok(responseDto);
	}
}