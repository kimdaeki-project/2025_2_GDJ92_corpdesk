const params = new URLSearchParams(window.location.search);
const username = params.get("username");

/**
 * 목록에서 항목 클릭시 상세정보 페이지로 이동
 */

const approvalRows = document.querySelectorAll('.approval-row');

approvalRows.forEach((row) => {
  row.addEventListener('click', function() {
    const approvalId = row.getAttribute('data-approval-id');
    
    location.href=`/approval/${approvalId}`;
  });
});

/**
 * 
 */

const approvalFormNames = document.querySelectorAll('.approval-form-name');
const approvalTitle = document.querySelector('#approvalTitle');

let formId = 0;

approvalFormNames.forEach((name) => {
  name.addEventListener('click', function () {
    approvalTitle.textContent = name.textContent;

    formId = name.getAttribute('data-approval-form-id');
  });
});

const formCheckBtn = document.querySelector('#formCheck');
const departmentIdEl = document.querySelector('#departmentId');

formCheckBtn.addEventListener('click', function () {
    if(formId === 0) {
      Swal.fire({
        text: '결재 양식을 선택해 주세요.',
        icon: 'waring'
      });
    }
    else location.href=`/approval-form/${formId}?departmentId=${departmentIdEl.value}`;
});

/**
 *
 */
const url = new URL(window.location.href);
const path = url.pathname;
const pathSegments = path.split('/');
const approvalFormId = pathSegments[2];

switch (approvalFormId) {
  case '1':
    /**
     * 휴가 신청 폼 - 휴가 사용 일수 자동 계산
     */
    // 날짜 입력 필드 참조
    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');
    const usedDaysInput = document.getElementById('usedDays');

    // 평일(주말 제외) 계산 함수
    function calculateWeekdays(startDate, endDate) {
      if (!startDate || !endDate || startDate > endDate) {
        return 0;
      }

      let count = 0;
      let currentDate = new Date(startDate);

      while (currentDate <= endDate) {
        const dayOfWeek = currentDate.getDay();
        // 0: 일요일, 6: 토요일 제외
        if (dayOfWeek !== 0 && dayOfWeek !== 6) {
          count++;
        }
        currentDate.setDate(currentDate.getDate() + 1);
      }

      return count;
    }

    // 사용일수 업데이트 함수
    function updateUsedDays() {
      const startDate = startDateInput.value ? new Date(startDateInput.value) : null;
      const endDate = endDateInput.value ? new Date(endDateInput.value) : null;

      if (startDate && endDate) {
        if (startDate > endDate) {
          usedDaysInput.value = '종료일이 시작일보다 빨라요';
          usedDaysInput.style.color = '#dc3545';
        } else {
          const weekdays = calculateWeekdays(startDate, endDate);
          usedDaysInput.value = `${weekdays}`;
          usedDaysInput.style.color = '#198754';
        }
      } else {
        usedDaysInput.value = '';
        usedDaysInput.style.color = '#6c757d';
      }
    }

    // 이벤트 리스너 추가
    startDateInput.addEventListener('change', updateUsedDays);
    endDateInput.addEventListener('change', updateUsedDays);

    // 시작일 변경 시 종료일 최소값 설정
    startDateInput.addEventListener('change', function() {
      if (this.value) {
        endDateInput.min = this.value;
        // 종료일이 시작일보다 빠른 경우 초기화
        if (endDateInput.value && endDateInput.value < this.value) {
          endDateInput.value = '';
        }
      }
    });

    break;

  case '2':
    /**
     * 출장 신청 폼 - 금액 합계 자동 계산
     */
        // 금액 입력 필드들을 모두 선택
    const amountInputs = document.querySelectorAll('.amount-input');
    const totalDisplay = document.getElementById('total-amount');

    // 총합을 계산하는 함수
    function calculateTotal() {
      let total = 0;
      amountInputs.forEach(input => {
        const value = parseFloat(input.value) || 0;
        total += value;
      });
      totalDisplay.textContent = total.toLocaleString('ko-KR');
    }

    // 각 금액 입력 필드에 이벤트 리스너 추가
    amountInputs.forEach(input => {
      input.addEventListener('input', calculateTotal);
      input.addEventListener('blur', calculateTotal);
    });

    // 페이지 로드 시 초기 계산
    calculateTotal();

    // -------------------------------------------------------------

    /**
     * 출장 신청 폼 - 출장 기간 유효성 검사
     */
        // 날짜 입력 필드 참조 (ID는 HTML에서 재사용됨)
    const businessStartDateInput = document.getElementById('startDate');
    const businessEndDateInput = document.getElementById('endDate');

    // 날짜 유효성 검사 및 min 속성 설정 함수
    function validateBusinessTripDates() {
      const startDateValue = businessStartDateInput.value;
      const endDateValue = businessEndDateInput.value;

      if (startDateValue && businessEndDateInput) {
        // 1. 시작일 변경 시 종료일 최소값 설정
        businessEndDateInput.min = startDateValue;

        // 2. 종료일이 시작일보다 빠른 경우 초기화 및 알림
        if (endDateValue && endDateValue < startDateValue) {
          Swal.fire({
            text: '출장 종료일은 시작일보다 빠를 수 없습니다.',
            icon: 'waring'
          });
          businessEndDateInput.value = ''; // 값 초기화
        }
      }
    }

    // 이벤트 리스너 추가: 시작일 및 종료일 변경 시 유효성 검사 실행
    if (businessStartDateInput && businessEndDateInput) {
      businessStartDateInput.addEventListener('change', validateBusinessTripDates);
      businessEndDateInput.addEventListener('change', validateBusinessTripDates);
    }

    break;

  case '3':
    break;
  default:

}

