<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>공지 수정</title>
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
    <h2 class="mb-0">공지 수정</h2>
  </div>

  <div class="card-body">
    <form method="post" action="${pageContext.request.contextPath}/board/notice/${post.boardId}">

      <div class="form-group">
        <label for="title">제목</label>
        <input type="text" name="title" id="title" class="form-control" value="${post.title}" required />
      </div>

      <div class="form-group">
        <label for="content">내용</label>
        <textarea name="content" id="content" rows="10" class="form-control" required>${post.content}</textarea>
      </div>

      <div class="d-flex justify-content-end">
        <a href="${pageContext.request.contextPath}/board/notice/${post.boardId}" class="btn btn-light mr-2">취소</a>
        <button type="submit" class="btn btn-info">
          <i class="mdi mdi-content-save mr-1"></i>저장
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