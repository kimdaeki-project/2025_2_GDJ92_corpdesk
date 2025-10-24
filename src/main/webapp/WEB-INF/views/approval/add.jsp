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
    <link href="https://unpkg.com/filepond/dist/filepond.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <script defer type="text/javascript" src="/js/approval/approval.js"></script>

    <style>
        /* 테이블 전체의 위쪽 테두리 제거 */
        .no-top-border {
            border-top: none;
        }

        /* 테이블 헤더(thead) 내부의 th/td 셀의 위쪽 테두리 제거 */
        /* table-bordered 클래스에 의해 셀에도 테두리가 적용되므로 이를 제거합니다. */
        .no-top-border thead th,
        .no-top-border thead td {
            border-top: none;
        }

        /* Bootstrap 5의 경우, thead, tbody, tfoot 요소에도 기본적으로 border-top이 적용될 수 있습니다. */
        .no-top-border > :not(caption) > * > * {
            border-top: none;
        }
    </style>
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/>

<!-- Modal -->
<c:import url="modal.jsp"/>

<!-- Modal2 - 결재선 지정 -->
<div class="modal fade" id="selectApproverModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg modal-dialog-centered">
    <div class="modal-content">

      <div class="modal-header">
        <h5 class="modal-title fs-5" id="exampleModalLabel">결재선 지정</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>

      <div class="modal-body d-flex justify-content-between">

        <!-- left col -->
        <div class="card mb-4 p-0 w-75">

          <div class="card-body p-4">
            <ul class="list-unstyled" id="employee-list">
              <c:forEach items="${employeeList }" var="el">
                  <c:if test="${el.username ne userInfo.username}"> <%-- 기안자를 제외한 나머지 직원들의 데이터만 뿌림 --%>
                        <li class="text-start d-flex align-items-center" draggable="true"
                            data-username="${el.username}" data-name="${el.name}"
                            data-department-name="${el.departmentName}" data-position-name="${el.positionName}">
                            <div class="col-lg-3">
                                <img class="mr-2 img-fluid" style="border-radius: 50%; width: 40px; height: 40px; object-fit: cover;"
                                     src="${el.saveName ne null ?
                                                '/files/profile/' += el.saveName += '.' += el.extension
                                                : '/images/default_profile.jpg'}"> <%-- TODO 프로필이미지가 정확히 어떤 경로에 저장되는지 준수님과 이야기해야 함 --%>
                            </div>
                            <a class="approval-form-name btn px-0 mr-3 text-dark">${el.departmentName } ${el.positionName } ${el.name }</a>
                        </li>
                  </c:if>
              </c:forEach>
            </ul>
          </div>

        </div>

        <!-- right col -->
        <div class="card mb-4 p-0 ml-2 w-75">

<%--          <h5 class="card-title pt-4 px-4">상세정보</h5>--%>

          <div class="card-body p-4" id="approver-drop-area">
              <ul id="approver-list" class="list-unstyled"></ul>
          </div>

        </div>

      </div>

      <div class="modal-footer">
        <button type="button" id="approverCheck" class="btn btn-info" data-bs-dismiss="modal">확인</button>
        <button type="button" class="btn btn-light" data-bs-dismiss="modal">취소</button>
      </div>

    </div>
  </div>
