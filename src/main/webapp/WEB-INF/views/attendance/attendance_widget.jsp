<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!-- 출퇴근 시간 표시 -->

<%-- 현재 년월일시분 표시 --%>
<div id="clock" class="mb-5">
  <span>현재 시</span>
</div>
<script>
  function updateClock() {
    const now = new Date();

    // 날짜
    const year = now.getFullYear();
    const month = now.getMonth() + 1;
    const day = now.getDate();

    // 시간
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const seconds = String(now.getSeconds()).padStart(2, '0');

    // 요일
    const week = ['일', '월', '화', '수', '목', '금', '토'];
    const dayOfWeek = week[now.getDay()];

    const dateTimeString = year + '년 ' + month + '월 ' + day + '일 (' + dayOfWeek + ') ' +  hours + ':' + minutes + ':' + seconds;
    document.getElementById('clock').textContent = dateTimeString;
  }

  updateClock(); // 최초 화면 로딩 시에는 바로 실행
  setInterval(updateClock, 1000); // 1000밀리초(1초)마다 실행
</script>

<div class="card card-default mb-3">
  <div class="card-body d-flex justify-content-around p-3">

    <c:choose>
      <c:when test="${currAttd.workStatus eq '출근전' or (currAttd.workStatus eq '퇴근' and !currAttd.today)}">
        <div class="text-center">
          <p class="card-text mb-1">출근</p>
          <p class="card-text mb-0">00:00:00</p>
        </div>
        <div class="text-center">
          <p class="card-text mb-1">퇴근</p>
          <p class="card-text mb-0">00:00:00</p>
        </div>
      </c:when>
      <c:otherwise>
        <div class="text-center">
          <p class="card-text mb-1">출근</p>
          <p class="card-text mb-0">${fn:substring(currAttd.checkInTime, 0, 8)}</p>
        </div>
        <div class="text-center">
          <p class="card-text mb-1">퇴근</p>
          <p class="card-text mb-0">${currAttd.checkOutTime eq null ? '00:00:00' : fn:substring(currAttd.checkOutTime, 0, 8)}</p>
        </div>
      </c:otherwise>
    </c:choose>

  </div>
</div>

<!-- 출근/퇴근 버튼 -->
<div class="row no-gutters">
  <div class="col-6 pr-2">
    <form method="POST" action="/attendance">
      <button class="btn btn-primary btn-lg btn-block"
      ${currAttd.workStatus eq '휴가' or currAttd.workStatus eq '출근' ? 'disabled' : ''}>출근
      </button>
    </form>
  </div>

  <div class="col-6 pl-2">
    <form method="post" action="/attendance/${currAttd.attendanceId}">
      <input type="hidden" name="_method" value="PATCH">
      <button class="btn btn-primary btn-lg btn-block"
      ${currAttd.workStatus eq '휴가'
        or currAttd.workStatus eq '출근전'
        or currAttd.workStatus eq '퇴근' ? 'disabled' : ''}>퇴근
      </button>
    </form>
  </div>
</div>