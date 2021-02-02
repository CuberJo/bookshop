<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 

<c:choose>
    <c:when test="${locale eq 'US'}">
        <fmt:setLocale value="en_US" />
    </c:when>
    <c:when test="${locale eq 'RU'}">
        <fmt:setLocale value="ru_RU" />
    </c:when>
</c:choose>
<fmt:setBundle basename="jsp_text" var="lang" />
<fmt:setBundle basename="message" var="msg" />



<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Forgot password - Bookstore</title>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" type="text/css" href="../../styles/forgot_password.css">
    <link rel="stylesheet" type="text/css" href="../../styles/account.css">
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
            <h1><fmt:message key="type_email_for_pass_reset" bundle="${msg}"/></h1>
            <form id="ResetPassForm"  class="sc-container" method="post" action="/home?command=reset_password">
                <input type="email" placeholder="Email" name="email"/>
                <pre id="errorResetPassMessage" style="color: #ff523b; text-align: center; margin: 10px"></pre>
                <c:if test="${not empty error_message}">
                    <pre style="color: #ff523b; height: 40px; text-align: center">${error_message}</pre>
                    <c:remove var="error_message" scope="session" />
                </c:if>
<%--                <input type="submit" onclick="validateResetPassForm(event)" value="Get New Password" />--%>
<%--                <button type="submit" onclick="return validateRegisterForm(event)" class="btn" formmethod="post" formaction="/home?command=register">Register</button>--%>
                <button type="submit" onclick="return validateResetPassForm(event)"><fmt:message key="label,get_new_pass" bundle="${lang}"/></button>
            </form>
        </div>
    </div>
</div>

<!---------- footer --------------->

<jsp:include page="footer.jsp" />


<script>
    function validateResetPassForm(event) {
        let emailField = document.forms["ResetPassForm"]["email"].value;

        console.log("hi")
        let error = "";

        let email_regex = /[\w-]+@[\w-]+\.[a-z]{2,5}/;
        let malicious_regex = /[<>*;='#)+&("]+/;
        if (malicious_regex.test(emailField) || !email_regex.test(emailField)) {
            event.preventDefault();
            error = "Incorrect email";
            console.log("Incorrect email")
        }

        let whitespace_regex = /[\s]+/;
        if (emailField == "" || whitespace_regex.test(emailField)) {
            event.preventDefault();
            error = "Please input your email";
            console.log("Please input your email")
        }

        if (error != "") {
            $("#errorResetPassMessage").text(error);
            return false;
        }

        return true;
    }
</script>

</body>
</html>