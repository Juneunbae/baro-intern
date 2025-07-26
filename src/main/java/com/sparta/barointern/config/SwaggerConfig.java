package com.sparta.barointern.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.addSecurityItem(
				new SecurityRequirement().addList("JWT Authentication"))
			.components(new Components()
				.addSecuritySchemes("JWT Authentication",
					new SecurityScheme()
						.type(SecurityScheme.Type.HTTP)
						.scheme("bearer")
						.bearerFormat("JWT")))
			.info(apiInfo());
	}

	private Info apiInfo() {
		return new Info()
			.title("Baro-Intern Assignment")
			.description("바로 인턴 과제 API")
			.version("1.0");
	}

	@Bean
	public GroupedOpenApi v1Api() {
		return GroupedOpenApi.builder()
			.group("api v1")
			.pathsToMatch("/api/**")
			.build();
	}

	@Bean
	public ModelResolver modelResolver(ObjectMapper objectMapper) {
		return new ModelResolver(objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE));
	}
}