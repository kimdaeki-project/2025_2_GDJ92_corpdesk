package com.goodee.corpdesk.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goodee.corpdesk.file.entity.ApprovalFile;

import java.util.List;

public interface ApprovalFileRepository extends JpaRepository<ApprovalFile, Long> {

    public List<ApprovalFile> findAllByApprovalIdAndUseYn(Long approvalId, Boolean useYn);

}
