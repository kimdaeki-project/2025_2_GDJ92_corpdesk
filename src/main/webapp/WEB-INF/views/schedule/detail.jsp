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
        <h2 class="mb-0">일정 상세</h2>
      </div>

      <div class="card-body">

        <%-- TODO 여기에 일정 상세조회 정보 뿌림 --%>
          <%-- 일정 상세조회 정보 --%>
          <div class="row">
            <div class="col-12">
              <div class="form-group">
                <label class="font-weight-bold">일정명</label>
                <p class="form-control-plaintext border-bottom pb-2">
                  ${schedule.scheduleName}
                </p>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-12">
              <div class="form-group">
                <label class="font-weight-bold">날짜 및 시간</label>
                <p class="form-control-plaintext border-bottom pb-2">
                  <c:choose>
                    <c:when test="${schedule.scheduleDateTime != null}">
                      ${fn:substring(schedule.scheduleDateTime, 0, 10)} · ${fn:substring(schedule.scheduleDateTime, 11, 16)}
                    </c:when>
                    <c:otherwise>
                      -
                    </c:otherwise>
                  </c:choose>
                </p>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-12">
              <div class="form-group">
                <label class="font-weight-bold">주소</label>
                <p class="form-control-plaintext border-bottom pb-2">
                  ${schedule.address != null ? schedule.address : '-'}
                </p>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-12">
              <div class="form-group">
                <label class="font-weight-bold">내용</label>
                <div class="border rounded p-3 bg-light" style="min-height: 150px;">
                  ${schedule.content != null ? schedule.content : '-'}
                </div>
              </div>
            </div>
          </div>

          <div class="row mt-4">
            <div class="col-12 text-right d-flex justify-content-end">
              <form class="mr-1" method="GET" action="/personal-schedule/${schedule.personalScheduleId}/edit">
                <button class="btn btn-primary">수정</button>
              </form>
              <form class="mr-1" method="POST" action="/personal-schedule/${schedule.personalScheduleId}">
                <input type="hidden" name="_method" value="DELETE">
                <button class="btn btn-danger">삭제</button>
              </form>
              <button type="button" class="btn btn-secondary" onclick="location.href='/personal-schedule/list'">목록</button>
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