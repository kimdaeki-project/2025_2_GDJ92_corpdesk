package com.goodee.corpdesk.security.token;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	Optional<RefreshToken> findTopByUsernameOrderByTokenIdDesc(String username);
}
