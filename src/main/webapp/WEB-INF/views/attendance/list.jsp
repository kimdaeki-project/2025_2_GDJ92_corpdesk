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

  <!-- 출퇴근 버튼 영역 -->
  <div class="col-lg-3">
    <div class="card card-default">
      <div class="card-body">
        <c:import url="attendance_widget.jsp"/>
      </div>
    </div>
  </div>

  <!-- 출퇴근 목록 영역 -->
  <div class="col-lg-9">
    <div class="card card-default">

      <div class="card-header">
        <h2>내 출퇴근 목록</h2>
      </div>

      <div class="card-body">

        <!-- 년, 월 선택 - 한 줄 배치 -->
        <div class="row mb-4 ml-0">

          <%-- TODO 추후 인증정보를 사용하게 되면 username 삭제 --%>
          <form method="get" action="/attendance/list">

            <input type="hidden" name="username" value="jung_frontend">

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

        <!-- 근태현황, 근무시간 - 한 줄 배치 -->
        <br>
        <div class="row mb-4 col-11 m-auto">
          <div class="col-xl-6">
            <h5 class="mb-2">근태 현황</h5>
            <div class="card card-default">
              <div class="card-body p-4 d-flex justify-content-between flex-wrap">

                <div class="col-4 text-center">
                  <h6>지각</h6>
                  <br>
                  <b>${attCnts.lateArrivalsCnt}</b><span>회</span>
                </div>
                <div class="col-4 text-center">
                  <h6>조퇴</h6>
                  <br>
                  <b>${attCnts.earlyLeavingsCnt}</b><span>회</span>
                </div>
                <div class="col-4 text-center">
                  <h6>결근</h6>
                  <br>
                  <b>${attCnts.absentDaysCnt}</b><span>회</span>
                </div>

              </div>
            </div>
          </div>

          <div class="col-xl-6">
            <h5 class="mb-2">근무 시간</h5>
            <div class="card card-default">
              <div class="card-body p-4 d-flex justify-content-around flex-wrap">

                <div class="col-4 text-center">
                  <h6>근무 일수</h6>
                  <br>
                  <b>${workSummary.totalWorkDays}</b><span>일</span>
                </div>
                <div class="col-4 text-center">
                  <h6>근무 시간</h6>
                  <br>
                  <b>${workSummary.totalWorkHours}</b><span>시간</span>
                </div>

              </div>
            </div>
          </div>
        </div>

        <!-- 출퇴근 목록 테이블 -->
        <br>
        <div class="table-responsive">
          <table class="table table-hover">
            <thead>
              <tr>
                <th>출근일</th>
                <th>출근 시간</th>
                <th>퇴근일</th>
                <th>퇴근 시간</th>
                <th>근무 상태</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach items="${attDatilList}" var="el">
                <tr>
                  <td>${el.checkInDateTime ne null ? fn:substring(el.checkInDateTime, 0, 10) : '-'}</td>
                  <td>${el.checkInDateTime ne null ? fn:substring(el.checkInDateTime, 11, 16) : '-'}</td>
                  <td>${el.checkOutDateTime ne null ? fn:substring(el.checkOutDateTime, 0, 10) : '-'}</td>
                  <td>${el.checkOutDateTime ne null ? fn:substring(el.checkOutDateTime, 11, 16) : '-'}</td>
                  <td>${el.workStatus}</td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>

      </div>
    </div>
  </div>

</div>

<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>