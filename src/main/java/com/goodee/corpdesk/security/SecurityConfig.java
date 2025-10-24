package com.goodee.corpdesk.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtTokenManager jwtTokenManager;
	@Autowired
	private AuthenticationConfiguration authenticationConfiguration;

	@Value("${email.secret}")
	private String emailSecret;
	@Value("${email.key}")
	private String emailKey;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
//              .cors(cors -> cors.disable())
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/board/notice/write", "/board/notice/*/edit").hasAnyRole("ADMIN")
						.requestMatchers(HttpMethod.POST, "/board/notice/**").hasAnyRole("ADMIN")

						.requestMatchers("/employee/add", "/employee/list", "/employee/export", "/employee/import",
						"/employee/edit/**", "/employee/delete/**", "/employee/*/attendance/**", "/position/**",
						"/admin/**", "/organization/**", "/salary/**", "/stats/**").hasAnyRole("ADMIN", "HR")

						.requestMatchers("/dashboard", "/approval/**", "/attendance/**", "/board/**",
								"/calendar/**", "/chat/**", "/email/**", "/employee/**", "/notice/**",
								"/personal-schedule/**", "/vacation/**").authenticated()    // 접근 제한
						.anyRequest().permitAll()
				)
				.exceptionHandling(e -> e
						.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/")))   // 접근 제한 리다이렉트
				.formLogin(form -> form.disable())
				.logout(logout -> logout    // 로그아웃 설정
						.logoutUrl("/logout")
						.invalidateHttpSession(true)
						.deleteCookies("accessToken")    // 액세스 토큰 삭제
						.logoutSuccessUrl("/"))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilter(new JwtLoginFilter(authenticationConfiguration.getAuthenticationManager(), jwtTokenManager))
				.addFilter(new JwtAuthenticationFilter(authenticationConfiguration.getAuthenticationManager(), jwtTokenManager))
		;

		return httpSecurity.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AesBytesEncryptor aesBytesEncryptor() {
		return new AesBytesEncryptor(emailSecret, emailKey);
	}

	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfig = new CorsConfiguration();

		corsConfig.addAllowedOrigin("http://localhost");
		corsConfig.addAllowedHeader("*");
		corsConfig.addAllowedMethod("*");
		corsConfig.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);

		return source;
	}
}
