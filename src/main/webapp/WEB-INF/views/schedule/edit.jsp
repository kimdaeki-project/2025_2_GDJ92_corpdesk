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

  <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>

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
        <form method="POST" action="/personal-schedule/${schedule.personalScheduleId}">
          <input type="hidden" name="_method" value="PUT">

          <%-- 일정 상세조회 정보 --%>
          <div class="row">
            <div class="col-12">
              <div class="form-group">
                <label class="font-weight-bold">일정명</label>
                <input type="text" class="form-control-plaintext border-bottom pb-2" name="scheduleName" value="${schedule.scheduleName}"/>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-12">
              <div class="form-group">
                <label class="font-weight-bold">날짜 및 시간</label>
                <input type="datetime-local" class="form-control-plaintext border-bottom pb-2" name="scheduleDateTime" value="${schedule.scheduleDateTime != null ? schedule.scheduleDateTime : ''}"/>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-12">
              <div class="form-group">
                <label class="font-weight-bold">주소</label>
                <div class="d-flex justify-content-between">
                  <input type="text" id="scheduleLocationDetail" class="form-control-plaintext border-bottom pb-2" name="address" value="${schedule.address != null ? schedule.address : ''}">
                  <input type="button" onclick="searchAddressDetail()" value="주소 검색" class="d_btn btn btn-light ml-2">
                </div>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-12">
              <div class="form-group">
                <label class="font-weight-bold">내용</label>
                <textarea class="form-control p-3" style="min-height: 150px;" name="content">
                  ${schedule.content != null ? schedule.content : '-'}
                </textarea>
              </div>
            </div>
          </div>

          <div class="row mt-4">
            <div class="col-12 text-right">
              <button class="btn btn-primary">저장</button>
              <button type="button" class="btn btn-secondary" onclick="location.href='/personal-schedule/${schedule.personalScheduleId}'">취소</button>
            </div>
          </div>

        </form>

      </div>
    </div>
  </div>

<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>

<script>
  function searchAddressDetail() {
    new daum.Postcode({
      oncomplete: function(data) {
        const locationInput = document.getElementById("scheduleLocationDetail");
        locationInput.value = data.address;
      }
    }).open();
  }
</script>
</html>

