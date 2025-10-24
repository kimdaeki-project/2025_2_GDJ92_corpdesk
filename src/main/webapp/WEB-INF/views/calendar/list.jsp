<%@ page language="java" contentType="text/html; charset=UTF-8"
				 pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<c:import url="/WEB-INF/views/include/head.jsp"/>

<%--	<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css' rel='stylesheet'>--%>
<%--	<link href='https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css' rel='stylesheet'>--%>
<%--	<link rel="stylesheet" href="/css/calendar/list.css">--%>

	<script src='https://cdn.jsdelivr.net/npm/fullcalendar@6.1.19/index.global.min.js'></script>
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/>

<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

<c:import url="/WEB-INF/views/include/header.jsp"/>

<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>

<c:import url="/WEB-INF/views/schedule/modal.jsp"/>

<!-- 내용 시작 -->
<div class="row">
  <div class="col-lg-2">
    <div class="card card-default">
      <div class="card-body">
        <jsp:include page="aside.jsp"/>
      </div>
    </div>
  </div>

  <div class="col-lg-10">
    <div class="card card-default w-100">
      <div class="card-body">
        <div id='calendar'></div>
      </div>
    </div>
  </div>
</div>

<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<script src="/js/calendar/list.js"></script>

<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>