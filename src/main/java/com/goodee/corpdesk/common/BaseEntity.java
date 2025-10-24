package com.goodee.corpdesk.common;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@MappedSuperclass
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseEntity {
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
	@CreationTimestamp
	private LocalDateTime createdAt;

	private String modifiedBy;

	@ColumnDefault("true")
	private Boolean useYn = true;
}
