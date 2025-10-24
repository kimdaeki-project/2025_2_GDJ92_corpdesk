<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>직위</title>
  <c:import url="/WEB-INF/views/include/head.jsp"/>
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/>

<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

<c:import url="/WEB-INF/views/include/header.jsp"/>

<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
<!-- 내용 시작 -->

<div class="card card-default">
  <div class="card-header">
    <h2 class="mb-0">직위체계</h2>
  </div>

  <div class="card-body">
    <h5 class="mb-3">직위목록</h5>

    <!-- 상단 툴바 -->
    <div class="d-flex justify-content-between align-items-center mb-3">
      <div class="d-flex">
        <button type="button" class="btn btn-primary mr-2" onclick="openAddModal()">
          <i class="mdi mdi-plus mr-1"></i>추가
        </button>
        <button type="button" class="btn btn-danger" onclick="deleteSelected()">
          <i class="mdi mdi-delete mr-1"></i>삭제
        </button>
      </div>
      <div>
        <form action="<c:url value='/position/list'/>" method="get" class="form-inline" role="search">
          <div class="input-group">
            <input type="text" class="form-control" name="q" value="${param.q}" placeholder="검색(직위명)">
            <div class="input-group-append">
              <button class="btn btn-outline-secondary" type="submit">
                <i class="mdi mdi-magnify"></i> Search
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>

    <!-- 테이블 -->
    <div class="table-responsive">
      <table class="table table-hover">
        <thead>
        <tr>
          <th style="width:48px;">
            <input type="checkbox" id="selectAll" onclick="toggleAll(this)">
          </th>
          <th>직위명(내림차순 정렬)</th>
          <th style="width:160px;" class="text-center">사용사원(명)</th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
          <c:when test="${not empty positionList}">
            <c:forEach var="p" items="${positionList}">
              <tr>
                <td>
                  <input type="checkbox" name="positionId" value="${p.positionId}">
                </td>
                <td><c:out value="${p.positionName}"/></td>
                <td class="text-center">${p.employeeCount}</td>
              </tr>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <tr>
              <td colspan="3" class="text-center text-muted p-5">표시할 직위가 없습니다.</td>
            </tr>
          </c:otherwise>
        </c:choose>
        </tbody>
      </table>
    </div>
  </div>
</div>

<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>

<!-- 모달 -->
<div class="modal fade" id="addPositionModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog">
    <form id="addPositionForm" class="modal-content" onsubmit="return submitAddPosition(event)">
      <div class="modal-header">
        <h5 class="modal-title">직위 추가</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="닫기">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>

      <div class="modal-body">
        <div class="form-group">
          <label class="form-label">직위명</label>
          <input class="form-control" name="positionName" required maxlength="30" placeholder="예) 대리">
        </div>

        <div class="form-group">
          <label class="form-label">상위 직위 선택</label>
          <select class="form-control" name="parentPositionId">
            <option value="">(최상위 직위)</option>
            <c:forEach var="p" items="${allPositions}">
              <option value="${p.positionId}">${p.positionName}</option>
            </c:forEach>
          </select>
        </div>
      </div>

      <div class="modal-footer">
        <button class="btn btn-light" type="button" data-dismiss="modal">취소</button>
        <button class="btn btn-primary" type="submit">저장</button>
      </div>
    </form>
  </div>
</div>

<script>
  // CSRF (head.jsp에 meta 태그가 있다고 가정)
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content;
  const csrfToken  = document.querySelector('meta[name="_csrf"]')?.content;

  function toggleAll(master){
    document.querySelectorAll('input[name="positionId"]').forEach(cb => cb.checked = master.checked);
  }

  function openAddModal(){
    document.getElementById('addPositionForm').reset();
    $('#addPositionModal').modal('show');
  }

  async function submitAddPosition(e) {
    e.preventDefault();
    const form = e.target;
    const body = {
      positionName: form.positionName.value.trim(),
      parentPositionId: form.parentPositionId.value ? Number(form.parentPositionId.value) : null
    };
    if (!body.positionName) return;

    const res = await fetch('<c:url value="/position"/>', {
      method: 'POST',
      headers: Object.assign(
          {'Content-Type': 'application/json'},
          csrfHeader ? {[csrfHeader]: csrfToken} : {}
      ),
      body: JSON.stringify(body)
    });

    if (res.ok) {
      $('#addPositionModal').modal('hide');
      location.reload();
    } else if (res.status === 409) {
      const msg = await res.text();
      // 중복일 때 메시지 표시
      Swal.fire({
        text: msg,
        icon: "warning"
      });
    } else {
      Swal.fire({
        text: '직위 추가에 실패했습니다.',
        icon: "error"
      });
    }
  }

  async function deleteSelected(){
    // 1. 검증 로직
    const ids = Array.from(document.querySelectorAll('input[name="positionId"]:checked'))
        .map(cb => Number(cb.value));
    if(ids.length === 0){
      Swal.fire({
        text: "선택된 항목이 없습니다.",
        icon: "warning"
      });
      return;
    }

    // 2. 사용자 확인 (await로 기다림)
    const result = await Swal.fire({
      text: "정말 삭제하시겠습니까?",
      icon: "question",
      showCancelButton: true,
      confirmButtonText: "삭제",
      cancelButtonText: "취소",
      reverseButtons: true
    });

    // 3. 확인했으면 삭제 진행 (await로 기다림)
    if (result.isConfirmed) {
      const res = await fetch('<c:url value="/position/delete"/>', {
        method: 'POST',
        headers: Object.assign(
            { 'Content-Type': 'application/json' },
            csrfHeader ? { [csrfHeader]: csrfToken } : {}
        ),
        body: JSON.stringify({ ids })
      });

      // 4. 결과 처리
      if(res.ok){
        location.reload();
      }else{
        Swal.fire({
          text: "삭제에 실패했습니다.",
          icon: "error"
        });
      }
    }
  }
</script>

</html>