<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Insert title here</title>
  <c:import url="/WEB-INF/views/include/head.jsp"/>

  <script defer src="/js/schedule/list.js"></script>
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/>

<!-- 일정 등록 모달 -->
<c:import url="modal.jsp"/>

<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

<c:import url="/WEB-INF/views/include/header.jsp"/>

<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
<!-- 내용 시작 -->

<div class="row">
  <!-- 왼쪽 사이드바 -->
  <div class="col-lg-3">
    <div class="card card-default">
      <div class="card-body">
        <button type="button" class="btn btn-info btn-lg btn-block mb-7" data-toggle="modal" data-target="#scheduleModal">
          일정 등록
        </button>

        <h5  class="mb-4">오늘의 일정</h5>
        <c:import url="today_schedules.jsp"/>

        </div>
      </div>
    </div>

  <!-- 오른쪽 메인 콘텐츠 -->
  <div class="col-lg-9">
    <div class="card card-default">
      <div class="card-header d-flex justify-content-between align-items-center">
        <h2 class="mb-0">일정 목록</h2>
      </div>

      <div class="card-body">

        <!-- 년, 월 선택 - 한 줄 배치 -->
        <div class="row mb-4 ml-0">

          <form method="get" action="/personal-schedule/list">

            <div class="mr-3">
              <div class="form-group">
                <div class="d-flex align-items-center">
                  <select class="form-control mr-2" id="yearSelect" name="year">

                    <c:choose>
                      <c:when test="${selectedYear ne null}">
                        <option value="" ${selectedYear eq '' ? 'selected' : ''}>전체</option>
                        <c:forEach var="year" items="${yearRange}">
                          <option value="${year}" ${selectedYear eq year ? 'selected' : ''}>
                              ${year}
                          </option>
                        </c:forEach>
                      </c:when>
                      <c:otherwise>
                        <option value="">전체</option>
                        <c:forEach var="year" items="${yearRange}">
                          <option value="${year}">
                              ${year}
                          </option>
                        </c:forEach>
                      </c:otherwise>
                    </c:choose>

                  </select>
                  <span>년</span>
                </div>
              </div>
            </div>

            <div class="mr-6">
              <div class="form-group">
                <div class="d-flex align-items-center">
                  <select class="form-control mr-2" id="monthSelect" name="month">

                    <c:choose>
                      <c:when test="${selectedMonth ne null}">
                        <option value="" ${selectedMonth eq '' ? 'selected' : ''}>전체</option>
                        <c:forEach begin="1" end="12" var="num">
                          <option value="${num}" ${selectedMonth eq num ? 'selected' : ''}>${num}</option>
                        </c:forEach>
                      </c:when>

                      <c:otherwise>
                        <option value="">전체</option>
                        <c:forEach begin="1" end="12" var="num">
                          <option value="${num}">${num}</option>
                        </c:forEach>
                      </c:otherwise>
                    </c:choose>

                  </select>
                  <span>월</span>
                </div>
              </div>
            </div>

            <div>
              <%-- TODO 조회 버튼 클릭시 년, 월 데이터로 페이지 다시 조회 --%>
              <button class="btn btn-primary">조회</button>
            </div>

          </form>
        </div>

        <%-- 테이블 --%>
        <table class="table table-hover">
          <thead>
            <tr>
              <th class="col-2">날짜</th>
              <th class="col-1">시간</th>
              <th class="col-3">일정명</th>
              <th class="col-3">주소</th>
              <th class="col-3">내용</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach items="${schedules}" var="el">
              <tr class="schedule-row" data-personal-schedule-id="${el.personalScheduleId}" style="cursor: pointer;">
                <td>${fn: substring(el.scheduleDateTime, 0, 10)}</td>
                <td>${fn: substring(el.scheduleDateTime, 11, 16)}</td>
                <td>${el.scheduleName}</td>
                <td>${el.address}</td>
                <td>${el.content}</td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>
    </div>
  </div>

<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>