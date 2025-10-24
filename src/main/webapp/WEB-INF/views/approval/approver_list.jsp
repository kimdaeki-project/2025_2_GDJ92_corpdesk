<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%-- 결재자 수 계산 --%>
<c:set var="approverCount" value="${fn:length(detail.approverDTOList)}" />
<c:set var="approverRows" value="${approverCount eq 0 ? 1 : (approverCount + 3) / 4}" /> <%-- 4명씩 나눈 행 수 (올림) --%>

<table class="table table-bordered">
  <tbody>
  <%-- 결재자 행을 동적으로 생성 --%>
  <c:forEach var="rowIndex" begin="0" end="${approverRows - 1}">
    <%-- 첫 번째 행에만 "승인" 헤더를 rowspan으로 추가 --%>
    <tr id="approverPosition${rowIndex}">
      <c:if test="${rowIndex == 0}">
        <th class="align-middle table-light col-2" rowspan="${approverRows * 3}">승인</th>
      </c:if>

        <%-- 현재 행의 결재자들 (최대 4명) --%>
      <c:forEach var="colIndex" begin="0" end="3">
        <c:set var="approverIndex" value="${rowIndex * 4 + colIndex}" />
        <c:choose>
          <c:when test="${approverIndex < approverCount}">
            <%-- 결재자가 있는 경우 --%>
            <td class="text-center" style="width: 20%;">
                ${detail.approverDTOList[approverIndex].positionName}
            </td>
          </c:when>
          <c:otherwise>
            <%-- 빈 셀 --%>
            <td class="text-center" style="width: 20%;">&nbsp;</td>
          </c:otherwise>
        </c:choose>
      </c:forEach>
    </tr>

    <%-- 이름 행 --%>
    <tr id="approverName${rowIndex}" style="height: 90px;">
      <c:forEach var="colIndex" begin="0" end="3">
        <c:set var="approverIndex" value="${rowIndex * 4 + colIndex}" />
        <c:choose>
          <c:when test="${approverIndex < approverCount}">
            <%-- 결재자가 있는 경우 --%>
            <td class="text-center align-middle" style="width: 20%;">
                ${detail.approverDTOList[approverIndex].name}
            </td>
          </c:when>
          <c:otherwise>
            <%-- 빈 셀 --%>
            <td class="text-center align-middle" style="width: 20%;">&nbsp;</td>
          </c:otherwise>
        </c:choose>
      </c:forEach>
    </tr>

    <%-- 승인일자 행 --%>
    <tr id="approvalDate${rowIndex}">
      <c:forEach var="colIndex" begin="0" end="3">
        <c:set var="approverIndex" value="${rowIndex * 4 + colIndex}" />
        <c:choose>
          <c:when test="${approverIndex < approverCount}">
            <%-- 결재자가 있는 경우 --%>
            <td class="text-center align-middle" style="width: 20%;">
              <c:if test="${detail.approverDTOList[approverIndex].approveYn eq 'N'.charAt(0)}">
                <p class="text-danger">반려: ${fn:substring(detail.approverDTOList[approverIndex].createdAt, 0, 10)}</p>
              </c:if>
              <c:if test="${detail.approverDTOList[approverIndex].approveYn eq 'Y'.charAt(0)}">
                <p class="text-info">승인: ${fn:substring(detail.approverDTOList[approverIndex].createdAt, 0, 10)}</p>
              </c:if>
              &nbsp;
            </td>
          </c:when>
          <c:otherwise>
            <%-- 빈 셀 --%>
            <td class="text-center align-middle" style="width: 20%;">&nbsp;</td>
          </c:otherwise>
        </c:choose>
      </c:forEach>
    </tr>
  </c:forEach>
  </tbody>
</table>
<%--  --%>