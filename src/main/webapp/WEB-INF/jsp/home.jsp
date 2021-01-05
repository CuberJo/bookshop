<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Bookstore</title>
    <link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" type="text/css" href='<c:url value="/styles/popup.css"/>'>
    <link rel="stylesheet" type="text/css" href='<c:url value="/styles/home.css"/>'>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>

<body>
<c:if test="false">
<%--<div id="test1" onload="window.location.hash='back'; ">--%>
<script>
    $(document).ready(function(){
        $('.overlay').css({'visibility': 'visible', 'opacity': '1'});
        $('.popup .close').click(function(){
            // $('.popup .close').css({'position': 'absolute', 'top': '20px', 'right': '30px', 'transition': 'all 200ms', 'font-size': '30px', 'font-weight': 'bold', 'text-decoration': 'none', 'color': '#333'});
            $('.overlay').css({'visibility': 'hidden', 'opacity': '0'});
        })
    });
</script>
<%--    <a href="#popup1">Forgot password</a>--%>
</c:if>
<%--        popup                --%>
<div id="popup1" class="overlay">
    <div class="popup">
        <h2>Finish registration</h2>
        <a class="close" href="#">&times;</a>
        <div class="content">
            Would you like to <a href="/home?command=bank_account" style="color: #F37326">link</a> your bank card for further purchases?
        </div>
    </div>
</div>

<!---------- header --------------->

<div class="header">
    <div class="container">
        <div class="navbar">
            <div class="logo">
                <img src="/images/bookstore2.png" width="125px">
            </div>
            <nav>
                <ul id="Menuitems">
                    <li><a href="/home">Home</a></li>
                    <li><a href="/home?command=books">Store</a></li>
                    <li><a href="/home?command=contact_us">Contact us</a></li>
                    <li><a href="/home?command=search">Search</a></li>
                    <c:choose>
                        <c:when test="${not empty sessionScope.role}">
                            <li><a href="/home?command=logout">Log out</a></li>
                        </c:when>
                        <c:when test="${empty sessionScope.role}">
                            <li><a href="/home?command=account">Log in</a></li>
                        </c:when>
                    </c:choose>
                </ul>
            </nav>
            <c:if test="${not empty sessionScope.role}">
                <a href="home?command=cart"><img src="/images/cart.png" width="30px" height="30px"></a>
            </c:if>
            <img src="/images/menu-icon.png" class="menu-icon" onclick="menutoggle()">
        </div>
        <div class="row">
            <div class="col-2">
                <h1>Your cozy bookstore</h1>
                <p>Fancy read something? Do not know what to read? Here you will find an interesting book up to you!</p>
                <a href="/home?command=books" class="btn">Explore now &#8594</a>
            </div>
            <div class="col-2">
                <img src="/images/bookshelf2.png">
            </div>
        </div>
    </div>
</div>

<!---------- genres --------------->

<div class="genres">
    <div class="small-container">
        <div class="row">
            <div class="col-4"><a href="/home?command=books&genre=ROMANCE">
                <img src="images/genres/romance.png">
                <h3>ROMANCE</h3>
            </a></div>
            <div class="col-4"><a href="/home?command=books&genre=ACTION%20%26%20ADVENTURE">
                <img src="images/genres/action_and_adventure.png">
                <h3>ACTION & ADVENTURE</h3>
            </a></div>
            <div class="col-4"><a href="/home?command=books&genre=MYSTERY%20%26%20THRILLER">
                <img src="images/genres/mystery_and_thriller.png">
                <h3>MYSTERY & THRILLER</h3>
            </a></div>
            <div class="col-4"><a href="/home?command=books&genre=BIOGRAPHIES%20%26%20HISTORY">
                <img src="images/genres/biographies_and_history.png">
                <h3>BIOGRAPHIES & HISTORY</h3>
            </a></div>
        </div>
        <div class="row">
            <div class="col-4"><a href="/home?command=books&genre=CHILDREN">
                <img src="images/genres/children.png">
                <h3>CHILDREN</h3>
            </a></div>
            <div class="col-4"><a href="/home?command=books&genre=FANTASY">
                <img src="images/genres/fantasy.png">
                <h3>FANTASY</h3>
            </a></div>
            <div class="col-4"><a href="/home?command=books&genre=HISTORICAL%20FICTION">
                <img src="images/genres/historical_fiction.png">
                <h3>HISTORICAL FICTION</h3>
            </a></div>
            <div class="col-4"><a href="/home?command=books&genre=HORROR">
                <img src="images/genres/horror.png">
                <h3>HORROR</h3>
            </a></div>
        </div>
        <div class="row">
            <div class="col-3"><a href="/home?command=books&genre=LITERARY%20FICTION">
                <img src="images/genres/literary_fiction.png">
                <h3>LITERARY FICTION</h3>
            </a></div>
            <div class="col-3"><a href="/home?command=books&genre=NON%2DFICTION">
                <img src="images/genres/non_fiction.png">
                <h3>NON-FICTION</h3>
            </a></div>
            <div class="col-3"><a href="/home?command=books&genre=SCIENCE%20FICTION">
                <img src="images/genres/science_fiction.png">
                <h3>SCIENCE FICTION</h3>
            </a></div>
        </div>
    </div>
</div>

<!---------- bestsellers --------------->

<div class="small-container s">
    <h2 class="title">Bestsellers</h2>
    <div class="row bestsellers">
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
    <div class="row bestsellers">
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


<!---------- Offer --------------->

<div class="offer">
    <div class="small-container">
        <div class="row">
            <div class="col-2">
                <img src="images/books/To Kill a Mockingbird.jpg" class="offer-img">
            </div>
            <div class="col-2">
                <p>Exclusive Available on Bookstore</p>
                <h1>To Kill a Mockingbird</h1>
                <small>Read this amazing book. You will never regret. bla bla bla</small>
                <a href="" class="btn">Buy Now &#8594;</a>
            </div>
        </div>
    </div>
</div>

<!---------- latest books --------------->

<div class="small-container">
    <h2 class="title">Latest Products</h2>
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

</body>
</html>