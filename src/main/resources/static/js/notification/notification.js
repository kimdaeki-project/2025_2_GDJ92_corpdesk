const socket = new SockJS("/ws");
stompClient = Stomp.over(socket);
stompClient.connect({}, function(frame) {
	// 기본적으로 본인 개인큐 구독 (알림/DM 수신)
	stompClient.subscribe("/user/queue/notifications", (message) => {
        const notification = JSON.parse(message.body)
        //채팅 관련 알림
        if (notification.chatRoomId != null) {
                //메세지 알림 목록에 추가
				if(notification.notificationType !== "read" && !notification.focused){
					appendMessageNotification(notification);
			}

        }
		if(notification.notificationType=='approval'){
			appendApprovalNotification(notification);
		}
		
        
    })
    

})



//알림 시간 
const notificationTime = document.querySelectorAll(".notificationTime");
notificationTime.forEach(t=>{
	t.textContent=formatTime(t.getAttribute("data-notificationTime"));
})
//메세지 알림 생성 로직
// 새 메시지 알림 요소 생성 함수
 function appendMessageNotification(notification) {
  const messageTab = document.getElementById("message");


  //  알림 요소 생성
  const div = document.createElement("div");
  div.className = "media media-sm p-4 mb-0 notification messageNotification";
  div.setAttribute("data-roomId", notification.chatRoomId);
  div.style.cursor = "pointer";

  div.innerHTML = `
    <div class="media-sm-wrapper">
      <img src="${notification.imgPath}" alt="User Image" style="width:50px; height:50px;">
    </div>
    <div class="media-body">
      <span class="title mb-0">${notification.viewName}</span>
      <span class="discribe"> ${notification.messageContent}</span>
      <span class="time">
        <time class="notificationTime" data-notificationTime="${notification.sentAt}">${formatTime(notification.sentAt)}</time>
      </span>
    </div>
  `;

  //  새 알림을 목록 맨 위에 추가
  messageTab.prepend(div);

  //  전체 및 메시지 카운트 갱신
  const allCount = document.querySelector(".all-count");
  const messageCount = document.querySelector(".message-count");

  const currentAll = parseInt(allCount.getAttribute("data-allCount") || "0", 10);
  const currentMsg = parseInt(messageCount.getAttribute("data-count") || "0", 10);

  const newAll = currentAll + 1;
  const newMsg = currentMsg + 1;

  allCount.textContent = newAll;
  allCount.setAttribute("data-allCount", newAll);

  messageCount.textContent = "메시지 (" + newMsg + ")";
  messageCount.setAttribute("data-count", newMsg);
}

