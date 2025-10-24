package com.goodee.corpdesk.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.goodee.corpdesk.file.entity.MessageFile;

public interface MessageFileRepository extends JpaRepository<MessageFile, Long> {

}
