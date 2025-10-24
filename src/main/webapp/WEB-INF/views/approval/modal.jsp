<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="modal fade" id="newApprovalModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg modal-dialog-centered">
    <div class="modal-content">

      <div class="modal-header">
        <h5 class="modal-title fs-5" id="exampleModalLabel">결재 양식 선택</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>

      <div class="modal-body d-flex justify-content-between">

        <!-- left col -->
        <div class="card mb-4 p-0 w-75">

          <div class="card-body p-4">
            <ul class="list-unstyled">
              <c:forEach items="${formList }" var="el">
                <li class="text-start">
                  <a data-approval-form-id="${el.approvalFormId }" class="approval-form-name btn px-0 mr-3 text-dark">${el.formTitle }</a>
                </li>
              </c:forEach>
            </ul>
          </div>

        </div>

        <!-- right col -->
        <div class="card mb-4 p-0 ml-2 w-100">

          <h5 class="card-title pt-4 px-4">상세정보</h5>

          <div class="card-body p-4">
            <ul class="list-unstyled">
              <li class="d-flex py-2 text-dark">
                <b class="col-5 p-0">제목</b>
                <p id="approvalTitle"></p>
              </li>
              <li class="d-flex py-2 text-dark">
                <b class="col-5 p-0">기안부서</b>
                <p>${userDetail.departmentName}</p>
              </li>
              <li class="d-flex py-2 text-dark align-items-center">
                <label class="col-5 p-0">결재부서</label>
                <div class="form-group">
                  <select class="form-control" id="departmentId">
                    <c:forEach items="${departmentList }" var="el">
                      <option value="${el.departmentId}">${el.departmentName}</option>
                    </c:forEach>
                  </select>
                </div>
              </li>
            </ul>
          </div>

        </div>

      </div>

      <div class="modal-footer">
        <button type="button" id="formCheck" class="btn btn-info">확인</button>
        <button type="button" class="btn btn-light" data-bs-dismiss="modal">취소</button>
      </div>

    </div>
  </div>
</div>
