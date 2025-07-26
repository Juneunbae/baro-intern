package com.sparta.barointern.mapper;

import org.mapstruct.Mapper;

import com.sparta.barointern.dto.response.SignupResponseDto;
import com.sparta.barointern.entity.Role;

@Mapper(componentModel = "spring")
public interface UserMapper {
	SignupResponseDto toSignupResponseDto(Long id, String username, String password, String nickname, Role role);
}