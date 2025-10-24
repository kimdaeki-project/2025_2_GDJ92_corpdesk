package com.goodee.corpdesk.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goodee.corpdesk.file.entity.BoardFile;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {

}
