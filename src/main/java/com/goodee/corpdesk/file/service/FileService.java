package com.goodee.corpdesk.file.service;

import java.io.File;
import java.net.URLEncoder;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.goodee.corpdesk.file.dto.FileDTO;
import com.goodee.corpdesk.file.entity.ApprovalFile;
import com.goodee.corpdesk.file.entity.FileBase;
import com.goodee.corpdesk.file.repository.ApprovalFileRepository;
import com.goodee.corpdesk.file.repository.BoardFileRepository;
import com.goodee.corpdesk.file.repository.MessageFileRepository;

@Service
public class FileService {
	@Value("${app.upload}")
	private String path;
	@Value("${app.upload.approval}")
    private String approvalPath;
    @Value("${app.upload.board}")
    private String boardPath;
    @Value("${app.upload.message}")
    private String messagePath;

	@Autowired
	private ApprovalFileRepository approvalFileRepository;
	@Autowired
	private BoardFileRepository boardFileRepository;
	@Autowired
	private MessageFileRepository messageFileRepository;
	
	// 도메인 타입에 따라 filepath를 달리 함
	public FileDTO getDownloadFile(String fileType, Long fileId) throws Exception {
		
		// 1. 파일id로 파일의 메타데이터를 받아옴
		Optional<? extends FileBase> result = null;
		
		switch (fileType) {
		case "approval" -> result = approvalFileRepository.findById(fileId);
		case "board" -> result = boardFileRepository.findById(fileId);
		case "message" -> result = messageFileRepository.findById(fileId);
		}
		
		FileBase fileBase = result.orElseThrow();// 조회결과가 null이면 예외를 발생시키고, 조회결과가 null이 아니라면 객체에 값 할당
		
		// 2. 메타데이터로 file 객체 생성
		String filePath = path + fileType;
		File file = new File(filePath, fileBase.getSaveName() + "." + fileBase.getExtension());
		String fileName = URLEncoder.encode(fileBase.getOriName(), "UTF-8");
		
		// 3. 파일 정보를 fileDTO에 담아서 보냄
		FileDTO fileDTO = new FileDTO();
		fileDTO.setOriName(fileName);
        fileDTO.setExtension(fileBase.getExtension());
		fileDTO.setFile(file);
		
		return fileDTO;
	}
	
}
