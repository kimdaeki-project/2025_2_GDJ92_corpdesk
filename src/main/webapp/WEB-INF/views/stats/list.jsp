<%@ page language="java" contentType="text/html; charset=UTF-8"
				 pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<c:import url="/WEB-INF/views/include/head.jsp"/>

	<script src="https://cdn.jsdelivr.net/npm/apexcharts"></script>
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/>

<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

<c:import url="/WEB-INF/views/include/header.jsp"/>

<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
<!-- 내용 시작 -->

<div class="card card-default">
	<div class="card-body p-6">
		<form id="searchForm">
			<div class="form-group d-flex">

				<div class="mr-3">
					<label class="d-block">조회기간</label>
					<div class="d-flex">
						<input type="date" id="start" class="form-control mr-2">
            <span class=" mr-2">~</span>
						<input type="date" id="end" class="form-control mr-2">
					</div>
				</div>

				<div class="mr-3">
					<label class="d-block">부서</label>
					<select id="department" class="form-control">
						<option>선택</option>
						<c:forEach var="d" items="${departmentList}">
							<option value="${d.departmentId}">${d.departmentName}</option>
						</c:forEach>
					</select>
				</div>

				<div class="mr-3">
					<label class="d-block">직급</label>
					<select id="position" class="form-control">
						<option>선택</option>
						<c:forEach var="p" items="${positionList}">
							<option value="${p.positionId}">${p.positionName}</option>
						</c:forEach>
					</select>
				</div>

				<div>
					<label class="d-block">&nbsp;</label>
					<button class="btn btn-sm btn-primary">
						<i class="mdi mdi-database-search"></i> 조회
					</button>
				</div>

			</div>
		</form>
	</div>
</div>
<div class="card card-default">
	<div class="card-body">
		<div id="chart1"></div>
	</div>
</div>
<div class="row">
	<div class="card card-default col-xl-3 col-md-6">
		<div class="card-body">
			<div id="chart2"></div>
		</div>
	</div>
	<div class="card card-default col-xl-3 col-md-6">
		<div class="card-body">
			<div id="chart3"></div>
		</div>
	</div>
	<div class="card card-default col-xl-3 col-md-6">
		<div class="card-body">
			<div id="chart4"></div>
		</div>
	</div>
	<div class="card card-default col-xl-3 col-md-6">
		<div class="card-body">
			<div id="chart5"></div>
		</div>
	</div>
</div>
<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<script src="/js/stats/chart1.js"></script>
<script src="/js/stats/chart2.js"></script>
<script src="/js/stats/chart3.js"></script>
<script src="/js/stats/chart4.js"></script>
<script src="/js/stats/chart5.js"></script>

<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>