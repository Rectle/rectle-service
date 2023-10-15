package com.rectle.security;

import com.rectle.security.filter.TokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Configuration
@EnableWebSecurity
@Profile("!local")
public class WebSecurityConfig {

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
				}).and().authorizeRequests()
				.antMatchers("/api/v1/compilations/**").permitAll()
				.antMatchers("/swagger-ui/**").permitAll()
				.anyRequest()
				.authenticated()
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.exceptionHandling()
				.authenticationEntryPoint((request, response, error) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED"))
				.and()
				.addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public TokenFilter authFilter() {
		return new TokenFilter();
	}
}
