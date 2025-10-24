<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>사원 상세 페이지</title>
  <c:import url="/WEB-INF/views/include/head.jsp"/>
  <style>
      /* 기존 스타일 그대로 유지 */
      .headProfile {
          margin-top: 70px;
          margin-bottom: 70px;
          border: 1px solid gray;
          padding: 20px;
          align-items: stretch;
          gap: 50px;
      }

      .headProfile .d-flex {
          flex-direction: column;
      }

      .info-container {
          flex: 1;
          width: 100%;
      }

      .employeeInformation {
          flex-direction: column;
          flex-wrap: wrap;
          jusify-content: space-evenly;
          margin-bottom: 90px;
          margin-top: 20px;
      }

      .buttonBox {
          margin-top: 50px;
          display: flex;
          justify-content: center;
          gap: 10px;
      }

      .tabs {
          margin-bottom: 30px;
      }

      .tab-content {
          display: none;
      }

      .tab-content.active {
          display: block;
      }

      .tab-button {
          border: none;
          background: none;
          padding: 10px 20px;
          cursor: pointer;
      }

      .tab-button.active {
          font-weight: bold;
          border-bottom: 2px solid #007bff;
      }

  </style>

  <meta name="_csrf" content="${_csrf.token}"/>
  <meta name="_csrf_header" content="${_csrf.headerName}"/>
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/>
<c:import url="/WEB-INF/views/include/sidebar.jsp"/>
<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>
<c:import url="/WEB-INF/views/include/header.jsp"/>
<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
<!-- 사원 정보 폼 -->
<div class="card card-default">
  <div class="card-body">
    <form:form method="post" modelAttribute="employee" action="/employee/edit" enctype="multipart/form-data">
    <form:hidden path="username"/>

    <div class="card-header">
      <h2 class="mb-0">사원 상세정보</h2>
    </div>

    <div class="box">
      <div class="d-flex justify-content-between">
        <div class="d-flex flex-column align-items-center col-lg-3">
          <c:set var="profileImageUrl" value="/images/default_profile.jpg"/>
          <c:if test="${employeeFile != null and employeeFile.useYn}">
            <c:set var="profileImageUrl" value="/files/profile/${employeeFile.saveName}.${employeeFile.extension}"/>
          </c:if>
          <img id="profileImage" src="${profileImageUrl}" alt="Profile Image"
               style="width:150px; height:150px; border-radius:50%; object-fit: cover;">
          <input type="file" id="profileImageInput" name="profileImageFile" style="display:none;" accept="image/*">
          <div class="text-center mt-1">
            <button type="button" class="btn btn-primary btn-sm"
                    onclick="document.getElementById('profileImageInput').click()">사진 변경
            </button>
            <button type="button" class="btn btn-danger btn-sm" onclick="deleteProfileImage()">사진 삭제</button>
          </div>
        </div>
        <div class="col-lg-9">
            <%-- 이름, 직원구분, 부서 --%>
          <div class="d-flex justify-content-between mb-2">
            <div class="col-md-4 d-flex align-items-center">
              <p class="col-md-4 text-left">이름</p>
              <form:input path="name" class="form-control"/>
            </div>
            <div class="col-md-4 d-flex align-items-center">
              <p class="col-md-4 text-left">직원구분</p>
              <form:select path="employeeType" class="form-control">
                <form:option value="" label="선택하세요"/>
                <form:option value="정규" label="정규"/>
                <form:option value="계약" label="계약"/>
                <form:option value="외주" label="외주"/>
                <form:option value="파견" label="파견"/>
                <form:option value="파트" label="파트"/>
                <form:option value="기타" label="기타"/>
              </form:select>
            </div>
            <div class="col-md-4 d-flex align-items-center">
              <p class="col-md-4 text-left">부서</p>
              <form:select path="departmentId" class="form-control">
                <form:option value="" label="부서를 선택하세요"/>
                <form:options items="${departments}" itemValue="departmentId" itemLabel="departmentName"/>
              </form:select>
            </div>
          </div>

            <%-- 입사일, 휴대전화. 직통번호 --%>
          <div class="d-flex justify-content-between mb-2">
            <div class="col-md-4 d-flex align-items-center">
              <p class="col-md-4 text-left">입사일</p>
              <form:input path="hireDate" type="date" class="form-control"/>
            </div>
            <div class="col-md-4 d-flex align-items-center">
              <p class="col-md-4 text-left">휴대전화</p>
              <form:input path="mobilePhone" id="mobilePhone" class="form-control"/>
            </div>
            <div class="col-md-4 d-flex align-items-center">
              <p class="col-md-4 text-left">직통번호</p>
              <form:input path="directPhone" id="directPhone" class="form-control"/>
            </div>
          </div>

            <%-- 외부이메일, 직위 --%>
          <div class="d-flex justify-content-between mb-2">
            <div class="col-md-4 d-flex align-items-center">
              <p class="col-md-4 text-left">외부이메일</p>
              <form:input path="externalEmail" id="externalEmail" class="form-control"/>
            </div>
            <div class="col-md-4 d-flex align-items-center">
              <p class="col-md-4 text-left">직위</p>
              <form:select path="positionId" class="form-control">
                <form:option value="" label="직위를 선택하세요"/>
                <form:options items="${positions}" itemValue="positionId" itemLabel="positionName"/>
              </form:select>
            </div>
            <div class="col-md-4 d-flex align-items-center">
            </div>
          </div>

        </div>
      </div>
    </div>
  </div>
