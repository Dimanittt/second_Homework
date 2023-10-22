<%@ page import="entity.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
  <title>Личный кабинет</title>
</head>
<body>
<br>
<c:if test="${sessionScope.user == null}">
  <c:redirect url="login"></c:redirect>
</c:if>
<button onclick="location.href='/logout'">Выйти из аккаунта</button>
<button onclick="location.href='..'">На главную</button>
<%
  User user = (User) request.getSession().getAttribute("user");
%>
<br>
<h3>Привет, <% out.print(user.getUsername()); %>, здесь ты можешь посмотреть историю запросов или изменить свой логин и пароль</h3>
<br>
<% out.print(user.getWeatherRequests()); %>
</body>
</html>
