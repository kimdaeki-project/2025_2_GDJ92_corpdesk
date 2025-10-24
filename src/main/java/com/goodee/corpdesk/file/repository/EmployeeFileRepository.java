package com.goodee.corpdesk.file.repository;

import com.goodee.corpdesk.file.entity.EmployeeFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeFileRepository extends JpaRepository<EmployeeFile, Long> {
    // ⭐ username으로 EmployeeFile을 찾습니다.
    Optional<EmployeeFile> findByUsername(String username);

    Optional<EmployeeFile> findByUseYnAndUsername(Boolean useYn, String username);
}
