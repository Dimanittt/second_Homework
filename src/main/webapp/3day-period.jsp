<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <title>3 day period temperature</title>
</head>
<body>
<%
    List<String> time = (List<String>) request.getAttribute("time");
    List<Double> temperature = (List<Double>) request.getAttribute("temperature");
    List<Integer> humidity = (List<Integer>) request.getAttribute("humidity");
    List<Integer> precipitation = (List<Integer>) request.getAttribute("precipitation");

    out.println("<h4>");
    out.println("</br>");
    out.println("\n" + time.get(0).substring(0, 10) + "\n");
    out.println("</br>");
    out.println("</br>");
    for (int i = 0; i < 24; i++) {
        out.println("time: " + time.get(i).substring(11, 16) +
                " temperature — " + temperature.get(i) + "celsius, " +
                "humidity — " + humidity.get(i) + " %, " +
                "precipitation probability — " + precipitation.get(i) + " %");
        out.println("</br>");
    }
    out.println("</br>");
    out.println("\n" + time.get(25).substring(0, 10) + "\n");
    out.println("</br>");
    out.println("</br>");
    for (int i = 24; i < 48; i++) {
        out.println("time: " + time.get(i).substring(11, 16) +
                " temperature — " + temperature.get(i) + "celsius, " +
                "humidity — " + humidity.get(i) + " %, " +
                "precipitation probability — " + precipitation.get(i) + " %");
        out.println("</br>");
    }
    out.println("</br>");
    out.println("\n" + time.get(60).substring(0, 10) + "\n");
    out.println("</br>");
    out.println("</br>");
    for (int i = 48; i < 72; i++) {
        out.println("time: " + time.get(i).substring(11, 16) +
                " temperature — " + temperature.get(i) + "celsius, " +
                "humidity — " + humidity.get(i) + " %, " +
                "precipitation probability — " + precipitation.get(i) + " %");
        out.println("</br>");
    }
    out.println("</h4>");
%>
<button onclick="location.href='/'">Back to menu</button>
</body>
</html>
