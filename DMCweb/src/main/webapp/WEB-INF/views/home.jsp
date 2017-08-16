<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<title>Home</title>
</head>
<body>
	<h1>Hello world!</h1>

	<P>The time on the server is ${serverTime}.</P>
	<P>Locale is ${locale}</P>
	
	<a href="<c:url value="/hello" />">Click to get Hello</a> <br>
	<a href="<c:url value="showtable/table/12" />">Show table</a> <br>
	<a href="<c:url value="showtable/listbeans" />">List Beans</a> <br>
	
</body>
</html>
