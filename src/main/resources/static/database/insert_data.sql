-- ====================================
-- 1. role
-- ====================================
INSERT INTO role (role_id, role_name) VALUES 
(1, 'ROLE_ADMIN'),
(2, 'ROLE_HR'),
(3, 'ROLE_USER');

-- ====================================
-- 2. position
-- ====================================
INSERT INTO `position` (position_id, position_name, parent_position_id, use_yn) VALUES
(1, '대표이사', NULL, b'1'),
(2, '전무', 1, b'1'),
(3, '상무', 2, b'1'),
(4, '이사', 3, b'1'),
(5, '부장', 4, b'1'),
(6, '차장', 5, b'1'),
(7, '과장', 6, b'1'),
(8, '대리', 7, b'1'),
(9, '주임', 8, b'1'),
(10, '사원', 9, b'1');

-- ====================================
-- 3. department
-- ====================================
-- 깊이 1: 본부
INSERT INTO department (department_id, department_name, parent_department_id, use_yn) VALUES
(1, '경영지원본부', NULL, b'1'),
(2, '개발본부', NULL, b'1');

-- 깊이 2: 팀
INSERT INTO department (department_id, department_name, parent_department_id, use_yn) VALUES
(11, '인사팀', 1, b'1'),
(12, '재무팀', 1, b'1'),
(21, '백엔드개발팀', 2, b'1'),
(22, '프론트엔드개발팀', 2, b'1');

-- 깊이 3: 파트
INSERT INTO department (department_id, department_name, parent_department_id, use_yn) VALUES
(111, '채용파트', 11, b'1'),
(112, '교육파트', 11, b'1'),
(121, '회계파트', 12, b'1'),
(122, '자금파트', 12, b'1'),
(211, 'API개발파트', 21, b'1'),
(212, 'DB파트', 21, b'1'),
(221, 'UI파트', 22, b'1'),
(222, '웹개발파트', 22, b'1');

-- ====================================
-- 4. employee
-- ====================================
-- 초기 관리자 계정
INSERT INTO employee (
    username, password, role_id,
    account_non_expired, account_non_locked, credentials_non_expired, enabled, use_yn
) VALUES (
    'admin', '$2a$10$Q8B0W4DbMt8Wx3sDJ6d5nOZZbep0qtjvbZogxZZIYIYKyZsnBspa6', 1,
    b'1', b'1', b'1', b'1', b'1'
);

-- 관리자 계정 1 - 경영지원본부 대표이사
INSERT INTO employee (
    username, password, name, department_id, position_id, role_id,
    birth_date, hire_date, gender, employee_type,
    mobile_phone, external_email,
    account_non_expired, account_non_locked, credentials_non_expired, enabled, use_yn,
    current_base_salary
) VALUES (
    'admin001', '$2a$10$HRpgm5uL5nBkeHnuTLEON.ysWaaZ8TbC.sSXfKQBqXfjlxJhF0pZ6', 
    '김관리', 1, 1, 1,
    '1975-01-15', '2015-01-01', 'M', '기타',
    '010-1234-5001', 'admin001@company.com',
    b'1', b'1', b'1', b'1', b'1',
    10000000
);

-- 관리자 계정 2 - 인사팀 이사
INSERT INTO employee (
    username, password, name, department_id, position_id, role_id,
    birth_date, hire_date, gender, employee_type,
    mobile_phone, external_email,
    account_non_expired, account_non_locked, credentials_non_expired, enabled, use_yn,
    current_base_salary
) VALUES (
    'admin002', '$2a$10$da2MyyPyqivDIG4wyHMUbuvipkKIsOOIlMf/9zNsxMI5hE7kBTcnK', 
    '이인사', 11, 4, 1,
    '1980-03-22', '2016-02-01', 'F', '계약',
    '010-1234-5002', 'admin002@company.com',
    b'1', b'1', b'1', b'1', b'1',
    7000000
);

-- 관리자 계정 3 - 개발본부 전무
INSERT INTO employee (
    username, password, name, department_id, position_id, role_id,
    birth_date, hire_date, gender, employee_type,
    mobile_phone, external_email,
    account_non_expired, account_non_locked, credentials_non_expired, enabled, use_yn,
    current_base_salary
) VALUES (
    'admin003', '$2a$10$wXeCXv1PRWkmlPj2iwRNB.ndr3whNNnLGcvzIQdhzmhfv0T9cKTR2', 
    '박개발', 2, 2, 1,
    '1978-07-10', '2015-03-01', 'M', '정규',
    '010-1234-5003', 'admin003@company.com',
    b'1', b'1', b'1', b'1', b'1',
    9000000
);

-- 관리자 계정 4 - 재무팀 부장
INSERT INTO employee (
    username, password, name, department_id, position_id, role_id,
    birth_date, hire_date, gender, employee_type,
    mobile_phone, external_email,
    account_non_expired, account_non_locked, credentials_non_expired, enabled, use_yn,
    current_base_salary
) VALUES (
    'admin004', '$2a$10$ONcaZRdzu5yw8WxNXtBcVu5GnHolL702OCUe.g4CkLBOFY4Gs12nG', 
    '최재무', 12, 5, 1,
    '1982-11-05', '2017-01-01', 'F', '정규',
    '010-1234-5004', 'admin004@company.com',
    b'1', b'1', b'1', b'1', b'1',
    6000000
);

-- 관리자 계정 5 - 백엔드개발팀 차장
INSERT INTO employee (
    username, password, name, department_id, position_id, role_id,
    birth_date, hire_date, gender, employee_type,
    mobile_phone, external_email,
    account_non_expired, account_non_locked, credentials_non_expired, enabled, use_yn,
    current_base_salary
) VALUES (
    'admin005', '$2a$10$hvkUTrXpVKD2Oss5rlayUuodpNc2ijF9PfwhwPD4gQdIZbviv/iPe', 
    '정백엔드', 21, 6, 1,
    '1985-09-18', '2018-06-01', 'M', '정규',
    '010-1234-5005', 'admin005@company.com',
    b'1', b'1', b'1', b'1', b'1',
    5500000
);

-- ====================================
-- 5. vacation_type
-- ====================================
INSERT INTO vacation_type (vacation_type_id, vacation_type_name, use_yn) VALUES
(1, '연차휴가', b'1'),
(2, '병가', b'1'),
(3, '경조사휴가', b'1'),
(4, '출산휴가', b'1'),
(5, '육아휴직', b'1'),
(6, '공가', b'1');

-- ====================================
-- 6. approval_form
-- ====================================
INSERT INTO approval_form (approval_form_id, form_title, use_yn) VALUES
(1, '휴가 신청', b'1'),
(2, '출장 신청', b'1'),
(3, '업무 기안', b'1');