/**
 * 결재자 목록 드래그앤드롭 및 제거 로직
 */

(function() {
    // 1. DOM 요소 선택
    const employeeList = document.getElementById('employee-list');
    const dropArea = document.getElementById('approver-drop-area');
    const approverList = document.getElementById('approver-list');

    // 필수 요소 없으면 스크립트 중단
    if (!employeeList || !dropArea || !approverList) return;

    // 제거 버튼 생성 및 이벤트 등록 함수
    function addRemoveButton(liElement) {
        // 이미 버튼이 있다면 중단
        if (liElement.querySelector('.remove-approver')) return;

        const removeBtn = document.createElement('button');
        removeBtn.textContent = 'X';
        removeBtn.type = 'button';
        // ms-auto로 버튼을 오른쪽 끝으로 정렬
        removeBtn.className = 'btn btn-sm btn-outline-danger ms-3 remove-approver ms-auto';

        // 기존 자식 노드들을 감싸는 Wrapper 생성 (이미지/이름)
        const contentWrapper = document.createElement('div');
        // flex-grow-1: 남은 공간 모두 차지 -> 버튼을 오른쪽 끝으로 밀어냄
        contentWrapper.className = 'd-flex align-items-center flex-grow-1';

        // li의 모든 내용을 Wrapper로 이동
        Array.from(liElement.children).forEach(child => contentWrapper.appendChild(child));

        // li 내부 초기화 후, Wrapper와 버튼을 추가
        liElement.innerHTML = '';
        liElement.appendChild(contentWrapper);
        liElement.appendChild(removeBtn);

        // 제거 이벤트 등록
        removeBtn.addEventListener('click', () => liElement.remove());
    }

    // ========== 페이지 로드 시 기존 결재자 정보를 모달에 추가 ==========
    (function loadExistingApprovers() {
      // 수정 모드에서 기존 결재자 정보가 있는지 확인
      const existingApprovers = document.querySelectorAll('#approvalContentCommon input[name^="approverDTOList"][name$=".username"]');

      if (existingApprovers.length > 0) {
        existingApprovers.forEach((input, index) => {
          const username = input.value;

          // employeeList에서 해당 username을 가진 li 요소 찾기
          const employeeLi = employeeList.querySelector(`li[data-username="${username}"]`);

          if (employeeLi) {
            // 기존 li 요소를 복제하여 결재선 목록에 추가
            const clonedLi = employeeLi.cloneNode(true);
            clonedLi.classList.add('added-approver');
            addRemoveButton(clonedLi);
            approverList.appendChild(clonedLi);
          }
        });
      }
    })();
    // ================================================================

    // 2. 드래그 시작/종료 이벤트
    employeeList.addEventListener('dragstart', (e) => {
        const targetLi = e.target.closest('li[draggable="true"]');
        if (!targetLi) return;

        e.dataTransfer.setData('text/html', targetLi.outerHTML);
        e.dataTransfer.effectAllowed = 'copy';
        targetLi.classList.add('is-dragging');
    });

    employeeList.addEventListener('dragend', (e) => {
        const targetLi = e.target.closest('li[draggable="true"]');
        if (targetLi) targetLi.classList.remove('is-dragging');
    });

    // 3. 드롭 영역 이벤트
    dropArea.addEventListener('dragover', (e) => {
        e.preventDefault();
        e.dataTransfer.dropEffect = 'copy';
        dropArea.classList.add('drag-over');
    });

    dropArea.addEventListener('dragleave', () => dropArea.classList.remove('drag-over'));

    dropArea.addEventListener('drop', (e) => {
        e.preventDefault();
        dropArea.classList.remove('drag-over');

        const draggedHtml = e.dataTransfer.getData('text/html');
        const tempDiv = document.createElement('div');
        tempDiv.innerHTML = draggedHtml.trim();
        const newLi = tempDiv.firstChild;

        const username = newLi.getAttribute('data-username');
        // 중복 확인: data-username이 같은 요소가 이미 있는지 확인
        if (approverList.querySelector(`[data-username="${username}"]`)) {
            Swal.fire({
              text: '이미 추가된 결재자입니다.',
              icon: 'waring'
            });
            return;
        }

        newLi.classList.remove('is-dragging');
        newLi.classList.add('added-approver');

        addRemoveButton(newLi);
        approverList.appendChild(newLi);
    });
})();

