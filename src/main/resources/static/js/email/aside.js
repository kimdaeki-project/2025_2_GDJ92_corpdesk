
const path = location.pathname;
if (path.includes('received')) {
	const received = document.getElementById('received');
	received.classList.add('active');
} else if (path.includes('sent')) {
	const sent = document.getElementById('sent');
	sent.classList.add('active');
}