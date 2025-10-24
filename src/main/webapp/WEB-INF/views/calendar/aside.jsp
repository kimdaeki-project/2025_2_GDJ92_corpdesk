<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<aside>
	<div class="email-left-column email-options p-0">
		<button href="#" class="btn btn-block btn-primary mb-4 mb-xl-5" data-toggle="modal" data-target="#scheduleModal">
			<i class="mdi mdi-calendar-plus mr-2"></i> 일정 추가하기
		</button>
		<ul class="pb-2">
			<li id="attendance" class="d-block">
				<div class="custom-control custom-checkbox d-inline-block mr-3 mb-3">
					<input type="checkbox" class="custom-control-input" id="customCheckPrimary">
					<label class="custom-control-label text-primary" for="customCheckPrimary">출퇴근</label>
				</div>
			</li>
			<li id="vacation" class="d-block">
				<div class="custom-control custom-checkbox checkbox-secondary d-inline-block mr-3 mb-3">
					<input type="checkbox" class="custom-control-input" id="customCheckSecondary">
					<label class="custom-control-label text-secondary" for="customCheckSecondary">휴가</label>
				</div>
			</li>
			<li id="schedule" class="d-block">
				<div class="custom-control custom-checkbox checkbox-success d-inline-block mr-3 mb-3">
					<input type="checkbox" class="custom-control-input" id="customCheckSuccess" checked="checked">
					<label class="custom-control-label text-success" for="customCheckSuccess">개인 일정</label>
				</div>
			</li>
			<li id="EveryVacation" class="d-block">
				<div class="custom-control custom-checkbox checkbox-warning d-inline-block mr-3 mb-3">
					<input type="checkbox" class="custom-control-input" id="customCheckWarning">
					<label class="custom-control-label text-warning" for="customCheckWarning">전사 휴가</label>
				</div>
			</li>
		</ul>
	</div>
</aside>