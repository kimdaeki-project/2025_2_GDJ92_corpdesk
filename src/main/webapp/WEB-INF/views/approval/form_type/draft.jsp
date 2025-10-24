<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%-- 상세조회인지 확인하는 변수 설정 --%>
<c:set var="isDetail" value="${detail ne null and edit eq null}" />

<%-- 상세조회용 CSS 스타일 --%>
<c:if test="${isDetail}">
    <style>
        .readonly-form {
            border: none !important;
            background-color: white !important;
            box-shadow: none !important;
            outline: none !important;
        }
        .readonly-form:focus {
            border: none !important;
            box-shadow: none !important;
        }
        .readonly-select {
            border: none !important;
            background-color: white !important;
            appearance: none !important;
            -webkit-appearance: none !important;
            -moz-appearance: none !important;
            pointer-events: none;
        }
    </style>
</c:if>

<br>
<table class="table table-bordered">
    <tbody>
    <tr>
        <th class="col-2 table-light align-middle">시행일자</th>
        <td>
            <input name="executionDate"
                   type="date"
                   class="form-control ${isDetail ? 'readonly-form' : ''}"
                   value="${approvalContentMap ne null ? approvalContentMap.executionDate : ''}"
            ${isDetail ? 'readonly' : ''}>
        </td>
        <th class="col-2 table-light align-middle">협조부서</th>
        <td>
            <div class="form-group">
                <select name="coopDepartmentId"
                        class="form-control ${isDetail ? 'readonly-select' : ''}"
                        id="departmentId"
                ${isDetail ? 'disabled' : ''}>
                    <c:forEach items="${departmentList}" var="el">
                        <c:choose>
                            <c:when test="${isDetail and (el.departmentId eq approvalContentMap.coopDepartmentId)}">
                                <option value="${el.departmentId}" selected>${el.departmentName}</option>
                            </c:when>
                            <c:when test="${not isDetail}">
                                <option value="${el.departmentId}">${el.departmentName}</option>
                            </c:when>
                        </c:choose>
                    </c:forEach>
                </select>
            </div>
        </td>
    </tr>
    <tr>
        <th class="table-light align-middle">합의</th>
        <td colspan="3">${userInfo.departmentName}</td>
    </tr>
    <tr>
        <th class="table-light align-middle">제목</th>
        <td colspan="3">
            <input name="draftTitle"
                   type="text"
                   class="form-control ${isDetail ? 'readonly-form' : ''}"
                   value="${approvalContentMap ne null ? approvalContentMap.draftTitle : ''}"
            ${isDetail ? 'readonly' : ''}>
        </td>
    </tr>
    <tr>
        <th class="table-light align-middle">내용</th>
        <td colspan="3">
      <textarea name="draftContent"
                class="form-control ${isDetail ? 'readonly-form' : ''}"
                rows="5"
      ${isDetail ? 'readonly' : ''}>${approvalContentMap ne null ? approvalContentMap.draftContent : ''}</textarea>
        </td>
    </tr>
    </tbody>
</table>