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

<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Books - Bookstore</title>
    <link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" type="text/css" href="../../styles/book-details.css">
    <link rel="stylesheet" type="text/css" href="../../styles/home.css">
    <link rel="stylesheet" type="text/css" href="../../styles/books.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>

<body>

<!---------- header --------------->

<div class="container">
    <div class="navbar">
        <div class="logo">
            <img src="images/bookstore2.png" width="125px">
        </div>
        <nav>
            <ul id="Menuitems">
                <li>
                    <form action="">
                        <input type="search" style="outline: none">
                    </form>
                </li>
                <li><a href="/home"><fmt:message key="label.home" bundle="${lang}"/></a></li>
                <li><a href="/home?command=books"><fmt:message key="label.store" bundle="${lang}"/></a></li>
                <li><a href="/home?command=contact_us"><fmt:message key="label.contact_us" bundle="${lang}"/></a></li>
                <li><a href="/home?command=search&from=${param.command}"><fmt:message key="label.search_book" bundle="${lang}"/></a></li>
                <c:choose>
                    <c:when test="${not empty sessionScope.role}">
                        <li><a href="/home?command=logout"><fmt:message key="label.log_out" bundle="${lang}"/></a></li>
                    </c:when>
                    <c:when test="${empty sessionScope.role}">
                        <li><a href="/home?command=account"><fmt:message key="label.log_in" bundle="${lang}"/></a></li>
                    </c:when>
                </c:choose>
                <li style="font-weight: bold"><a href="/home?command=change_locale&locale=RU&from=${param.command}&isbn=${book.ISBN}">RU</a> | <a href="/home?command=change_locale&locale=RN&from=${param.command}&isbn=${book.ISBN}">EN</a></li>
            </ul>
        </nav>
        <c:if test="${not empty sessionScope.role}">
            <a href="home?command=cart"><img src="images/cart.png" width="30px" height="30px"></a>
        </c:if>
        <img src="images/menu-icon.png" class="menu-icon" onclick="menutoggle()">
    </div>
</div>

<!---------- single book details --------------->

<div class="small-container single-product">
    <div class="row">
        <div class="col-2">
            <img src="data:image/jpg;base64,${book.base64Image}" width="100%" id="book-img">
        </div>
        <div class="col-2">
            <p><fmt:message key="label.genre" bundle="${lang}"/> / <a href="/home?command=books?genre=${book.genre.genre}"><fmt:message key="label.${book.genre.genre}" bundle="${lang}"/></a></p>
            <h1>${book.title}</h1>
            <h4>$${book.price}</h4>
            <c:set var="book_to_cart" scope="session" value="${book}"/>
            <form action="/home?command=add_to_cart" method="post">
                <button style="cursor: pointer; outline: none; border: none" type="submit" class="btn"><fmt:message key="label.add_to_cart" bundle="${lang}"/></button>
            </form>
            <br>
            <p><fmt:message key="label.author" bundle="${lang}"/> - ${book.author}</p>
        </div>
    </div>
</div>

<!---------- title --------------->

<div class="small-container">
    <div class="row row-2">
        <h2><fmt:message key="label.related_books" bundle="${lang}"/></h2>
        <p><a href="/home?command=books"><fmt:message key="label.view_more" bundle="${lang}"/></a></p>
    </div>
</div>

<div class="small-container">
    <div class="row">
        <div class="col-4">
            <img src="images/books/The Three Musketeers Paperback.jpg">
            <h4>The Three Musketeers Paperback</h4>
            <div class="rating">
                <i class="fa fa-star"></i>
                <i class="fa fa-star"></i>
                <i class="fa fa-star"></i>
                <i class="fa fa-star"></i>
                <i class="fa fa-star-o"></i>
            </div>
            <p>$11.79</p>
        </div>
        <div class="col-4">
            <img src="images/books/Brazen and the Beast.jpg">
            <h4>Brazen and the Beast</h4>
            <div class="rating">
                <i class="fa fa-star"></i>
                <i class="fa fa-star"></i>
                <i class="fa fa-star"></i>
                <i class="fa fa-star"></i>
                <i class="fa fa-star-half-o"></i>
            </div>
            <p>$8.25</p>
        </div>
        <div class="col-4">
            <img src="images/books/Circe.jpg">
            <h4>Circe</h4>
            <div class="rating">
                <i class="fa fa-star"></i>
                <i class="fa fa-star"></i>
                <i class="fa fa-star"></i>
                <i class="fa fa-star-half-o"></i>
                <i class="fa fa-star-o"></i>
            </div>
            <p>$22.00</p>
        </div>
        <div class="col-4">
            <img src="images/books/To Kill a Mockingbird.jpg">
            <h4>To Kill a Mockingbird</h4>
            <div class="rating">
                <i class="fa fa-star"></i>
                <i class="fa fa-star"></i>
                <i class="fa fa-star"></i>
                <i class="fa fa-star-half-o"></i>
                <i class="fa fa-star-o"></i>
            </div>
            <p>$12.65</p>
        </div>
    </div>

</div>

<!---------- footer --------------->

<jsp:include page="footer.jsp" />

<!---------- js for book gallery --------------->

<script type="text/javascript">
    var book_img = $('#book-img');

    var small_img = $('.small-img');

    $(small_img[0]).bind('click', function () {
        book_img.attr('src', $(small_img[0]).attr('src'));
    });
    $(small_img[1]).bind('click', function () {
        book_img.attr('src', $(small_img[1]).attr('src'));
    });
    $(small_img[2]).bind('click', function () {
        book_img.attr('src', $(small_img[2]).attr('src'));
    });
    $(small_img[3]).bind('click', function () {
        book_img.attr('src', $(small_img[3]).attr('src'));
    });

</script>

</body>
</html>