/**
 * 결재선 지정 후 확인 버튼을 눌렀을 때의 로직
 */
const MAX_COLUMNS = 4; // 승인란 한 행에 표시되는 최대 결재자 수
const newApproverRow = document.querySelector('#newApproverRow'); // HTML에 정의된 기준점 DIV

// 1. 셀 생성 헬퍼 함수
const createCells = (approver) => {
    const hasApprover = !!approver;

    // 결재자 정보 추출 (없으면 &nbsp;)
    const positionText = hasApprover
        ? `${approver.getAttribute('data-department-name')} ${approver.getAttribute('data-position-name')}`
        : '&nbsp;';
    const nameText = hasApprover ? approver.getAttribute('data-name') : '&nbsp;';

    // TD 엘리먼트 생성
    const createTd = (text, width = '20%') => {
        const td = document.createElement('td');
        td.className = 'text-center align-middle';
        td.style.width = width;
        td.innerHTML = text;
        return td;
    };

    // 직위/부서, 이름, 날짜(빈 칸) TD를 반환
    return [createTd(positionText), createTd(nameText), createTd('&nbsp;')];
};

// 2. 추가 승인란 테이블 컨테이너 생성 함수
function createExtraApprovalTableContainer() {
    const container = document.createElement('div');
    container.id = 'extraApprovalTableContainer';

    const table = document.createElement('table');
    table.className = 'table table-bordered no-top-border';

    // 왼쪽 <th>는 빈 칸(&nbsp;) 처리
    table.innerHTML = `
        <tbody>
            <tr class="approver-position">
                <th class="align-middle table-light col-2" rowspan="3">&nbsp;</th> 
            </tr>
            <tr class="approver-name" style="height: 90px;"></tr>
            <tr class="approval-date"></tr>
        </tbody>
    `;

    container.appendChild(table);
    return container;
}

