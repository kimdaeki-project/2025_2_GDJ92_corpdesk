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
	
	<script defer type="text/javascript" src="/js/approval/approval.js"></script>
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

            <c:choose>

              <c:when test="${reqList ne null }">
                <!-- section1 - 결재 요청 목록 -->
                <section class="email-details pl-4 pr-4 pt-4">

                    <div class="d-flex justify-content-between align-items-center">
                        <h4 class="text-dark">결재 요청 목록</h4>
                        <a href="/approval/list?listType=request" class="btn btn-outline-primary pt-1 pb-1 pl-2 pr-2"><i class="mdi mdi-plus"></i></a>
                    </div>

                    <div class="email-details-content pl-0 pr-0">
                        <table class="table">

                          <thead>
                            <tr>
                              <th class="col-2">기안일</th>
                              <th class="col-6">제목</th>
                              <th class="col-1">파일</th>
                              <th class="col-2">부서</th>
                              <th class="col-1">상태</th>
                            </tr>
                          </thead>

                          <tbody>
                            <c:forEach items="${reqList }" var="el">
                                <tr class="approval-row" data-approval-id="${el.approvalId }" style="cursor: pointer;">
                                  <td>${fn:substring(el.createdAt, 0, 10) }</td>
                                  <td>${el.formTitle }</td>
                                  <td>${el.fileCount eq 0 ? '' : '<i class="mdi mdi-paperclip"></i>' += el.fileCount }</td>
                                  <td>${el.departmentName }</td>
                                  <td>
                                    <c:choose>
                                        <c:when test="${(el.status eq 'Y') or (el.status eq 'y') }">
                                            <span class="badge badge-info">승인</span>
                                        </c:when>
                                        <c:when test="${(el.status eq 'N') or (el.status eq 'n') }">
                                            <span class="badge badge-danger">반려</span>
                                        </c:when>
                                        <c:when test="${(el.status eq 'W') or (el.status eq 'w') }">
                                            <span class="badge badge-light">대기</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-outline-info">임시저장</span>
                                        </c:otherwise>
                                    </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                          </tbody>

                        </table>
                    </div>

                </section>
                <!--  -->

                <!-- section2 - 결재 대기 목록 -->
                <section class="email-details pl-4 pr-4 pt-4">

                    <div class="d-flex justify-content-between align-items-center">
                        <h4 class="text-dark">결재 대기 목록</h4>
                        <a href="/approval/list?listType=wait" class="btn btn-outline-primary pt-1 pb-1 pl-2 pr-2"><i class="mdi mdi-plus"></i></a>
                    </div>

                    <div class="email-details-content pl-0 pr-0">
                        <table class="table">

                          <thead>
                            <tr>
                              <th class="col-2">기안일</th>
                              <th class="col-4">제목</th>
                              <th class="col-1">파일</th>
                              <th class="col-2">기안부서</th>
                              <th class="col-2">기안자</th>
                              <th class="col-1">상태</th>
                            </tr>
                          </thead>

                          <tbody>
                            <c:forEach items="${waitList }" var="el">
                                <tr class="approval-row" data-approval-id="${el.approvalId }" style="cursor: pointer;">
                                  <td>${fn:substring(el.createdAt, 0, 10) }</td>
                                  <td>${el.formTitle }</td>
                                  <td>${el.fileCount eq 0 ? '' : '<i class="mdi mdi-paperclip"></i>' += el.fileCount }</td>
                                  <td>${el.departmentName }</td>
                                  <td>${el.username }</td>
                                  <td>
                                    <c:choose>
                                        <c:when test="${(el.status eq 'Y') or (el.status eq 'y') }">
                                            <span class="badge badge-info">승인</span>
                                        </c:when>
                                        <c:when test="${(el.status eq 'N') or (el.status eq 'n') }">
                                            <span class="badge badge-danger">반려</span>
                                        </c:when>
                                        <c:when test="${(el.status eq 'W') or (el.status eq 'w') }">
                                            <span class="badge badge-light">대기</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-outline-info">임시저장</span>
                                        </c:otherwise>
                                    </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                          </tbody>

                        </table>
                    </div>

                </section>
                <!--  -->

                <!-- section3 - 결재 완료 목록 -->
                <section class="email-details pl-4 pr-4 pt-4">

                    <div class="d-flex justify-content-between align-items-center">
                        <h4 class="text-dark">결재 완료 목록</h4>
                        <a href="/approval/list?listType=storage" class="btn btn-outline-primary pt-1 pb-1 pl-2 pr-2"><i class="mdi mdi-plus"></i></a>
                    </div>

                    <div class="email-details-content pl-0 pr-0">
                        <table class="table">

                            <thead>
                            <tr>
                                <th class="col-2">기안일</th>
                                <th class="col-4">제목</th>
                                <th class="col-1">파일</th>
                                <th class="col-2">기안부서</th>
                                <th class="col-2">기안자</th>
                                <th class="col-1">상태</th>
                            </tr>
                            </thead>

                            <tbody>
                            <c:forEach items="${storList }" var="el">
                                <tr class="approval-row" data-approval-id="${el.approvalId }" style="cursor: pointer;">
                                    <td>${fn:substring(el.createdAt, 0, 10) }</td>
                                    <td>${el.formTitle }</td>
                                    <td>${el.fileCount eq 0 ? '' : '<i class="mdi mdi-paperclip"></i>' += el.fileCount }</td>
                                    <td>${el.departmentName }</td>
                                    <td>${el.username }</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${(el.status eq 'Y') or (el.status eq 'y') }">
                                                <span class="badge badge-info">승인</span>
                                            </c:when>
                                            <c:when test="${(el.status eq 'N') or (el.status eq 'n') }">
                                                <span class="badge badge-danger">반려</span>
                                            </c:when>
                                            <c:when test="${(el.status eq 'W') or (el.status eq 'w') }">
                                                <span class="badge badge-light">대기</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-outline-info">임시저장</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>

                        </table>
                    </div>

                </section>
                <!--  -->

                <!-- section4 - 임시 보관함 -->
                <section class="email-details pl-4 pr-4 pt-4">

                    <div class="d-flex justify-content-between align-items-center">
                        <h4 class="text-dark">임시 보관함</h4>
                        <a href="/approval/list?listType=temp" class="btn btn-outline-primary pt-1 pb-1 pl-2 pr-2"><i class="mdi mdi-plus"></i></a>
                    </div>

                    <div class="email-details-content pl-0 pr-0">
                        <table class="table">

                            <thead>
                            <tr>
                                <th class="col-2">기안일</th>
                                <th class="col-6">제목</th>
                                <th class="col-1">파일</th>
                                <th class="col-2">부서</th>
                                <th class="col-1">상태</th>
                            </tr>
                            </thead>

                            <tbody>
                            <c:forEach items="${tempList }" var="el">
                                <tr class="approval-row" data-approval-id="${el.approvalId }" style="cursor: pointer;">
                                    <td>${fn:substring(el.createdAt, 0, 10) }</td>
                                    <td>${el.formTitle }</td>
                                    <td>${el.fileCount eq 0 ? '' : '<i class="mdi mdi-paperclip"></i>' += el.fileCount }</td>
                                    <td>${el.departmentName }</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${(el.status eq 'Y') or (el.status eq 'y') }">
                                                <span class="badge badge-info">승인</span>
                                            </c:when>
                                            <c:when test="${(el.status eq 'N') or (el.status eq 'n') }">
                                                <span class="badge badge-danger">반려</span>
                                            </c:when>
                                            <c:when test="${(el.status eq 'W') or (el.status eq 'w') }">
                                                <span class="badge badge-light">대기</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-outline-info">임시저장</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>

                        </table>
                    </div>

                </section>
                <!--  -->
            </c:when>

            <c:when test="${list ne null
                            and (listType eq 'request'
                                 or listType eq 'temp')}">
                <!-- 결재 요청 목록 / 임시 저장 목록 -->
                <section class="email-details pl-4 pr-4 pt-4">

                    <div class="d-flex justify-content-between align-items-center">
                        <h4 class="text-dark">${listType eq 'request' ? '결재 요청 목록' : '임시 보관함'}</h4>
                    </div>

                    <div class="email-details-content pl-0 pr-0">
                        <table class="table">

                            <thead>
                            <tr>
                                <th class="col-2">기안일</th>
                                <th class="col-6">제목</th>
                                <th class="col-1">파일</th>
                                <th class="col-2">부서</th>
                                <th class="col-1">상태</th>
                            </tr>
                            </thead>

                            <tbody>
                              <c:forEach items="${list.content}" var="el">
                                  <tr class="approval-row" data-approval-id="${el.approvalId }" style="cursor: pointer;">
                                      <td>${fn:substring(el.createdAt, 0, 10) }</td>
                                      <td>${el.formTitle }</td>
                                      <td>${el.fileCount eq 0 ? '' : '<i class="mdi mdi-paperclip"></i>' += el.fileCount }</td>
                                      <td>${el.departmentName }</td>
                                      <td>
                                          <c:choose>
                                              <c:when test="${(el.status eq 'Y') or (el.status eq 'y') }">
                                                  <span class="badge badge-info">승인</span>
                                              </c:when>
                                              <c:when test="${(el.status eq 'N') or (el.status eq 'n') }">
                                                  <span class="badge badge-danger">반려</span>
                                              </c:when>
                                              <c:when test="${(el.status eq 'W' or (el.status eq 'w')) }">
                                                  <span class="badge badge-light">대기</span>
                                              </c:when>
                                              <c:otherwise>
                                                  <span class="badge badge-outline-info">임시저장</span>
                                              </c:otherwise>
                                          </c:choose>
                                      </td>
                                  </tr>
                              </c:forEach>
                            </tbody>

                        </table>
                    </div>

                </section>
                <!--  -->
            </c:when>

              <c:when test="${list ne null
                            and (listType eq 'wait'
                                 or listType eq 'storage')}">
                <!-- 결재 대기 목록 / 결재 문서함 -->
                <section class="email-details pl-4 pr-4 pt-4">

                    <div class="d-flex justify-content-between align-items-center">
                        <h4 class="text-dark">${listType eq 'wait' ? '결재 대기 목록': '결재 완료 목록'}</h4>
                    </div>

                    <div class="email-details-content pl-0 pr-0">
                        <table class="table">

                            <thead>
                            <tr>
                                <th class="col-2">기안일</th>
                                <th class="col-4">제목</th>
                                <th class="col-1">파일</th>
                                <th class="col-2">기안부서</th>
                                <th class="col-2">기안자</th>
                                <th class="col-1">상태</th>
                            </tr>
                            </thead>

                            <tbody>
                            <c:forEach items="${list.content}" var="el">
                                <tr class="approval-row" data-approval-id="${el.approvalId }" style="cursor: pointer;">
                                    <td>${fn:substring(el.createdAt, 0, 10) }</td>
                                    <td>${el.formTitle }</td>
                                    <td>${el.fileCount eq 0 ? '' : '<i class="mdi mdi-paperclip"></i>' += el.fileCount }</td>
                                    <td>${el.departmentName }</td>
                                    <td>${el.username }</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${(el.status eq 'Y') or (el.status eq 'y') }">
                                                <span class="badge badge-info">승인</span>
                                            </c:when>
                                            <c:when test="${(el.status eq 'N') or (el.status eq 'n') }">
                                                <span class="badge badge-danger">반려</span>
                                            </c:when>
                                            <c:when test="${(el.status eq 'W') or (el.status eq 'w') }">
                                                <span class="badge badge-light">대기</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-outline-info">임시저장</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>

                        </table>
                    </div>

                </section>
                <%--  --%>
              </c:when>

          </c:choose>

          <%-- 페이징 --%>
          <c:import url="paging_bar.jsp"/>

					</div>
				</div>
				
				</div>
			</div>
			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>
</html>