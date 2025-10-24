<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<link rel="stylesheet" href="/css/header/header.css">
<!-- Header -->
<header class="main-header" id="header">
	<nav class="navbar navbar-expand-lg navbar-light" id="navbar">
		<!-- Sidebar toggle button -->
		<button id="sidebar-toggler" class="sidebar-toggle">
			<span class="sr-only">Toggle navigation</span>
		</button>

		<span class="page-title"> <!-- TODO 여기에 카테고리명 입력 --> <span>${cat }</span>
		</span>

		<div class="navbar-right ">

			<ul class="nav navbar-nav">
				<!-- Offcanvas -->
				<li class="custom-dropdown">
					<button class="notify-toggler custom-dropdown-toggler">
						<i class="mdi mdi-bell-outline icon"></i> <span
							class="badge badge-xs rounded-circle all-count"
							data-allCount="${(msgNotificationList != null ? msgNotificationList.size() : 0) + (approvalNotificationList != null ? approvalNotificationList.size() : 0)}"><c:if
								test="${(msgNotificationList != null ? msgNotificationList.size() : 0) + (approvalNotificationList != null ? approvalNotificationList.size() : 0) > 0}">
                 ${(msgNotificationList != null ? msgNotificationList.size() : 0) + (approvalNotificationList != null ? approvalNotificationList.size() : 0)}
            </c:if></span>
					</button>
					<div class="dropdown-notify">

						<!-- 알림 헤더 -->
						<header>
							<div class="nav nav-underline" id="nav-tab" role="tablist">
								<!-- <a class="nav-item nav-link active all-count" id="all-tabs" data-toggle="tab" href="#all" role="tab" aria-controls="nav-home"
                  aria-selected="true" data-allCount="${(msgNotificationList != null ? msgNotificationList.size() : 0) 
                   + (approvalNotificationList != null ? approvalNotificationList.size() : 0)}">전체 (${(msgNotificationList != null ? msgNotificationList.size() : 0) + (approvalNotificationList != null ? approvalNotificationList.size() : 0)})</a> TODO 추후 사용시 () 안에 실제 테이터 넣기 -->
								<a class="nav-item nav-link message-count active"
									id="message-tab" data-toggle="tab" href="#message" role="tab"
									aria-controls="nav-profile" aria-selected="false"
									data-count="${msgNotificationList != null ? msgNotificationList.size() : 0}">메시지
									(${msgNotificationList != null ? msgNotificationList.size() : 0})</a>
								<!-- TODO 추후 사용시 () 안에 실제 테이터 넣기 -->
								<a class="nav-item nav-link approval-count" id="approval-tab"
									data-toggle="tab" href="#approval" role="tab"
									aria-controls="nav-contact" aria-selected="false"
									data-count="${approvalNotificationList != null ? approvalNotificationList.size() : 0}">결재
									(${approvalNotificationList != null ? approvalNotificationList.size() : 0})</a>
								<!-- TODO 추후 사용시 () 안에 실제 테이터 넣기 -->
							</div>
						</header>
						<!--  -->

						<!-- 알림 내용 -->
						<!-- TODO 알림 기능 화면 구현시, 아래의 샘플들을 참고하여 구현 -->
						<div class="" data-simplebar style="height: 325px;">
							<div class="tab-content" id="myTabContent">
								<!-- 메시지 알림 -->
								<div class="tab-pane fade show active" id="message"
									role="tabpanel" aria-labelledby="message-tab">
									<c:forEach items="${msgNotificationList}" var="notification">
										<div
											class="media media-sm p-4 mb-0 notification messageNotification"
											data-roomId="${notification.chatRoomId}"
											style="cursor: pointer;">
											<div class="media-sm-wrapper">

												<img src="${notification.imgPath}" alt="User Image"
													style="width: 50px; height: 50px;">
											</div>
											<div class="media-body">
												<span class="title mb-0">${notification.viewName}</span> <span
													class="discribe"> ${notification.messageContent}</span> <span
													class="time"> <time class="notificationTime"
														data-notificationTime="${notification.sentAt}"></time>
												</span>
											</div>
										</div>
									</c:forEach>
									<div class="text-right mt-2 mb-2 pr-3">
										<button type="button"
											class="btn btn-outline-secondary btn-sm mark-all-msg-read">모두읽음</button>
									</div>
									
								</div>
								<!--  -->

								<!-- 결재 알림 -->
								<div class="tab-pane fade" id="approval" role="tabpanel"
									aria-labelledby="contact-tab">
									<c:forEach items="${approvalNotificationList}"
										var="notification">
										<div
											class="media media-sm p-4 mb-0 notification approvalNotification"
											data-approvalId="${notification.relatedId}"
											style="cursor: pointer;">
											<div class="media-sm-wrapper bg-info-dark">
												<i class="mdi mdi-bell"></i>
											</div>
											<div class="media-body">
												<span class="title mb-0">${notification.title}</span> <span
													class="discribe">${notification.content}</span> <span
													class="time"> <time class="notificationTime"
														data-notificationTime="${notification.createdAt}"></time>
												</span>

											</div>
										</div>


									</c:forEach>
									
								</div>
								<!--  -->
							</div>
						</div>
						<!--  -->

					</div>
				</li>
				<!-- User Account -->
				<li class="dropdown user-menu">
					<button class="dropdown-toggle nav-link" data-toggle="dropdown">
            <c:set var="hasProfileImg" value="${profileImgName ne null and profileImgName ne '' and profileImgExt ne null and profileImgExt ne ''}"></c:set>

            <c:choose>
              <c:when test="${hasProfileImg}">
                <img src="/files/${profileImgPath}/${profileImgName}.${profileImgExt}" class="user-image rounded-circle" style="width: 40px; height: 40px; object-fit: cover;" alt="User Image" />
              </c:when>
              <c:otherwise>
						    <img src="/images/default_profile.jpg" class="user-image rounded-circle" style="width: 40px; height: 40px; object-fit: cover;" alt="User Image" />
              </c:otherwise>
            </c:choose>
						<span class="d-none d-lg-inline-block">${profileName ne null ? profileName : ''}${profilePosition ne null ? ' ' += profilePosition : ''}</span>
					</button>
					<ul class="dropdown-menu dropdown-menu-right">
						<li><a class="dropdown-link-item" href="/employee/detail">
								<!-- TODO 추후 사용시 href 변경 --> <i class="mdi mdi-account-outline"></i>
								<span class="nav-text">내 정보</span>
						</a></li>

						<li class="dropdown-footer"><a class="dropdown-link-item"
							href="/logout"> <!-- TODO 추후 사용시 href 변경 --> <i
								class="mdi mdi-logout"></i> 로그아웃
						</a></li>
					</ul>
				</li>
			</ul>
		</div>
	</nav>


</header>