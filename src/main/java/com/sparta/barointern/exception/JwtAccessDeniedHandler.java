package com.sparta.barointern.exception;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.barointern.dto.response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request,
		HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException {

		ErrorCode errorCode = ErrorCode.ACCESS_DENIED;
		sendErrorResponse(response, errorCode);
	}

	private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(errorCode.getHttpStatus().value());

		ErrorResponse errorResponse = ErrorResponse.from(errorCode);

		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(errorResponse);
		response.getWriter().write(json);
	}
}