package com.goodee.corpdesk.email;

import com.goodee.corpdesk.employee.Employee;
import com.goodee.corpdesk.employee.EmployeeRepository;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.internet.MimeUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class EmailService {

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired // 양방향 암호화
	private AesBytesEncryptor aesBytesEncryptor;

	// 받은 메일 상세
	public EmailDTO receivedDetail(String username, Integer emailNo) {
		Employee employee = getEmployee(username);
		return mailDetail(employee, emailNo, "INBOX");
	}

	// 보낸 메일 상세
	public EmailDTO sentDetail(String username, Integer emailNo) {
		Employee employee = getEmployee(username);
		return mailDetail(employee, emailNo, getSentFolderName(employee));
	}

	// 받은 메일 목록
	public PagedModel<EmailDTO> receivedList(String username, Pageable pageable) {
		Employee employee = getEmployee(username);
		return this.mailList(employee, pageable, "INBOX");
	}

	// 보낸 메일 목록
	public PagedModel<EmailDTO> sentList(String username, Pageable pageable) {
		Employee employee = getEmployee(username);
		return mailList(employee, pageable, getSentFolderName(employee));
	}

	// 보낸 메일함 이름
	private String getSentFolderName(Employee employee) {
		if (employee.getExternalEmail().contains("gmail")) {
			return "[Gmail]/보낸편지함";
		} else {
			return "Sent Messages";
		}
	}

	// 메일 상세 조회
	private EmailDTO mailDetail(Employee employee, Integer emailNo, String folderName) {
		return withStoreAndFolder(employee, folderName, (folder) -> {
			Message[] messages = folder.getMessages();
			Message message = messages[emailNo];
			return messageToDto(message);
		});
	}

	// 메일 목록 가져오기
	private PagedModel<EmailDTO> mailList(Employee employee, Pageable pageable, String folderName) {
		return withStoreAndFolder(employee, folderName, (folder) -> {
			int total = folder.getMessageCount();
			int size = pageable.getPageSize();
			int page = pageable.getPageNumber();

			int end = total - (page * size);
			int start = end - size + 1;

			if (total == 0 || end < start) {
				Page<EmailDTO> result = new PageImpl<>(Collections.emptyList(), pageable, total);
				return new PagedModel<>(result);
			}

			Message[] messages = folder.getMessages(start, end);
			List<EmailDTO> messageList = new ArrayList<>();
			for (Message message : messages) {
				messageList.add(messageToDto(message));
			}

			Page<EmailDTO> result = new PageImpl<>(messageList, pageable, total);

			return new PagedModel<>(result);
		});
	}

	// 메일 폴더 가져오기
	private <T> T withStoreAndFolder(Employee employee, String folderName, EmailCallback<T> callback) {
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

		try { // store, folder 연결
			store = session.getStore("imap");
			store.connect(host, myEmail.split("@")[0], password);

			folder = store.getFolder(folderName);
			folder.open(Folder.READ_ONLY);

			return callback.doInFolder(folder);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally { // 연결 닫기
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

	// 람다식을 쓰기 위한 인터페이스
	@FunctionalInterface
	interface EmailCallback<T> {
		T doInFolder(Folder folder) throws Exception;
	}

	// 꺼내온 Message를 DTO로 전환
	private EmailDTO messageToDto(Message message) throws Exception {
		EmailDTO emailDTO = new EmailDTO();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String from = Arrays.toString(message.getFrom());
		if (from.toUpperCase().contains("=?UTF-8?B?")) {
			from = '[' + MimeUtility.decodeText(from.substring(from.indexOf("=?"), from.lastIndexOf("?=") + 2))
					+ from.substring(from.lastIndexOf("?=") + 2);
		}
		emailDTO.setFrom(from);
		emailDTO.setSubject(message.getSubject());
		emailDTO.setEmailNo(message.getMessageNumber());
		if (message.getReceivedDate() == null) emailDTO.setReceivedDate("0000-00-00 00:00:00");
		else emailDTO.setReceivedDate(format.format(message.getReceivedDate()));
		if (message.getSentDate() == null) emailDTO.setSentDate("0000-00-00 00:00:00");
		else emailDTO.setSentDate(format.format(message.getSentDate()));
		emailDTO.setRecipients(Arrays.toString(message.getAllRecipients()));

		Object content = message.getContent();

		if (content instanceof String) {
			String text = content.toString().replaceAll("\n", "<br/>")
					.replaceAll(" ", "&nbsp;");
			emailDTO.setText(text);
		} else if (content instanceof Multipart) {
			emailDTO.setText(getTextFromMultipart((Multipart) content));
		}

		return emailDTO;
	}

	private String getTextFromMultipart(Multipart multipart) throws Exception {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < multipart.getCount(); i++) {
			BodyPart bodyPart = multipart.getBodyPart(i);

//			if (bodyPart.isMimeType("text/plain")) {
//				sb.append(bodyPart.getContent()).append('\n');
//			} else
			if (bodyPart.isMimeType("text/html")) {
				sb.append(bodyPart.getContent());
			} else if (bodyPart.isMimeType("multipart/*")) {
				MimeMultipart mimeMultipart = (MimeMultipart) bodyPart.getContent();
				sb.append(this.getTextFromMultipart(mimeMultipart));
			}
//			else {
//				log.info("첨부 파일: {}", bodyPart.getFileName());
//			}
		}

		return sb.toString();
	}

	// 유저 정보를 가져오면서, 이메일 비밀번호를 디코딩
	private Employee getEmployee(String username) {
		Optional<Employee> optional = employeeRepository.findById(username);
		Employee employee = optional.get();

		if (employee.getEncodedEmailPassword() == null) {
			throw new RuntimeException("비밀번호가 입력되지 않았습니다.");
		}

		byte[] decoded = aesBytesEncryptor.decrypt(employee.getEncodedEmailPassword());
		employee.setExternalEmailPassword(new String(decoded));

		return employee;
	}

	// 메일 보내기
	public void sendMail(SendDTO sendDTO) throws Exception {
		Optional<Employee> optional = employeeRepository.findById(sendDTO.getUser());
		Employee employee = optional.get();

		if (employee.getEncodedEmailPassword() != null) {
			byte[] decoded = aesBytesEncryptor.decrypt(employee.getEncodedEmailPassword());
			employee.setExternalEmailPassword(new String(decoded));
		}

		JavaMailSender mailSender = this.javaMailSender(employee);

		// NOTE: SimpleMessage에서 MimeMessage로 전환
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		String encoded = MimeUtility.encodeText(employee.getName());
		String from = encoded + " <" + employee.getExternalEmail() + ">";
		helper.setFrom(from);
		helper.setTo(sendDTO.getTo());
		helper.setSubject(sendDTO.getSubject());
		helper.setText(sendDTO.getText(), true);

		mailSender.send(message);
	}

	// JavaMailSender 구현
	public JavaMailSender javaMailSender(Employee employee) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		String email = employee.getExternalEmail();
		String host = "smtp." + email.split("@")[1];
		String password = employee.getExternalEmailPassword();

		mailSender.setHost(host);
//		mailSender.setUsername(email.split("@")[0]);
		mailSender.setUsername(email);
		mailSender.setPassword(password);

		mailSender.setPort(465);

		mailSender.setJavaMailProperties(getMailProperties());

		return mailSender;
	}

	// JavaMailSender 설정
	private Properties getMailProperties() {
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
//		props.setProperty("mail.smtp.host", host);
//		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.starttls.enable", "true");
		props.setProperty("mail.smtp.ssl.enable", "true");

		return props;
	}
}
