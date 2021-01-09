<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@ taglib prefix = "ex" uri = "/WEB-INF/tlds/custom.tld"%>

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
    <title>Cart - Bookstore</title>
    <link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" type="text/css" href="../../styles/cart.css">
    <link rel="stylesheet" type="text/css" href="../../styles/home.css">
    <link rel="stylesheet" type="text/css" href="../../styles/books.css">
    <link rel="stylesheet" type="text/css" href="../../styles/book-details.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>

<body>

<!---------- header --------------->

<jsp:include page="header.jsp" />

<!---------- cart item details --------------->

<div class="small-container cart-page">
    <c:choose>
    <c:when test="${not empty cart}">
        <table style="margin: 40px auto">
            <tr>
                <th><fmt:message key="label.book" bundle="${lang}"/></th>
                <th><fmt:message key="label.price" bundle="${lang}"/></th>
            </tr>
            <c:forEach var="book" items="${cart}">
                <tr>
                    <td><c:if test="${not empty book.base64Image}">
                             <div class="cart-info" style="flex-basis: 20%">
                                <img src="data:image/jpg;base64,${book.base64Image}">
                                <div>
                                    <p>${book.title}</p>
                                    <small><fmt:message key="label.price" bundle="${lang}"/>: $${book.price}</small>
                                    <br>
                                    <c:set var="boot_to_remove" scope="session" value="${book}"/>
                                    <form method="post" action="/home?command=remove_from_cart">
                                        <button type="submit" style="cursor: pointer; color: #ff523b; font-size: 12px; background: none; border: none"><fmt:message key="label.rmv" bundle="${lang}"/></button>
                                    </form>
                                </div>
                            </div>
                    </c:if></td>
                    <td>$${book.price}</td>
                </tr>
            </c:forEach>
        </table>

        <div class="total-price">
            <table>
                <tr>
                    <td><fmt:message key="label.subtotal" bundle="${lang}"/></td>
                    <td>$<ex:totalPriceCounter cart="${cart}"/></td>
                </tr>
                <tr>
                    <td><fmt:message key="label.discount" bundle="${lang}"/></td>
                    <c:set var="discount" value="5.00"/>
                    <c:forEach var="book" items="${cart}">

                    </c:forEach>
                    <td>$${discount}</td>
                </tr>
                <tr>
                    <td><fmt:message key="label.total" bundle="${lang}"/></td>
                    <td>$<ex:countDiscount discount="${discount}" cart="${cart}"/></td>
                </tr>
                <tr>
                    <td></td>
                    <td><a href="/home?command=purchase" class="btn"><fmt:message key="label.purchase" bundle="${lang}"/></a></td>
<%--                    <td><form method="post" action="/home?command=purchase">--%>
<%--                        <button type="submit" style="cursor: pointer; color: #ff523b; font-size: 12px; background: none; border: none"><fmt:message key="label.purchase" bundle="${lang}"/></button>--%>
<%--                    </form></td>--%>
<%--                    <td><button type="submit" style="cursor: pointer; border: none; outline: none" class="btn"><fmt:message key="label.purchase" bundle="${lang}"/></button></td>--%>
                </tr>
            </table>
        </div>
    </c:when>
    <c:otherwise>
        <div class="row" style="margin: 100px auto">
            <div class="mcontainer" align="center">
                <br>
                <h1><fmt:message key="empty_cart" bundle="${mes}"/></h1>
                <br>
                <p><fmt:message key="seem_you_havent_chosen_book" bundle="${mes}"/></p>
                <div style="height: 30px"></div>
                <a href="/home?command=books" class="btn"><fmt:message key="go_to_store" bundle="${mes}"/></a>
                <div style="height: 50px"></div>
            </div>
        </div>
    </c:otherwise>
    </c:choose>
</div>

<!---------- footer --------------->

<jsp:include page="footer.jsp" />

</body>
</html>