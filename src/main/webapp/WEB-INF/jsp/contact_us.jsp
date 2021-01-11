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
<fmt:setBundle basename="error_message" var="err" />
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>About - Bookstore</title>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" type="text/css" href="/styles/contact_us.css">
    <link rel="stylesheet" type="text/css" href="/styles/home.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>

<body>

<!---------- header --------------->

<jsp:include page="header.jsp"/>

<!---------- contact us page --------------->

<div class="small-container">
    <div class="row">
        <div class="mcontainer">
            <form id="Contact" method="post" action="/home?command=send_contact_form">

                <label for="name"><fmt:message key="label.name" bundle="${lang}"/></label>
                <c:set var="your_name">
                    <fmt:message key="label.your_name" bundle="${lang}"/>
                </c:set>
                <input type="text" id="name" name="name" placeholder="${your_name}">

                <label for="email">Email</label>
                <c:set var="your_email">
                    <fmt:message key="label.your_email" bundle="${lang}"/>
                </c:set>
                <input type="email" id="email" name="email" placeholder="${your_email}">

                <label for="subject"><fmt:message key="label.subject" bundle="${lang}"/></label>
                <c:set var="write_smth">
                    <fmt:message key="label.write_smth" bundle="${lang}"/>
                </c:set>
                <textarea id="subject" name="subject" placeholder="${write_smth}" style="height:200px"></textarea>

                <pre id="errorContactUsMessage" style="color: #ff523b; margin: 20px;"></pre>
                <c:if test="${not empty error_contact_us_message}">
                    <pre style="color: #ff523b; height: 20px">${error_contact_us_message}</pre>
                    <c:remove var="error_reg_message" scope="session" />
                </c:if>
                <button type="submit" onclick="return validateContactForm(event)" class="btn"><fmt:message key="label.submit" bundle="${lang}"/></button>

            </form>
        </div>
    </div>
</div>

<!---------- footer --------------->

<jsp:include page="footer.jsp" />

<!---------- script for form validation --------------->

<script>
    function validateContactForm(event) {
        let nameField = document.forms["Contact"]["name"].value;
        let emailField = document.forms["Contact"]["email"].value;
        let subjField = document.forms["Contact"]["subject"].value;

        let error = "";

        let email_regex = /[\w-]+@[\w-]+\.[a-z]{2,5}/;
        let malicious_regex = /^[-<>*;='#)+&("]+$/;
        if (malicious_regex.test(subjField)) {
            event.preventDefault();
            error = "<fmt:message key="invalid_input_data" bundle="${err}"/>";
        }
        if (malicious_regex.test(emailField) || !email_regex.test(emailField)) {
            event.preventDefault();
            error = "<fmt:message key="incorrect_email" bundle="${err}"/>";
        }
        if (malicious_regex.test(nameField)) {
            event.preventDefault();
            error = "<fmt:message key="invalid_input_data" bundle="${err}"/>";
        }

        let whitespace_regex = /[\s]+/;
        if (subjField == "" || whitespace_regex.test(subjField)) {
            event.preventDefault();
            error = "<fmt:message key="input_subj" bundle="${err}"/>";
        }
        if (emailField == "" || whitespace_regex.test(emailField)) {
            event.preventDefault();
            error = "<fmt:message key="input_email" bundle="${err}"/>";
        }
        if (nameField == "" || whitespace_regex.test(nameField)) {
            event.preventDefault();
            error = "<fmt:message key="input_name" bundle="${err}"/>";
        }

        if (error != "") {
            $("#errorContactUsMessage").text(error);
            return false;
        }

        return true;
    }
</script>

</body>
</html>