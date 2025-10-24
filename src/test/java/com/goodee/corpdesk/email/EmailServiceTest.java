package com.goodee.corpdesk.email;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;

import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeRepository;

import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;

@SpringBootTest
class EmailServiceTest {

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private AesBytesEncryptor aesBytesEncryptor;

	@Test
	void mailBox() {
		Employee employee = employeeRepository.findById("user01").get();

		byte[] decoded = aesBytesEncryptor.decrypt(employee.getEncodedEmailPassword());
		employee.setExternalEmailPassword(new String(decoded));

		String myEmail = employee.getExternalEmail();
		String host = "imap." + myEmail.split("@")[1];
		String password = employee.getExternalEmailPassword();

		Properties props = new Properties();
		props.put("mail.store.protocol", "imap");
		props.put("mail.imap.host", host);
		props.put("mail.imap.port", "993");
		props.put("mail.imap.ssl.enable", "true");

		Session session = Session.getInstance(props);
		Store store = null;
		Folder folder = null;

		List<EmailDTO> messageList = new ArrayList<>();

		try {
			store = session.getStore("imap");
			store.connect(host, myEmail.split("@")[0], password);

			folder =  store.getFolder("[Gmail]/보낸편지함");
			folder.open(Folder.READ_ONLY);
			Message message = folder.getMessage(1);
			
//			Folder[] folders = store.getDefaultFolder().list("*");

			
			System.out.println("==================================");
//			System.out.println(folders[1].getName());
			System.out.println(message.getSubject());
			
//			for (Folder f : folders) {
//				System.out.println("==================================");
//				System.out.println(f.getFullName());
//			}

//			int total = folder.getMessageCount();
//			int start = total;
//			int page = pageable.getPageNumber();
//			int size = pageable.getPageSize();
//
//			// 받아온 메일 목록
//			Message[] messages = folder.getMessages(start - size - page, start - page);

//			for (Message message : messages) {
//
//				EmailDTO emailDTO = new EmailDTO();
//				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//				String from = Arrays.toString(message.getFrom());
//				if (from.toUpperCase().contains("=?UTF-8?B?")) {
//					from = '[' + MimeUtility.decodeText(from.substring(from.indexOf("=?"), from.lastIndexOf("?=") + 2))
//							+ from.substring(from.lastIndexOf("?=") + 2);
//				}
//				emailDTO.setFrom(from);
//				emailDTO.setSubject(message.getSubject());
//				emailDTO.setEmailNo(message.getMessageNumber());
//				emailDTO.setReceivedDate(format.format(message.getReceivedDate()));
//				emailDTO.setSentDate(format.format(message.getSentDate()));
//				emailDTO.setRecipients(Arrays.toString(message.getAllRecipients()));
////				emailDTO.setRecipients(Arrays.toString(message.getRecipients(Message.RecipientType.TO)));
//
//				Object content = message.getContent();
//
//				if (content instanceof String) {
//					emailDTO.setText(content.toString());
//				} else if (content instanceof Multipart) {
//					emailDTO.setText(this.getTextFromMultipart((Multipart) content));
//				}
//
//				messageList.add(emailDTO);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (folder != null && folder.isOpen()) {
					folder.close(false);
				}
				if (store != null && store.isConnected()) {
					store.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	void receivedBox() {
	}

	@Test
	void sentBox() {
	}
}