<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Current temperature in Samara</title>
</head>
<body>
<h3>This is boring page, try to use another one ;)</h3>
<%
  String time = (String) request.getAttribute("time");
  Double temperature = (Double) request.getAttribute("temperature");
  Integer humidity = (Integer) request.getAttribute("humidity");
  Integer precipitation = (Integer) request.getAttribute("precipitation");

  out.println("<h4>");
    out.println("time: " + time.substring(11, 16) +
            " temperature — " + temperature + " celsius, " +
            "humidity — " + humidity + " %, " +
            "precipitation probability — " + precipitation + " %");
  out.println("</h4>");
%>
<button onclick="location.href='/'">Back to menu</button>
</body>
</html>