// 3. 결재자 정보를 테이블에 렌더링하는 핵심 함수
function renderApproversToTable(approvers) {
    // ID를 통해 실제 결재 승인 테이블을 정확히 찾음
    const baseRow = document.getElementById('approverPosition');
    if (!baseRow) return console.error("기본 결재 TR을 찾을 수 없습니다.");

    // 기존 추가 테이블 제거
    document.querySelectorAll('#extraApprovalTableContainer').forEach(el => el.remove());

    // 기본 테이블의 TR 엘리먼트들
    const baseRows = [
        baseRow,
        document.getElementById('approverName'),
        document.getElementById('approvalDate')
    ];

    // 기본 테이블의 기존 TD 셀들 제거 (<th>는 남김)
    baseRows.forEach(row => {
        if (!row) return;
        while (row.lastElementChild && row.lastElementChild.tagName === 'TD') {
            row.removeChild(row.lastElementChild);
        }
    });

    // --- hidden input 필드 관리 ---
    const form = document.getElementById('approvalContentCommon');
    // 기존의 모든 approverDTOList 관련 hidden input을 제거
    form.querySelectorAll('input[name^="approverDTOList"]').forEach(el => el.remove());

    // 각 결재자마다 hidden input 생성 및 추가
    approvers.forEach((approver, index) => {
      const username = approver.getAttribute('data-username');

      const hiddenInputUsername = document.createElement('input');
      hiddenInputUsername.type = 'hidden';
      hiddenInputUsername.name = `approverDTOList[${index}].username`;
      hiddenInputUsername.value = username;
      form.appendChild(hiddenInputUsername);

      const hiddenInputOrder = document.createElement('input');
      hiddenInputOrder.type = 'hidden';
      hiddenInputOrder.name = `approverDTOList[${index}].approvalOrder`;
      hiddenInputOrder.value = index + 1;
      form.appendChild(hiddenInputOrder);
    });
    // ---  ---

    // 4. 테이블 채우기 로직
    let insertionReferenceNode = newApproverRow; // 첫 삽입 기준은 newApproverRow

    // 결재자 목록을 4명 단위로 순회 (4명 이하일 경우 1회 실행)
    for (let i = 0; i < approvers.length || i < MAX_COLUMNS; i += MAX_COLUMNS) {
        const approverChunk = approvers.slice(i, i + MAX_COLUMNS);

        let currentRowSet = baseRows;

        if (i > 0) { // 4명 초과 시 새 테이블 생성
            const extraTableContainer = createExtraApprovalTableContainer();

            // 직전 삽입된 요소 다음에 추가
            insertionReferenceNode.after(extraTableContainer);
            insertionReferenceNode = extraTableContainer; // 다음 삽입 위치 업데이트

            // 새 테이블의 TR 엘리먼트 가져오기
            currentRowSet = [
                extraTableContainer.querySelector('.approver-position'),
                extraTableContainer.querySelector('.approver-name'),
                extraTableContainer.querySelector('.approval-date')
            ];
        }

        // 현재 4칸을 채움 (결재자가 없으면 빈 칸으로)
        for (let j = 0; j < MAX_COLUMNS; j++) {
            const approver = approverChunk[j];
            const [positionTd, nameTd, dateTd] = createCells(approver);

            currentRowSet[0].appendChild(positionTd);
            currentRowSet[1].appendChild(nameTd);
            currentRowSet[2].appendChild(dateTd);
        }

        // 4명 이하이고 첫 루프가 끝났으면 종료 (불필요한 i < MAX_COLUMNS 루프 방지)
        if (approvers.length <= MAX_COLUMNS && i === 0) break;
    }
}

// 4. 결재선 확인 버튼 이벤트 리스너 등록
document.getElementById('approverCheck').addEventListener('click', () => {
  const approvers = Array.from(document.querySelectorAll('#approver-list li'));
  renderApproversToTable(approvers);

  // 확인 버튼에서 포커스 제거 (접근성 오류 해결)
  document.activeElement.blur();

  // 모달은 data-dismiss="modal" 속성이나 다른 방식으로 닫힐 것임
});

