package com.goodee.corpdesk.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private JwtTokenManager jwtTokenManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   JwtTokenManager jwtTokenManager) {
        super(authenticationManager);

        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Cookie[] cookies = request.getCookies();
        String accessToken = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("accessToken".equalsIgnoreCase(c.getName())) {
                    accessToken = c.getValue();
                    break;
                }
            }
            if (accessToken != null && accessToken.length() != 0) {
                try {
                    Authentication authentication = jwtTokenManager.getAuthentication(accessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (ExpiredJwtException expiredAccess) {   // 액세스 토큰이 만료됐을 때
                    try {
                        String refreshToken = jwtTokenManager.getRefreshToken(expiredAccess).getBody();

                        Authentication authentication = jwtTokenManager.getAuthentication(refreshToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        accessToken = jwtTokenManager.makeAccessToken(authentication);

                        Cookie cookie = new Cookie("accessToken", accessToken);
                        cookie.setPath("/");
                        cookie.setMaxAge(jwtTokenManager.getRefreshValidTime());
                        cookie.setHttpOnly(true);

                        response.addCookie(cookie);
                    } catch (ExpiredJwtException expiredRefresh) {  // 리프레시 토큰이 만료됐을 때
                        Cookie cookie = new Cookie("accessToken", accessToken);
                        cookie.setPath("/");
                        cookie.setHttpOnly(true);
                        cookie.setMaxAge(0);

                        response.addCookie(cookie);
                        response.sendRedirect("/logout");
                        return;
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

//		super.doFilterInternal(request, response, chain);
        chain.doFilter(request, response);
    }
}
