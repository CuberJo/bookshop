<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Forgot password - Bookstore</title>
    <link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" type="text/css" href="../../styles/forgot_password.css">
    <link rel="stylesheet" type="text/css" href="/styles/home.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>

<body>

<!---------- header --------------->

<jsp:include page="header.jsp"/>

<!---------- account-page --------------->

<div class="small-container">
    <div class="row">
        <div id="sc-password">
            <h1>Reset Password</h1>
            <form class="sc-container" method="post" action="/home?command=reset_password">
                <input type="text" placeholder="Username or Email" />
                <input type="submit" value="Get New Password" />
            </form>
        </div>
    </div>
</div>

<!---------- footer --------------->

<jsp:include page="footer.jsp" />

</body>
</html>