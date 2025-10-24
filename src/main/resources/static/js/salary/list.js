const tbody = document.getElementById('tbody');
const pathArr = location.pathname.split('/');
if (pathArr.length <= 3) pathArr.push('1');

// let page = parseInt(pathArr[3]);

async function getSalary(page) {
	try {

		const response = await fetch('/api/salary', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify({
				'page': page,
			})
		});
		if (!response.ok) throw new Error('수신 오류');
		const data = await response.json();

		console.log(data);

		tbody.innerHTML = '';
		data.content.forEach(function (e) {
			const tr = document.createElement('tr');

			tr.setAttribute("onclick", `detail(${e.paymentId})`)
			tr.setAttribute("style", "cursor:pointer")
			// tr.classList.add('pe-auto');

			// <td onclick="detail(${e.paymentId})">조회</td>
			tr.innerHTML = `
				<td>${e.name}</td>
				<td>${e.paymentId}</td>
				<td>${e.departmentName}</td>
				<td>${e.positionName}</td>
				<td>${e.responsibility}</td>
				<td>${e.baseSalary.toLocaleString('ko-KR')}</td>
				<td>${e.allowanceAmount.toLocaleString('ko-KR')}</td>
				<td>${e.deductionAmount.toLocaleString('ko-KR')}</td>
				<td>${(e.baseSalary + e.allowanceAmount - e.deductionAmount).toLocaleString('ko-KR')}</td>
				<td>${e.paymentDate.substring(0, 10)}</td>
			`;

			tbody.appendChild(tr);
		});

		paging(data.page);
	} catch(e) {
		console.log(e);
	}
}

getSalary(1);

function detail(e) {
	location.href = '/salary/' + e;
}


function paging(data) {

	const div = document.getElementById('pagination');
	const pagination = document.createElement('ul');
	pagination.classList.add('pagination');
	pagination.classList.add('pagination-flat');
	pagination.classList.add('pagination-flat-rounded');

	const totalPages = data.totalPages;
	const currentPage = data.number;

	const startPage = Math.floor(currentPage / 5) * 5;
	const endPage = Math.min(startPage + 4, totalPages - 1);

	const isFirst = startPage < 1;
	const isLast = endPage >= totalPages - 1;

	let page = `
		<li class="page-item ${isFirst ? 'd-none' : ''}">
			<a class="page-link d-inline" href="#" onclick="getPage(event, ${startPage})" aria-label="Previous">
				<span aria-hidden="true" class="mdi mdi-chevron-left"></span>
				<span class="sr-only">Previous</span>
			</a>
		</li>
	`

	for (let i = startPage; i <= endPage; i++) {
		page += `
				<li class="page-item ${i == currentPage ? 'active' : ''}">
					<a class="page-link d-inline" href="#" onclick="getPage(event, ${i + 1})">${i + 1}</a>
				</li>
			`;
	}

	page += `
				<li class="page-item ${isLast ? 'd-none' : ''}">
					<a class="page-link d-inline" href="#" onclick="getPage(event, ${startPage + 6})" aria-label="Next">
						<span aria-hidden="true" class="mdi mdi-chevron-right"></span>
						<span class="sr-only">Next</span>
					</a>
				</li>
	`

	pagination.innerHTML = page;
	div.innerHTML = '';
	div.appendChild(pagination);
}

function getPage(e, page) {
	e.preventDefault();
	getSalary(page);
}


