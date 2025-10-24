-- 1. DB 생성
CREATE DATABASE corpdesk;
SHOW DATABASES;

-- 2. 유저 생성
CREATE USER 'corpdesk'@'%' IDENTIFIED BY 'corpdesk';
SELECT USER, host FROM mysql.user;

-- 3. 유저에게 권한 부여
GRANT ALL PRIVILEGES ON corpdesk.* TO 'corpdesk'@'%';
SHOW GRANTS FOR 'corpdesk'@'%';

-- 4. 권한 저장
FLUSH PRIVILEGES;