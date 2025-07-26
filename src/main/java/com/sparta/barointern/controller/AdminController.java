package com.sparta.barointern.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.barointern.dto.response.GiveAdminRoleResponseDto;
import com.sparta.barointern.dto.response.SignupResponseDto;
import com.sparta.barointern.service.AdminService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "관리자")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
	private final AdminService adminService;

	@Operation(summary = "관리자 권한 부여", description = "사용자에게 관리자 권한 부여")
	@ApiResponse(responseCode = "200", description = "권한 부여 성공")
	@ApiResponse(responseCode = "400", description = "권한 부여 실패")
	@PostMapping("/users/{userId}/roles")
	public ResponseEntity<GiveAdminRoleResponseDto> giveAdminRole(@PathVariable Long userId) {
		GiveAdminRoleResponseDto responseDto = adminService.giveAdminRole(userId);
		return ResponseEntity.ok(responseDto);
	}

	@Operation(summary = "관리자 생성", description = "테스트를 위해 관리자 생성 API [PASSWORD:12345]")
	@PostMapping
	public ResponseEntity<SignupResponseDto> createAdmin() {
		SignupResponseDto responseDto = adminService.createAdmin();
		return ResponseEntity.ok(responseDto);
	}
}