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

      .textarea--resize--none {
          resize: none;
      }
  </style>
</c:if>

<br>
<table class="table table-bordered">
  <tbody>
  <tr>
    <th class="col-2 table-light align-middle">휴가 종류</th>
    <td>
      <div class="form-group">
        <select name="vacationTypeId"
                class="form-control ${isDetail ? 'readonly-select' : ''}"
                id="vacationTypeId"
        ${isDetail ? 'disabled' : ''}>
          <c:forEach items="${vacationTypeList}" var="el">
            <c:choose>
              <c:when test="${isDetail and (el.vacationTypeId eq approvalContentMap.vacationTypeId)}">
                <option value="${el.vacationTypeId}" selected>${el.vacationTypeName}</option>
              </c:when>
              <c:when test="${not isDetail}">
                <option value="${el.vacationTypeId}">${el.vacationTypeName}</option>
              </c:when>
            </c:choose>
          </c:forEach>
        </select>
      </div>
    </td>
  </tr>
  <tr>
    <th class="table-light align-middle">휴가 기간</th>
    <td>
      <div class="d-flex align-items-center">
        <input name="startDate"
               type="date"
               class="form-control col-sm-5 mb-2 ${isDetail ? 'readonly-form' : ''}"
               id="startDate"
               value="${approvalContentMap ne null ? approvalContentMap.startDate : ''}"
        ${isDetail ? 'readonly' : ''}>
        <p>&nbsp;&nbsp;&nbsp;&nbsp;~</p>
      </div>
      <input name="endDate"
             type="date"
             class="form-control col-sm-5 ${isDetail ? 'readonly-form' : ''}"
             id="endDate"
             value="${approvalContentMap ne null ? approvalContentMap.endDate : ''}"
      ${isDetail ? 'readonly' : ''}>
    </td>
  </tr>
  <tr>
    <th class="table-light align-middle">사용일수</th>
    <td>
      <input name="usedDays"
             type="number"
             class="form-control ${isDetail ? 'readonly-form' : ''}"
             id="usedDays"
             value="${approvalContentMap ne null ? approvalContentMap.usedDays : ''}"
             readonly>
    </td>
  </tr>
  <tr>
    <th class="table-light align-middle">휴가 사유</th>
    <td>
      <textarea name="reason"
                class="form-control ${isDetail ? 'readonly-form textarea--resize--none' : ''}"
                rows="5"
      ${isDetail ? 'readonly' : ''}>${approvalContentMap ne null ? approvalContentMap.reason : ''}</textarea>
    </td>
  </tr>
  </tbody>
</table>