package com.sparta.barointern.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sparta.barointern.dto.response.GiveAdminRoleResponseDto;
import com.sparta.barointern.entity.Role;
import com.sparta.barointern.entity.User;
import com.sparta.barointern.exception.ErrorCode;
import com.sparta.barointern.exception.GlobalException;
import com.sparta.barointern.mapper.UserMapper;
import com.sparta.barointern.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private UserMapper userMapper;

	@InjectMocks
	private AdminService adminService;

	@Test
	@DisplayName("관리자 권한 부여 성공")
	void giveAdminRoleSuccess() {
		// given
		Long userId = 1L;
		User user = new User("testuser", "password", "nickname", Role.USER);
		user.setId(userId);

		GiveAdminRoleResponseDto expectedResponse = new GiveAdminRoleResponseDto(
			user.getUsername(),
			user.getNickname(),
			Role.ADMIN
		);

		given(userRepository.findById(userId)).willReturn(Optional.of(user));
		// 실제 메서드 내에서 user.updateRole(Role.ADMIN) 호출되므로 Role 변경됨
		given(userMapper.toGiveAdminRoleResponseDto(user.getUsername(), user.getNickname(), Role.ADMIN))
			.willReturn(expectedResponse);

		// when
		GiveAdminRoleResponseDto response = adminService.giveAdminRole(userId);

		// then
		assertThat(response).isNotNull();
		assertThat(response.username()).isEqualTo(user.getUsername());
		assertThat(response.nickname()).isEqualTo(user.getNickname());
		assertThat(response.role()).isEqualTo(Role.ADMIN);

		verify(userRepository).findById(userId);
		verify(userMapper).toGiveAdminRoleResponseDto(user.getUsername(), user.getNickname(), Role.ADMIN);
	}

	@Test
	@DisplayName("관리자 권한 부여 실패 - 사용자 미존재")
	void giveAdminRoleFailUserNotFound() {
		// given
		Long userId = 1L;

		given(userRepository.findById(userId)).willReturn(Optional.empty());

		// when & then
		GlobalException exception = assertThrows(GlobalException.class, () -> {
			adminService.giveAdminRole(userId);
		});

		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_EXISTS_USER);

		verify(userRepository).findById(userId);
		verifyNoMoreInteractions(userMapper);
	}
}