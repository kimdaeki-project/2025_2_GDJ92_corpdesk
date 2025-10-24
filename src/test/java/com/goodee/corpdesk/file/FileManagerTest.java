package com.goodee.corpdesk.file;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import com.goodee.corpdesk.file.dto.FileDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class FileManagerTest {
	@Value("${app.upload}")
	private String path;
	
	@Autowired
	private FileManager fileManager;
	
	@Test
	void testSaveFile() throws Exception {
	    
		String filePath = path + "approval";
		MockMultipartFile file1 = new MockMultipartFile(
	            "file", "test1.png", "image/png", new byte[10]);
		
		FileDTO fileDTO = fileManager.saveFile(filePath, file1);
		
		log.warn("{}", fileDTO);
		assertNotNull(fileDTO);
	
	}

	@Test
	void testDeleteFile() throws Exception {
		
		String filePath = path + "approval";
		FileDTO fileDTO = new FileDTO();
		fileDTO.setSaveName("13f62604036e4bc6a3b83c9c01bad13f");
		fileDTO.setExtension("png");
		
		boolean result = fileManager.deleteFile(filePath, fileDTO);
		log.warn("{}", fileDTO);
		assertTrue(result);
	
	}
	
	
}
