
const roles = document.querySelectorAll(".role");
console.log(roles);
roles.forEach(role => {
	role.addEventListener("change", (e) => {
		const username = e.target.dataset.username;
		const roleId = Number(e.target.value);
		updateRole(username, roleId);
	})
})

async function updateRole(username, roleId) {
	try {
		const response = await fetch(`/admin`, {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
			},
			body: JSON.stringify({
				'username': username,
				'roleId': roleId
			})
		});

		if (!response.ok) throw new Error('수신 오류');
		const data = await response.text() // json();
		console.log(data);

	} catch (error) {
		console.log(error);
	}
}