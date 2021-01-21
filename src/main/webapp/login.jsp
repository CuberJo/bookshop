<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <script src="https://www.google.com/recaptcha/api.js" async defer></script>

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
    <div class="g-recaptcha"
         data-sitekey=<%--"6LeMOTYaAAAAACydRxKNWXqz25PSNXfiOzFDuXfR" --%>"6LfLHzYaAAAAAHTl9Fe3pbqnQeIXQ3fNcqE-ePKd"
    <%--                             data-callback="onSubmit"--%>
         data-size="invisible">
    </div>
    <input type="submit" value="Log in">
</form>
<%--<button>--%>
<%--    <jsp:include page="/login" flush="true" />--%>
<%--</button>--%>
<%--&lt;%&ndash;<button>&ndash;%&gt;--%>
<%--<jsp:forward page="/login" ></jsp:forward>--%>
<%--&lt;%&ndash;</button>&ndash;%&gt;--%>
<%--&lt;%&ndash;<a href="${pageContext.request.contextPath}/login">login</a>&ndash;%&gt;--%>
<br>
</body>
</html>
