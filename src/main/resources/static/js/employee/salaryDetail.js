const salary = document.getElementById('salary');
const allowance = document.getElementById('allowance');
const deduction = document.getElementById('deduction');
const info = document.getElementById('info');
const sum = document.getElementById('sum');
const total = document.getElementById('total');

const pathArr = location.pathname.split('/');

async function getDetail() {
	const paymentId = parseInt(pathArr[pathArr.length - 1]);

	try {
		const response = await fetch(`/employee/salary/detail?paymentId=${paymentId}`, {
			method: 'GET',
			headers: {
				'Content-Type': 'application/json',
			},
		});
		if (!response.ok) throw new Error('수신 오류');
		const data = await response.json();

		const name = document.createElement('li');
		name.classList.add('row', 'mb-1');
		name.innerHTML += `<h6 class="col-6">이름</h6>`
		name.innerHTML += `<span class="col-6">${data.employee.name}</span>`;
		info.appendChild(name);

		const department = document.createElement('li');
		department.classList.add('row', 'mb-1');
		department.innerHTML += `<h6 class="col-6">부서</h6>`
		department.innerHTML += `<span class="col-6">${data.employee.departmentName}</span>`;
		info.appendChild(department);

		const position = document.createElement('li');
		position.classList.add('row', 'mb-1');
		position.innerHTML += `<h6 class="col-6">직위</h6>`
		position.innerHTML += `<span class="col-6">${data.employee.positionName}</span>`;
		info.appendChild(position);

		const date = document.createElement('li');
		date.classList.add('row', 'mb-1');
		date.innerHTML += `<h6 class="col-6">지급일</h6>`
		date.innerHTML += `<span class="col-6">${data.salaryPayment.paymentDate.substring(0, 10)}</span>`;
		info.appendChild(date);

		let deductionTotal = 0;

		data.deductionList.forEach(function (e) {
			const li = document.createElement('li');
			li.classList.add('row', 'mb-1');
			li.innerHTML += `<h6 class="col-6">${e.deductionName}</h6>`
			li.innerHTML += `<span class="col-6">${e.deductionAmount.toLocaleString('ko-KR')}</span>`;
			deduction.appendChild(li);

			if (['국민연금', '건강보험', '고용보험'].includes(e.deductionName)) {
				deductionTotal += e.deductionAmount;
			}
		})

		const deductionSum = document.createElement('li');
		deductionSum.classList.add('col-6');
		deductionSum.innerHTML += `<div class="row mb-1">
			<h6 class="col-6">공제 합계</h6>
			<span class="col-6">${deductionTotal.toLocaleString('ko-KR')}</span>
		</div>`;
		sum.appendChild(deductionSum);

		let payTotal = 0;

		const base = document.createElement('li');
		base.classList.add('row', 'mb-1');
		base.innerHTML += `<h6 class="col-6">기본급</h6>`
		base.innerHTML += `<span class="col-6">${data.salaryPayment.baseSalary.toLocaleString('ko-KR')}</span>`;
		salary.appendChild(base);
		payTotal += data.salaryPayment.baseSalary;

		data.allowanceList.forEach(function (e) {
			const li = document.createElement('li');
			li.classList.add('row', 'mb-1');
			li.innerHTML += `<h6 class="col-6">${e.allowanceName}</h6>`
			li.innerHTML += `<span class="col-6">${e.allowanceAmount.toLocaleString('ko-KR')}</span>`;
			allowance.appendChild(li);

			payTotal += e.allowanceAmount;
		})

		const paySum = document.createElement('li');
		paySum.classList.add('col-6');
		paySum.innerHTML += `<div class="row mb-1">
			<h6 class="col-6">지급 합계</h6>
			<span class="col-6">${payTotal.toLocaleString('ko-KR')}</span>
		</div>`;
		sum.appendChild(paySum);

		const totalSum = document.createElement('li');
		totalSum.classList.add('col-6');
		totalSum.innerHTML += `<div class="row mb-1">
			<h6 class="col-6">총 합계</h6>
			<span class="col-6">${(payTotal - deductionTotal).toLocaleString('ko-KR')}</span>
		</div>`;
		total.appendChild(totalSum);
	} catch (e) {
		console.log(e);
	}
}

getDetail();