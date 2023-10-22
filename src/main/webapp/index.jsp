<!DOCTYPE html>
<html lang="ru_RU">
<head>
    <title>Погодное приложение</title>
</head>
<br>
<h2>Выберите город, по которому хотите получить прогноз погоды :)</h2>

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
<form action="/forecast" method="post">
    Город: <input type="text" name="city"/>
    <br><br>
    Количество дней: <input type="text" name="days"/>
    <br><br>
    <input type="submit" value="Узнать"/>
</form>

<br>
<h2>Также можете выбрать один из предложенных ниже вариантов</h2>

<form action="/forecast" method="post">
    Количество дней: <input type="text" name="days"/>
    <br><br>
    <input type="submit" name="city" value="Самара"/>
    <input type="submit" name="city" value="Москва"/>
    <input type="submit" name="city" value="Санкт-Петербург"/>
    <input type="submit" name="city" value="Казань"/>
    <input type="submit" name="city" value="Владивосток"/>
</form>

</body>
</html>