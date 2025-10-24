package com.goodee.corpdesk.security.token;

import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity @Table
@DynamicInsert
public class RefreshToken {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tokenId;
	private String body;
	private String username;
}
