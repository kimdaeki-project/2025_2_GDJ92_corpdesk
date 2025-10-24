package com.goodee.corpdesk.file.entity;

import com.goodee.corpdesk.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class FileBase extends BaseEntity {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fileId;
	
	@Column(nullable = false)
	private String oriName;
	
	@Column(nullable = false)
	private String saveName;
	
	@Column(nullable = false)
	private String extension;
}
