<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>게시판</title>
  <c:import url="/WEB-INF/views/include/head.jsp"/>
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/>

<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

<c:import url="/WEB-INF/views/include/header.jsp"/>

<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
<!-- 내용 시작 -->

<div class="card card-default">
  <div class="card-header d-flex justify-content-between align-items-center">
    <h2 class="mb-0"><c:out value="${title != null ? title : '게시판'}"/></h2>
    <a class="btn btn-primary" href="${writePath}">
      <i class="mdi mdi-pencil mr-1"></i>글쓰기
    </a>
  </div>

  <div class="card-body">
    <!-- 탭/네비게이션 -->
    <div class="mb-4">
      <a href="${pageContext.request.contextPath}/board/notice" class="btn btn-outline-primary">공지 게시판</a>
      <a href="${pageContext.request.contextPath}/board/department" class="btn btn-outline-primary">부서 게시판</a>
    </div>

    <!-- 빈 목록 처리 -->
    <c:if test="${empty post}">
      <div class="text-center p-5 text-muted">
        <p class="mb-0">게시글이 없습니다.</p>
      </div>
    </c:if>

    <!-- 목록 -->
    <c:if test="${not empty post}">
      <div class="table-responsive">
        <table class="table table-hover">
          <thead>
          <tr>
            <th style="width:55%;">제목</th>
            <th style="width:15%;">작성자</th>
            <th style="width:15%;">작성일</th>
            <th style="width:15%;">조회수</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach items="${post}" var="post">
            <tr>
              <td>
                <c:choose>
                  <c:when test="${title eq '공지 게시판'}">
                    <a href="${pageContext.request.contextPath}/board/notice/${post.boardId}" style="color: inherit; text-decoration: none;">
                      <c:out value="${post.title}"/>
                    </a>
                  </c:when>
                  <c:otherwise>
                    <a href="${pageContext.request.contextPath}/board/department/${post.boardId}" style="color: inherit; text-decoration: none;">
                      <c:out value="${post.title}"/>
                    </a>
                  </c:otherwise>
                </c:choose>
              </td>
              <td><c:out value="${post.username}"/></td>
              <td>
                <spring:eval expression="T(java.time.format.DateTimeFormatter).ofPattern('yyyy-MM-dd HH:mm').format(post.createdAt)"/>
              </td>
              <td><c:out value="${post.viewCount != null ? post.viewCount : 0}"/></td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>
    </c:if>
		<c:set var="pageNumber1" value="${page.number + 1}"/>
		<c:set var="totalPages" value="${page.totalPages > 0 ? page.totalPages : 1}"/>
		<c:set var="window" value="5"/>
		
		<c:set var="start" value="${pageNumber1 - (window - 1) / 2}"/>
		<c:if test="${start < 1}">
		  <c:set var="start" value="1"/>
		</c:if>
		
		<c:set var="end" value="${start + window - 1}"/>
		<c:if test="${end > totalPages}">
		  <c:set var="end" value="${totalPages}"/>
		  <c:set var="start" value="${end - window + 1}"/>
		  <c:if test="${start < 1}">
		    <c:set var="start" value="1"/>
		  </c:if>
		</c:if>
		
		<c:choose>
		  <c:when test="${title eq '공지 게시판'}">
		    <c:set var="basePath" value="${pageContext.request.contextPath}/board/notice"/>
		  </c:when>
		  <c:otherwise>
		    <c:set var="basePath" value="${pageContext.request.contextPath}/board/department"/>
		  </c:otherwise>
		</c:choose>
		
		<nav aria-label="board pagination" class="d-flex justify-content-center mt-4">
		  <ul class="pagination mb-0">
		
		    <li class="page-item ${page.first ? 'disabled' : ''}">
		      <a class="page-link" href="${basePath}?page=0&size=${page.size}">&laquo;</a>
		    </li>
		
		    <li class="page-item ${page.first ? 'disabled' : ''}">
		      <a class="page-link" href="${basePath}?page=${page.number - 1 < 0 ? 0 : page.number - 1}&size=${page.size}">&lsaquo;</a>
		    </li>
		
		    <c:forEach begin="${start}" end="${end}" var="i">
		      <li class="page-item ${i == pageNumber1 ? 'active' : ''}">
		        <a class="page-link" href="${basePath}?page=${i-1}&size=${page.size}">${i}</a>
		      </li>
		    </c:forEach>
		
		    <li class="page-item ${page.last ? 'disabled' : ''}">
		      <a class="page-link" href="${basePath}?page=${page.number + 1 >= totalPages ? totalPages - 1 : page.number + 1}&size=${page.size}">&rsaquo;</a>
		    </li>
		
		    <li class="page-item ${page.last ? 'disabled' : ''}">
		      <a class="page-link" href="${basePath}?page=${totalPages - 1}&size=${page.size}">&raquo;</a>
		    </li>
		
		  </ul>
		</nav>
  </div>
  
</div>


<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>