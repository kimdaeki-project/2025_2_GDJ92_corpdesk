document.addEventListener('DOMContentLoaded', function () {

	const searchForm = document.getElementById('searchForm');

	const options3 = {
		title: {
			text: '나이 통계',
			align: 'left',
			style: {
				fontSize: '22px',
			}
		},
		series: [0],
		chart: {
			type: 'donut',
		},
		dataLabels: {
			enabled: false
		},
		legend: {
			position: 'bottom',
			markers: {
				width: 24,
				height: 12,
				radius: 0
			}
		},
		theme: {
			palette: 'palette2'
		}
	};

	const chart3 = new ApexCharts(document.querySelector("#chart3"), options3);
	chart3.render();

	async function getChart3(start, end, departmentId, positionId) {
		try {
			const response = await fetch('/api/stats/chart3', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
				},
				body: JSON.stringify({
					'start': start,
					'end': end,
					'departmentId': departmentId,
					'positionId': positionId
				})
			});
			if (!response.ok) throw new Error('수신 오류');
			const data = await response.json();

			// console.log(data);

			chart3.updateSeries(data.count);
			chart3.updateOptions({
				labels: data.diff,
			})

		} catch (e) {
			console.error(e);
		}
	}

	getChart3();

	searchForm.addEventListener('submit', function (e) {
		e.preventDefault();

		const start = document.getElementById('start').value;
		const end = document.getElementById('end').value;
		const department = document.getElementById('department');
		const position = document.getElementById('position');

		const departmentId = Number(department.options[department.selectedIndex].value);
		const positionId = Number(position.options[position.selectedIndex].value);

		getChart3(start, end, departmentId, positionId);
	});
});