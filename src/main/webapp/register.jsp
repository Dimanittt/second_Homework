<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
    <title>Регистрация</title>
</head>
<body>
<c:if test="${sessionScope.user != null}">
    <c:redirect url="index.jsp"></c:redirect>
</c:if>
<br>
<h2>Чтобы зарегестрироваться достаточно придумать логин и пароль</h2>
<br>
<form action="/register" method="post">
    <label for="username"><h3>Имя пользователя:</h3>
        <input type="text" name="username" id="username" required>
    </label><br>
    <c:if test="${param.error != null}">
        <div style="color: red">
            <span>Такое имя пользователя уже существует</span>
        </div>
    </c:if>
    <label for="username"><h3>Пароль:</h3>
        <input type="password" name="password" id="password" required>
    </label>
    <button type="submit">Зарегестрироваться</button>
    <br><br>
    <button onclick="location.href='/login'">У меня уже есть аккаунта</button>

</form>
</body>
</html>
