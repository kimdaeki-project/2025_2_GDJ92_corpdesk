/**
 * 테이블의 한 행 클릭시 상세정보 페이지로 이동
 */
const scheduleRows = document.querySelectorAll('.schedule-row');

scheduleRows.forEach((row) => {
  row.addEventListener('click', function () {
    const id = row.getAttribute('data-personal-schedule-id');

    location.href = `/personal-schedule/${id}`;
  });
});