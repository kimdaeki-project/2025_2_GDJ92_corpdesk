<%@ page language="java" contentType="text/html; charset=UTF-8"
				 pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
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
<main class="container d-flex align-items-center justify-content-center" style="min-height: 60vh">
	<div class="d-flex flex-column justify-content-between">
		<div class="row justify-content-center">
			<div>
				<div class="card card-default mb-0">
					<div class="card-header pb-0">
						<div class="app-brand w-100 d-flex justify-content-center border-bottom-0">
							<h4 class="mb-5">내 급여</h4>
						</div>
					</div>

					<div class="card-body px-5 pb-5 pt-0">
						<table class="table table-hover table-product table-striped">
							<thead>
							<tr>
								<th>번호</th>
								<th>기본급</th>
								<th>수당</th>
								<th>공제</th>
								<th>실지급액</th>
								<th>지급일</th>
							</tr>
							</thead>
							<tbody>
							<c:forEach var="s" items="${salaryList}">
								<tr onclick="location.href='./salary/${s.paymentId}'" style="cursor: pointer">
									<td>${s.paymentId}</td>
									<td><fmt:formatNumber value="${s.baseSalary}" pattern="#,###" /></td>
									<td><fmt:formatNumber value="${s.allowanceAmount}" pattern="#,###" /></td>
									<td><fmt:formatNumber value="${s.deductionAmount}" pattern="#,###" /></td>
									<td><fmt:formatNumber value="${s.baseSalary + s.allowanceAmount - s.deductionAmount}" pattern="#,###" /></td>
									<td>${fn:substring(s.paymentDate, 0, 10)}</td>
								</tr>
							</c:forEach>
							</tbody>
						</table>
					</div>

					<div class="card card-default align-items-center  border-0">
						<nav aria-label="Page navigation example">
							<ul class="pagination">
								<li class="page-item ${isFirst ? 'd-none' : ''}">
									<a class="page-link" href="./salary?page=${startPage}" aria-label="Previous">
										<span aria-hidden="true" class="mdi mdi-chevron-left"></span>
										<span class="sr-only">Previous</span>
									</a>
								</li>
								<c:forEach var="p" begin="${startPage}" end="${endPage}">
									<li class="page-item ${p eq currentPage ? 'active' : ''}">
										<a class="page-link" href="./salary?page=${p + 1}">${p + 1}</a>
									</li>
								</c:forEach>
								<li class="page-item ${isLast ? 'd-none' : ''}">
									<a class="page-link" href="./salary?page=${endPage + 2}" aria-label="Next">
										<span aria-hidden="true" class="mdi mdi-chevron-right"></span>
										<span class="sr-only">Next</span>
									</a>
								</li>
							</ul>
						</nav>
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