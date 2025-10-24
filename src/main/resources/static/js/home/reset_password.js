const userInput = document.getElementById('username');
const searchBtn = document.getElementById('searchBtn');
const email = document.getElementById('email');
const sendBtn = document.getElementById('sendBtn');
const msg = document.getElementById('msg');

let status = false;

async function getEmail(username) {
	try {
		const response = await fetch('/get-mail?username=' + username, {
			method: 'GET',
		});
		if (!response.ok) throw new Error('수신 오류');
		const data = await response.text();

		console.log(data);
		if (data == null || data === '') {
			msg.innerText = '등록된 이메일 주소가 없습니다.';
			status = false;
			return;
		}

		msg.innerText = '';
		email.innerText = data.substring(0, 2) + "***" + data.substring(data.indexOf('@'), data.length);
		status = true;
		sendBtn.removeAttribute('disabled');

	} catch (e) {
		console.error(e);
		msg.innerText = '없는 아이디 입니다.';
		status = false;
	}
}

searchBtn.addEventListener('click', e => {
	const username = userInput.value;

	getEmail(username);
});

async function resetPassword(username) {
	try {
		const response = await fetch('/reset-password?username=' + username, {
			method: 'POST',
		});
		if (!response.ok) throw new Error('수신 오류');
		const data = await response.text();
		console.log(data);

		if (data == 'success') {
			Swal.fire({
        text: '임시 비밀번호가 발송되었습니다.',
        icon: 'success'
      }).then(result => location.href = '/');
		}

	} catch (e) {
		console.error(e);
	}
}

sendBtn.addEventListener('click', e => {
	const username = userInput.value;
	if (status == true) {
		resetPassword(username);
	}
});