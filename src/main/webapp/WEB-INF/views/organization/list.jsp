<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>조직 설계</title>
  <c:import url="/WEB-INF/views/include/head.jsp"/>

  <!-- jsTree -->
  <link rel="stylesheet" href="<c:url value='/css/themes/default/style.min.css'/>" />
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/>

<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

<c:import url="/WEB-INF/views/include/header.jsp"/>

<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
<!-- 내용 시작 -->

<div class="row">
  <!-- 좌측: 조직도 트리 -->
  <div class="col-lg-4">
    <div class="card card-default">
      <div class="card-header">
        <h2 class="mb-0">조직설계</h2>
      </div>
      <div class="card-body">
        <h5 class="mb-3">조직도</h5>
        <div id="orgTree" class="mb-4"></div>

        <!-- 버튼 영역 -->
        <div class="d-flex">
          <button id="addBtn" class="btn btn-primary btn-sm mr-2">
            <i class="mdi mdi-plus mr-1"></i>부서 추가
          </button>
          <button id="deleteBtn" class="btn btn-danger btn-sm">
            <i class="mdi mdi-delete mr-1"></i>부서 삭제
          </button>
        </div>
      </div>
    </div>
  </div>

  <!-- 우측: 부서 상세 정보 -->
  <div class="col-lg-8">
    <div class="card card-default">
      <div class="card-header">
        <h2 class="mb-0" id="deptHeader">부서상세</h2>
      </div>
      <div class="card-body">
        <!-- 탭 네비게이션 -->
        <ul class="nav nav-tabs mb-3" id="deptTab" role="tablist">
          <li class="nav-item" role="presentation">
            <button class="nav-link active" id="info-tab" data-bs-toggle="tab" data-bs-target="#deptInfo" type="button" role="tab">부서 정보</button>
          </li>
          <li class="nav-item" role="presentation">
            <button class="nav-link" id="members-tab" data-bs-toggle="tab" data-bs-target="#deptMembers" type="button" role="tab">부서원 목록</button>
          </li>
        </ul>

        <!-- 탭 컨텐츠 -->
        <div class="tab-content">
          <!-- 부서 정보 탭 -->
          <div class="tab-pane fade show active" id="deptInfo" role="tabpanel">
            <div class="table-responsive">
              <table class="table">
                <tbody>
                <tr>
                  <th class="col-3">부서명</th>
                  <td id="deptName"></td>
                </tr>
                <tr>
                  <th>생성일</th>
                  <td id="deptCreated"></td>
                </tr>
                <tr>
                  <th>상위부서</th>
                  <td id="deptParent"></td>
                </tr>
                <tr>
                  <th>하위부서</th>
                  <td id="deptChildren"></td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>

          <!-- 부서원 목록 탭 -->
          <div class="tab-pane fade" id="deptMembers" role="tabpanel">
            <ul class="list-group" id="deptMemberList"></ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<!-- 추가 모달 -->
<div class="modal fade" id="addModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">부서 추가</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body">
        <div class="form-group">
          <label class="form-label">부서명</label>
          <input type="text" id="newDeptName" class="form-control" placeholder="예: 품질관리팀">
        </div>
        <div class="form-group">
          <label class="form-label">상위 부서</label>
          <select id="parentDeptSelect" class="form-control">
            <option value="">(없음 / 루트)</option>
          </select>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-light" data-bs-dismiss="modal">취소</button>
        <button type="button" class="btn btn-primary" id="saveDeptBtn">저장</button>
      </div>
    </div>
  </div>
</div>

<!-- 삭제 모달 -->
<div class="modal fade" id="deleteModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">부서 삭제</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body">
        정말 삭제하시겠습니까? (하위 부서도 같이 삭제됩니다)
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-light" data-bs-dismiss="modal">취소</button>
        <button type="button" class="btn btn-danger" id="confirmDeleteBtn">삭제</button>
      </div>
    </div>
  </div>
</div>

<!-- 부서 이동 모달 -->
<div class="modal fade" id="moveDeptModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">부서 이동</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body">
        <label class="form-label">이동할 부서 선택</label>
        <select id="targetDeptSelect" class="form-control">
          <option value="">-- 부서를 선택하세요 --</option>
        </select>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-light" data-bs-dismiss="modal">취소</button>
        <button type="button" class="btn btn-primary" id="confirmMoveBtn">이동</button>
      </div>
    </div>
  </div>
</div>

<!-- 부서원 제외 모달 -->
<div class="modal fade" id="excludeModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">부서원 제외</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body">
        선택한 부서원을 정말 제외하시겠습니까?
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-light" data-bs-dismiss="modal">취소</button>
        <button type="button" class="btn btn-danger" id="confirmExcludeBtn">제외</button>
      </div>
    </div>
  </div>
