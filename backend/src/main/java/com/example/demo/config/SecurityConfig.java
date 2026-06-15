package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(HttpMethod.GET, "/api/recipes", "/api/recipes/**").permitAll()
						.requestMatchers("/h2-console/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/recipes").authenticated()
						.requestMatchers(HttpMethod.PUT, "/api/recipes/**").authenticated()
						.requestMatchers(HttpMethod.DELETE, "/api/recipes/**").authenticated()
						.anyRequest().authenticated())
				.httpBasic(Customizer.withDefaults());

		return http.build();
	}

}
