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
		<main class="col-lg-8 col-xl-9 col-xxl-10">
			<div class="email-right-column p-4 p-xl-5">
				<div id="spinner" class="d-flex d-none align-items-center justify-content-center" style="height: 160px">
					<div class="sk-pulse"></div>
				</div>
				<div id="detail" class="d-none border rounded email-details">
					<div class="email-details-header">
						<h4 id="subject" class="text-dark"></h4>
					</div>
					<div class="email-details-content">
						<div class="email-details-content-header">
							<ul>
								<li>
									<h6 id="from" class="mt-0 text-dark font-weight-bold">From: </h6>
								</li>
								<li>
									<span id="recipients">To: </span>
								</li>
								<li>
									<time id="sentDate">Sent: </time>
								</li>
							</ul>
						</div>
						<div>
							<div id="content" class="overflow-auto"></div>
						</div>
					</div>
				</div>
			</div>
		</main>
	</div>
</div>

<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/dompurify@3.2.7/dist/purify.min.js"></script>
<script src="/js/email/detail.js"></script>

<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>