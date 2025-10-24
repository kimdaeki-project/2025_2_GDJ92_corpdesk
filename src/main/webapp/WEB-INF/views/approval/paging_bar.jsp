<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:if test="${(list.content ne null) and (not empty list.content)}">
  <br>
  <nav class="d-flex justify-content-center">
    <ul class="pagination pagination-flat pagination-flat-rounded">

      <li class="page-item ${list.first ? 'disabled' : ''}">
        <a class="page-link" href="?listType=${param.listType}&page=${list.number + 1}&size=${list.size}" aria-label="Previous">
          <span aria-hidden="true" class="mdi mdi-chevron-left"></span>
          <span class="sr-only">Previous</span>
        </a>
      </li>

      <!-- 페이지 번호 (5개씩 표시) -->
      <c:set var="startPage" value="${list.number - 2 < 0 ? 0 : list.number - 2}" />
      <c:set var="endPage" value="${startPage + 4 >= list.totalPages ? list.totalPages - 1 : startPage + 4}" />

      <c:forEach begin="${startPage}" end="${endPage}" var="pageNum">
        <li class="page-item ${pageNum == list.number ? 'active' : ''}">
          <a class="page-link" href="?listType=${param.listType}&page=${pageNum}&size=${list.size}">
              ${pageNum + 1}
          </a>
        </li>
      </c:forEach>

      <li class="page-item ${list.last ? 'disabled' : ''}">
        <a class="page-link" href="?listType=${param.listType}&page=${list.totalPages - 1}&size=${list.size}" aria-label="Next">
          <span aria-hidden="true" class="mdi mdi-chevron-right"></span>
          <span class="sr-only">Next</span>
        </a>
      </li>

    </ul>
  </nav>
</c:if>

