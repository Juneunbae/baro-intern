package com.sparta.barointern.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sparta.barointern.config.Security.JwtAuthenticationFilter;
import com.sparta.barointern.entity.Role;
import com.sparta.barointern.exception.JwtAccessDeniedHandler;
import com.sparta.barointern.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final CustomUserDetailsService customUserDetailsService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
		return new ProviderManager(daoAuthenticationProvider);
	}

	private static final String[] SWAGGER_WHITELIST = {
		"/swagger-resources/**",
		"/swagger-ui.html",
		"/swagger-ui/**", // Spring Boot 2.6.x 이상 및 OpenAPI 3의 경우
		"/v2/api-docs",
		"/v3/api-docs/**", // OpenAPI 3
		"/webjars/**"
	};

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)

			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(SWAGGER_WHITELIST).permitAll()
				.requestMatchers("/api/v1/users/**").permitAll()
				.requestMatchers("/api/v1/admin").permitAll()
				.requestMatchers("api/v1/admin/users/**").hasRole(Role.ADMIN.name())
				.requestMatchers("/admin/**").hasRole(Role.ADMIN.name())
				.anyRequest().authenticated()
			)

			.httpBasic(AbstractHttpConfigurer::disable)
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.exceptionHandling(
				exception -> exception.accessDeniedHandler(jwtAccessDeniedHandler)
			)

			// 토큰 사용으로 세션 사용 X
			.sessionManagement(
				sessionManagement ->
					sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			);

		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}