<%@ page language="java" contentType="text/html; charset=UTF-8"
				 pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<aside class="left-sidebar sidebar-light" id="left-sidebar">
	<div id="sidebar" class="sidebar sidebar-with-footer">
		<!-- Aplication Brand -->
		<div class="app-brand">
			<a href="/dashboard"> <img src="/images/logo.png" alt="Corpdesk">
				<span class="brand-name">Corpdesk</span>
			</a>
		</div>
		<!-- begin sidebar scrollbar -->
		<div class="sidebar-left" data-simplebar style="height: 100%;">
			<!-- sidebar menu -->
			<ul class="nav sidebar-inner" id="sidebar-menu">

				<!-- 홈 -->
				<li>
					<a class="sidenav-item-link" href="/dashboard">
						<i class="mdi mdi-home"></i> <span class="nav-text">홈</span>
					</a>
				</li>

				<sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_HR')">
					<li>
						<a class="sidenav-item-link" href="/admin">
							<i class="mdi mdi-account-key"></i> <span class="nav-text">권한</span>
						</a>
					</li>
				</sec:authorize>

				<li class="section-title pt-0 pb-0">
					<hr>
				</li>
				<!--  -->

				<!-- 메신저, 메일, 전자결재, 캘린더, 게시판 -->
				<li>
					<a class="sidenav-item-link" href="/chat/room/list">
						<i class="mdi mdi-wechat"></i> <span class="nav-text">메신저</span>
					</a>
				</li>

				<li>
					<a class="sidenav-item-link" href="/email/received">
						<i class="mdi mdi-email"></i> <span class="nav-text">메일</span>
					</a>
				</li>

				<li>
					<a class="sidenav-item-link" href="/approval/list">
						<i class="mdi mdi-pencil-box-outline"></i> <span class="nav-text">전자결재</span>
					</a>
				</li>

				<li>
					<a class="sidenav-item-link" href="/calendar/list">
						<i class="mdi mdi-calendar-check"></i> <span class="nav-text">캘린더</span>
					</a>
				</li>

				<li>
					<a class="sidenav-item-link" href="/board/notice">
						<i class="mdi mdi-clipboard-text-outline"></i> <span class="nav-text">게시판</span>
					</a>
				</li>
				<!--  -->

				<li class="section-title pt-0 pb-0">
					<hr>
				</li>

				<!-- 근태, 휴가, 일정 -->
				<li>
					<a class="sidenav-item-link" href="/attendance/list">
						<i class="mdi mdi-account-clock"></i> <span class="nav-text">근태</span>
					</a>
				</li>

				<li>
					<a class="sidenav-item-link" href="/vacation/list?listType=personal">
						<i class="mdi mdi-beach"></i> <span class="nav-text">휴가</span>
					</a>
				</li>

				<li>
					<a class="sidenav-item-link" href="/personal-schedule/list">
						<i class="mdi mdi-alarm-check"></i> <span class="nav-text">일정</span>
					</a>
				</li>

				<sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_HR')">
					<!--  -->

					<li class="section-title pt-0 pb-0">
						<hr>
					</li>

					<!-- 조직관리, 인사관리 -->
				<li class="has-sub">
					<a class="sidenav-item-link" href="javascript:void(0)"
						 data-toggle="collapse" data-target="#hr" aria-expanded="false">
						<i class="mdi mdi-account-group"></i> <span class="nav-text">조직관리</span>
						<b class="caret"></b>
					</a>

					<ul class="collapse" id="hr" data-parent="#sidebar-menu">
						<div class="sub-menu">

							<li>
								<a class="sidenav-item-link" href="/organization/list"> <!-- TODO href 수정 -->
										<span class="nav-text">조직설계</span>
								</a>
							</li>

							<li>
								<a class="sidenav-item-link" href="/position/list">
									<span class="nav-text">직위체계</span>
								</a>
							</li>

						</div>
					</ul>
				</li>

				<li>
					<a class="sidenav-item-link" href="/employee/list">
						<i class="mdi mdi-account-details"></i> <span class="nav-text">인사관리</span>
					</a>
				</li>
				<li>
					<a class="sidenav-item-link" href="/salary">
						<i class="mdi mdi-currency-krw"></i> <span class="nav-text">급여관리</span>
					</a>
				</li>
				<li>
					<a class="sidenav-item-link" href="/stats">
						<i class="mdi mdi-chart-areaspline"></i> <span class="nav-text">통계</span>
					</a>
				</li>
				</sec:authorize>

			</ul>
		</div>
	</div>
</aside>

<script>
  document.addEventListener('DOMContentLoaded', function() {
    var currentPath = window.location.pathname;

    if (currentPath === '/' || currentPath === '/index' || currentPath === '/index.html') {
      currentPath = '/dashboard';
    }

    var menuLinks = document.querySelectorAll('#sidebar-menu a.sidenav-item-link');

    var activeItems = document.querySelectorAll('#sidebar-menu li.active');
    activeItems.forEach(function(li) {
      li.classList.remove('active');
    });

    menuLinks.forEach(function(link) {
      var linkHref = link.getAttribute('href');

      if (linkHref && linkHref !== 'javascript:void(0)' && currentPath.startsWith(linkHref)) {
        var listItem = link.closest('li');

        if (listItem) {
          listItem.classList.add('active');

          var parentHasSubLi = link.closest('li.has-sub');
          if (parentHasSubLi) {
            parentHasSubLi.classList.add('active');

            var subMenuCollapse = parentHasSubLi.querySelector('.collapse');
            if (subMenuCollapse) {
              subMenuCollapse.classList.add('show');
              subMenuCollapse.setAttribute('aria-expanded', 'true');
            }
          }
        }
      }
    });
  });
</script>