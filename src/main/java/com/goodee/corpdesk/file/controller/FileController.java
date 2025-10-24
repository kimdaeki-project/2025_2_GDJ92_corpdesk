package com.goodee.corpdesk.file.controller;

import java.io.FileInputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.goodee.corpdesk.file.dto.FileDTO;
import com.goodee.corpdesk.file.service.FileService;

import jakarta.servlet.http.HttpServletResponse;


@Controller
@RequestMapping(value = "/file/**")
public class FileController {
	
	@Autowired
	private FileService fileService;
	
	@GetMapping("{fileType}/{fileId}")
	public void downloadFile(@PathVariable("fileType") String fileType, @PathVariable("fileId") Long fileId, HttpServletResponse response) throws Exception {
		FileDTO fileDTO = fileService.getDownloadFile(fileType, fileId);
		
		// 응답시 필요한 값을 response에 바인딩
		response.setContentLengthLong(fileDTO.getFile().length()); // 총 파일의 크기 -> 파일 다운로드시 시간이 얼마나 남았는지 알려주려면 이게 필요함
		
		// header 설정
		// 파일을 보낼 때 이미지인지, 음악인지, 문서 등인지에 대한 정보를 담아 보내야 함
		response.setHeader("Content-Disposition", "attachment;filename=\"" + fileDTO.getOriName() + "." + fileDTO.getExtension() + "\"");
		response.setHeader("Content-Transfer-Encoding", "binary");
		
		// 3. 실제 파일을 클라이언트에게 전송
		FileInputStream fi = new FileInputStream(fileDTO.getFile()); // File을 읽어서 전송
		OutputStream os = response.getOutputStream(); // client로 연결
		FileCopyUtils.copy(fi, os); // 전송 & 자원 해제
	}
	
}
