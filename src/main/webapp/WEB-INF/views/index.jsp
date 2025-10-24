<%@ page language="java" contentType="text/html; charset=UTF-8"
				 pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>로그인</title>
	<c:import url="/WEB-INF/views/include/head.jsp"/>
</head>

<body class="bg-light-gray" id="body">
<div class="container d-flex align-items-center justify-content-center"
		 style="min-height: 100vh">
	<div class="d-flex flex-column justify-content-between">
		<div class="row justify-content-center">
			<div class="col-lg-6 col-md-10">
				<div class="card card-default mb-0">
					<div class="card-header pb-0">
						<div
										class="app-brand w-100 d-flex justify-content-center border-bottom-0">
							<a class="w-auto pl-0" href="javascript:void(0)" style="pointer-events: none;">
								<img src="/images/logo.png" alt="Mono">
								<span class="brand-name text-dark">Corpdesk</span>
							</a>
						</div>
					</div>
					<div class="card-body px-5 pb-5 pt-0">
						<p>${msg}</p>
						<form method="post" action="/login-process"> <!-- TODO 추후 사용시 form태그 속성 수정 -->
							<div class="row">
								<div class="form-group col-md-12 mb-4">
									<input type="text" class="form-control input-lg" id="username" name="username"
												 placeholder="Id">
								</div>
								<div class="form-group col-md-12 ">
									<input type="password" class="form-control input-lg"
												 id="password" name="password" placeholder="Password">
								</div>
								<div class="col-md-12">

									<div class="d-flex justify-content-end mb-3">

<%--										<div class="custom-control custom-checkbox mr-3 mb-3">--%>
<%--											<input type="checkbox" class="custom-control-input"--%>
<%--														 id="customCheck2"> <label--%>
<%--														class="custom-control-label" for="customCheck2">계정--%>
<%--											기억하기</label>--%>
<%--										</div>--%>
										<a class="text-color" href="/reset/password"> 비밀번호를 잊어버리셨나요? </a> <!-- TODO 추후 사용시 a태그의 href속성 수정 -->

									</div>

									<div class="d-flex justify-content-center">
										<button type="submit" class="btn btn-primary btn-pill mb-4">로그인</button>
									</div>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>