function formatTime(time) {
  const date = new Date(time);
  const now = new Date();

  const diffMin = Math.floor((now - date) / 60000);
  if (diffMin < 1) return "방금 전";
  if (diffMin < 60) return diffMin + "분 전";

  const diffHour = Math.floor(diffMin / 60);
  if (diffHour < 24) return diffHour + "시간 전";

  return date.toLocaleString("ko-KR", {
    month: "short",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
}
//알림 목록 처리
function ReadNotification(chatRoomId){
	
	//목록에서 같은 방 메세지 전부 제거 
	const sameRoomNotifications = document.querySelectorAll('.messageNotification[data-roomId="'+ chatRoomId+'"]');
	 sameRoomNotifications.forEach(el=>{
		el.remove();
	 })
	 // 알림 아이콘 위 숫자 변경
	const allCount = document.querySelector(".all-count");
	const allCountData = parseInt(allCount.getAttribute("data-allCount") || "0", 10);
	const removedCount = sameRoomNotifications.length;
	// 남은 개수 계산
	const newAllCount = allCountData - removedCount;
	allCount.textContent = newAllCount > 0 ? newAllCount : "";
	allCount.setAttribute("data-allCount", newAllCount);

	// 메시지 탭 숫자 변경
	const messageCount = document.querySelector(".message-count");
	const messageContentData = parseInt(messageCount.getAttribute("data-count") || "0", 10);
	const newMsgCount = messageContentData - removedCount;

	messageCount.textContent = "메시지 (" + (newMsgCount > 0 ? newMsgCount : 0) + ")";
	messageCount.setAttribute("data-count", newMsgCount);
}

//메세지 알림 클릭시 해당방을 띄움
const messageTab = document.getElementById("message");
messageTab.addEventListener("click",(e)=>{
	const notificationOne = e.target.closest('.messageNotification');
	if(!notificationOne)return;

	//메세지 목록 처리
	const chatRoomId = notificationOne.getAttribute("data-roomId");
		const pop = window.open("/chat/room/detail/" + chatRoomId, "room_no_" + chatRoomId, "width=700,height=650 ,left=600, top=100");
			if (pop) {
				pop.focus();
			}
			fetch("/chat/participant/lastMessage/" + chatRoomId, {
				method: "POST"
			});
	ReadNotification(chatRoomId);

})


//결재 알림 로직


//결재 알림 생성
function appendApprovalNotification(notification){
	const approvalTab = document.getElementById("approval");


	 //  알림 요소 생성
	 const div = document.createElement("div");
	 div.className = "media media-sm p-4 mb-0 notification approvalNotification";
	 div.setAttribute("data-approvalId", notification.relatedId);
	 div.style.cursor = "pointer";

	 div.innerHTML = `
	   <div class="media-sm-wrapper bg-info-dark">
	   	 <i class="mdi mdi-bell"></i>
	   </div>
	   <div class="media-body">
	 	 <span class="title mb-0">${notification.title}</span>
	  	 <span class="discribe">${notification.content}</span>
      	 <span class="time">
       		  <time class="notificationTime" data-notificationTime="${notification.createdAt}">${formatTime(notification.createdAt)}</time>
      	 </span>
	   </div>
	 `;

	 //  새 알림을 목록 맨 위에 추가
	 approvalTab.prepend(div);

	 //  전체 및 메시지 카운트 갱신
	 const allCount = document.querySelector(".all-count");
	 const approvalCount = document.querySelector(".approval-count");

	 const currentAll = parseInt(allCount.getAttribute("data-allCount") || "0", 10);
	 const currentNoti = parseInt(approvalCount.getAttribute("data-count") || "0", 10);

	 const newAll = currentAll + 1;
	 const newNoti = currentNoti + 1;

	 allCount.textContent = newAll;
	 allCount.setAttribute("data-allCount", newAll);

	 approvalCount.textContent = "결재 (" + newNoti + ")";
	 approvalCount.setAttribute("data-count", newNoti);
}

const approvalTab = document.getElementById("approval");
approvalTab.addEventListener("click",(e)=>{
	const notificationOne = e.target.closest('.approvalNotification');
	if(!notificationOne)return;
	const approvalId = notificationOne.getAttribute("data-approvalId");
	fetch("/notification/read/" + approvalId, {
					method: "POST"
				}).then(()=>{
					location.href=`/approval/${approvalId}`;
				});
	

})

const msgAllread = document.querySelector(".mark-all-msg-read");
msgAllread.addEventListener("click",()=>{
	const msgAll = document.querySelectorAll(".messageNotification");
	msgAll.forEach(el=>{
		el.remove();
	})
	 // 알림 아이콘 위 숫자 변경
	const allCount = document.querySelector(".all-count");
	const allCountData = parseInt(allCount.getAttribute("data-allCount") || "0", 10);
	const removedCount = msgAll.length;
	// 남은 개수 계산
	const newAllCount = allCountData - removedCount;
	allCount.textContent = newAllCount > 0 ? newAllCount : "";
	allCount.setAttribute("data-allCount", newAllCount);

	// 메시지 탭 숫자 변경
	const messageCount = document.querySelector(".message-count");
	const messageContentData = parseInt(messageCount.getAttribute("data-count") || "0", 10);

	messageCount.textContent = "메시지 (0)";
	messageCount.setAttribute("data-count", '0');
	fetch("/notification/MsgReadAll", {
	  method: "POST",
	  headers: { "Content-Type": "application/json" },
	})
})