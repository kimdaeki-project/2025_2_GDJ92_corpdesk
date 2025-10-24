package com.goodee.corpdesk.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.net.URLEncoder;

@Slf4j
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	private JwtTokenManager jwtTokenManager;
	
	public JwtLoginFilter(AuthenticationManager authenticationManager, JwtTokenManager jwtTokenManager) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenManager = jwtTokenManager;
		
		// 로그인 URL
		this.setFilterProcessesUrl("/login-process");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		UsernamePasswordAuthenticationToken authenticationToken
				= new UsernamePasswordAuthenticationToken(username, password);
		
		return authenticationManager.authenticate(authenticationToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		String accessToken = jwtTokenManager.makeAccessToken(authResult);
		String refreshToken = jwtTokenManager.makeRefreshToken(authResult);
		
		Cookie cookie = new Cookie("accessToken", accessToken);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setMaxAge(jwtTokenManager.getRefreshValidTime());
		cookie.setHttpOnly(true);
		
		response.addCookie(cookie);
		
		response.sendRedirect("/dashboard"); // 로그인 성공 시 리다이렉트
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		String message = "로그인 실패";
		
		switch (failed.getClass().getSimpleName()) {
		case "InternalAuthenticationServiceException":
			message = "아이디가 다릅니다.";
			break;
		case "BadCredentialsException":
			message = "비밀번호가 다릅니다.";
			break;
		case "DisabledException":
			message = "유효하지 않은 사용자입니다.";
			break;
		case "AccountExpiredException":
			message = "사용자 계정의 유효 기간이 만료되었습니다.";
			break;
		case "LockedException":
			message = "사용자 계정이 잠겨 있습니다.";
			break;
		case "CredentialsExpiredException":
			message = "자격 증명 유효 기간이 만료되었습니다.";
			break;
		case "AuthenticationCredentialsNotFoundException":
			message = "관리자에게 문의하세요.";
			break;
		}
		
		message = URLEncoder.encode(message, "UTF-8");
		response.sendRedirect("/login/" + message);
	}
	
	
}
