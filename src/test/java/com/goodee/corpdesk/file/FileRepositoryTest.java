package com.goodee.corpdesk.file;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.goodee.corpdesk.file.entity.ApprovalFile;
import com.goodee.corpdesk.file.entity.BoardFile;
import com.goodee.corpdesk.file.entity.MessageFile;
import com.goodee.corpdesk.file.repository.ApprovalFileRepository;
//import com.goodee.corpdesk.file.repository.BoardFileRepository;
//import com.goodee.corpdesk.file.repository.MessageFileRepository;
import com.goodee.corpdesk.file.repository.BoardFileRepository;
import com.goodee.corpdesk.file.repository.MessageFileRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class FileRepositoryTest {
	@Autowired
	private ApprovalFileRepository ar;
	@Autowired
	private BoardFileRepository br;
	@Autowired
	private MessageFileRepository mr;
	
	@Test
	void testSave1() {
		ApprovalFile af = new ApprovalFile();
		af.setApprovalId(1L);
		af.setOriName("test1");
		af.setSaveName("671c284fa67e49fa82ce483c643d1cfc");
		af.setExtension("png");
//		af.setModifiedBy(1);
		
		af = ar.save(af);
		log.warn("{}", af);
		assertNotNull(af);
	}
	
	@Test
	void testSave2() {
		BoardFile af = new BoardFile();
		af.setBoardId(1L);
		af.setOriName("test1");
		af.setSaveName("5ff0a87779ae41e19305016ecd8b1ffe");
		af.setExtension("png");
//		af.setModifiedBy(1);
		
		af = br.save(af);
		log.warn("{}", af);
		assertNotNull(af);
	}
	
	@Test
	void testSave3() {
		MessageFile af = new MessageFile();
		af.setMessageId(1L);
		af.setOriName("test1");
		af.setSaveName("5ff0a87779ae41e19305016ecd8b1ffe");
		af.setExtension("png");
	//	af.setModifiedBy(1);
		
		af = mr.save(af);
		log.warn("{}", af);
		assertNotNull(af);
	}

}