</div>
<div class="card card-defalut">
  <div class="card-body">
    <div class="tabs">
      <button type="button" class="tab-button active" data-tab="info">정보</button>
      <span>&nbsp;|&nbsp;</span>
      <button type="button" class="tab-button" data-tab="attendance">출퇴근 현황</button>
    </div>

    <!-- 정보 탭 -->
    <div class="tab-content active" id="info">
      <div class="d-flex flex-column">
        <div class="employeebox mb-5">

            <%-- 주민번호, 국적, 체류자격 --%>
          <div class="d-flex justify-content-between mb-2">
            <div class="col-md-4 d-flex align-items-center">
              <p class="col-md-4 text-left">주민(외국인)번호</p>
              <form:input path="residentNumber" class="form-control"/>
            </div>
            <div class="col-md-4 d-flex align-items-center">
              <p class="col-md-4 text-left">국적</p>
              <form:input path="nationality" class="form-control"/>
            </div>
            <div class="col-md-4 d-flex align-items-center">
              <p class="col-md-4 text-left">체류자격</p>
              <form:input path="visaStatus" class="form-control"/>
            </div>
          </div>

            <%-- 영문이름, 성별, 생년월일 --%>
          <div class="d-flex justify-content-between mb-2">
            <div class="col-md-4 d-flex align-items-center">
              <p class="col-md-4 text-left">영문이름</p>
              <form:input path="englishName" class="form-control"/>
            </div>
            <div class="col-md-4 d-flex align-items-center">
              <p class="col-md-4 text-left">성별</p>
              <form:select path="gender" class="form-control">
                <form:option value="M" label="남"/>
                <form:option value="F" label="여"/>
              </form:select>
            </div>
            <div class="col-md-4 d-flex align-items-center">
              <p class="col-md-4 text-left">생년월일</p>
              <form:input path="birthDate" type="date" class="form-control"/>
            </div>
          </div>

            <%-- 주소, 퇴사일자 --%>
          <div class="d-flex justify-content-between mb-2">
            <div class="col-md-4 d-flex align-items-center">
              <p class="col-md-4 text-left">주소</p>
              <form:input path="address" class="form-control"/>
            </div>
            <div class="col-md-4 d-flex align-items-center">
              <p class="col-md-4 text-left">퇴사일자</p>
              <form:input path="lastWorkingDay" id="lastWorkingDay" type="date" class="form-control"/>
            </div>
            <div class="col-md-4 d-flex align-items-center">
            </div>
          </div>
        </div>

        <div class="m-auto mt-3">
          <input type="submit" value="수정" class="btn btn-primary">
          <button type="button" onclick="deleteEmployee('${employee.username}')" class="btn btn-danger">삭제</button>
        </div>
      </div>
    </div>

    </form:form>

    <!-- 출퇴근 탭 -->
    <div class="tab-content" id="attendance">

      <!-- 출퇴근 추가 폼 -->
      <div class="mb-3">
        <label class="mr-2">구분</label>
        <select id="newWorkStatus" class="form-control mr-4" style="width:150px; display:inline-block;">
          <option value="출근">출근</option>
          <option value="퇴근">퇴근</option>
        </select>

        <label class="mr-2">일시</label>
        <input type="datetime-local" id="newDateTime" class="form-control mr-4" style="width:200px; display:inline-block;">

        <button type="button" id="addAttendanceBtn" class="btn btn-info">추가</button>
      </div>

      <%-- 출퇴근 목록 테이블 --%>
      <form id="attendanceForm">
        <div class="mb-3">
          <button type="submit" class="btn btn-danger btn-sm">선택 삭제</button>
        </div>

        <input type="hidden" name="username" value="${employee.username}"/>
        <table class="table table-hover">
          <thead>
			  <tr>
			    <th><input type="checkbox" id="checkAll"/></th>
			    <th>구분</th>
			    <th>출근일시</th>
			    <th>퇴근일시</th>
			    <th>생성일시</th>
			    <th>수정일시</th>
			    <th>수정</th>
			  </tr>
		</thead>
          <tbody>
			<c:forEach var="att" items="${attendanceList}">
			  <tr data-attendance-id="${att.attendanceId}">
			    <td><input type="checkbox" name="attendanceIds" value="${att.attendanceId}"/></td>
			
			    <td class="workStatusCell" data-status="${att.workStatus != null ? att.workStatus : '-'}">
			      <span class="badge badge-primary">
			        ${att.workStatus != null ? att.workStatus : '-'}
			      </span>
			    </td>
			
			    <td class="checkinCell"
			        data-checkin="${att.checkInDateTimeForInput != null ? att.checkInDateTimeForInput : ''}">
			      <c:choose>
			        <c:when test="${att.checkInDateTimeForInput != null}">
			          ${att.checkInDateTimeForInput.replace("T"," ")}
			        </c:when>
			        <c:otherwise>-</c:otherwise>
			      </c:choose>
			    </td>
			
			    <td class="checkoutCell"
			        data-checkout="${att.checkOutDateTimeForInput != null ? att.checkOutDateTimeForInput : ''}">
			      <c:choose>
			        <c:when test="${att.checkOutDateTimeForInput != null}">
			          ${att.checkOutDateTimeForInput.replace("T"," ")}
			        </c:when>
			        <c:otherwise>-</c:otherwise>
			      </c:choose>
			    </td>
			
			    <td class="createdAtCell">
				  ${att.createdAtForView}
				</td>
				
				<td class="updatedAtCell">
				  ${att.updatedAtForView}
				</td>
			
			    <td>
			      <button type="button" class="btn btn-warning updateAttendanceBtn">수정</button>
			    </td>
			  </tr>
			</c:forEach>
			</tbody>
        </table>
      </form>
    </div>
  <div class="text-right mt-6">
    <a href="/employee/list" class="btn btn-primary">목록으로</a>
  </div>
  </div>


