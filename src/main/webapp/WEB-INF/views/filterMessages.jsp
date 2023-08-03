<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<link href="/css/filterMessages.css" rel="stylesheet" />
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<div class="bg">
		<div class="title-div">
			<p class="title">PARKINGLOT SERVICE</p>
			<p class="subtitle">MESSAGE PROCESSOR</p>
			<img src="/images/trianel-logo.png" alt="trianel-logo" class="logo">
		</div>
		<form action="/inspectMessages" method="post" class="options"
			name="options">
			<div class="checkbox-div">
				<!-- reproduced checkbox -->
				<label class="reproduced-switch"> <input type="checkbox"
					name="reproduced"> <span class="reproduced-slider round">
				</span>
				</label>

				<!-- reproduced label -->
				<label class="reproduced-label">reproduced</label>

				<!-- inspected checkbox -->
				<label class="inspected-switch"> <input type="checkbox"
					name="inspected"> <span class="inspected-slider round">
				</span>
				</label>

				<!-- inspected label -->
				<label class="inspected-label">inspected</label>
			</div>
			
			<!-- service label -->
			<label class="service-label">Choose a Service:</label><br>
			<select class="servicename" name="servicename">
				<option value="all-services">all services</option>
				<c:forEach items="${servicename}" var="servicename">
					<option value="${servicename}">${servicename}</option>
				</c:forEach>
			</select>
			 
			 <!-- parkinglot label -->
			<label class="parkinglot-label">Choose a Service:</label><br>
			<select class="parkinglot" name="parkinglot">
				<option value="all-parking-lots">all parking lots</option>
				<c:forEach items="${parkinglot}" var="parkinglot">
					<option value="${parkinglot}">${parkinglot}</option>
				</c:forEach>
			</select>
			
			
			 <label for="before" class="before-label">Before:</label> <input type="date"
				class="before" name="before">
				 <label for="after" class="after-label">After:</label>
			<input type="date" class="after" name="after">
			
			 <input class="filter-button"
				type="submit" value="filter" name="submit">
		</form>
	</div>
</body>
</html>