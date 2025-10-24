package com.goodee.corpdesk.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileConfig implements WebMvcConfigurer {
	@Value("${app.upload}")
	private String path; // C:/upload/corpdesk/
	
	@Value("${app.url}")
	private String url; // /files/**
	
	// url로 요청이 들어오면 path로 보내는 메서드
	// /files/notice/파일명 이라는 경로가 들어오면, D:/upload/notice/파일명 으로 가게끔 설정
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
		.addResourceHandler(url)
		.addResourceLocations("file:" + path);
	}
}

