window.addEventListener("load", () => {
  Swal.fire({
    text: "참여되지 않은 사용자입니다.",
    icon: "warning"
  }).then(()=>{
	window.location.href = "/chat/room/list";
  });
});