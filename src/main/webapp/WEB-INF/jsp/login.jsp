<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<form name="loginForm" action="/login" method="post">
    <input type="hidden" name="command" value="login">
    Login:<br>
    <input type="text" name="login">
    <br>Password:<br>
    <input type="password" name="password" >
    <br>
    ${errorLoginPassMessage}
    <br>
    ${wrongAction}
    <br>
    ${nullPage}
    <br>
    <input type="submit" value="Log in">
</form>
<button>
    <jsp:include page="/login" flush="true" />
</button>
<%--<button>--%>
<jsp:forward page="/login" ></jsp:forward>
<%--</button>--%>
<%--<a href="${pageContext.request.contextPath}/login">login</a>--%>
<br>
</body>
</html>