</div>

<!-- 내용 끝 -->
<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>

<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>

<script src="<c:url value='/js/jstree.min.js'/>"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<script>
  const ctx = '${pageContext.request.contextPath}';

  $(function(){
    $.getJSON("/organization/tree", function(data){
      $('#orgTree').jstree({
        "core": {
          "check_callback": true,
          "data": data
        }
      }).on('ready.jstree', function() {
        // 트리 준비 완료 시
        let rootNode = data.find(d => d.parent === "#"); // 루트(인피니티오토)
        if (rootNode) {
          // 루트 노드 선택
          $('#orgTree').jstree("select_node", rootNode.id);

          // 강제로 이벤트 발생시켜 부서 상세 호출
          $('#orgTree').trigger("select_node.jstree", {
            node: { id: rootNode.id }
          });
        }
      });
    });

    // 노드 클릭 이벤트
    $('#orgTree').on("select_node.jstree", function (e, data) {
      let deptId = data.node.id;

      $.getJSON("/organization/" + deptId, function(detail){

        console.log("📌 전체 detail 객체:", detail);
        console.log("📌 하위부서(childDepartments):", detail.childDepartments);
        console.log("📌 부서원(members):", detail.members);

        console.log("부서원 데이터:", detail.members);
        detail.members.forEach(m => {
          console.log("이름:", m.name, "직위:", m.positionName);
        });



        // 부서상세 제목
        $("#deptHeader").text(detail.departmentName + " (" + detail.employeeCount + ")");

        // 기본 정보
        $("#deptName").text(detail.departmentName);
        $("#deptCreated").text(detail.createdDate);
        $("#deptParent").text(detail.parentDepartmentName || "미지정");

        // 하위부서
        console.log("하위부서 데이터:", detail.childDepartments);
        if (detail.childDepartments && detail.childDepartments.length > 0) {
          $("#deptChildren").text(detail.childDepartments.join(", "));
        } else {
          $("#deptChildren").text("정보 없음");
        }

        // 부서원 목록
        if (detail.members && detail.members.length > 0) {
          let memberHtml = `
            <li class="list-group-item d-flex justify-content-between align-items-center">
            <div>
            <input type="checkbox" id="checkAll"> 전체 선택
            </div>
            <div>
            <button id="moveDeptBtn" class="btn btn-sm btn-warning mr-2">부서 이동</button>
            <button id="excludeBtn" class="btn btn-sm btn-danger">부서원 제외</button>
            </div>
          </li>
        `;

          detail.members.forEach(m => {
            let deptName = m.departmentName ? m.departmentName : "부서 없음";
            let posName = m.positionName ? m.positionName : "직위 없음";
            memberHtml += `
          <li class="list-group-item">
            <input type="checkbox" class="memberCheck mr-2" value="\${m.username}">
            \${m.name} \${m.positionName}
          </li>
        `;
          });



          console.log("👉 최종 렌더링될 HTML:", memberHtml);
          $("#deptMemberList").html(memberHtml);
        } else {
          $("#deptMemberList").html("<li class='list-group-item text-center text-muted'>부서원이 없습니다</li>");
        }
      }).fail(function(){
        Swal.fire({
          text: "부서 상세 정보를 불러오지 못했습니다.",
          icon: "error"
        });
      });
    });

    // 현재 선택된 노드 반환
    function getSelectedNode() {
      var tree = $('#orgTree').jstree(true);
      var sel = tree.get_selected(true);
      return sel.length > 0 ? sel[0] : null;
    }

//추가 버튼 → 모달 열기
    $("#addBtn").on("click", function(){
      $("#newDeptName").val("");
      $("#parentDeptSelect").empty().append('<option value="">(없음 / 루트)</option>');

      // DB에 있는 부서 목록 가져오기
      $.getJSON("/organization/tree", function(data){
        data.forEach(dept => {
          $("#parentDeptSelect").append(`<option value="\${dept.id}">\${dept.text}</option>`);
        });
      });

      $("#addModal").modal("show");
    });


//저장 버튼 → DB 반영
    $("#saveDeptBtn").on("click", function(){
      let name = $("#newDeptName").val().trim();
      let parentId = $("#parentDeptSelect").val();

      if(!name) {

        Swal.fire({
          text: "부서명을 입력해주세요.",
          icon: "warning"
        });

        return;
      }

      $.post("/organization/add", { parentId, name })
          .done(function(newNode){
            var tree = $('#orgTree').jstree(true);
            tree.create_node(newNode.parentId == null ? "#" : newNode.parentId, {
              id: newNode.id,
              text: newNode.name
            });

            $("#addModal").modal("hide");

            Swal.fire({
              text: "추가되었습니다.",
              icon: "success"
            }).then(result => location.reload());

          })
          .fail(function(xhr){
            if(xhr.status === 409) {
              Swal.fire({
                text: "이미 존재하는 부서명입니다.",
                icon: "warning"
              });
            } else {
              Swal.fire({
                text: "부서 추가 중 오류가 발생했습니다.",
                icon: "error"
              });
            }
          });
    });


    // ===========================
    // 삭제 버튼 → 모달 열기
    // ===========================
    $("#deleteBtn").on("click", function(){
      var selected = getSelectedNode();
      if(!selected) {
        Swal.fire({
          text: "삭제할 부서를 선택하세요.",
          icon: "warning"
        });

        return;
      }
      $("#deleteModal").modal("show");
    });

    // 삭제 확인
    $("#confirmDeleteBtn").on("click", function(){
      var selected = getSelectedNode();
      if(!selected) return;

      $.post("/organization/deleteCascade", { id: selected.id }, function(){
        $('#orgTree').jstree(true).delete_node(selected);
        $("#deleteModal").modal("hide");
        Swal.fire({
          text: "삭제되었습니다.",
          icon: "success"
        }).then(result => location.reload());
      });
    });
  });

  //전체 선택
  $(document).on("change", "#checkAll", function(){
    $(".memberCheck").prop("checked", $(this).is(":checked"));
  });

  //부서 이동 버튼 동작
  $(document).on("click", "#moveDeptBtn", function(){
    let selected = $(".memberCheck:checked").map(function(){ return this.value; }).get();
    if (selected.length === 0) {
      Swal.fire({
        text: "이동할 직원을 선택해주세요.",
        icon: "warning"
      });

      return;
    }

    // 기존 부서들 가져오기
    $.getJSON("/organization/tree", function(data){
      let options = '<option value="">-- 부서를 선택하세요 --</option>';

      data.forEach(dept => {
        // 루트(최상위, parent가 "#")는 제외
        if (dept.parent === "#") return;

        options += `<option value="\${dept.id}">\${dept.text}</option>`;
      });

      $("#targetDeptSelect").html(options);
      $("#moveDeptModal").modal("show");
    });

    // 선택된 직원 목록 임시 저장
    $("#confirmMoveBtn").data("selectedEmployees", selected);
  });


  $(document).on("click", "#confirmMoveBtn", function(){
    let newDeptId = $("#targetDeptSelect").val();
    let selected = $(this).data("selectedEmployees");

    if (!newDeptId) {
      Swal.fire({
        text: "이동할 부서를 선택해주세요.",
        icon: "warning"
      });

      return;
    }

    $.ajax({
      url: "/organization/moveEmployees",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify({ employeeUsernames: selected, newDeptId: newDeptId }),
      success: function(){
        Swal.fire({
          text: "부서 이동이 완료되었습니다.",
          icon: "success"
        }).then(result => {
          $("#moveDeptModal").modal("hide");
          location.reload();
        });
      },
      error: function(){
        Swal.fire({
          text: "부서 이동 중 오류가 발생했습니다.",
          icon: "error"
        });
      }
    });
  });

  //제외 버튼 → 모달 열기
  $(document).on("click", "#excludeBtn", function(){
    let selected = $(".memberCheck:checked").map(function(){ return this.value; }).get();
    if (selected.length === 0) {
      Swal.fire({
        text: "제외할 직원을 선택해주세요.",
        icon: "warning"
      });

      return;
    }

    // 선택된 직원들 데이터 저장해두기
    $("#confirmExcludeBtn").data("selectedEmployees", selected);

    // 모달 열기
    $("#excludeModal").modal("show");
  });

  // 모달에서 확인 클릭 시 제외 처리
  $(document).on("click", "#confirmExcludeBtn", function(){
    let selected = $(this).data("selectedEmployees");

    $.ajax({
      url: "/organization/excludeEmployees",
      type: "POST",
      contentType: "application/json",
      data: JSON.stringify({ employeeUsernames: selected }),
      success: function(){
        Swal.fire({
          text: "제외되었습니다.",
          icon: "success"
        }).then(result => {
          $("#excludeModal").modal("hide");
          location.reload();
        });
      },
      error: function(){
        Swal.fire({
          text: "직원 제외 중 오류가 발생했습니다.",
          icon: "error"
        });
      }
    });
  });

</script>

</html>