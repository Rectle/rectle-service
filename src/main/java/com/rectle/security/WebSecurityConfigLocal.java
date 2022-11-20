package com.rectle.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@Profile("local")
public class WebSecurityConfigLocal {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.cors().configurationSource(request -> {
					var cors = new CorsConfiguration();
					cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
					cors.setAllowedHeaders(List.of("*"));
					cors.setAllowedOrigins(List.of("*"));
					return cors;
				});
		return http.build();
	}
}
