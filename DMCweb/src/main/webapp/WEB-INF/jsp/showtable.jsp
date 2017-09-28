<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Table content</title>
</head>
<body>
	
	List avaliable Beans: <br>
	<c:out value="${data}" />
	
	<c:forEach var="name" items="${namesList}">
		<tr>
			<td>${name}</td>
		</tr>
	</c:forEach>
</body>
</html>