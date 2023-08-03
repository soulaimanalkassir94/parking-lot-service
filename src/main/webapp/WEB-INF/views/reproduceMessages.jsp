<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<link href="/css/reproduceMessages.css" rel="stylesheet" />
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
		<form action="/success" method="post">
			<div class="message-container">
				<ul name="info">
					
					<li name="service" class="service"
						value="${message.getService()}">Service:
						${message.getService()}</li>
					<li name="queue" class="queue"
						value="${message.getQueue()}">Queue:
						${message.getQueue()}</li>
					<li name="routingkey" class="routingkey"
						value="${message.getRoutingkey()}">Routingkey:
						${message.getRoutingkey()}</li>
					<li name="date" class="date" value="${message.getDate()}">Date:
						${message.getDate()}</li>
					<li name="reproduced" class="reproduced"
						value="${message.reproduced}">Reproduced:
						${message.reproduced}</li>
					<li name="inspected" class="inspected"
						value="${message.inspected}">Inspected:
						${message.inspected}</li>
				</ul>
				<textarea hidden class="id" name="id" value="${message.getId()}">${message.getId()}</textarea>
				<pre oninput="update()" id="shown-message" rows="20" columns="20"
					class="shown-message" name="shown-message" contenteditable="true"
					value="${message.getMessage()}">${message.getMessage()}
			</pre>
			<input id="submit_button" class="submit_button" type="submit"
				value=reproduce><br>
			</div>

			<textarea hidden name="hidden-message" id="hidden-message"
				value="${message.getMessage()}">${message.getMessage()}</textarea>

			

			<script>
		function update(){
			let message = document.getElementById("shown-message");
			let copy = document.getElementById("hidden-message");
			message.addEventListener("keyup", e =>{
				copy.value = message.innerText;	
			});
		}
		</script>
		</form>
	</div>
</body>
</html>