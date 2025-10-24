document.addEventListener('DOMContentLoaded', function () {

	const searchForm = document.getElementById('searchForm');

	const options4 = {
		title: {
			text: '근태 통계',
			align: 'left',
			style: {
				fontSize: '22px',
			}
		},
		series: [{
			name: 'default',
			type: 'column',
			data: []
		}],
		chart: {
			type: 'bar',
			height: 300,
		},
		dataLabels: {
			enabled: false
		},
		yaxis: {
			labels: {
				formatter: function (y) {
					return y.toFixed(0) + "%";
				}
			}
		},
		legend: {
			position: 'top',
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

	const chart4 = new ApexCharts(document.querySelector("#chart4"), options4);
	chart4.render();

	async function getChart4(start, end, departmentId, positionId) {
		try {
			const response = await fetch('/api/stats/chart4', {
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

			chart4.updateOptions({
				xaxis: {
					categories: data.months,
				}
			})

			chart4.updateSeries([{
				name: '출근',
				type: 'column',
				data: data.attended
			}, {
				name: '지각',
				type: 'column',
				data: data.late
			}, {
				name: '결근',
				type: 'column',
				data: data.absent
			}]);

		} catch (e) {
			console.error(e);
		}
	}

	getChart4();


	searchForm.addEventListener('submit', function (e) {
		e.preventDefault();

		const start = document.getElementById('start').value;
		const end = document.getElementById('end').value;
		const department = document.getElementById('department');
		const position = document.getElementById('position');

		const departmentId = Number(department.options[department.selectedIndex].value);
		const positionId = Number(position.options[position.selectedIndex].value);

		getChart4(start, end, departmentId, positionId);
	});
});