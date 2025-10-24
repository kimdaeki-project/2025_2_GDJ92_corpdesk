<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- JSTL 태그 선언 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 날짜 포맷 태그 -->
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<div class="container">

  <!-- 등록 버튼 -->
  <div class="toolbar">
    <h2>공지사항</h2>
    <a class="btn" href="<c:url value='/notice/new'/>">등록</a>
  </div>

  <!-- 검색 폼 -->
  <form class="search-box" method="get" action="<c:url value='/notice'/>">
    <!-- 검색 필드 선택 -->
    <select name="field">
      <option value="title" <c:if test="${field == 'title'}">selected</c:if>>제목</option>
      <option value="content" <c:if test="${field == 'content'}">selected</c:if>>내용</option>
    </select>
    <!-- 키워드 입력 -->
    <input type="text" name="q" value="${q}" placeholder="검색어 입력">
    <!-- 페이지 유지용 히든(size 유지) -->
    <input type="hidden" name="size" value="${size}">
    <!-- 검색 제출 -->
    <button class="btn btn-outline" type="submit">검색</button>
  </form>

  <!-- 목록 테이블 -->
  <table>
    <thead>
      <tr>
        <th style="width:80px;">번호</th>
        <th>제목</th>
        <th style="width:120px;">작성자</th>
        <th style="width:160px;">등록일</th>
        <th style="width:100px;">조회수</th>
      </tr>
    </thead>
    <tbody>
    <c:forEach var="n" items="${notices.content}">
      <tr>
        <!-- 번호 -->
        <td>${n.noticeId}</td>
        <!-- 상세 링크 -->
        <td><a href="<c:url value='/notice/${n.noticeId}'/>">${n.title}</a></td>
        <!-- 작성자 -->
        <td>${n.writer}</td>
        <!-- 등록일 -->
        <td><fmt:formatDate value="${n.createdAt}" pattern="yyyy-MM-dd HH:mm"/></td>
        <!-- 조회수 -->
        <td>${n.viewCount}</td>
      </tr>
    </c:forEach>
    <!-- 데이터 없음 안내 -->
    <c:if test="${empty notices.content}">
      <tr><td colspan="5" style="text-align:center; color:#888;">등록된 공지사항이 없습니다.</td></tr>
    </c:if>
    </tbody>
  </table>

  <!-- 페이지네이션 -->
  <div class="pagination">
    <!-- 이전 -->
    <c:if test="${notices.hasPrevious()}">
      <a href="<c:url value='/notice?page=${notices.number - 1}&size=${size}&q=${q}&field=${field}'/>">이전</a>
    </c:if>

    <!-- 페이지 번호들 -->
    <c:forEach var="p" begin="0" end="${notices.totalPages - 1}">
      <a class="${p == notices.number ? 'active' : ''}"
         href="<c:url value='/notice?page=${p}&size=${size}&q=${q}&field=${field}'/>">${p + 1}</a>
    </c:forEach>

    <!-- 다음 -->
    <c:if test="${notices.hasNext()}">
      <a href="<c:url value='/notice?page=${notices.number + 1}&size=${size}&q=${q}&field=${field}'/>">다음</a>
    </c:if>
  </div>

</div>

</body>
</html>