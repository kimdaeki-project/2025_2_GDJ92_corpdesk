<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Chat Page</title>
<c:import url="/WEB-INF/views/include/head.jsp" />
<script
	src="https://cdn.jsdelivr.net/npm/sockjs-client/dist/sockjs.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/stompjs/lib/stomp.min.js"></script>
<link rel="stylesheet" href="/css/chat/chat_page.css">
</head>
<body>
	<sec:authentication property="principal.username" var="user" />

	<input type="hidden" class="username" value="${user}">
	<input type="hidden" class="roomData" value='${roomData }'>
	<div class="card card-default chat-right-sidebar">
	 <div class="card-header">
        <h2 class="roomTitle"></h2>
        

        <div class="dropdown">
          <div class="dropdown">
            <a class="dropdown-toggle icon-burger-mini" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
            </a>

            <div class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownMenuLink">
              <a class="dropdown-item invite"data-toggle="modal" data-target="#inviteRoomStep1" href="javascript:void(0)">초대하기</a>
              <a class="dropdown-item participant-list" data-toggle="modal" data-target="#participantModal" href="javascript:void(0)">참여자목록</a>
            </div>
          </div>
        </div>
      </div>

	<div class="card-body pb-0 messageContainer">
		<div id="chat-list">
			

			
		</div>
	</div>
	<!-- Chat Footer -->
	<div class="chat-footer "style="background-color:#f0f1f5;" >
	 <div class="d-flex justify-content-end mb-2 align-items-center" style="margin-right:20px; ">
    <span class="mr-2" id="translateStatusLabel">번역 비활성화</span>
    <div class="dropdown">
      <button class="btn btn-outline-primary dropdown-toggle" type="button"
              id="translateToggle" data-toggle="dropdown"
              aria-haspopup="true" aria-expanded="false">
        번역 설정
      </button>
      <div class="dropdown-menu dropdown-menu-right" aria-labelledby="translateToggle">
        <a class="dropdown-item set-lang" data-lang="EN-US" href="#">영어 (English)</a>
        <a class="dropdown-item set-lang" data-lang="JA" href="#">일본어 (日本語)</a>
        <a class="dropdown-item set-lang" data-lang="ZH" href="#">중국어 (中文)</a>
        <a class="dropdown-item set-lang" data-lang="ES" href="#">스페인어 (Español)</a>
        <a class="dropdown-item set-lang" data-lang="KO" href="#">한국어</a>
        <div class="dropdown-divider"></div>
        <a class="dropdown-item" href="#" id="deacivateTranslation">번역 비활성화</a>
      </div>
    </div>
  </div>
	
	
		<form id="chatForm">
			<div class="input-group input-group-chat">
				<textarea class="form-control messageInput" id="messageInput"
					 aria-label="Text input with dropdown button"></textarea>
				<div class="input-group-append">
					<button type="button" id="sendBtn" class="btn btn-primary">
						메시지 전송</button>
				</div>
			</div>
		</form>
	</div>
	</div>
	
	
	<!-- 참여자 목록 모달 -->
<div class="modal fade" id="participantModal" tabindex="-1" role="dialog" aria-labelledby="participantModal"
  aria-hidden="true">
  <div class="modal-dialog modal-sm" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalSmallTitle">참여자 목록</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">×</span>
        </button>
      </div>
      <div class="modal-body participant-content">
       <ul class="list-group">
       </ul>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-danger btn-pill" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>

<!-- Step 1: 초대자 선택 모달 -->
<div class="modal fade" id="inviteRoomStep1" tabindex="-1" role="dialog" aria-labelledby="step1Label" aria-hidden="true">
  <div class="modal-dialog modal-dialog-scrollable" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="step1Label">초대대상 선택</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="닫기">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <!-- 검색창 -->
        <div class="input-group mb-3">
          <input type="text" class="form-control" id="searchUserInput" placeholder="이름 검색">
          
        </div>

        <!-- 사원 목록 (스크롤 가능) -->
        <ul class="list-group" id="participantList" style="max-height: 300px; overflow-y: auto;">
          <c:forEach items="${employeeList}" var="employee">
            <li class="list-group-item d-flex align-items-center" data-username="${employee.username}">
             <!-- 추후 사진 바꿔주면됨  -->
              <img src="${employee.imgPath}" class="rounded-circle mr-3" style="width:40px; height:40px;">
              <div class="flex-fill">
                <strong class="employeeName">${employee.name}</strong><br>
                <small>${employee.departmentName} ${employee.positionName}</small>
              </div>
              <input type="checkbox" value="${employee.username}" class="participant-checkbox">
            </li>
          </c:forEach>
        </ul>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
        <button type="button" class="btn btn-primary" id="nextStepBtn">초대 하기</button>
      </div>
    </div>
  </div>
</div>

<!-- Step 2: 현재 방이 1대1인데 초대할 경우 제목을 설정해줘야함-->
<div class="modal fade" id="inviteRoomStep2" tabindex="-1" role="dialog" aria-labelledby="step2Label" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="step2Label">채팅방 제목 설정</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="닫기">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <label for="roomTitle">채팅방 제목</label>
        <input type="text" id="roomTitle" class="form-control" placeholder="채팅방 이름 입력">
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
        <button type="button" class="btn btn-success" id="inviteRoomConfirmBtn">초대</button>
      </div>
    </div>
  </div>
</div>
	
	
	
	
	
	

	<script type="text/javascript" src="/js/chat/chatPage.js"></script>
	<!-- 내용 끝 -->


	<c:import url="/WEB-INF/views/include/body_wrapper_end2.jsp" />
</html>