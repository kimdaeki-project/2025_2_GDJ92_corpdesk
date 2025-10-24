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
<div class="card-footer card-profile-footer">
	<ul class="nav nav-border-top justify-content-center">
		<li class="nav-item">
			<a class="nav-link" href="/employee/detail">내 정보</a>
		</li>
		<li class="nav-item">
			<a class="nav-link active" href="/employee/salary">내 급여</a>
		</li>
		<li class="nav-item">
			<a class="nav-link" href="/employee/update/email">이메일 변경</a>
		</li>
		<li class="nav-item">
			<a class="nav-link" href="/employee/update/password">비밀번호 변경</a>
		</li>
	</ul>
</div>
<div class="card card-default col-xl-6">
	<div class="card-header">
		<div>
			<h2>급여 상세</h2>
		</div>
	</div>
	<div class="card-body">
		<main>
			<div>
				<nav class="row">
					<div class="col-12 py-3 mb-3 border rounded">
						<ul id="info" class="col row row-cols-2 justify-content-center"></ul>
					</div>
				</nav>
				<article class="row row-cols-2">
					<div class="col-6">
						<div class="text-center">
							<h5>공제 항목</h5>
							<hr/>
						</div>
						<ul id="deduction" class="text-right"></ul>
					</div>
					<div class="col-6">
						<div class="text-center">
							<h5>지급 항목</h5>
							<hr/>
						</div>
						<ul id="salary" class="text-right"></ul>
						<ul id="allowance" class="text-right"></ul>
					</div>
					<div class="col-12">
						<div>
							<ul id="sum" class="row text-right my-3"></ul>
							<ul id="total" class="row text-right justify-content-end"></ul>
						</div>
					</div>
				</article>
			</div>
		</main>
	</div>
</div>
<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<script>
	const data = JSON.parse('${map}');
	console.log(data);


</script>

<script src="/js/employee/salaryDetail.js"></script>
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>