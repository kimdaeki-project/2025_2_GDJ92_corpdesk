<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="d" uri="http://java.sun.com/jsp/jstl/core" %>

<style>

.addBox{

	margin: 0 auto;
	width:40%;

}

.error {
        color: red;
        font-size: 0.9em;
        margin-left: 5px;
    }
    label.required::after {
        content: "*";
        color: red;
        margin-left: 5px;
    }

.buttonBox{
	width: 33%;
	margin: 0 auto;

}

</style>
<head>
	<meta charset="UTF-8">
	<title>사원 등록</title>
	<c:import url="/WEB-INF/views/include/head.jsp"/>
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/> 

	<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

	<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

		<c:import url="/WEB-INF/views/include/header.jsp"/>
	
		<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
			<!-- 내용 시작 -->


	<div class="addBox">
	 <div class="card card-default">
	  <div class="card-body">
			<h2 class="card-title">사원 등록</h2>
		<p>*항목은 필수 입력 항목입니다</p>
		<form:form id="addEmployeeForm" method="post" modelAttribute="employee">
		    <label class="required">이름</label> 
		    <form:input path="name" class="form-control"/><span class="error"><form:errors path="name"/></span><br/>
		
		    <label class="required">아이디(로그인용)</label> 
		    <form:input path="username" class="form-control"/><span class="error"><form:errors path="username"/></span><br/>
		
		    <label class="required">비밀번호</label> 
		    <form:password path="password" class="form-control"/><span class="error"><form:errors path="password"/></span><br/>
		
		    이메일 <form:input path="externalEmail" class="form-control"/><span class="error"><form:errors path="externalEmail"/></span><br/>
		    입사일 <form:input path="hireDate" type="date" class="form-control"/><span class="error"><form:errors path="hireDate"/></span><br/>
		    
		    성별 <form:select path="gender" class="form-control">
		            <form:option value="" label="선택"/>
		            <form:option value="M" label="남"/>
		            <form:option value="F" label="여"/>
		         </form:select><span class="error"><form:errors path="gender"/></span><br/>
		
		    <label class="required">전화번호</label>
		    <form:input path="mobilePhone" class="form-control"/><span class="error"><form:errors path="mobilePhone"/></span><br/>
		
		    부서 <form:select path="departmentId" class="form-control">
		            <form:option value="" label="부서를 선택하세요"/>
		            <form:options items="${departments}" itemValue="departmentId" itemLabel="departmentName"/>
		          </form:select><span class="error"><form:errors path="departmentId"/></span><br/>
		
		    직위 <form:select path="positionId" class="form-control">
		            <form:option value="" label="직위를 선택하세요"/>
		            <form:options items="${positions}" itemValue="positionId" itemLabel="positionName"/>
		          </form:select><span class="error"><form:errors path="positionId"/></span><br/>
		
		    계정상태 <form:select path="enabled" class="form-control">
		                <form:option value="true" label="활성"/>
		                <form:option value="false" label="비활성"/>
		              </form:select><br/>
			<div class="buttonBox">
			    <input type="submit" value="등록" class="btn btn-success"/>
			    <a href="<c:url value='/employee/list'/>" class="btn btn-primary">목록으로</a>
		    </div>
		</form:form>
		 </div>
		</div>
	</div>
			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>



</html>