/**
 * 결재 요청/임시저장/취소 버튼을 눌렀을 때
 */
const filepondEl = document.querySelector('.filepond#file');
const pond = FilePond.create(filepondEl, {
    labelIdle: `파일을 드래그 앤 드롭하거나 <span class="filepond--label-action">여기</span>를 클릭`,
});

const btnSubmits = document.querySelectorAll('.btn-submit');
btnSubmits.forEach((btn) => {
  btn.addEventListener('click', async function () {
    console.log(btn.id);

    // 0. 유효성 검사: hidden input 요소의 개수로 결재자 지정 여부 확인
    // (결재 요청 시에만 검사하며, 임시저장/취소는 검사하지 않음)
    if (btn.id !== 'tempSave' && btn.id !== 'cancel') {
      console.log(1);

      const approverHiddenInputs = document.querySelectorAll(
          '#approvalContentCommon input[name^="approverDTOList"][name$=".username"]'
      );

      let confirmed = false;

      if (approverHiddenInputs.length === 0) {
        console.log(2);

        confirmed = await Swal.fire({
          text: "결재선을 지정하지 않고 결재를 요청하면 즉시 승인 처리됩니다. 요청을 진행하시겠습니까?",
          icon: "question",
          showCancelButton: true,
          confirmButtonText: "요청",
          cancelButtonText: "취소",
          reverseButtons: true
        }).then(result => {
          if (result.isConfirmed) return true;
          else return false;
        });
      } else {
        confirmed = true;
      }

      if(confirmed) {
        // 1. 결재 공통내용 가져오기
        const commonForm = document.querySelector('#approvalContentCommon');
        const formData = new FormData(commonForm);

        // 2. 결재 상세내용 가져오기
        const formByType = document.querySelector('#approvalContentByType');
        const formData2 = new FormData(formByType);

        // 3. formData2를 json 문자열로 formData에 추가
        // 1) formData2를 일반 객체로 변환
        const data2 = {};
        for (const [key, value] of formData2.entries()) {
          data2[key] = value;
        }
        // 2) JSON 문자열로 변환
        const jsonData2 = JSON.stringify(data2);
        // 3) formData에 추가
        formData.append('approvalContent', jsonData2);

        // 3. FilePond에서 파일 가져오기
        const pondFiles = pond.getFiles();

        // 4. formData에 파일 추가
        for (let i = 0; i < pondFiles.length; i++) {
          formData.append('files', pondFiles[i].file);
        }

        // 버튼 종류(결재 요청/임시저장)에 따라 status를 다르게 지정
        if(btn.id === 'tempSave') formData.append('status', 't');

        // 3. ajax 요청
        fetch('/approval', {
          method: 'POST',
          body: formData
        })
            .then(r => r.json())
            .then(r => {
              console.log(r);
              console.log(r.approvalId);

              location.href=`/approval/${r.approvalId}`;
            })
        ;
      }

    }

  });
});

/**
 * 첨부파일 삭제
 */
const fileDelBtns = document.querySelectorAll('.btn-del-file');
fileDelBtns.forEach((btn) => {
  btn.addEventListener('click', function () {

    Swal.fire({
      text: "파일을 삭제하시겠습니까?",
      icon: "question",
      showCancelButton: true,
      confirmButtonText: "삭제",
      cancelButtonText: "취소",
      reverseButtons: true
    }).then(result => {
      if (result.isConfirmed) {

        const fileId = btn.getAttribute('data-file-id');
        console.log('fileId', fileId);

        // 서버에 삭제 요청
        fetch(`/approval/file/${fileId}`, {
          method: 'DELETE'
        })
            .then(r => {
              if (r.ok) btn.parentElement.remove();
              else {
                Swal.fire({
                  text: '파일 삭제에 실패했습니다.',
                  icon: 'error'
                });
              }
            })
            .catch(() => {
              Swal.fire({
                text: '파일 삭제 중 오류가 발생했습니다.',
                icon: 'error'
              });
            })
        ;

      }
    });

  });
});