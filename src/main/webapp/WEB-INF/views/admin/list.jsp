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

<div class="card card-default">
	<div class="card-header">
    <h2>직원 권한 관리</h2>
	</div>
	<div class="card-body">
		<div class="table-responsive">
			<table class="table table-hover">
				<thead>
          <tr>
            <th>ID</th>
            <th>이름</th>
            <th>부서</th>
            <th>직위</th>
            <th>권한</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="e" items="${employeeList}">
            <tr>
              <td>${e.username}</td>
              <td>${e.name}</td>
              <td>${e.departmentName}</td>
              <td>${e.positionName}</td>
              <td>
                <select class="form-control role" data-username="${e.username}">
                  <c:forEach var="r" items="${roleList}">
                    <option value="${r.roleId}" ${r.roleName == e.roleName ? 'selected' : ''}>${r.roleName}</option>
                  </c:forEach>
                </select>
              </td>
            </tr>
          </c:forEach>
        </tbody>
			</table>
		</div>


    <%-- 페이지네이션 --%>
    <nav class="d-flex justify-content-center mt-4" aria-label="Page navigation example">
      <ul class="pagination pagination-flat pagination-flat-rounded">
        <li class="page-item ${startPage < 1 ? 'd-none' : ''}">
          <a class="page-link" href="/admin?page=${startPage}" aria-label="Previous">
            <span aria-hidden="true" class="mdi mdi-chevron-left"></span>
            <span class="sr-only">Previous</span>
          </a>
        </li>
        <c:forEach var="p" begin="${startPage}" end="${endPage}">
          <li class="page-item ${p eq page.number ? 'active' : ''}">
            <a class="page-link" href="/admin?page=${p + 1}">${p + 1}</a>
          </li>
        </c:forEach>
        <li class="page-item ${endPage >= page.totalPages - 1 ? 'd-none' : ''}">
          <a class="page-link" href="/admin?page=${startPage + 6}" aria-label="Next">
            <span aria-hidden="true" class="mdi mdi-chevron-right"></span>
            <span class="sr-only">Next</span>
          </a>
        </li>
      </ul>
    </nav>

  </div>
</div>

<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<script src="/js/admin/list.js"></script>

<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>