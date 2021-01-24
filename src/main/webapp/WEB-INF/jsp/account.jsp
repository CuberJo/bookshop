<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
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
<fmt:setBundle basename="error_message" var="err" />

<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Books - Bookstore</title>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" type="text/css" href="../../styles/account.css">
    <link rel="stylesheet" type="text/css" href="/styles/home.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<%--    <script src="https://www.google.com/recaptcha/api.js" async defer></script>--%>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">

<%--    <script>--%>
<%--        function onSubmit(token) {--%>
<%--            if (validateLoginForm(event)) {--%>
<%--                document.getElementById("LoginForm").submit();--%>
<%--            }--%>
<%--        }--%>
<%--    </script>--%>


    <script type="text/javascript">
        var onloadCallback = function() {
            grecaptcha.render('html_element', {
                'sitekey' : '6LfLHzYaAAAAAHTl9Fe3pbqnQeIXQ3fNcqE-ePKd'
            });
            grecaptcha.render('html_element2', {
                'sitekey' : '6LfLHzYaAAAAAHTl9Fe3pbqnQeIXQ3fNcqE-ePKd'
            });
        };
    </script>
</head>

<body>

<!---------- header --------------->

<jsp:include page="header.jsp"/>

<!---------- account-page --------------->

<div class="account-page">
    <div class="container">
        <div class="row">
            <div class="col-2">
                <img src="images/bookshelf2.png" width="100%">
            </div>
            <div class="col-2">
                <div class="form-container">
                    <div class="form-btn">
                        <span onclick="login()"><fmt:message key="label.sign_in_label" bundle="${lang}"/></span>
                        <span onclick="register()"><fmt:message key="label.sign_up_label" bundle="${lang}"/></span>
                        <hr id="Indicator">
                    </div>

                    <form id="LoginForm" method="post" action="/home?command=login">
                        <pre id="errorLogMessage" style="color: #ff523b"></pre>
                        <c:if test="${not empty error_log_message}">
                            <pre style="color: #ff523b">${error_log_message}</pre>
                            <c:remove var="error_log_message" scope="session" />
                        </c:if>
                        <c:set var="login">
                            <fmt:message key="label.login" bundle="${lang}"/>
                        </c:set>
                        <input type="text" name="login" placeholder="${login}">
                        <c:set var="pass">
                            <fmt:message key="label.password" bundle="${lang}"/>
                        </c:set>
                        <input type="password" name="password" placeholder="${pass}">


<%--                        <button type="submit"--%>
<%--                                class="recaptcha" data-sitekey="6LfLHzYaAAAAAHTl9Fe3pbqnQeIXQ3fNcqE-ePKd" data-callback="onSubmit"--%>
<%--                                class="btn"><fmt:message key="label.sign_in_btn" bundle="${lang}"/></button>--%>
                        <div style="transform:scale(0.85); transform-origin:0;" id="html_element"></div>
                        <button type="submit" onclick="return validateLoginForm(event)" class="btn"><fmt:message key="label.sign_in_btn" bundle="${lang}"/></button>
                        <a href="/home?command=forgot_password"><fmt:message key="label.forgot_password" bundle="${lang}"/></a>
                    </form>
                    <form id="RegForm" method="post" action="/home?command=register">
                        <pre id="errorRegMessage" style="color: #ff523b"></pre>
                        <c:if test="${not empty error_reg_message}">
                            <pre style="color: #ff523b; height: 20px">${error_reg_message}</pre>
                            <c:remove var="error_reg_message" scope="session" />
                        </c:if>
                        <c:set var="name">
                            <fmt:message key="label.name" bundle="${lang}"/>
                        </c:set>
                        <input type="text" name="name" placeholder="${name}">
                        <c:set var="login">
                            <fmt:message key="label.login" bundle="${lang}"/>
                        </c:set>
                        <input type="text" name="login" placeholder="${login}">
                        <input type="email" name="email" placeholder="Email">
                        <c:set var="pass">
                            <fmt:message key="label.password" bundle="${lang}"/>
                        </c:set>
                        <input type="password" name="password" placeholder="${pass}">

                        <div style="transform:scale(0.85); transform-origin:0;" id="html_element2"></div>

                        <button type="submit" onclick="return validateRegisterForm(event)" class="btn"><fmt:message key="label.sign_up_btn" bundle="${lang}"/></button>
<%--                        <button type="submit" onclick="return validateRegisterForm(event)" class="btn" formmethod="post" formaction="/home?command=register"><fmt:message key="label.sign_up_btn" bundle="${lang}"/></button>--%>
                    </form>
<%--                    <div id='recaptcha' class="g-recaptcha"--%>
<%--                         data-sitekey="6LfLHzYaAAAAAHTl9Fe3pbqnQeIXQ3fNcqE"--%>
<%--&lt;%&ndash;                         data-callback="onSubmit"&ndash;%&gt;--%>
<%--                         data-size="invisible"></div>--%>

<%--                    <div id='recaptcha' class="g-recaptcha"--%>
<%--                         data-sitekey="6LfLHzYaAAAAAHTl9Fe3pbqnQeIXQ3fNcqE-ePKd"--%>
<%--                    &lt;%&ndash;                         data-callback="onSubmit"&ndash;%&gt;--%>
<%--                         data-size="invisible"></div>--%>

                    <script src="https://www.google.com/recaptcha/api.js?onload=onloadCallback&render=explicit"
                            async defer>
                    </script>
