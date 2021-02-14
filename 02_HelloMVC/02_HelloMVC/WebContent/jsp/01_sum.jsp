<%@page import="java.text.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import = java.util.Date %>
    
<% 
   //server단 처리 : java 코드로 연산
   
   int sum = 0;
   for(int i = 0; i <= 10; i++)
      sum += i;
   
   SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss.SSS");
   String time = sdf.format(new Date());
   
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!-- 여기에 적은 html은 자바파일 안에서 문자열로 처리가 되고 있다. _01_005fsum_jsp.java 경로에서 확인 가능 -->
<title>JSP Test</title>
<script>
window.onload = function(){
   var sum = 0;
   for(var i = 1; i <= 10; i++)
      sum += i;
   document.querySelector("span#result").innerHTML = sum;
   alert('클라이언트 계산이 끝났습니다!');
   
   var now = new Date();
   var s = now.getFullYear()
   
   document.querySelector()
};

</script>
</head>
<body>
   <h1>JSP Test</h1>
   <p>1 ~ 10까지의 합 구하기</p>
   
   <ul>
      <li>서버 : <%-- <%= sum %> --%><% out.print(sum); %></li>
      <li>클라이언트 : <span id="result"></span></li> 
   </ul>
   <p>시각표시</p>
   <ul>
   		<li>서버: <%= time %></li>
   		<li>클라이언트: <span id="time"></span></li>
   </ul>
</body>
</html>