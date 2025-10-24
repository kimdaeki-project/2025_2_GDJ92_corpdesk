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
			<a class="nav-link active" href="/employee/detail">내 정보</a>
		</li>
		<li class="nav-item">
			<a class="nav-link" href="/employee/salary">내 급여</a>
		</li>
		<li class="nav-item">
			<a class="nav-link" href="/employee/update/email">이메일 변경</a>
		</li>
		<li class="nav-item">
			<a class="nav-link" href="/employee/update/password">비밀번호 변경</a>
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
							<h4 class="mb-5">내 정보</h4>
						</div>
					</div>

					<div class="card-body px-5 pb-5 pt-0">
						<ul class="row justify-content-start">
							<li class="d-flex col-md-12 mb-1">
								<h6 class="col-4 px-0">이름:</h6>
								<span class="col-8 px-0">${employee.name}</span>
							</li>
							<li class="d-flex col-md-12 mb-1">
								<h6 class="col-4 px-0">이메일:</h6>
								<span class="col-8 px-0">${employee.externalEmail}</span>
							</li>
							<li class="d-flex col-md-12 mb-1">
								<h6 class="col-4 px-0">부서:</h6>
								<span class="col-8 px-0">${department.departmentName}</span>
							</li>
							<%--		<li>직책: ${employee.}</li>--%>
							<li class="d-flex col-md-12 mb-1">
								<h6 class="col-4 px-0">직위:</h6>
								<span class="col-8 px-0">${position.positionName}</span>
							</li>
							<li class="d-flex col-md-12 mb-1">
								<h6 class="col-4 px-0">휴대전화:</h6>
								<span class="col-8 px-0">${employee.mobilePhone}</span>
							</li>
							<li class="d-flex col-md-12 mb-1">
								<h6 class="col-4 px-0">직통전화:</h6>
								<span class="col-8 px-0">${employee.directPhone}</span>
							</li>
							<li class="d-flex col-md-12 mb-1">
								<h6 class="col-4 px-0">입사일자:</h6>
								<span class="col-8 px-0">${employee.hireDate}</span>
							</li>

							<li class="d-flex col-md-12 mb-1">
								<h6 class="col-4 px-0">주민 (외국인)</br>등록 번호:</h6>
								<span class="col-8 px-0">${employee.residentNumber}</span>
							</li>
							<li class="d-flex col-md-12 mb-1">
								<h6 class="col-4 px-0">국적:</h6>
								<span class="col-8 px-0">${employee.nationality}</span>
							</li>
							<li class="d-flex col-md-12 mb-1">
								<h6 class="col-4 px-0">체류 자격:</h6>
								<span class="col-8 px-0">${employee.visaStatus}</span>
							</li>
							<li class="d-flex col-md-12 mb-1">
								<h6 class="col-4 px-0">영문 이름:</h6>
								<span class="col-8 px-0">${employee.englishName}</span>
							</li>

							<li class="d-flex col-md-12 mb-1">
								<h6 class="col-4 px-0">성별:</h6>
								<span class="col-8 px-0">${employee.gender}</span>
							</li>
							<li class="d-flex col-md-12 mb-1">
								<h6 class="col-4 px-0">생년월일:</h6>
								<span class="col-8 px-0">${employee.birthDate}</span>
							</li>
							<li class="d-flex col-md-12 mb-1">
								<h6 class="col-4 px-0">주소:</h6>
								<span class="col-8 px-0">${employee.address}</span>
							</li>
							<%--		<li>현재 기본급: ${employee.}</li>--%>
							<%--		<li>급여 은행: ${employee.}</li>--%>
							<%--		<li>급여 계좌: ${employee.}</li>--%>
						</ul>
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