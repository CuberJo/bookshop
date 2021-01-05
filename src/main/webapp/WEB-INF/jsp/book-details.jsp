<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

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

<jsp:include page="header.jsp" />

<!---------- single book details --------------->

<div class="small-container single-product">
    <div class="row">
        <div class="col-2">
            <img src="images/books/Brazen and the Beast.jpg" width="100%" id="book-img">
            <div class="small-img-row">
                <div class="small-img-col">
                    <img src="images/books/Brazen and the Beast.jpg" width="100%" class="small-img">
                </div>
                <div class="small-img-col">
                    <img src="images/books/The Three Musketeers Paperback.jpg" width="100%" class="small-img">
                </div>
                <div class="small-img-col">
                    <img src="images/books/Circe.jpg" width="100%" class="small-img">
                </div>
                <div class="small-img-col">
                    <img src="images/books/Brazen and the Beast.jpg" width="100%" class="small-img">
                </div>
            </div>
        </div>
        <div class="col-2">
            <p>Home / Love stories</p>
            <h1>Brazen and the Beast</h1>
            <h4>$50.00</h4>
            <select>
                <option>Select size</option>
                <option>XXL</option>
                <option>XL</option>
                <option>Large</option>
                <option>Medium</option>
                <option>Small</option>
            </select>
            <input type="number" value="1">
            <a href="" class="btn">Add to cart</a>

            <h3>Book details <i class="fa fa-indent"></i></h3>
            <br>
            <p>This book was written by...</p>
        </div>
    </div>
</div>

<!---------- title --------------->

<div class="small-container">
    <div class="row row-2">
        <h2>Related books</h2>
        <p>View More</p>
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