<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Choose IBAN - Bookstore</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/home.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/choose_iban.css">
    <link href="https://fonts.googleapis.com/css?family=Montserrat:200,400,700" rel="stylesheet">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <%--    <!--  -->--%>
    <%--    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>--%>
    <%--    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>--%>
    <%--    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>--%>
    <%--    <!--  -->--%>
    <link href="https://fonts.googleapis.com/css?family=Roboto:700" rel="stylesheet">

</head>


<body>

<!---------- header --------------->

<jsp:include page="header.jsp"/>

<div class="small-container">
    <div class="row">
        <div class="control-group">
            <h1>Radio buttons</h1>
            <label class="control control--radio">First radio
                <input type="radio" name="radio" checked="checked"/>
                <div class="control__indicator"></div>
            </label>
            <label class="control control--radio">Second radio
                <input type="radio" name="radio"/>
                <div class="control__indicator"></div>
            </label>
            <label class="control control--radio">Disabled
                <input type="radio" name="radio2" disabled="disabled"/>
                <div class="control__indicator"></div>
            </label>
            <label class="control control--radio">Disabled & checked
                <input type="radio" name="radio2" disabled="disabled" checked="checked"/>
                <div class="control__indicator"></div>
            </label>
        </div>
    </div>
</div>

<!---------- footer --------------->

<jsp:include page="footer.jsp"/>

</body>
</html>
