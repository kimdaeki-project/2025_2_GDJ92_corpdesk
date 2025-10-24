<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>공지 게시판</title>
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
    <h2 class="mb-0"><c:out value="${post.title}"/></h2>
  </div>

  <div class="card-body">
    <!-- 게시글 정보 -->
    <div class="mb-4 pb-3 border-bottom">
			<span class="text-muted">
				<i class="mdi mdi-account mr-1"></i>작성자: <c:out value="${post.username}"/>
			</span>
      <span class="text-muted mx-2">|</span>
      <span class="text-muted">
				<i class="mdi mdi-clock-outline mr-1"></i>작성일:
				<spring:eval expression="T(java.time.format.DateTimeFormatter).ofPattern('yyyy-MM-dd HH:mm').format(post.createdAt)"/>
			</span>
    </div>

    <!-- 게시글 내용 -->
    <div class="mb-4" style="min-height: 200px; white-space: pre-wrap; line-height: 1.8;">
      <c:out value="${post.content}"/>
    </div>

    <!-- 버튼 영역 -->
    <div class="d-flex justify-content-between align-items-center pt-3 border-top">
      <a href="${pageContext.request.contextPath}/board/notice" class="btn btn-light">
        <i class="mdi mdi-format-list-bulleted mr-1"></i>목록으로
      </a>
      <c:if test="${isOwner}">
        <div>
          <a href="${pageContext.request.contextPath}/board/notice/${post.boardId}/edit" class="btn btn-info">
            <i class="mdi mdi-pencil mr-1"></i>수정
          </a>
          <form id="noticeDelForm" method="post" action="${pageContext.request.contextPath}/board/notice/${post.boardId}/delete" class="d-inline">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="button" onclick="deleteNotice()" class="btn btn-danger">
              <i class="mdi mdi-delete mr-1"></i>삭제
            </button>
          </form>
        </div>
      </c:if>
    </div>
  </div>
</div>

<script>
  async function deleteNotice() {
    const form = document.querySelector('#noticeDelForm');

    const result = await Swal.fire({
      text: "정말 삭제하시겠습니까?",
      icon: "question",
      showCancelButton: true,
      confirmButtonText: "삭제",
      cancelButtonText: "취소",
      reverseButtons: true
    });

    if(result.isConfirmed) form.submit();
  }
</script>

<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>