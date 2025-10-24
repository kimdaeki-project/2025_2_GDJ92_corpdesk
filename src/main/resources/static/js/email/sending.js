const form = document.getElementById('form_data');
const textArea = document.getElementById('text');
const editor = document.getElementById('editor');
const submitBtn = document.getElementById('submit_btn');

submitBtn.onclick = async function () {
	try {
		const content = editor.firstElementChild;
		textArea.innerHTML = content.innerHTML;

		const formData = new FormData(form);

		console.log(textArea);

		const response = await fetch('/api/email/sending', {
			method: 'POST',
			body: formData
		});
		if (!response.ok) throw new Error('수신 오류');
		const data = await response.text();

		if (data == 'success') {
			location.href = '/email/sent';
		}

		console.log(data);
	} catch (e) {
		console.log(e)
	}
};