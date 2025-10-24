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
			<a class="nav-link" href="/employee/salary">내 급여</a>
		</li>
		<li class="nav-item">
			<a class="nav-link" href="/employee/update/email">이메일 변경</a>
		</li>
		<li class="nav-item">
			<a class="nav-link active" href="/employee/update/password">비밀번호 변경</a>
		</li>
	</ul>
</div>

<main class="container d-flex align-items-center justify-content-center" style="min-height: 60vh">
	<div class="d-flex flex-column justify-content-between">
		<div class="row justify-content-center">
			<div class="col-lg-6 col-xl-5 col-md-10 ">
				<div class="card card-default mb-0">
					<div class="card-header pb-0">
						<div class="app-brand w-100 d-flex justify-content-center border-bottom-0">
							<h4 class="text-dark text-center mb-5">비밀번호 변경</h4>
<%--							<a class="w-auto pl-0" href="/index.html">--%>
<%--								<img src="images/logo.png" alt="Mono">--%>
<%--								<span class="brand-name text-dark">MONO</span>--%>
<%--							</a>--%>
						</div>
					</div>
					<div class="card-body px-5 pb-5 pt-0">
						<form:form action="/employee/update/password" method="post" modelAttribute="employee">
							<div class="row">
								<div class="form-group col-md-12 mb-4">
										<%--								<form:label path="password">현재 비밀번호: </form:label>--%>
									<form:password path="password" placeholder="현재 비밀번호" class="form-control input-lg"/>
									<form:errors path="password"/>
								</div>
								<div class="form-group col-md-12 mb-4">
										<%--								<form:label path="passwordNew">새 비밀번호: </form:label>--%>
									<form:password path="passwordNew" placeholder="새 비밀번호" class="form-control input-lg"/>
									<form:errors path="passwordNew"/>
								</div>
								<div class="form-group col-md-12">
										<%--								<form:label path="passwordCheck">새 비밀번호 확인: </form:label>--%>
									<form:password path="passwordCheck" placeholder="새 비밀번호 확인" class="form-control input-lg"/>
									<form:errors path="passwordCheck"/>
								</div>
								<div class="col-md-12">
									<form:button class="btn btn-primary btn-pill mb-4">비밀번호 변경</form:button>
								</div>
							</div>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</main>
<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>