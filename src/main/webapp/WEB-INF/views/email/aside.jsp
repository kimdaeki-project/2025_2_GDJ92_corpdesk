<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<aside class="col-lg-4 col-xl-3 col-xxl-2">
	<div class="email-left-column email-options p-4 p-xl-5">
		<a href="/email/sending" class="btn btn-block btn-primary btn-pill mb-4 mb-xl-5">메일 쓰기</a>
		<ul class="pb-2">
			<li id="received" class="d-block mb-4"><a href="/email/received">
				<i class="mdi mdi-download mr-2"></i>
				받은 메일
			</a></li>
			<li id="sent" class="d-block mb-4"><a href="/email/sent">
				<i class="mdi mdi-open-in-new mr-2"></i>
				보낸 메일
			</a></li>
		</ul>
	</div>
</aside>
<script src="/js/email/aside.js"></script>