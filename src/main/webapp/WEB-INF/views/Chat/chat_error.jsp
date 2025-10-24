<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Chat Page</title>
<c:import url="/WEB-INF/views/include/head.jsp" />
<script
	src="https://cdn.jsdelivr.net/npm/sockjs-client/dist/sockjs.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/stompjs/lib/stomp.min.js"></script>
<link rel="stylesheet" href="/css/chat/chat_page.css">
</head>
<body>
	
	
	
	
	

	<script type="text/javascript" src="/js/chat/chatError.js"></script>
	<!-- 내용 끝 -->


	<c:import url="/WEB-INF/views/include/body_wrapper_end2.jsp" />
</html>