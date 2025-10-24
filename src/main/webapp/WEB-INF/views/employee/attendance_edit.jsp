<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<h2>출퇴근 기록 수정</h2>

<form:form method="post" modelAttribute="attendance" action="/employee/${username}/attendance/edit">
    <form:hidden path="attendanceId"/>

    <label>출근 일시:</label>
    <form:input path="checkInDateTime" type="datetime-local"/><br/>

    <label>퇴근 일시:</label>
    <form:input path="checkOutDateTime" type="datetime-local"/><br/>

    <label>근무 상태:</label>
    <form:select path="workStatus">
        <form:option value="출근" label="출근"/>
        <form:option value="출근전" label="출근전"/>
        <form:option value="퇴근" label="퇴근"/>
        <form:option value="휴가" label="휴가"/>
    </form:select><br/>

    <label>휴일 여부:</label>
    <form:checkbox path="holiday"/><br/>

    <input type="submit" value="저장"/>
</form:form>