</div>
</div>
</div>
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>

<script>

  document.addEventListener("DOMContentLoaded", function () {

    // ---------------- 탭 처리 ----------------
    document.querySelectorAll(".tab-button").forEach(button => {
      button.addEventListener("click", () => {
        document.querySelectorAll(".tab-button").forEach(btn => btn.classList.remove("active"));
        document.querySelectorAll(".tab-content").forEach(tab => tab.classList.remove("active"));
        button.classList.add("active");
        const tabId = button.getAttribute("data-tab");
        document.getElementById(tabId).classList.add("active");
      });
    });

    // 페이지 로드 시 hash 체크해서 해당 탭 활성화
    if (window.location.hash === "#attendance") {
      document.querySelectorAll(".tab-button").forEach(btn => btn.classList.remove("active"));
      document.querySelectorAll(".tab-content").forEach(tab => tab.classList.remove("active"));

      const attendanceButton = document.querySelector(".tab-button[data-tab='attendance']");
      const attendanceTab = document.getElementById("attendance");
      if (attendanceButton && attendanceTab) {
        attendanceButton.classList.add("active");
        attendanceTab.classList.add("active");
      }
    }

    // 이미지 미리보기
    const profileInput = document.getElementById('profileImageInput');
    if (profileInput) {
      profileInput.addEventListener('change', function (event) {
        const file = event.target.files[0];
        if (file) {
          const reader = new FileReader();
          reader.onload = function (e) {
            document.getElementById('profileImage').src = e.target.result;
          }
          reader.readAsDataURL(file);
        }
      });
    }

    // ---------------- 출퇴근 추가 ----------------
    document.getElementById("addAttendanceBtn").addEventListener("click", function () {
      const username = document.getElementById("username").value;
      const workStatus = document.getElementById("newWorkStatus").value;
      const dateTime = document.getElementById("newDateTime").value;

      if (!dateTime) {
        Swal.fire({
          text: "날짜와 시간을 입력해주세요.",
          icon: "warning"
        });
        return;
      }

      fetch(`/employee/${username}/attendance/add`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({workStatus, dateTime})
      })
          .then(res => res.json())
          .then(data => {
            if (data.success) {
              // 새로고침 전에 hash 설정
              location.hash = "#attendance";
              location.reload(); // 페이지 새로고침

            } else {
              Swal.fire({
                text: "추가에 실패했습니다.",
                icon: "error"
              });
            }
          })
          .catch(err => {
            console.error(err);
            Swal.fire({
              text: "추가 중 오류가 발생했습니다.",
              icon: "error"
            });
          });
    });


    // 사진 삭제
    function deleteProfileImage() {
      const username = document.getElementById('username').value;
      fetch('/employee/deleteProfileImage', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: 'username=' + encodeURIComponent(username)
      })
          .then(response => response.text())
          .then(result => {
            if (result == 'success') {
              document.getElementById('profileImage').src = '/images/default_profile.jpg';

              // 여기가 중요: input 초기화
              const profileInput = document.getElementById('profileImageInput');
              if (profileInput) profileInput.value = "";

              Swal.fire({
                text: "사진이 삭제되었습니다.",
                icon: "success"
              });

            } else {
              Swal.fire({
                text: "사진 삭제에 실패했습니다.",
                icon: "error"
              });
            }
          })
          .catch(error => {
            console.error('에러:', error);

            Swal.fire({
              text: "사진 삭제 중 오류가 발생했습니다.",
              icon: "error"
            });

          });
    }

    window.deleteProfileImage = deleteProfileImage; // 버튼에서 접근 가능하도록 전역 등록

    // 체크박스 전체 선택
    const checkAll = document.getElementById("checkAll");
    if (checkAll) {
      checkAll.addEventListener("click", function () {
        const checked = this.checked;
        document.querySelectorAll("input[name='attendanceIds']").forEach(cb => cb.checked = checked);
      });
    }

    // 출퇴근 삭제 처리
    const attendanceForm = document.getElementById("attendanceForm");
    if (attendanceForm) {
      attendanceForm.addEventListener("submit", function (e) {

        e.preventDefault(); // 기본 submit 막기

        // 선택된 체크박스 확인
        const selectedIds = Array.from(document.querySelectorAll("input[name='attendanceIds']:checked"))
            .map(cb => cb.value);
        if (selectedIds.length === 0) {
          Swal.fire({
            text: "삭제할 항목을 선택해주세요.",
            icon: "warning"
          });

          return;
        }

        Swal.fire({
          text: "정말 삭제하시겠습니까?",
          icon: "question",
          showCancelButton: true,
          confirmButtonText: "삭제",
          cancelButtonText: "취소",
          reverseButtons: true
        }).then(result => {

          if (result.isConfirmed) {

            const username = document.getElementById("username").value;

            fetch(`/employee/${username}/attendance/delete`, {
              method: 'POST',
              headers: {'Content-Type': 'application/json'},
              body: JSON.stringify({attendanceIds: selectedIds})
            })
                .then(res => res.json())
                .then(data => {
                  if (data.success) {
                    Swal.fire({
                      text: "삭제되었습니다.",
                      icon: "success"
                    }).then(result => {
                      // 확인 버튼을 누르거나 창 밖을 클릭했을 때 모두 실행
                      if (result.isConfirmed || result.isDismissed) {

                        // 삭제된 행 제거
                        selectedIds.forEach(id => {
                          const row = document.querySelector(`tr[data-attendance-id='${id}']`);
                          if (row) row.remove();
                          // 삭제 성공 후 hash 설정 + 페이지 새로고침
                          location.hash = "#attendance";
                          location.reload();
                        });

                      }
                    });

                  } else {
                    Swal.fire({
                      text: "삭제에 실패했습니다.",
                      icon: "error"
                    });
                  }
                })
                .catch(err => {
                  console.error(err);

                  Swal.fire({
                    text: "삭제 중 오류가 발생했습니다.",
                    icon: "error"
                  });

                });

          }

        });

      });
    }

  });

  //삭제 버튼
  function deleteEmployee(username) {
    const lastWorkingDayInput = document.getElementById('lastWorkingDay');
    if (lastWorkingDayInput && !lastWorkingDayInput.value) {

      Swal.fire({
        text: "퇴사일자를 먼저 설정해 주세요.",
        icon: "warning"
      });

      return;
    }

    Swal.fire({
      text: "정말 삭제하시겠습니까?",
      icon: "question",
      showCancelButton: true,
      confirmButtonText: "삭제",
      cancelButtonText: "취소",
      reverseButtons: true
    }).then(result => {
      if (result.isConfirmed) {

        // CSRF 메타 (둘 다 있을 때만 추가 → Invalid name 방지)
        const csrfTokenEl = document.querySelector('meta[name="_csrf"]');
        const csrfHeaderEl = document.querySelector('meta[name="_csrf_header"]');
        const token = csrfTokenEl ? csrfTokenEl.getAttribute('content') : null;
        const header = csrfHeaderEl ? csrfHeaderEl.getAttribute('content') : null;

        const headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');
        if (header && token) headers.append(header.trim(), token.trim());

        const body = new URLSearchParams({
          lastWorkingDay: lastWorkingDayInput ? lastWorkingDayInput.value : ''
        });

        fetch('/employee/delete/' + encodeURIComponent(username), {
          method: 'POST',
          headers,
          body: body.toString()
        })
            .then(res => res.json())
            .then(data => {
              if (data.success) {

                Swal.fire({
                  text: "삭제되었습니다.",
                  icon: "success"
                }).then(result => {
                  // 확인 버튼을 누르거나 창 밖을 클릭했을 때 모두 실행
                  if (result.isConfirmed || result.isDismissed) window.location.href = "/employee/list";
                });

              } else {
                Swal.fire({
                  text: "삭제에 실패했습니다.",
                  icon: "error"
                });
              }
            })
            .catch(err => {
              console.error(err);

              Swal.fire({
                text: "삭제 중 오류가 발생했습니다.",
                icon: "error"
              });

            });

      }
    });

  }

  //---------------- 출퇴근 수정 이벤트 함수 ----------------
  function attachUpdateEvent(button) {
  button.addEventListener("click", function () {
    const row = this.closest("tr");
    const workStatusCell = row.querySelector(".workStatusCell");
    const checkinCell = row.querySelector(".checkinCell");
    const checkoutCell = row.querySelector(".checkoutCell");
    const username = document.getElementById("username").value;

    if (this.textContent.trim() == "수정") {
      const currentStatus = (workStatusCell.dataset.status || "").trim();

      // 현재 값들
      let checkinVal  = checkinCell.dataset.checkin  || '';
      let checkoutVal = checkoutCell.dataset.checkout || '';

      if (checkinVal)  checkinVal  = checkinVal.replace(" ", "T").slice(0, 16);
      if (checkoutVal) checkoutVal = checkoutVal.replace(" ", "T").slice(0, 16);

      // 상태 select + 입력 필드 구성
      workStatusCell.innerHTML = `
        <select class="form-control workStatusInput">
          <option value="출근" ${currentStatus == "출근" ? "selected" : ""}>출근</option>
          <option value="퇴근" ${currentStatus == "퇴근" ? "selected" : ""}>퇴근</option>
        </select>`;

      // 출근/퇴근 각각의 입력 칸
      checkinCell.innerHTML  = `<input type="datetime-local" class="form-control checkinInput" value="${checkinVal}">`;
      checkoutCell.innerHTML = `<input type="datetime-local" class="form-control checkoutInput" value="${checkoutVal}" ${currentStatus=='퇴근'?'':'disabled'}>`;

      // 상태 변경 시, 퇴근이면 checkout 활성화 / 출근이면 checkout 비활성화
      workStatusCell.querySelector('.workStatusInput').addEventListener('change', (e) => {
        const v = e.target.value;
        const co = row.querySelector('.checkoutInput');
        if (v == '퇴근') co.removeAttribute('disabled');
        else { co.value = ''; co.setAttribute('disabled', 'disabled'); }
      });

      this.textContent = "완료";
      this.classList.remove("btn-warning");
      this.classList.add("btn-success");

    } else {
      // 완료 클릭 → 현재 선택 상태에 따라 '하나의 일시'만 서버로 보냄 (기존 API)
      const newStatus = row.querySelector(".workStatusInput").value.trim();
      const newCheckIn  = row.querySelector(".checkinInput").value || '';
      const newCheckOut = row.querySelector(".checkoutInput")?.value || '';
      const attendanceId = row.dataset.attendanceId;

      // 서버 계약: dateTime 하나만 보냄
      let dateTimeToSend = '';
      if (newStatus == '출근') dateTimeToSend = newCheckIn;
      else if (newStatus == '퇴근') dateTimeToSend = newCheckOut;

      if (!dateTimeToSend) {
        Swal.fire({ text: "일시를 입력해주세요.", icon: "warning" });
        return;
      }

      fetch(`/employee/${username}/attendance/edit`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
          attendanceId: attendanceId,
          workStatus: newStatus,
          dateTime: dateTimeToSend
        })
      })
      .then(res => res.json())
      .then(data => {
        if (data.success) {
          // 화면 반영
          workStatusCell.innerHTML = '';
          const span = document.createElement('span');
          span.className = 'badge badge-primary';
          span.textContent = newStatus;
          workStatusCell.appendChild(span);
          workStatusCell.dataset.status = newStatus;

          // 셀 텍스트 갱신
          const fmt = (s) => s ? s.replace("T"," ") : '-';
          const inTxt  = newStatus == '출근' ? fmt(newCheckIn)  : fmt(checkinCell.dataset.checkin);
          const outTxt = newStatus == '퇴근' ? fmt(newCheckOut) : fmt(checkoutCell.dataset.checkout);

          checkinCell.textContent  = inTxt;
          checkoutCell.textContent = outTxt;

          // data-*도 갱신
          if (newStatus == '출근') {
            checkinCell.dataset.checkin = newCheckIn;
          } else if (newStatus == '퇴근') {
            checkoutCell.dataset.checkout = newCheckOut;
          }

          // 버튼 복구
          this.textContent = "수정";
          this.classList.remove("btn-success");
          this.classList.add("btn-warning");
        } else {
          Swal.fire({ text: "수정에 실패했습니다.", icon: "error" });
        }
      })
      .catch(err => {
        console.error(err);
        Swal.fire({ text: "수정 중 오류가 발생했습니다.", icon: "error" });
      });
    }
  });
}


  document.addEventListener("DOMContentLoaded", () => {
	  const englishInput = document.querySelector("input[name='englishName']");
	  if (englishInput) {
	    englishInput.addEventListener("input", (e) => {
	      // 1) 자동 대문자 변환
	      let value = e.target.value.toUpperCase();
	      // 2) 영문 대문자와 공백만 허용 (기타 문자 제거)
	      value = value.replace(/[^A-Z ]/g, '');
	      e.target.value = value;
	    });
	  }
	});

  // 기존 버튼에도 적용
  document.querySelectorAll(".updateAttendanceBtn").forEach(btn => attachUpdateEvent(btn));

  
