<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<c:import url="/WEB-INF/views/include/head.jsp"/>
</head>

<body>
  <!-- 내용 시작 -->
  <div class="container d-flex align-items-center justify-content-center" style="min-height: 100vh">
    <div class="d-flex flex-column justify-content-between">
      <div class="row justify-content-center mt-5">
        <div class="text-center page-404">
          <h1 class="error-title">${errorCode}</h1>
          <p class="pt-4 pb-5 error-subtitle">${errorMessage}</p>
          <a href="/" class="btn btn-primary btn-pill">홈으로 돌아가기</a>
        </div>
      </div>
    </div>
  </div>
  <!-- 내용 끝 -->
</body>
</html>