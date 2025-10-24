<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:choose>
  <c:when test="${todaySchedules eq null or empty todaySchedules}">
    <p>일정이 없습니다.</p>
  </c:when>
  <c:otherwise>
    <c:forEach items="${todaySchedules}" var="el">
      <a href="/personal-schedule/${el.personalScheduleId}" class="list-group-item list-group-item-action border-0 px-0 py-2">
        <div class="d-flex align-items-start">
          <i class="mdi mdi-checkbox-blank-circle text-primary mr-3"></i>
          <div>
            <div class="font-weight-semibold text-dark">${fn:substring(el.scheduleDateTime, 11, 16)}&nbsp;&nbsp;${el.scheduleName}</div>
            <small class="text-muted">${el.address}</small>
          </div>
        </div>
      </a>
    </c:forEach>
  </c:otherwise>
</c:choose>