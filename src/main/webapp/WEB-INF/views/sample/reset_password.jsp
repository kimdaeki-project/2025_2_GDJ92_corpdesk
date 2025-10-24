<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<c:import url="/WEB-INF/views/include/head.jsp"/>
</head>

<body class="bg-light-gray" id="body">
	<div class="container d-flex align-items-center justify-content-center"
		style="min-height: 100vh">
		<div class="d-flex flex-column justify-content-between">
			<div class="row justify-content-center mt-5">
				<div class="col-md-11">
					<div class="card card-default">
						<div class="card-header">
							<div
								class="app-brand w-100 d-flex justify-content-center border-bottom-0">
								<a class="w-auto pl-0" href="javascript:void(0)" style="pointer-events: none;"> <img
									src="/images/logo.png" alt="Mono"> <span
									class="brand-name text-dark">Corpdesk</span>
								</a>
							</div>
						</div>
						<div class="card-body p-5">
							<h4 class="text-dark mb-5">비밀번호 변경</h4>
							<span>Corpdesk 계정정보에 등록된 외부 메일 주소로 임시 비밀번호를 전송합니다.</span>
							<form action="/index.html"> <!-- TODO 추후 사용시 form태그 속성 수정 -->
								<div class="row">
									<div class="form-group col-md-12 mb-4">
										<input type="text" class="form-control input-lg" id="username" placeholder="Id">
									</div>

									<div class="col-md-12">
										<button type="submit" class="btn btn-primary btn-pill">조회</button>
										
										<hr>
										<div class="d-flex flex-wrap justify-content-between">
											<p>등록된 외부 메일 주소</p>
											<p>as**@asdf.com</p> <!-- TODO 샘플 텍스트를 넣어놨으므로 추후 실제 데이더를 여기에 넣기 -->
										</div>
										<hr>
										
										<div class="d-flex justify-content-center pt-4">
											<button type="submit" class="btn btn-outline-primary btn-pill mr-1">취소</button>
											<button type="submit" class="btn btn-primary btn-pill">임시 비밀번호 전송</button>
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