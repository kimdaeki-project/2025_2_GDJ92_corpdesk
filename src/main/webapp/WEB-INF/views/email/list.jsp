<%@ page language="java" contentType="text/html; charset=UTF-8"
				 pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<c:import url="/WEB-INF/views/include/head.jsp"/>
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/>

<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

<c:import url="/WEB-INF/views/include/header.jsp"/>

<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
<!-- 내용 시작 -->
<div class="email-wrapper rounded border bg-white">
	<div class="row no-gutters justify-content-center">
		<jsp:include page="aside.jsp"/>
		<%--	<%@ include file="aside.jsp"%>--%>
		<main class="col-lg-8 col-xl-9 col-xxl-10">
			<div id="spinner" class="d-flex d-none align-items-center justify-content-center" style="height: 160px">
				<div class="sk-pulse"></div>
			</div>
			<div id="missing" class="d-none align-items-center justify-content-center" style="height: 160px">
				<p id="errorMsg">메일이 없습니다.</p>
			</div>
			<div id="list" class="d-none email-right-column p-4 p-xl-5">
				<div class="email-right-header justify-content-center mb-0">
					<%-- 페이징 --%>
					<!-- Flat Rounded Pagination -->
					<div id="card" class="card card-default align-items-center border-0 mb-0">
					</div>
				</div>
				<div class="border border-top-0 rounded table-responsive email-list">
					<table class="table mb-0 table-email">
						<tbody id="tbody"></tbody>
					</table>
				</div>
			</div>
		</main>
	</div>
</div>
<script src="/js/email/list.js"></script>
<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>