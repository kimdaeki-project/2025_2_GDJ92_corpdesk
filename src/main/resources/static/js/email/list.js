const spinner = document.getElementById('spinner');
const missing = document.getElementById('missing');
const list = document.getElementById('list');
const tbody = document.getElementById('tbody');
const pathArr = location.pathname.split('/');
if (pathArr.length <= 3) pathArr.push('1');

let page = parseInt(pathArr[3]);
const category = pathArr[2];

// 메일 목록 요청
async function getMail() {
	try {
		if (page < 1) page = 1;
		if (page > 100) page = 100;
		const response = await fetch(`/api/email/${category}/${page}`, {
			method: 'POST'
		});
		console.log(response);
		if (!response.ok) throw new Error('메일을 가져올 수 없습니다.');
		const data = await response.json();

		spinner.classList.remove('d-flex');
		list.classList.remove('d-none');

		if (data.content.length == 0) {
			const cat = category == 'received' ? '받은 ' : '보낸 '

			throw new Error(cat + '메일이 없습니다.');
		}

		data.content.forEach(function (e) {
			const tr = document.createElement('tr');
			let date = new Date(e.sentDate);
			if (isNaN(date.getTime())) {
				date = new Date(e.receivedDate);
			}

			tr.innerHTML = `
				<td class="sender-name text-dark">${e.from}</td>
				<td><a href="/email/${category}/detail/${e.emailNo}" class="text-default d-inline-block text-smoke" style="height: 22.5px">
					<span class="subject text-dark">${e.subject}</span>
				</a></td>
				<td class="date">${date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate()}</td>
			`;
			// tbody.appendChild(tr);
			// 백엔드에서 오래된 순으로 넣었기 때문에, 거꾸로 출력
			tbody.prepend(tr);
		});

		paging(data.page);
	} catch (e) {
		const errorMsg = document.getElementById('errorMsg');
		errorMsg.innerText = e.message;

		console.error(e);
		spinner.classList.remove('d-flex');
		missing.classList.remove('d-none');
		list.classList.add('d-none');
		missing.classList.add('d-flex');
	}
}

function paging(data) {
	const card = document.getElementById('card');
	const cardBody = document.createElement('div');

	const totalPages = data.totalPages;
	const currentPage = data.number;

	const startPage = Math.floor(currentPage / 5) * 5;
	const endPage = Math.min(startPage + 4, totalPages - 1);

	const isFirst = startPage < 1;
	const isLast = endPage >= totalPages - 1;

	let pagination = `
	<div class="card-body">
		<nav aria-label="Page navigation">
			<ul class="pagination pagination-flat pagination-flat-rounded">
	`;

	// 이전 페이지
	pagination += `
		<li class="page-item ${isFirst ? 'd-none' : ''}">
			<a class="page-link d-inline" href="/email/${category}/${startPage}" aria-label="Previous">
				<span aria-hidden="true" class="mdi mdi-chevron-left"></span>
				<span class="sr-only">Previous</span>
			</a>
		</li>
		`;

	for (let i = startPage; i <= endPage; i++) {
		pagination += `
				<li class="page-item ${i == currentPage ? 'active' : ''}">
					<a class="page-link d-inline" href="/email/${category}/${i + 1}">${i + 1}</a>
				</li>
			`;
	}

	pagination += `
				<li class="page-item ${isLast ? 'd-none' : ''}">
					<a class="page-link d-inline" href="/email/${category}/${startPage + 6}" aria-label="Next">
						<span aria-hidden="true" class="mdi mdi-chevron-right"></span>
						<span class="sr-only">Next</span>
					</a>
				</li>
			</ul>
		</nav>
	</div>
	`
	cardBody.innerHTML = pagination;
	card.appendChild(cardBody);
}

getMail();