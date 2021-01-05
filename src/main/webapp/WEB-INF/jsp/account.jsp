<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Books - Bookstore</title>
    <link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" type="text/css" href="../../styles/account.css">
    <link rel="stylesheet" type="text/css" href="/styles/home.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">
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
                        <span onclick="login()">Login</span>
                        <span onclick="register()">Register</span>
                        <hr id="Indicator">
                    </div>

                    <form id="LoginForm">
                        <input type="text" name="login" placeholder="Login">
                        <input type="password" name="password" placeholder ="Password">
                        <br><pre id="errorLogMessage" style="color: #ff523b; height: 10px"></pre><br>
                        <c:if test="${not empty error_log_message}">
                            <br><pre style="color: #ff523b">${error_log_message}</pre><br>
                            <c:remove var="error_log_message" scope="session" />
                        </c:if>
                        <button type="submit" onclick="return validateLoginForm(event)" class="btn" formmethod="post" formaction="/home?command=login">Login</button>
                        <a href="/home?command=forgot_password">Forgot password</a>
                    </form>
                    <form id="RegForm">
                        <input type="text" name="name" placeholder="Name">
                        <input type="text" name="login" placeholder="Login">
                        <input type="email" name="email" placeholder="Email">
                        <input type="password" name="password" placeholder="Password">
                        <pre id="errorRegMessage" style="color: #ff523b"></pre>
                        <c:if test="${not empty error_reg_message}">
                            <pre style="color: #ff523b; height: 20px">${error_reg_message}</pre>
                            <c:remove var="error_reg_message" scope="session" />
                        </c:if>
                        <button type="submit" onclick="return validateRegisterForm(event)" class="btn" formmethod="post" formaction="/home?command=register">Register</button>
                    </form>
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

        let malicious_regex = /^[-;='#)+&("]+$/;
        if (malicious_regex.test(passField)) {
            event.preventDefault();
            error = "Incorrect password";
        }  if (malicious_regex.test(loginField)) {
            event.preventDefault();
            error = "Incorrect login";
        }

        let whitespace_regex = /[\s]+/;
        if (passField == "" || whitespace_regex.test(passField)) {
            event.preventDefault();
            error = "Please input your password";
        }
        if (loginField == "" || whitespace_regex.test(loginField)) {
            event.preventDefault();
            error = "Please input your login";
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
        let malicious_regex = /^[-;='#)+&("]+$/;
        if (malicious_regex.test(passField)) {
            event.preventDefault();
            error = "Incorrect password";
            console.log("Incorrect pass")
        }
        if (malicious_regex.test(emailField) || !email_regex.test(emailField)) {
            event.preventDefault();
            error = "Incorrect email";
            console.log("Incorrect email")
        }
        if (malicious_regex.test(loginField)) {
            event.preventDefault();
            error = "Incorrect login";
            console.log("Incorrect login")
        }
        if (malicious_regex.test(nameField)) {
            event.preventDefault();
            error = "Incorrect name";
            console.log("Incorrect name")
        }

        let whitespace_regex = /[\s]+/;
        if (passField == "" || whitespace_regex.test(passField)) {
            event.preventDefault();
            console.log(passField);
            error = "Please input your password";
            console.log("Please input your password")
        }
        if (emailField == "" || whitespace_regex.test(emailField)) {
            event.preventDefault();
            error = "Please input your email";
            console.log("Please input your email")
        }
        if (loginField == "" || whitespace_regex.test(loginField)) {
            event.preventDefault();
            error = "Please input your login";
            console.log("Please input your login")
        }
        if (nameField == "" || whitespace_regex.test(nameField)) {
            event.preventDefault();
            error = "Please input your name";
            console.log("Please input your name")
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