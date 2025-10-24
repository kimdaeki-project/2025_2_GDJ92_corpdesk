package com.goodee.corpdesk.board;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.goodee.corpdesk.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 실제 데이터베이스 테이블 이름
@Table(name = "board")
@Entity
@DynamicInsert @DynamicUpdate
public class Board extends BaseEntity {

  // 게시글 번호
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long boardId;

  // 게시판 구분 ID (0=공지, 1부터=부서ID)
  @Column(nullable = true)
  private Integer departmentId;

  // 작성자
  @Column(nullable = false)
  private String username;
  
  // 제목
  @Column(nullable = false, length = 255)
  private String title;
  
  // 내용
  @Column(columnDefinition = "TEXT")
  private String content;
  
  // 조회수
  @ColumnDefault("0")
  private Long viewCount;

  // // 생성일
  // @Column(nullable = false)
  // private LocalDateTime createdAt;

  // // 수정일
  // @Column(nullable = false)
  // private LocalDateTime updatedAt;
  
  // // 수정자 ID
  // private Integer modifiedBy;
  
  // // 사용 여부
  // @Column(nullable = false)
  // private Boolean useYn;

  // 엔티티 저장 전에 자동 실행
  // 생성 시각 자동 설정
  // @PrePersist
  // protected void onCreate() {
  //   this.createdAt = LocalDateTime.now();
  //   this.updatedAt = LocalDateTime.now();
  //   if (this.viewCount == null) this.viewCount = 0L;
  //   if (this.useYn == null) this.useYn = true;
  // }

  // // 엔티티 저장 전에 자동 실행
  // // 수정 시각 자동 설정
  // @PreUpdate
  // protected void onUpdate() {
  //   this.updatedAt = LocalDateTime.now();
  // }

}