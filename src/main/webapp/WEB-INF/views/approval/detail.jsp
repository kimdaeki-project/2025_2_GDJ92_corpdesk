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

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>


    <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/html2canvas/1.4.1/html2canvas.min.js"></script>

    <script defer type="text/javascript" src="/js/approval/approval_detail.js"></script>

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

        #pdf-area, #pdf-area * {
            font-family: 'Noto Sans KR', 'Malgun Gothic', 'Apple SD Gothic Neo', sans-serif !important;
        }

        /* 테이블 내 텍스트도 강제 적용 */
        #pdf-area table,
        #pdf-area th,
        #pdf-area td,
        #pdf-area p,
        #pdf-area h1 {
            font-family: 'Noto Sans KR', 'Malgun Gothic', 'Apple SD Gothic Neo', sans-serif !important;
        }
    </style>
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/>

<!-- Modal -->
<c:import url="modal.jsp"/>

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
            <input type="hidden" id="approvalIdData" value="${detail.approvalId}">
            <%-- 양식 헤더 --%>
						<div class="d-flex justify-content-between" id="btnBox" data-approval-id="${detail.approvalId}" data-approver-id="${approverInfo.approverId}">
              <div>
                <c:choose>

                  <c:when test="${detail.username eq userInfo.username}">
                    <c:if test="${detail.status eq 't' or detail.status eq 'T'}">
                      <form action="/approval/${detail.approvalId}/edit" method="GET" class="d-inline">
                        <input type="hidden" name="approvalFormId" value="${detail.approvalFormId}">
                        <input type="hidden" name="departmentId" value="${detail.departmentId}">
                        <button class="btn btn-info mr-1 btn-action" id="btnEdit">수정</button>
                      </form>
                    </c:if>

                    <c:choose>
                      <c:when test="${detail.processed eq true}">
                        <span class="d-inline-block" tabindex="0" data-toggle="tooltip" title="이미 승인이 진행 중이거나 완료된 결재는 삭제할 수 없습니다.">
                          <button type="button" class="btn btn-outline-danger mr-1 btn-action" style="pointer-events: none;" id="btnDelete" disabled>
                            삭제
                          </button>
                        </span>
                      </c:when>
                      <c:otherwise>
                        <button type="button" class="btn btn-outline-danger mr-1 btn-action" id="btnDelete">삭제</button>
                      </c:otherwise>
                    </c:choose>

                  </c:when>

                  <c:otherwise>
                    <button type="button" class="btn btn-info mr-1 btn-action" id="btnApproval">승인</button>
                    <button type="button" class="btn btn-outline-danger mr-1 btn-action" id="btnReject">반려</button>
                  </c:otherwise>

                </c:choose>
                <button type="button" class="btn btn-light" id="btnCancel" onclick="history.back()">취소</button>
              </div>

              <div>
                <button type="button" id="pdf-btn" class="btn btn-link"><i class="mdi mdi-printer"></i> 인쇄</button>
              </div>
						</div>
						<hr>
            <%--  --%>

            <%-- 양식 내용 --%>
            <div class="col-lg-7" id="pdf-area">
            <form id="approvalContentCommon">

              <%-- 1. 공통 양식 --%>
              <h1 class="text-center p-4">${detail.formTitle}</h1>

              <table class="table table-bordered">
                <tbody>

                <tr>
                  <th class="col-2 table-light">기안부서</th>
                  <td class="col-4">${detail.departmentName}</td>
                  <th class="col-2 table-light">기안자</th>
                  <td class="col-4">${detail.name}</td>
                </tr>

                <tr>
                  <th class="table-light">기안일</th>
                  <td>${fn:substring(detail.createdAt, 0, 10)}</td>
                  <th class="table-light">완료일</th>
                  <td>
                    <c:if test="${detail.status eq 'N' or detail.status eq 'Y'}">
                      ${fn:substring(detail.updatedAt, 0, 10)}
                    </c:if>
                  </td>
                </tr>

                </tbody>
              </table>
              <br>

              <%-- 결재자 리스트 --%>
              <c:import url="approver_list.jsp"/>

            </form>

            <form id="approvalContentByType">
              <%-- 2. 결재 양식 종류에 따라 다른 HTML을 뿌림 --%>
              <c:choose>
                <c:when test="${detail.approvalFormId eq 1}">
                  <c:import url="/WEB-INF/views/approval/form_type/vacation.jsp"/>
                </c:when>
                <c:when test="${detail.approvalFormId eq 2}">
                  <c:import url="/WEB-INF/views/approval/form_type/business_trip.jsp"/>
                </c:when>
                <c:when test="${detail.approvalFormId eq 3}">
                  <c:import url="/WEB-INF/views/approval/form_type/draft.jsp"/>
                </c:when>
              </c:choose>
            </form>

            <c:if test="${detail.files ne null}">
              <div class="form-group">
                <label for="file" class="form-label mt-4 font-weight-bold">첨부파일 (${fn:length(detail.files)})</label>
                <div class="card">
                  <div class="list-group list-group-flush">
                    <c:forEach items="${detail.files}" var="el">
                      <%-- 확장자별 색상 설정 --%>
                      <c:choose>
                        <c:when test="${el.extension eq 'pdf'}"><c:set var="bgColor" value="bg-danger"/></c:when>
                        <c:when test="${el.extension eq 'doc' or el.extension eq 'docx'}"><c:set var="bgColor" value="bg-primary"/></c:when>
                        <c:when test="${el.extension eq 'xls' or el.extension eq 'xlsx'}"><c:set var="bgColor" value="bg-success"/></c:when>
                        <c:when test="${el.extension eq 'jpg' or el.extension eq 'jpeg' or el.extension eq 'png' or el.extension eq 'gif'}"><c:set var="bgColor" value="bg-info"/></c:when>
                        <c:when test="${el.extension eq 'zip' or el.extension eq 'rar' or el.extension eq '7z'}"><c:set var="bgColor" value="bg-warning"/></c:when>
                        <c:otherwise><c:set var="bgColor" value="bg-secondary"/></c:otherwise>
                      </c:choose>

                      <a href="/file/${approvalPath}/${el.fileId}" class="list-group-item list-group-item-action d-flex align-items-center">
                        <div class="d-flex align-items-center justify-content-center ${bgColor} text-white rounded mr-3" style="width: 40px; height: 40px; font-size: 12px; flex-shrink: 0;">
                          <strong>${fn:toUpperCase(el.extension)}</strong>
                        </div>
                        <div class="flex-grow-1 text-truncate">
                          <div class="font-weight-medium text-dark">${el.oriName}.${el.extension}</div>
                        </div>
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#6c757d" stroke-width="2" class="flex-shrink-0">
                          <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4M7 10l5 5 5-5M12 15V3"/>
                        </svg>
                      </a>
                    </c:forEach>
                  </div>
                </div>
              </div>
            </c:if>

            </div>

<%--            ${detail}--%>
            <%--  --%>
						<!--  -->

					</div>
				</div>

				</div>
			</div>
			
			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>

