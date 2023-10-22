<%@ page import="entity.User" %>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html lang="ru_RU">
<head>
    <title>Погодное приложение</title>
</head>
<br>
<button onclick="location.href='/profile'">Личный кабинет</button>
<h2>Выберите город, по которому хотите получить прогноз погоды :)</h2>

<c:if test="${sessionScope.user == null}">
    <c:redirect url="/login"></c:redirect>
</c:if>
<%
    boolean cityValidation;
    boolean daysValidation;
    if (request.getAttribute("cityValidation") == null) {
        cityValidation = true;
    } else {
        cityValidation = (boolean) request.getAttribute("cityValidation");
    }
    if (!cityValidation) {
        out.print(" <h3> К сожалению мы не смогли определить город по запросу, попробуйте еще раз :( </h3>");
    }
    if (request.getAttribute("daysValidation") == null) {
        daysValidation = true;
    } else {
        daysValidation = (boolean) request.getAttribute("daysValidation");
    }
    if (!daysValidation) {
        out.print(" <h3> Количество дней необходимо указать целым числом от 1 до 10 </h3>");
    }
%>
<br><br>
<form action="/forecast" method="post">
    Город: <input type="text" name="city" required/>
    <br><br>
    Количество дней: <input type="text" name="days" required/>
    <br><br>
    <input type="submit" value="Узнать"/>
</form>
<br>
<h2>Также можете выбрать один из предложенных ниже вариантов</h2>

<form action="/forecast" method="post">
    Количество дней: <input type="text" name="days" required/>
    <br><br>
    <input type="submit" name="city" value="Самара"/>
    <input type="submit" name="city" value="Москва"/>
    <input type="submit" name="city" value="Санкт-Петербург"/>
    <input type="submit" name="city" value="Казань"/>
    <input type="submit" name="city" value="Владивосток"/>
</form>
</body>
</html>