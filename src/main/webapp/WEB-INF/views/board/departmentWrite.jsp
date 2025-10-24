<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>부서글 쓰기</title>
  <c:import url="/WEB-INF/views/include/head.jsp"/>
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/>

<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

<c:import url="/WEB-INF/views/include/header.jsp"/>

<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
<!-- 내용 시작 -->

<div class="card card-default">
  <div class="card-header">
    <h2 class="mb-0">부서 글쓰기</h2>
  </div>

  <div class="card-body">
    <form method="post" action="${pageContext.request.contextPath}/board/department">
      <!-- departmentId는 비워두면 Service에서 Authentication 부서ID로 채움 -->

      <div class="form-group">
        <label for="title">제목</label>
        <input type="text" name="title" id="title" class="form-control" placeholder="제목을 입력하세요" required />
      </div>

      <div class="form-group">
        <label for="content">내용</label>
        <textarea name="content" id="content" rows="10" class="form-control" placeholder="내용을 입력하세요" required></textarea>
      </div>

      <div class="d-flex justify-content-end">
        <a href="${pageContext.request.contextPath}/board/department" class="btn btn-light mr-2">취소</a>
        <button type="submit" class="btn btn-primary">
          <i class="mdi mdi-check mr-1"></i>등록
        </button>
      </div>
    </form>
  </div>
</div>

<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>