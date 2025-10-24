<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<a data-bs-toggle="modal" data-bs-target="#newApprovalModal" id="newApproval" class="btn btn-block btn-primary btn-pill mb-4 mb-xl-5">새 결재 진행</a>

<ul class="pb-2">
  <li class="d-block mb-4">
    <a href="/approval/list">전체</a>
  </li>
  <li class="d-block mb-4">
    <a href="/approval/list?listType=request">요청 목록</a>
  </li>
  <li class="d-block mb-4">
    <a href="/approval/list?listType=wait">대기 목록</a>
  </li>
  <li class="d-block mb-4">
    <a href="/approval/list?listType=storage">완료 목록</a>
  </li>
  <li class="d-block mb-4">
    <a href="/approval/list?listType=temp">임시보관함</a>
  </li>
</ul>
