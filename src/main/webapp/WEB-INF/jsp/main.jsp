<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 021 21.12.20
  Time: 19:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
Main
<jsp:useBean id="calendar" class="java.util.GregorianCalendar" />
<form name="Simple" action="timeaction" method="post">
    <input type="hidden" name="time" value="${calendar.timeInMillis}" />
    <input type="submit" name="button" value="Count time">
</form>

<form action="loginForm" method="post" action="controller">
    <input type="hidden" name="command" value="logout">
    <br>
    <input type="submit" value="Logout">
</form>
</body>
</html>
