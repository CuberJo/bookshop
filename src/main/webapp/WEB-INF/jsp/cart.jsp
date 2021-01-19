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
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" type="text/css" href="../../styles/cart.css">
    <link rel="stylesheet" type="text/css" href="../../styles/home.css">
    <link rel="stylesheet" type="text/css" href="../../styles/books.css">
    <link rel="stylesheet" type="text/css" href="../../styles/book-details.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/choose_iban.css">
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
        <table  style="margin: 40px auto">
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
<%--                                    <small><fmt:message key="label.price" bundle="${lang}"/>: $${book.price}</small>--%>
                                    <br>
<%--                                    <c:set var="book_to_remove" scope="session" value="${book}"/>--%>
<%--                                    <form method="post" action="/home?command=remove_from_cart">--%>
                                        <button id="${book.ISBN}" type="submit" style="cursor: pointer; color: #ff523b; font-size: 12px; background: none; border: none">
<%--                                            ${book.ISBN}--%>
                                            <fmt:message key="label.rmv" bundle="${lang}"/>
                                        </button>
<%--                                    </form>--%>
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
                    <td><c:choose>
                        <c:when test="${not empty ibans and empty chosen_iban}">
                            <a href="/home?command=choose_iban" class="btn"><fmt:message key="label.choose_iban" bundle="${lang}"/></a>
                        </c:when>
                        <c:when test="${not empty chosen_iban}">
<%--                            <div>--%>
<%--                                <fmt:message key="label.chosen_iban" bundle="${lang}"/> ${chosen_iban}--%>
<%--                            </div>--%>
                            <b><fmt:message key="label.chosen_iban" bundle="${lang}"/> ${chosen_iban}</b>
                            <form method="post" action="/home?command=purchase">
                                <button class="btn"><fmt:message key="label.purchase" bundle="${lang}"/></button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <c:set var="back_to_cart" scope="session"/>
                            <c:set var="getAddIBANPage" scope="session"/>
                            <c:set var="fromCartPage" scope="session"/>
<%--                            <td><a href="/home?command=add_iban" class="btn"><fmt:message key="label.add_bank_acc" bundle="${lang}"/></a></td>--%>
                            <a href="/home?command=add_iban" class="btn"><fmt:message key="label.add_bank_acc" bundle="${lang}"/></a>
                        </c:otherwise>
                    </c:choose></td>
                </tr>
            </table>
        </div>
    </c:when>
    <c:otherwise>
        <div class="row" style="margin: 30px auto">
            <div class="mcontainer" style="box-shadow: none; border: none" align="center">
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


<script id="s">
    $(document).ready(function () {
        <c:forEach var="book" items="${cart}">
        $('#${book.ISBN}').bind('click', function () {
            $.ajax({
               url: 'http://localhost:8080/home?command=remove_from_cart',
               type: 'POST',
                data: ({isbn: '${book.ISBN}'}),
                success: function () {
                   $('.small-container').load(' .small-container');
                    $('#s').load('#s');
                }
            });
        })
        </c:forEach>
    })
</script>

<script>
    $(document).ready(function getIBANs() {
        $.ajax({
            url: 'http://localhost:8080/load_ibans',
            type: 'GET',
            success: function () {
                console.log('success');
                $('.total-price').load(' .total-price');
                // $('#s').load('#s');
            }
        });
    })
</script>

</body>
</html>