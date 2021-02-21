<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<%--
	isErrorPage="true" 
	내장객체 exception에 접근 가능
	statusCode로 예외페이지로 넘어온 경우, exception객체는 null
 --%>
<%
	String msg = exception != null ? 
					exception.getMessage() :
						String.valueOf(response.getStatus());
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>오류 페이지</title>
<style>
body { 
	text-align: center;
}
h1 {
	color: red;
}
</style>
</head>
<body>
	<h1><%= msg %></h1>
	<a href="<%= request.getContextPath() %>">인덱스페이지로 돌아가기</a>

</body>
</html>




