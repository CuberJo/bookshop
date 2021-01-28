<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>


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
    <title>Finished purchase - Bookstore</title>
    <link href="https://fonts.googleapis.com/css?family=Montserrat:200,400,700" rel="stylesheet">
    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon">
<%--    <link href="../../styles/home.css" rel="stylesheet" type="text/css">--%>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<%--    <!--  -->--%>
<%--    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>--%>
<%--    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>--%>
<%--    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>--%>
<%--    <!--  -->--%>
    <link href="https://fonts.googleapis.com/css?family=Roboto:700" rel="stylesheet">
<%--    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/account.css">--%>


</head>


<body>

<style>
    @import url('https://fonts.googleapis.com/css2?family=Castoro&display=swap');

    * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
    }
    body {
        font-family: 'Castoro', serif;
    }
    .navbar {
        display: flex;
        align-items: center;
        padding: 20px;
    }
    /* when bootstrap is included we need to be color attribute set not to let bootstrap override my own css on personal page */
    /*.navbar a:hover {*/
    /*    text-decoration: none;*/
    /*    color: #555;*/
    /*}*/
    /*.footer a:hover {*/
    /*    text-decoration: none;*/
    /*    color: #555;*/
    /*}*/
    /*******************************************************************************************************/
    nav {
        flex: 1;
        text-align: right;
    }
    nav ul {
        display: inline-block;
        list-style-type: none;
    }
    nav ul li {
        display: inline-block;
        margin-right: 20px;
    }
    a {
        text-decoration: none;
        color: #555;
    }
    p {
        color: #555;
    }
    .container {
        max-width: 1300px;
        margin: auto;
        padding-left: 25px;
        padding-right: 25px;
    }
    .row {
        display: flex;
        align-items: center;
        flex-wrap: wrap;
        justify-content: space-around;
    }
    .col-2 {
        flex-basis: 50%;
        min-width: 300px;
    }
    .col-2 img {
        max-width: 100%;
        padding: 50px 0;
    }
    .col-2 h1 {
        font-size: 50px;
        line-height: 60px;
        margin: 25px 0;
    }
    .col-2 p {
        margin: 25px 0;
    }
    .btn {
        display: inline-block;
        background: #ff523b;
        color: #fff;
        padding: 8px 30px;
        margin: 30px 0;
        border-radius: 30px;
        transition: background .5s;
        cursor: pointer;
        border: none;
        outline: none;
    }
    .btn:hover {
        background: #563434;
    }
    /* when bootstrap is included we need to be color attribute set not to let bootstrap override my own css on personal page */
    /*input[type=search] {*/
    /*    !*border-color: #4F4F4F;*!*/
    /*    all: revert;*/
    /*    outline: none;*/
    /*    height: 19px;*/
    /*    width: 173px;*/
    /*}*/
    /*******************************************************************************************************/
    .header {
        background: radial-gradient(#fff, #ffd6d6);
    }
    .header .row {
        margin-top: 70px;
    }

    /*----------  genres ---------------*/

    .genres {
        margin: 70px 0;
    }
    .col-3 {
        flex-basis: 30%;
        min-width: 250px;
        margin-bottom: 30px;
        transition: transform .5s;
    }
    .col-3 img {
        width: 100%;
    }
    .small-container {
        max-width: 1080px;
        margin: auto;
        padding-left: 25px;
        padding-right: 25px;
    }
    .genres .small-container .col-3:hover {
        transform: translateY(-5px);
    }


    /*---------- Bestsellers ---------------*/

    .small-container.s {
        max-width: 1300px;
        /* border: 2px solid red; */
    }
    .col-4 {
        flex-basis: 15%;
        padding: 10px;
        min-width: 200px;
        margin-bottom: 50px;
        transition: transform .5s;
    }
    .col-4 img {
        width: 100%;
    }
    .title {
        text-align: center;
        margin: 0 auto 80px;
        position: relative;
        line-height: 60px;
        color: #555;
    }
    /********************************************************************************************************************/
    .title::after {
        content: '';
        background: #ff523b;
        width: 80px;
        height: 5px;
        border-radius: 5px;
        position: absolute;

        bottom: 0;
        left: 50%;
        transform: translateX(-50%);
    }
    /********************************************************************************************************************/
    h4 {
        color: #555;
        font-weight: normal;
    }
    .col-4 p {
        font-size: 14px;
    }
    .rating .fa {
        color: #ff523b;
    }
    .col-4:hover {
        transform: translateY(-5px);
    }

    /*---------- Offer ---------------*/

    .offer {
        background: radial-gradient(#fff, #ffd6d6);
        margin-top: 80px;
        padding: 30px 0;
    }
    .col-2 .offer-img {
        padding: 50px;
    }
    small {
        color: #555;
        position: absolute;
    }
    /********************************************************************************************************************/
    .offer .small-container .row .col-2 a {
        position: relative;
        top: 50px;
    }
    /********************************************************************************************************************/

    /*---------- footer ---------------*/

    .footer .row {
        align-items:stretch;
    }
    .footer {
        background: #000;
        color: #8a8a8a8a;
        font-size: 14px;
        padding: 60px 0 20px;
    }
    .footer p {
        color: #8a8a8a8a;
    }
    .footer h3 {
        color: #fff;
        margin-bottom: 20px;
    }
    .footer-col-1, .footer-col-2, .footer-col-3, .footer-col-4 {
        min-width: 250px;
        margin-bottom: 20px;
    }
    .footer-col-1 {
        flex-basis: 30%;
    }
    .footer-col-2 {
        flex: 1;
        text-align: center;
    }
    .footer-col-2 img {
        width: 180px;
        margin-bottom: 20px;
    }
    .footer-col-3, .footer-col-4 {
        flex-basis: 12%;
        text-align: center;
    }
    /* when bootstrap is included we need to be color attribute set not to let bootstrap override  my own css on personal page */
    /*footer-col-3 a {*/
    /*    color: #8A8A8A8A;*/
    /*}*/
    /*******************************************************************************************************/
    ul {
        list-style-type: none;
    }
    .app-logo {
        margin-top: 20px;
    }
    .app-logo img {
        width: 140px;
    }
    .footer hr {
        border: none;
        background: #b5b5b5;
        height: 1px;
        margin: 20px 0;
    }
    .copyright {
        text-align: center;
    }
    .menu-icon {
        width: 28px;
        margin-left: 20px;
        display: none;
    }

    /*---------- for locale buttons -----------------*/


    li form {
        display: inline-block;
    }
    .localeBtn {
        border: none;
        text-align: center;
        text-decoration: none;
        background-color: transparent;
        cursor: pointer;
    }

    /*---------- media query for menu ---------------*/

    @media only screen and (max-width: 800px) {

        nav ul {
            position: absolute;
            top: 115px;
            left: 0;
            background: #333;
            width: 100%;
            overflow: hidden;
            transition: max-height .5s;

            /*---------- to display over bootstrap tabs in personal page -------*/
            z-index: 3;
        }
        nav ul li {
            display: block;
            margin-right: 50px;
            margin-top: 10px;
            margin-bottom: 10px;
        }
        nav ul li a {
            color: #fff;
        }
        .menu-icon {
            display: block;
            cursor: pointer;
        }
        .footer img {
            width: 280px;
            height: 215px;
        }

        /*---------- for locale buttons -----------------*/

        .localeBtn {
            color: #fff;
        }

        /*---------- for search field in search page -----------------*/

        .form-control #search {
            min-width: 250px;
        }
    }

    /*---------- media query for menu less than 60px screen size ---------------*/

    @media only screen and (max-width: 600px) {

        .row {
            text-align: center;
        }
        .col-2, .col-3, .col-4 {
            flex-basis: 100%;
        }

    }

</style>

<!---------- header --------------->

<jsp:include page="header.jsp"/>

<div class="small-container">
    <div class="row">
        <div class="mcontainer" align="center">
            <br>
            <br>
            <br>
            <h1><fmt:message key="success_purchase" bundle="${mes}"/></h1>
            <br>
            <h2><fmt:message key="checkout_account" bundle="${mes}"/></h2>
            <br>
<%--            <p></p>--%>
            <div style="height: 30px"></div>
            <a href="/home?command=personal_page" class="btn"><fmt:message key="label.account" bundle="${lang}"/></a>
            <div style="height: 100px"></div>
        </div>
    </div>
</div>

<!---------- footer --------------->

<jsp:include page="footer.jsp"/>

</body>
</html>



