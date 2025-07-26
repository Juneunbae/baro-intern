package com.sparta.barointern.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.barointern.dto.request.SignupRequestDto;
import com.sparta.barointern.dto.response.SignupResponseDto;
import com.sparta.barointern.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

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
}