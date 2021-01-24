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
<fmt:setBundle basename="message" var="mes" />



<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Choose IBAN - Bookstore</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/home.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/choose_iban.css">
    <link href="https://fonts.googleapis.com/css?family=Montserrat:200,400,700" rel="stylesheet">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <link href="https://fonts.googleapis.com/css?family=Roboto:700" rel="stylesheet">

</head>


<body>

<!---------- header --------------->

<jsp:include page="header.jsp"/>

<div class="small-container">
    <div class="row">
        <form class="mcontainer"  method="post" action="/home?command=cart">
            <h1><fmt:message key="label.choose_iban" bundle="${lang}"/></h1>
            <br>
            <c:set var="i" value="1" scope="page"/>
            <c:forEach var="iban" items="${ibans}">
                <div class="form_radio_btn">
                    <input id="radio-${i}" type="radio" name="chosen_iban" value="${iban}" checked>
                    <label for="radio-${i}">${iban}</label>
                    <c:set var="i" value="${i+1}"/>
                </div>
            </c:forEach>
            <c:set var="back_to_choose_iban" scope="session"/>
            <input type="hidden" value="additional_iban">
            <div><button type="submit" class="btn"><fmt:message key="label.purchase" bundle="${lang}"/></button></div>
            <a href="/home?command=add_iban&getAddIBANPage=getAddIBANPage&additional_iban=additional_iban"><fmt:message key="label.add_bank_acc" bundle="${lang}"/></a>
        </form>
    </div>
</div>

<!---------- footer --------------->

<jsp:include page="footer.jsp"/>

</body>
</html>
