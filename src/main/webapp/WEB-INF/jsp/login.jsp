<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<html>--%>
<%--<head>--%>
<%--    <title>Login</title>--%>
<%--    <script src="https://www.google.com/recaptcha/api.js" async defer></script>--%>
<%--    <script>--%>
<%--        // function onSubmit(token) {--%>
<%--        //     document.getElementById("loginForm").submit();--%>
<%--        // }--%>
<%--    </script>--%>
<%--</head>--%>
<%--<body>--%>
<%--<form name="loginForm" action="/login" method="post">--%>
<%--    <input type="hidden" name="command" value="login">--%>
<%--    Login:<br>--%>
<%--    <input type="text" name="login">--%>
<%--    <br>Password:<br>--%>
<%--    <input type="password" name="password" >--%>
<%--    <br>--%>
<%--    ${errorLoginPassMessage}--%>
<%--    <br>--%>
<%--    ${wrongAction}--%>
<%--    <br>--%>
<%--    ${nullPage}--%>
<%--    <br>--%>
<%--&lt;%&ndash;    <div class="g-recaptcha"&ndash;%&gt;--%>
<%--&lt;%&ndash;&lt;%&ndash;         data-sitekey="6LeMOTYaAAAAACydRxKNWXqz25PSNXfiOzFDuXfR"></div>&ndash;%&gt;&ndash;%&gt;--%>

<%--&lt;%&ndash;    <div class="g-recaptcha"&ndash;%&gt;--%>
<%--&lt;%&ndash;         data-sitekey="6LeMOTYaAAAAACydRxKNWXqz25PSNXfiOzFDuXfR" &lt;%&ndash;"6LfLHzYaAAAAAHTl9Fe3pbqnQeIXQ3fNcqE-ePKd"&ndash;%&gt;&ndash;%&gt;--%>
<%--&lt;%&ndash;&lt;%&ndash;                                 data-callback="onSubmit"&ndash;%&gt;&ndash;%&gt;--%>
<%--&lt;%&ndash;             data-size="invisible">&ndash;%&gt;--%>
<%--&lt;%&ndash;    </div>&ndash;%&gt;--%>
<%--    <div class="g-recaptcha" data-sitekey="6LfLHzYaAAAAAHTl9Fe3pbqnQeIXQ3fNcqE-ePKd"></div>--%>

<%--    <input type="submit" value="Log in">--%>
<%--</form>--%>
<%--&lt;%&ndash;<button>&ndash;%&gt;--%>
<%--&lt;%&ndash;    <jsp:include page="/login" flush="true" />&ndash;%&gt;--%>
<%--&lt;%&ndash;</button>&ndash;%&gt;--%>
<%--&lt;%&ndash;&lt;%&ndash;<button>&ndash;%&gt;&ndash;%&gt;--%>
<%--&lt;%&ndash;<jsp:forward page="/login" ></jsp:forward>&ndash;%&gt;--%>
<%--&lt;%&ndash;&lt;%&ndash;</button>&ndash;%&gt;&ndash;%&gt;--%>
<%--&lt;%&ndash;&lt;%&ndash;<a href="${pageContext.request.contextPath}/login">login</a>&ndash;%&gt;&ndash;%&gt;--%>
<%--<br>--%>
<%--</body>--%>
<%--</html>--%>
<html>
<head>
    <title>reCAPTCHA demo: Simple page</title>
    <script src="https://www.google.com/recaptcha/api.js" async defer></script>
    <script>
        function onSubmit(token) {
            document.getElementById("demo-form").submit();
        }
    </script>
</head>
<body>
<form id='demo-form' action="login" method="POST">
    <button class="g-recaptcha" data-sitekey="6LfLHzYaAAAAAHTl9Fe3pbqnQeIXQ3fNcqE-ePKd" data-callback='onSubmit'>Submit</button>
    <br/>
</form>
</body>
</html>









