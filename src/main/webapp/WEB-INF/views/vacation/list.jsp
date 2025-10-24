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
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/>

<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

<c:import url="/WEB-INF/views/include/header.jsp"/>

<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
<!-- 내용 시작 -->

<div class="row">

  <!-- 왼쪽 사이드바 영역 -->
  <div class="col-lg-3">
    <div class="card card-default">
      <div class="card-body">

        <!-- 휴가 신청 버튼 -->
        <form method="GET" action="/approval-form/1">
          <button class="btn btn-info btn-lg btn-block mb-6">휴가 신청</button>
        </form>

        <!-- 메뉴 리스트 -->
        <div class="email-options">
          <ul class="text-center">
            <li class="d-block mb-4">
              <a href="/vacation/list?listType=personal">휴가 현황</a>
            </li>
            <li class="d-block mb-4">
              <a href="/vacation/list?listType=all">전사 휴가 현황</a>
            </li>
          </ul>
        </div>

      </div>
    </div>
  </div>

  <!-- 메인 콘텐츠 영역 -->
  <div class="col-lg-9">
    <div class="card card-default">

      <div class="card-header d-flex justify-content-between align-items-center">
        <h2>휴가 현황</h2>
      </div>

      <div class="card-body">

        <!-- 휴가 종류 선택 필터 -->
        <div class="row mb-4 ml-0">


            <form method="GET" action="/vacation/list">
              <input type="hidden" name="listType" value="${param.listType}">
              <div class="mr-3">
                <div class="form-group">
                  <div class="d-flex align-items-center">
                    <select class="form-control mr-2" id="leaveTypeSelect" name="vacationType">
                      <option value="" ${vacationType eq null ? 'selected' : ''}>전체</option>
                      <option value="1" ${vacationType ne null and vacationType eq 1 ? 'selected' : ''}>연차휴가</option>
                      <option value="2" ${vacationType ne null and vacationType eq 2 ? 'selected' : ''}>병가</option>
                      <option value="3" ${vacationType ne null and vacationType eq 3 ? 'selected' : ''}>경조사휴가</option>
                      <option value="4" ${vacationType ne null and vacationType eq 4 ? 'selected' : ''}>출산휴가</option>
                      <option value="5" ${vacationType ne null and vacationType eq 5 ? 'selected' : ''}>육아휴직</option>
                      <option value="6" ${vacationType ne null and vacationType eq 6 ? 'selected' : ''}>공가</option>
                    </select>
                  </div>
                </div>
              </div>

              <div>
                <button type="submit" class="btn btn-primary">조회</button>
              </div>

            </form>
        </div>

        <%-- 휴가 집계 정보 --%>
        <c:if test="${vacation ne null}">
          <div class="row mb-4 col-11 m-auto">
            <div class="col-xl-6 mx-auto">
              <h5 class="mb-2">연차 현황</h5>
              <div class="card card-default">
                <div class="card-body p-4 d-flex justify-content-between flex-wrap">

                  <div class="col-4 text-center">
                    <h6>발생 연차</h6>
                    <br>
                    <b>${vacation.totalVacation}</b><span>일</span>
                  </div>
                  <div class="col-4 text-center">
                    <h6>사용 연차</h6>
                    <br>
                    <b>${vacation.usedVacation}</b><span>일</span>
                  </div>
                  <div class="col-4 text-center">
                    <h6>잔여 연차</h6>
                    <br>
                    <b>${vacation.remainingVacation}</b><span>일</span>
                  </div>

                </div>
              </div>
            </div>
          </div>
        </c:if>

        <!-- 휴가 목록 테이블 -->
        <div class="table-responsive">
          <table class="table table-hover">
            <thead>
              <tr>
                <th>휴가 종류</th>
                <th>시작일</th>
                <th>종료일</th>
                <th>사용일수</th>
                <c:if test="${param.listType eq 'all'}">
                  <th>부서</th>
                  <th>사용자</th>
                </c:if>
              </tr>
            </thead>
            <tbody>
              <c:forEach items="${details}" var="el">
                <tr data-vacation-detail-id="${el.vacationDetailId}">
                  <td>
                    <c:choose>
                      <c:when test="${el.vacationTypeId eq 1}">연차휴가</c:when>
                      <c:when test="${el.vacationTypeId eq 2}">병가</c:when>
                      <c:when test="${el.vacationTypeId eq 3}">경조사휴가</c:when>
                      <c:when test="${el.vacationTypeId eq 4}">출산휴가</c:when>
                      <c:when test="${el.vacationTypeId eq 5}">육아휴직</c:when>
                      <c:when test="${el.vacationTypeId eq 6}">공가</c:when>
                    </c:choose>
                  </td>
                  <td>${el.startDate}</td>
                  <td>${el.endDate}</td>
                  <td>${el.usedDays}</td>
                  <c:if test="${param.listType eq 'all'}">
                    <td>${el.department}</td>
                    <td>${el.name} ${el.position}</td>
                  </c:if>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>

      </div>
    </div>
  </div>

<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>