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
<div class="d-flex justify-content-center">
  <div class="card card-default col-xl-6">
    <div class="card-header">
      <div>
        <h2>급여 상세</h2>
      </div>
    </div>
    <div class="card-body">
      <main>
        <div>
          <div class="card mb-4">
            <div class="card-body p-4">
              <div class="row">
                <ul id="info" class="col row row-cols-2 justify-content-center"></ul>
              </div>
            </div>
          </div>
          <article class="row row-cols-2">

            <div class="col-6">
              <h5 class="mb-3">공제 항목</h5>
              <div class="card">
                <div class="card-body p-4">
                  <ul id="deduction" class="list-unstyled mb-0"></ul>
                </div>
              </div>
            </div>

            <div class="col-6">
              <h5 class="mb-3">지급 항목</h5>
              <div class="card">
                <div class="card-body p-4">
                  <ul id="salary" class="list-unstyled mb-0"></ul>
                  <ul id="allowance" class="list-unstyled mb-0"></ul>
                </div>
              </div>
            </div>

            <div class="col-12">
              <div class="card bg-light mt-4">
                <div class="card-body p-4">
                  <ul id="sum" class="col-12 list-unstyled mb-3"></ul>
                  <ul id="total" class="col-12 list-unstyled mb-0"></ul>
                </div>
              </div>
            </div>

          </article>
        </div>
      </main>

      <div class="d-flex justify-content-end mt-4">
        <a href="/salary" class="btn btn-primary">목록으로</a>
      </div>
    </div>
  </div>
</div>
<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<script src="/js/salary/detail.js"></script>
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>