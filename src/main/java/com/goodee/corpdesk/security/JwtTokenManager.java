package com.goodee.corpdesk.security;

import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeRepository;
import com.goodee.corpdesk.employee.Role;
import com.goodee.corpdesk.employee.RoleRepository;
import com.goodee.corpdesk.employee.dto.EmployeeSecurityDTO;
import com.goodee.corpdesk.security.token.RefreshToken;
import com.goodee.corpdesk.security.token.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtTokenManager {

	@Value("${jwt.secret.key}")
	private String secretKey;
	@Value("${jwt.valid.time.access}")
	private Long accessValidTime;
	@Value("${jwt.valid.time.refresh}")
	private Long refreshValidTime;
	@Value("${jwt.issuer}")
	private String issuer;
	
	private SecretKey key;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	@Autowired
	private RoleRepository roleRepository;
	
	@PostConstruct
	public void init() {
		byte[] base64 = Base64.getEncoder().encode(this.secretKey.getBytes());
		key = Keys.hmacShaKeyFor(base64);
	}
	
	// 엑세스 토큰
	public String makeAccessToken(Authentication authentication) {
		return this.makeToken(authentication, accessValidTime);
	}
	
	// 리프레시 토큰
	public String makeRefreshToken(Authentication authentication) {
		
		String token = this.makeToken(authentication, refreshValidTime);
		// NOTE: DB에 리프레시 토큰 저장
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setBody(token);
		refreshToken.setUsername(authentication.getName());
		
		refreshTokenRepository.save(refreshToken);
		
		return token;
	}
	
	// 토큰 발급
	public String makeToken(Authentication authentication, Long validTime) {
        Optional<Employee> optional = employeeRepository.findById(authentication.getName());
        Employee employee = optional.get();

		return Jwts
				.builder()
				.subject(authentication.getName())
//                .claim("pk", employee.getEmployeeId())
				.claim("roleId", employee.getRoleId())
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + validTime))
				.issuer(issuer)
				.signWith(key)
				.compact()
				;
	}
	
	public Authentication getAuthentication(String token) throws Exception {
		Claims claims = Jwts
				.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				;
//		Employee employee = employeeRepository.findById(claims.getSubject()).get();
//		Role role = roleRepository.findById(claims.get("roleId", Integer.class)).get();
//		employee.setRole(role);
		EmployeeSecurityDTO employee = employeeRepository.findEmployeeSecurityByUsername(claims.getSubject()).get();

		Authentication authentication = new UsernamePasswordAuthenticationToken(employee, null, employee.getAuthorities());
		
		return authentication;
	}
	
    // 만료된 액세스 토큰에서 username을 꺼내와서 DB에 있는 리프레시 토큰을 조회함
	public RefreshToken getRefreshToken(ExpiredJwtException expired) throws Exception {
		String username = expired.getClaims().getSubject();
		
		return refreshTokenRepository.findTopByUsernameOrderByTokenIdDesc(username).get();
	}

	public int getRefreshValidTime() {
		return (int) (refreshValidTime / 1000);
	}
}
