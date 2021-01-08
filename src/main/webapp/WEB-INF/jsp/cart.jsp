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
    <table>
        <tr>
            <th><fmt:message key="label.book" bundle="${lang}"/></th>
            <th>Subtotal</th>
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
                                <c:set var="book_to_cart" scope="request" value="${book}"/>
                                <a href="/home?command=remove_from_cart"><fmt:message key="label.rmv" bundle="${lang}"/></a>
                            </div>
                        </div>
                </c:if></td>
                <td>${book.price}</td>
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
                <c:set var="discount" value="35.00"/>
                <c:forEach var="book" items="${cart}">

                </c:forEach>
                <td>$${discount}</td>
            </tr>
            <tr>
                <td><fmt:message key="label.total" bundle="${lang}"/></td>
                <td>$<ex:countDiscount discount="${discount}" cart="${cart}"/></td>
            </tr>
        </table>
    </div>
</div>

<!---------- footer --------------->

<jsp:include page="footer.jsp" />
git

</body>
</html>