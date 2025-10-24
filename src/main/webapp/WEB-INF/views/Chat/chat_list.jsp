<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>채팅 목록</title>
	<c:import url="/WEB-INF/views/include/head.jsp"/>
<link rel="stylesheet" href="/css/chat/chat_list.css">

	
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/> 

	<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

	<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

		<c:import url="/WEB-INF/views/include/header.jsp"/>
	
		<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
			<!-- 내용 시작 -->
			<sec:authentication property="principal.username" var="user" />
			<input type="hidden" class="username" value="${user }">
	
<div class = "row no-gutters justify-content-center">
	<div class ="col-lg-4 col-xxl-3">
		<div class = "card card-default chat-left-sidebar">
				<div class="card-header">
			<h2 >연락처 목록</h2>
			 <div class="input-group mb-3">
          <input type="text" class="form-control" id="searchContactList" placeholder="이름 검색">
         </div>
				  <!-- 그룹 채팅방 생성하기 버튼 -->
		  
<span class="mdi mdi-account-multiple-plus ml-auto" style="cursor: pointer; font-size: 30px;" data-toggle="modal" data-target="#createRoomStep1">
</span> 

		</div>
			 <ul class="list-group" id="contactList" data-simplebar style="height: 562px;">
        	  <c:forEach items="${contactList}" var="employee">
          
            <li class="list-group-item-action d-flex align-items-center employeeListOne" data-employee="${employee.username}" style="padding: 10px;">
             <!-- 추후 사진 바꿔주면됨  -->
              <img src="${employee.imgPath}" class="rounded-circle mr-3" style="width:60px; height:60px;">
              <div class="flex-fill">
                <strong class="employeeName">${employee.name}</strong><br>
                <small>${employee.departmentName} ${employee.positionName}</small>
              </div>
            </li>
     
          </c:forEach>
        </ul>
		</div>
	</div>
			

<!-- 채팅방 목록 -->

<div class="col-lg-7 col-xxl-5">
	<div class="card card-default chat-right-sidebar">
		<div class="card-header">
			<h2 style="margin: 0 auto ">채팅방 목록</h2>

		</div>
		<ul class="card-body px-0 list-group chatList" data-simplebar style="height: 677px;">
			<c:forEach items="${roomList}" var="room">
				<li class="mb-4 px-5 py-2 chatListOne list-group-item-action "
					data-roomId="${room.chatRoomId }"
					data-unreadCount="${room.unreadCount }">
					<div class="media media-message">
						<div class="position-relative mr-3">
							<img class="rounded-circle" src="${room.imgPath}"
								alt="User Image" style="width: 70px; height:70px;">
						</div>

						<div class="media-body">
							<div class="message-contents">
								<span
									class="d-flex justify-content-between align-items-center mb-1">
									<span class="username text-dark">${room.chatRoomTitle}</span> <span
									class=""> <c:choose>
											<c:when test="${room.unreadCount ne 0 }">
												<span class="badge badge-secondary unreadCount">
													${room.unreadCount } </span>
											</c:when>
											<c:otherwise>
												<span class="unreadCount"> </span>
											</c:otherwise>
										</c:choose> <span class="state text-smoke last-msg-time"
										data-lastMessageTime="${room.lastMessageTime}"><em></em></span>
								</span>
								</span>

								<p class="last-msg text-smoke">${room.chatRoomLastMessage}</p>
							</div>
						</div>
							<div class="dropdown kebab-menu" style="margin-left:10px;">
								<a class="dropdown-toggle icon-burger-mini kebab-menu" href="#"
									role="button" id="dropdownMenuLink" data-toggle="dropdown"
									aria-haspopup="true" aria-expanded="false"> </a>

								<div class="dropdown-menu  dropdown-menu-right"
									aria-labelledby="dropdownMenuLink">
									<a class="dropdown-item room-out" href="javascript:void(0)">채팅방 나가기</a>
								</div>
							</div>
					</div>
				</li>
			</c:forEach>
		</ul>
	</div>
</div>
</div>

<c:forEach items ="${notificationList}" var="notification">

	<p>${notification.imgPath}</p>
	<p>${notification.viewName}</p>
	<p>${notification.messageContent}</p>
	<p>${notification.sentAt}</p>
	<p>${notification.chatRoomId}</p>


</c:forEach>


<!-- Step 1: 참여자 선택 모달 -->
<div class="modal fade" id="createRoomStep1" tabindex="-1" role="dialog" aria-labelledby="step1Label" aria-hidden="true">
  <div class="modal-dialog modal-dialog-scrollable" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="step1Label">대화상대 선택</h5>
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
          <c:forEach items="${contactList}" var="employee">
          <c:if test="${employee.username ne user }">
            <li class="list-group-item d-flex align-items-center">
             <!-- 추후 사진 바꿔주면됨  -->
              <img src="${employee.imgPath}" class="rounded-circle mr-3" style="width:40px; height:40px;">
              <div class="flex-fill">
                <strong class="employeeName">${employee.name}</strong><br>
                <small>${employee.departmentName} ${employee.positionName}</small>
              </div>
              <input type="checkbox" value="${employee.username}" class="participant-checkbox">
            </li>
            </c:if>
          </c:forEach>
        </ul>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
        <button type="button" class="btn btn-primary" id="nextStepBtn">다음</button>
      </div>
    </div>
  </div>
</div>



<!-- Step 2: 방 제목 입력 모달 -->
<div class="modal fade" id="createRoomStep2" tabindex="-1" role="dialog" aria-labelledby="step2Label" aria-hidden="true">
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
        <button type="button" class="btn btn-success" id="createRoomConfirmBtn">생성</button>
      </div>
    </div>
  </div>
</div>











				
				<script src="https://cdn.jsdelivr.net/npm/sockjs-client/dist/sockjs.min.js"></script>
  			    <script src="https://cdn.jsdelivr.net/npm/stompjs/lib/stomp.min.js"></script>
				<script src="/js/chat/chatList.js" ></script>
				
				
			
			
			
			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end2.jsp"/>
</html>