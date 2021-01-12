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
    <title>Personal page - Bookstore</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/home.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/personal_page.css">
    <link href="https://fonts.googleapis.com/css?family=Montserrat:200,400,700" rel="stylesheet">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <link href="https://fonts.googleapis.com/css?family=Roboto:700" rel="stylesheet">

</head>


<body>

<!---------- header --------------->

<jsp:include page="header.jsp"/>

<style>
    .acc {
        border: 2px solid rebeccapurple;
        /*display: flex;*/
        flex-basis: 250px;
        flex-direction: column;
        /*align-items: flex-start;*/
        /*justify-content: left;*/
    }
</style>

<div class="small-container">
    <div class="row">
        <h3><fmt:message key="label.account" bundle="${lang}"/></h3>
        <table>
            <tr>
                <td>Login</td>
                <td> ${login}</td>
            </tr>
            <tr>
                <td>Books</td>
                <td><c:forEach var="book" items="${library}">
                    <div><a href="">${book.title} - ${book.author}</a></div>
                </c:forEach></td>
            </tr>
            <tr>
                <td>IBANs</td>
                <td><c:forEach var="iban" items="${ibans}">
                    <div>${iban}</div>
                </c:forEach></td>
            </tr>
            <tr>
                <td>
<%--                    <form method="get" action="/home?command=add_iban">--%>
<%--                        <button class="btn-link"><fmt:message key="label.add_bank_acc" bundle="${lang}"/></button>--%>
                        <a href="/home?command=add_iban&getAddIBANPage=getAddIBANPage&additional_iban=additional_iban"><fmt:message key="label.add_bank_acc" bundle="${lang}"/></a>
<%--                        <input type="hidden" value="getAddIBANPage">--%>
<%--                    </form>--%>
                <br><a href="/home?command=logout"><fmt:message key="label.log_out" bundle="${lang}"/></a></td>
            </tr>
        </table>
    </div>
</div>

<!---------- footer --------------->

<jsp:include page="footer.jsp"/>

</body>
</html>
