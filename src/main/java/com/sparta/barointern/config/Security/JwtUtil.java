package com.sparta.barointern.config.Security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.sparta.barointern.dto.response.TokenDto;
import com.sparta.barointern.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {
	@Value("${jwt.access.secret}")
	private String accessSecret;

	@Value("${jwt.expiration}")
	private Long expiration;

	private final String bearer = "Bearer ";

	@PostConstruct
	public void init() {
		this.accessSecret = String.valueOf(Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8)));
	}

	public String generateAccessToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", user.getId());
		claims.put("role", user.getRole().name());
		claims.put("type", JwtType.ACCESS.name());

		return Jwts.builder()
			.claims(claims)
			.subject(user.getUsername())
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + expiration))
			.signWith(Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8)))
			.compact();
	}

	public TokenDto generateToken(User user) {
		return new TokenDto(
			bearer,
			this.generateAccessToken(user)
		);
	}

	public Claims parseAccessClaims(String token) {
		byte[] accessSecret = this.accessSecret.getBytes(StandardCharsets.UTF_8);

		try {
			return Jwts.parser()
				.verifyWith(Keys.hmacShaKeyFor(accessSecret))
				.build()
				.parseSignedClaims(token)
				.getPayload();
		} catch (ExpiredJwtException e) {
			log.warn("Access Token Expired: {}", e.getMessage());
			throw new RuntimeException("Access Token이 만료되었습니다.", e);
		} catch (Exception e) {
			log.error("Invalid Access Token: {}", e.getMessage());
			throw new RuntimeException("유효하지 않은 Access Token입니다.", e);
		}
	}

	public String extractUsername(String token) {
		Claims claims = parseAccessClaims(token);
		return claims.getSubject();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);

		try {
			return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
		} catch (RuntimeException e) {
			return false;
		}
	}

	public Boolean isTokenExpired(String token) {
		try {
			Claims claims = parseAccessClaims(token);
			return claims.getExpiration().before(new Date());
		} catch (RuntimeException e) {
			return true;
		}
	}
}