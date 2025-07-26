package com.sparta.barointern.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

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

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	@Mock
	private JwtUtil jwtUtil;

	@Mock
	private UserMapper mapper;

	@Mock
	private UserRepository userRepository;

	@Mock
	private UserValidation userValidation;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private UserDetailsService userDetailsService;

	@Mock
	private AuthenticationManager authenticationManager;

	@InjectMocks
	private UserService userService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		this.userService = new UserService(
			jwtUtil, mapper, userRepository, userValidation, passwordEncoder, userDetailsService, authenticationManager
		);
	}

	@Test
	@DisplayName("회원가입 성공")
	void successSignup() {
		// given
		String username = "test1";
		String password = "123123123";
		String encodedPassword = "encodedPassword";
		String nickname = "테스트유저1";
		Role role = Role.USER;

		SignupRequestDto request = new SignupRequestDto(username, password, nickname);
		User savedUser = new User(username, encodedPassword, nickname, role);
		savedUser.setId(1L);

		SignupResponseDto responseDto = new SignupResponseDto(1L, username, encodedPassword, nickname, role);

		when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
		when(userRepository.save(any(User.class))).thenReturn(savedUser);
		when(mapper.toSignupResponseDto(any(), any(), any(), any(), any())).thenReturn(responseDto);

		// when
		SignupResponseDto result = userService.signup(request);

		// then
		assertThat(username).isEqualTo(result.username());
		assertThat(nickname).isEqualTo(result.nickname());
		assertThat(role).isEqualTo(result.role());

		verify(userValidation).signupValid(request);
		verify(userRepository).save(any(User.class));
	}

	@Test
	@DisplayName("회원가입 실패 - 중복된 Username")
	void failSignupExistsUsername() {
		// given
		String username = "test1";
		String password = "123123123";
		String nickname = "테스트유저1";

		SignupRequestDto request = new SignupRequestDto(username, password, nickname);

		doThrow(new GlobalException(ErrorCode.ALREADY_EXISTS))
			.when(userValidation).signupValid(request);

		// when & then
		GlobalException globalException = assertThrows(GlobalException.class, () -> {
			userService.signup(request);
		});

		assertEquals(ErrorCode.ALREADY_EXISTS, globalException.getErrorCode());

		verify(userValidation).signupValid(request);
		verifyNoInteractions(userRepository);
	}

	@Test
	@DisplayName("회원가입 실패 - 닉네임 누락")
	void failSignupBlankNickname() {
		String username = "test1";
		String password = "123123123";

		SignupRequestDto request = new SignupRequestDto(username, password, "");

		doThrow(new GlobalException(ErrorCode.INTERNAL_SERVER_ERROR))
			.when(userValidation).signupValid(request);

		GlobalException globalException = assertThrows(GlobalException.class, () -> {
			userService.signup(request);
		});

		assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, globalException.getErrorCode());

		verify(userValidation).signupValid(request);
		verifyNoInteractions(userRepository);
	}

	@Test
	@DisplayName("회원가입 실패 - 아이디 5글자 미만")
	void failSignupInvalidUsername() {
		String username = "test";
		String password = "123123123";
		String nickname = "테스트유저1";

		SignupRequestDto request = new SignupRequestDto(username, password, nickname);

		doThrow(new GlobalException(ErrorCode.USERNAME_LENGTH_5_NOT_OVER))
			.when(userValidation).signupValid(request);

		GlobalException globalException = assertThrows(GlobalException.class, () -> {
			userService.signup(request);
		});

		assertEquals(ErrorCode.USERNAME_LENGTH_5_NOT_OVER, globalException.getErrorCode());

		verify(userValidation).signupValid(request);
		verifyNoInteractions(userRepository);
	}

	@Test
	@DisplayName("로그인 성공")
	void successLogin() {
		// given
		String username = "test1";
		String password = "123123123";
		String encodedPassword = "encodedPassword";

		LoginRequestDto request = new LoginRequestDto(username, password);
		User user = new User(username, encodedPassword, encodedPassword, Role.USER);
		user.setId(1L);

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, encodedPassword);

		Authentication authentication = mock(Authentication.class);
		when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);

		when(userDetailsService.loadUserByUsername(username)).thenReturn(user);
		when(jwtUtil.generateToken(user)).thenReturn(new TokenDto("Bearer", "access-token"));

		// when
		TokenDto result = userService.login(request);

		// then
		assertEquals("access-token", result.accessToken());

		verify(userValidation).loginValid(request);
		verify(authenticationManager).authenticate(any(Authentication.class));
		verify(jwtUtil).generateToken(user);
	}

	@Test
	@DisplayName("로그인 실패 - 존재하지 않는 사용자")
	void failLoginNotExistsUser() {
		// given
		String username = "noUser";
		String password = "123123123";

		LoginRequestDto request = new LoginRequestDto(username, password);

		when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(mock(Authentication.class));
		when(userDetailsService.loadUserByUsername(username)).thenThrow(
			new GlobalException(ErrorCode.INVALID_CREDENTIALS));

		// when & then
		assertThrows(GlobalException.class, () -> {
			userService.login(request);
		});

		verify(userValidation).loginValid(request);
		verify(authenticationManager).authenticate(any(Authentication.class));
		verify(userDetailsService).loadUserByUsername(username);
		verifyNoInteractions(jwtUtil);
	}

	@Test
	@DisplayName("로그인 실패 - 유효하지 않는 비밀번호")
	void failLoginInvalidPassword() {
		// given
		String username = "test1";
		String password = "invalidPassword";

		LoginRequestDto request = new LoginRequestDto(username, password);

		doNothing().when(userValidation).loginValid(request);

		when(authenticationManager.authenticate(any(Authentication.class)))
			.thenThrow(new BadCredentialsException("비밀번호 오류"));

		// when & then
		GlobalException exception = assertThrows(GlobalException.class, () -> {
			userService.login(request);
		});

		assertEquals(ErrorCode.INVALID_CREDENTIALS, exception.getErrorCode());

		verify(userValidation).loginValid(request);
		verify(authenticationManager).authenticate(any(Authentication.class));
		verifyNoInteractions(jwtUtil);
	}
}