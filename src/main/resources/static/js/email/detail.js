const spinner = document.getElementById('spinner');
const detail = document.getElementById('detail');

const subject = document.getElementById('subject');
const from = document.getElementById('from');
const recipients = document.getElementById('recipients');
const sentDate = document.getElementById('sentDate');
const content = document.getElementById('content');
const pathArr = location.pathname.split('/');
const emailNo = parseInt(pathArr[pathArr.length - 1]);

async function getDetail() {
	try {
		const response = await fetch(`/api/email/${pathArr[2]}/detail`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify({
				'emailNo': emailNo
			})
		});
		if (!response.ok) throw new Error('수신 오류');
		const data = await response.json();

		spinner.classList.remove('d-flex');
		detail.classList.remove('d-none');

		subject.append(data.subject);
		from.append(data.from);
		recipients.append(data.recipients);

		let date = new Date(data.sentDate);
		if (isNaN(date.getTime())) {
			date = new Date(data.receivedDate)
		}
		sentDate.append(date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate());
		// sentDate.setAttribute('datetime', data.sentDate);
		// content.append(data.text);
		content.innerHTML = DOMPurify.sanitize(data.text);

	} catch (e) {
		console.log(e);
	}
}

getDetail();