</div>
<!--  -->

	<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

	<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

		<c:import url="/WEB-INF/views/include/header.jsp"/>
	
		<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
			<!-- 내용 시작 -->
			<div class="email-wrapper rounded border bg-white">
				<div class="row no-gutters justify-content-center">
				
				<div class="col-lg-4 col-xl-3 col-xxl-2">
					<div class="email-left-column email-options p-4 p-xl-5">
            <c:import url="widget_content.jsp"/>
					</div>
				</div>
				
				<div class="col-lg-8 col-xl-9 col-xxl-10">
					<div class="email-right-column p-4 p-xl-5">
						
						<!-- 양식 시작 -->
            <%-- 양식 헤더 --%>
						<div class="d-flex justify-content-between">
              <div>
						  	<button type="button" class="btn btn-info mr-1 btn-submit" id="requestApproval">결재 요청</button>
						  	<button type="button" class="btn btn-outline-info mr-1 btn-submit" id="tempSave">임시저장</button>
							  <button type="button" class="btn btn-light" id="btnCancel" onclick="location.href='/approval/list'">취소</button>
              </div>
              <div>
                <button data-bs-toggle="modal" data-bs-target="#selectApproverModal" id="selectApprover" type="button" class="btn btn-block btn-primary"><i class="mdi mdi-plus mr-1"></i>결재선 지정</button>
              </div>
						</div>
						<hr>
            <%--  --%>

            <%-- 양식 내용 --%>
            <div class="col-lg-7">

            <form id="approvalContentCommon">
              <%-- 1. 공통 양식 --%>
              <h1 class="text-center p-4">${form.formTitle}</h1>

              <table class="table table-bordered">
                <tbody>

                <tr>
                  <th class="col-2 table-light">기안부서</th>
                  <td class="col-4">${userInfo.departmentName}</td>
                  <th class="col-2 table-light">기안자</th>
                  <td class="col-4">${userInfo.name}</td>
                </tr>

                <tr>
                  <th class="table-light">기안일</th>
                  <td>${today}</td>
                  <th class="table-light">완료일</th>
                  <td></td>
                </tr>

                </tbody>
              </table>
              <span>※ 기안일은 [결재 요청] 버튼을 클릭한 날짜로 확정됩니다.</span>
              <br><br>

              <%-- 결재자 리스트 표시 --%>
              <c:choose>

                <c:when test="${edit eq true and detail.approverDTOList ne null and not empty detail.approverDTOList}">
                  <%-- 수정 모드: 기존 결재자 정보 표시 --%>
                  <c:import url="approver_list.jsp"/>

                  <%-- 기존 결재자 정보를 hidden input으로 추가 (JavaScript에서 읽기 위함) --%>
                  <c:forEach items="${detail.approverDTOList}" var="approver" varStatus="status">
                    <input type="hidden" name="approverDTOList[${status.index}].username" value="${approver.username}">
                    <input type="hidden" name="approverDTOList[${status.index}].approvalOrder" value="${approver.approvalOrder}">
                  </c:forEach>
                </c:when>

                <c:otherwise>
                  <%-- 신규 작성 모드: 빈 결재선 표시 --%>
                  <table class="table table-bordered">
                    <tbody>
                    <%-- 승인란 --%>
                    <tr id="approverPosition">
                        <th class="align-middle table-light col-2" rowspan="3">승인</th>
                        <div>
                            <td class="text-center" style="width: 20%;">&nbsp;</td>
                            <td class="text-center" style="width: 20%;"></td>
                            <td class="text-center" style="width: 20%;"></td>
                            <td class="text-center" style="width: 20%;"></td>
                        </div>
                    </tr>

                    <tr id="approverName" style="height: 90px;">
                        <div>
                            <td class="text-center align-middle"></td>
                            <td class="text-center align-middle"></td>
                            <td class="text-center align-middle"></td>
                            <td class="text-center align-middle"></td>
                        </div>
                    </tr>

                    <tr id="approvalDate">
                        <div>
                            <td class="text-center">&nbsp;</td>
                            <td class="text-center"></td>
                            <td class="text-center"></td>
                            <td class="text-center"></td>
                        </div>
                    </tr>
                    <%--  --%>
                    </tbody>
                  </table>
                  <%--  --%>
                  <div id="newApproverRow"></div>
                </c:otherwise>

              </c:choose>


              <%-- 서버 전송용 input hidden --%>
              <input type="hidden" name="username" value="${userInfo.username}">
              <input type="hidden" name="departmentId" value="${targetDept.departmentId}">
              <input type="hidden" name="approvalFormId" value="${form.approvalFormId}">
            </form>

            <form id="approvalContentByType">
              <%-- 2. 결재 양식 종류에 따라 다른 HTML을 뿌림 --%>
              <c:choose>
                <c:when test="${form.approvalFormId eq 1}">
                  <c:import url="/WEB-INF/views/approval/form_type/vacation.jsp"/>
                </c:when>
                <c:when test="${form.approvalFormId eq 2}">
                  <c:import url="/WEB-INF/views/approval/form_type/business_trip.jsp"/>
                </c:when>
                <c:when test="${form.approvalFormId eq 3}">
                  <c:import url="/WEB-INF/views/approval/form_type/draft.jsp"/>
                </c:when>
              </c:choose>
            </form>
            <%--  --%>

            <%-- 첨부파일 --%>
            <div class="form-group">
              <label for="file" class="form-label mt-4">첨부파일</label>

              <c:if test="${edit eq true and (detail.files ne null and not empty detail.files)}">

                <br>
                <c:forEach items="${detail.files}" var="el">
                  <p class="badge badge-pill badge-light">
                    ${el.oriName}.${el.extension}&nbsp;
                    <a class="btn-del-file" data-file-id="${el.fileId}" style="cursor: pointer"><i class="mdi mdi-close"></i></a>
                  </p>
                  &nbsp;
                </c:forEach>
                <br><br>

              </c:if>
              <%-- multiple 속성: 태그 하나로 여러 파일을 업로드할 수 있도록 함 --%>
              <input type="file" class="filepond" name="file" id="file" multiple>
            </div>

						<!--  -->
            </div>
					
					</div>
				</div>
				
				</div>
			</div>
			
			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>

<script src="https://unpkg.com/filepond/dist/filepond.js"></script>

</html>