//010-1234-5678 자동 포맷
  function fmtMobile(v){
    const d=(v||'').replace(/\D/g,'');
    if(d.length<=3) return d;
    if(d.length<=7) return d.replace(/(\d{3})(\d{1,4})/,'$1-$2');
    return d.replace(/(\d{3})(\d{3,4})(\d{1,4}).*/,'$1-$2-$3');
  }

  const mobileEl=document.getElementById('mobilePhone');
  if(mobileEl){
	  mobileEl.addEventListener('input',e=>{
		      const oldLen=e.target.value.length;
		      const oldPos=e.target.selectionStart;
		       e.target.value=fmtMobile(e.target.value);
		      const newLen=e.target.value.length;
		      const newPos=oldPos+(newLen-oldLen);
		      try{ e.target.setSelectionRange(newPos,newPos); }catch(_){}
		     });
  }

  // ===== 이메일/전화 유틸 =====
  // 비어있으면 true, 값이 있으면 형식 검증
  function isValidEmailOrBlank(v){
    if(!v) return true;
    // 과하지 않은 현실적인 이메일 패턴 (로컬@도메인.최상위)
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(v.trim());
  }

  // 직통번호 자동 포맷: 02, 0XX, 0XXX 지역번호 지원
  function fmtDirect(v){
    const d=(v||'').replace(/\D/g,'');
    if(d.startsWith('02')){
      if(d.length<=2) return d;
      if(d.length<=5) return d.replace(/(02)(\d{0,3})/,'$1-$2');
      if(d.length<=9) return d.replace(/(02)(\d{3,4})(\d{0,4}).*/,'$1-$2-$3');
      return d.slice(0,10).replace(/(02)(\d{3,4})(\d{0,4}).*/,'$1-$2-$3');
    }else{
      if(d.length<=3) return d;
      if(d.length<=7) return d.replace(/(0\d{2})(\d{0,4})/,'$1-$2');
      return d.replace(/(0\d{2,3})(\d{3,4})(\d{0,4}).*/,'$1-$2-$3');
    }
  }

  function isValidDirectOrBlank(v){
    if(!v) return true;
    const d=v.replace(/\D/g,'');
    // 02-xxx-xxxx (9~10자리) 또는 0xx/0xxx-xxx-xxxx (10~11자리) 허용
    const isSeoul = d.startsWith('02') && (d.length===9 || d.length===10);
    const isArea  = /^0\d{1,2}/.test(d) && (d.length===10 || d.length===11);
    return isSeoul || isArea;
  }

  // ===== 필드 핸들 ====
  const emailEl = document.getElementById('externalEmail');
  const directEl = document.getElementById('directPhone');

  // 이메일: 실시간 기본 검증 (붉은 테두리 토글)
  if(emailEl){
    emailEl.addEventListener('input', e=>{
      const ok = isValidEmailOrBlank(e.target.value);
      e.target.classList.toggle('is-invalid', !ok);
    });
  }

  // 직통번호: 자동 포맷 + 기본 검증
  if(directEl){
    directEl.addEventListener('input', e=>{
      const formatted = fmtDirect(e.target.value);
      const pos = e.target.selectionStart;
      e.target.value = formatted;
      try{ e.target.setSelectionRange(e.target.value.length,e.target.value.length); }catch(_){}
      const ok = isValidDirectOrBlank(e.target.value);
      e.target.classList.toggle('is-invalid', !ok);
    });
  }

  // ===== 제출 전 최종 검증 (기존 휴대전화 검증에 이어서 이메일/직통 추가) =====
  (function(){
    const form = document.querySelector('form[action="/employee/edit"]');
    const mobileEl = document.getElementById('mobilePhone');
    if(!form) return;

    form.addEventListener('submit', e=>{
      let invalidMsg = [];

      // 1) 휴대전화 (기존 로직)
      if(mobileEl){
        const raw=mobileEl.value.replace(/\D/g,'');
        const ok=/^(01[0-9])\d{7,8}$/.test(raw);
        mobileEl.classList.toggle('is-invalid', !ok);
        if(!ok) invalidMsg.push('휴대전화 형식이 올바르지 않습니다. 예: 01012345678');
      }

      // 2) 외부이메일
      if(emailEl){
        const ok = isValidEmailOrBlank(emailEl.value);
        emailEl.classList.toggle('is-invalid', !ok);
        if(!ok) invalidMsg.push('외부이메일 형식이 올바르지 않습니다. 예: user@example.com');
      }

      // 3) 직통번호
      if(directEl){
        const ok = isValidDirectOrBlank(directEl.value);
        directEl.classList.toggle('is-invalid', !ok);
        if(!ok) invalidMsg.push('직통번호 형식이 올바르지 않습니다. 예: 02-123-4567, 031-123-4567');
      }

      if(invalidMsg.length>0){
        e.preventDefault();
        // SweetAlert2 사용 중이므로 같은 톤으로 안내
        Swal.fire({
          html: invalidMsg.join('<br>'),
          icon: 'warning'
        });
      }
    });
  })();
  
 
  
</script>

</html>
