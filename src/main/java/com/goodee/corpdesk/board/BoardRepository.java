package com.goodee.corpdesk.board;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

  // 활성 글만 페이징 조회
  Page<Board> findByDepartmentIdAndUseYnTrue(Integer departmentId, Pageable pageable);

  // 글 상세 (useYn = true)
  Optional<Board> findByBoardIdAndUseYnTrue(Long boardId);

  // 조회수 증가
  @Modifying
  @Query("update Board b set b.viewCount = coalesce(b.viewCount, 0) + 1 where b.boardId = :boardId and b.useYn = true")
  int increaseViewCount(@Param("boardId") Long boardId);

}