<%--                    <div class="g-recaptcha"--%>
<%--                         data-sitekey=&lt;%&ndash;"6LeMOTYaAAAAACydRxKNWXqz25PSNXfiOzFDuXfR" &ndash;%&gt;"6LfLHzYaAAAAAHTl9Fe3pbqnQeIXQ3fNcqE-ePKd"--%>
<%--                    &lt;%&ndash;                             data-callback="onSubmit"&ndash;%&gt;--%>
<%--                         data-size="invisible">--%>
<%--                    </div>--%>
                </div>
            </div>
        </div>
    </div>
</div>

<!---------- footer --------------->

<jsp:include page="footer.jsp" />

<!---------- js for toggle form --------------->

<script>
    var loginForm = $('#LoginForm');
    var regForm = $('#RegForm');
    var indicator = $('#Indicator');
    function register() {
        regForm.css('transform', 'translateX(0px)');
        loginForm.css('transform', 'translateX(0px)');
        indicator.css('transform', 'translateX(100px)');
    }
    function login() {
        regForm.css('transform', 'translateX(300px)');
        loginForm.css('transform', 'translateX(300px)');
        indicator.css('transform', 'translateX(0px)');
    }
</script>

<!---------- js form validation --------------->

<script>
    function validateLoginForm(event) {
        let loginField = document.forms["LoginForm"]["login"].value;
        let passField = document.forms["LoginForm"]["password"].value;

        let error = "";

        let malicious_regex = /^[-<>*;='#)+&("]+$/;
        if (malicious_regex.test(passField)) {
            event.preventDefault();
            error = "<fmt:message key="incorrect_pass" bundle="${err}"/>";
        }  if (malicious_regex.test(loginField)) {
            event.preventDefault();
            error = "<fmt:message key="incorrect_login" bundle="${err}"/>";
        }

        let whitespace_regex = /[\s]+/;
        if (passField == "" || whitespace_regex.test(passField)) {
            event.preventDefault();
            error = "<fmt:message key="input_pass" bundle="${err}"/>";
        }
        if (loginField == "" || whitespace_regex.test(loginField)) {
            event.preventDefault();
            error = "<fmt:message key="input_login" bundle="${err}"/>";
        }

        // if ((loginField.indexOf('"') > -1 && passField.indexOf('"') > -1) ||
        //     (loginField.indexOf('\'') > -1 && passField.indexOf('\'') > -1)) {
        //     error = "Please input your name and password\n without quotes";
        // }
        // if ((loginField.indexOf('"') > -1) || (loginField.indexOf('\'') > -1)) {
        //     error = "Please input your login\n without quotes";
        // } else if ((passField.indexOf('"') > -1) || (passField.indexOf('\'') > -1)) {
        //     error = "Please input your password\n without quotes";
        // }

        if (error != "") {
            $("#errorLogMessage").text(error);
            return false;
        }

        // window.location = "/home?command=login"

        // grecaptcha.execute();

        return true;
    }
</script>

<script>
    function validateRegisterForm(event) {
        let nameField = document.forms["RegForm"]["name"].value;
        let loginField = document.forms["RegForm"]["login"].value;
        let passField = document.forms["RegForm"]["password"].value;
        let emailField = document.forms["RegForm"]["email"].value;

        let error = "";

        let email_regex = /[\w-]+@[\w-]+\.[a-z]{2,5}/;
        let malicious_regex = /^[-<>*;='#)+&("]+$/;
        if (malicious_regex.test(passField)) {
            event.preventDefault();
            error = "<fmt:message key="incorrect_pass" bundle="${err}"/>";
        }
        if (malicious_regex.test(emailField) || !email_regex.test(emailField)) {
            event.preventDefault();
            error = "<fmt:message key="incorrect_email" bundle="${err}"/>";
        }
        if (malicious_regex.test(loginField)) {
            event.preventDefault();
            error = "<fmt:message key="incorrect_login" bundle="${err}"/>";
        }
        if (malicious_regex.test(nameField)) {
            event.preventDefault();
            error = "<fmt:message key="incorrect_name" bundle="${err}"/>";
        }

        let whitespace_regex = /[\s]+/;
        if (passField == "" || whitespace_regex.test(passField)) {
            event.preventDefault();
            error = "<fmt:message key="input_pass" bundle="${err}"/>";
        }
        if (emailField == "" || whitespace_regex.test(emailField)) {
            event.preventDefault();
            error = "<fmt:message key="input_email" bundle="${err}"/>";
        }
        if (loginField == "" || whitespace_regex.test(loginField)) {
            event.preventDefault();
            error = "<fmt:message key="input_login" bundle="${err}"/>";
        }
        if (nameField == "" || whitespace_regex.test(nameField)) {
            event.preventDefault();
            error = "<fmt:message key="input_name" bundle="${err}"/>";
        }


        // if ((loginField.indexOf('"') > -1 && passField.indexOf('"') > -1) ||
        //     (loginField.indexOf('\'') > -1 && passField.indexOf('\'') > -1)) {
        //     $("#errorMessage").text("Please input your name and password\n without quotes");
        //     return false;
        // }
        // if ((loginField.indexOf('"') > -1) || (loginField.indexOf('\'') > -1)) {
        //     $("#errorMessage").text("Please input your login\n without quotes");
        //     return false;
        // } else if ((passField.indexOf('"') > -1) || (passField.indexOf('\'') > -1)) {
        //     $("#errorMessage").text("Please input your password\n without quotes");
        //     return false;
        // }

        if (error != "") {
            $("#errorRegMessage").text(error);
            return false;
        }

        // window.location = "/home?command=register"
        return true;
    }
</script>

</body>
</html>