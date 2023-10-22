<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>Авторизация</title>
</head>
<body>
<br>
<h2>Добро пожаловать в наше погодное приложение!</h2>
<h2>чтобы испытать все его прелести вы должны авторизоваться</h2>
<br>
<c:if test="${param.registerSuccess != null}">
    <div style="color: green">
        <h3>Поздравляем ${param.username}, вы успешно зарегестрированы!</h3>
    </div>
</c:if>
<form action="/login" method="post">
    <label for="username"><h3>Имя пользователя:</h3>
        <input type="text" name="username" id="username" value="${param.username}" required>
    </label><br>
    <label for="username"><h3>Пароль:</h3>
        <input type="password" name="password" id="password" required>
    </label>
    <button type="submit">Войти</button>
    <a href="/register">
        <button type="button">Регистрация</button>
    </a>
    <c:if test="${param.error != null}">
        <div style="color: red">
            <span>Некорректное имя пользователя или пароль</span>
        </div>
    </c:if>
</form>
</body>
</html>
