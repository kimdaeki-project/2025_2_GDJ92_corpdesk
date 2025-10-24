<%@ page language="java" contentType="text/html; charset=UTF-8"
				 pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
			<div class="email-right-column p-4 p-xl-5">
				<div class="email-body-head mb-5 ">
					<h4 class="text-dark">New Message</h4>
				</div>
				<form id="form_data" class="email-compose mb-5" <%--action="/api/email/sending"--%> method="post">
					<div class="form-group">
						<input type="email" class="form-control" name="to" id="exampleEmail" placeholder="To: ">
					</div>
					<div class="form-group">
						<input type="text" class="form-control" name="subject" id="exampleSubject" placeholder="Subject ">
					</div>
					<div id="standalone">
						<div id="toolbar">
							<span class="ql-formats">
								<select class="ql-font"></select>
								<select class="ql-size"></select>
							</span>
							<span class="ql-formats">
								<button class="ql-bold"></button>
								<button class="ql-italic"></button>
								<button class="ql-underline"></button>
							</span>
							<span class="ql-formats">
								<select class="ql-color"></select>
							</span>
							<span class="ql-formats">
								<button class="ql-blockquote"></button>
							</span>
							<span class="ql-formats">
								<button class="ql-list" value="ordered"></button>
								<button class="ql-list" value="bullet"></button>
								<button class="ql-indent" value="-1"></button>
								<button class="ql-indent" value="+1"></button>
							</span>
							<span class="ql-formats">
								<button class="ql-direction" value="rtl"></button>
								<select class="ql-align"></select>
							</span>
						</div>
					</div>
					<textarea id="text" name="text" hidden></textarea>
					<div class="form-group">
						<div id="editor" class="ql-container ql-snow"></div>
					</div>
				</form>
				<button id="submit_btn" class="btn btn-primary btn-pill mb-5" type="submit">보내기</button>
			</div>
		</main>
	</div>
</div>
<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<script src="/js/email/sending.js"></script>

<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>