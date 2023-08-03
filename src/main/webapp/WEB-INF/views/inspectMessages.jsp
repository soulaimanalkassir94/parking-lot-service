<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<link href="/css/inspectMessages.css" rel="stylesheet" />
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
		<c:forEach items="${filteredMessages}" var="filteredMessages">
			<form class="filtered-message-form" method="post"
				action="/reproduceMessages" target="_target">
				<textarea hidden name="id" class="id" value="${filteredMessages.getId()}">${filteredMessages.getId()}</textarea>
				<ul name="info">
					
					<li name="service" class="service"
						value="${filteredMessages.getService()}">Service:
						${filteredMessages.getService()}</li>
					<li name="queue" class="queue"
						value="${filteredMessages.getQueue()}">Queue:
						${filteredMessages.getQueue()}</li>
					<li name="routingkey" class="routingkey"
						value="${filteredMessages.getRoutingkey()}">Routingkey:
						${filteredMessages.getRoutingkey()}</li>
					<li name="date" class="date" value="${filteredMessages.getDate()}">Date:
						${filteredMessages.getDate()}</li>
					<li name="reproduced" class="reproduced"
						value="${filteredMessages.reproduced}">Reproduced:
						${filteredMessages.reproduced}</li>
					<li name="inspected" class="inspected"
						value="${filteredMessages.inspected}">Inspected:
						${filteredMessages.inspected}</li>
				</ul>
				<div class="filteredMessage">
					<pre class="message" name="message"
						value="${filteredMessages.getMessage()}">${filteredMessages.getMessage()}
					</pre>
				</div>
				
				<input class="submit_button" type="submit" value=inspect><br>
			</form>
		</c:forEach>
	</div>
</body>

</html>