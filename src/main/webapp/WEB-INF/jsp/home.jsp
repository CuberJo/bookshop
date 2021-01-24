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
        <title>Bookstore</title>
        <link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon">
        <link rel="stylesheet" type="text/css" href='<c:url value="/styles/popup.css"/>'>
        <link rel="stylesheet" type="text/css" href='<c:url value="/styles/home.css"/>'>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    </head>

<body>
    <script>
        // var Title = $('html').html("&nbsp;").text();
        $('body').each(function(){
            $(this).html($(this).html().replace(/&nbsp;/gi,''));
        });
    </script>
    <%--<c:set var="v" scope="application" />--%>
<%--<c:out value="${v}"/>--%>

<!---------- bind bank account --------------->

<c:if test="${need_to_link_bank_account}">
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
    <c:set var="getAddIBANPage" scope="session"/>
    <c:set var="need_to_link_bank_account" scope="session" value="false"/>
</c:if>

<!---------- popup --------------->

<div id="popup1" class="overlay">
    <div class="popup">
        <h2><fmt:message key="label.finish_registration" bundle="${lang}"/></h2>
        <a class="close" href="#">&times;</a>
        <div class="content">
            <fmt:message key="label.bind_card" bundle="${lang}"/>
            <br/>
            <a href="/home?command=add_iban" style="color: #F37326"><fmt:message key="label.bind" bundle="${lang}"/></a>
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
                    <li><a href="/home"><fmt:message key="label.home" bundle="${lang}"/></a></li>
                    <li><a href="/home?command=books"><fmt:message key="label.store" bundle="${lang}"/></a></li>
                    <li><a href="/home?command=contact_us"><fmt:message key="label.contact_us" bundle="${lang}"/></a></li>
                    <li><a href="/home?command=search&from=${param.command}"><fmt:message key="label.search_book" bundle="${lang}"/></a></li>
                    <c:choose>
                        <c:when test="${not empty sessionScope.role}">
                            <li><a href="/home?command=personal_page"><fmt:message key="label.account" bundle="${lang}"/></a></li>
                        </c:when>
                        <c:when test="${empty sessionScope.role}">
                            <li><a href="/home?command=account"><fmt:message key="label.log_in" bundle="${lang}"/></a></li>
                        </c:when>
                    </c:choose>
                    <li style="font-weight: bold"><a href="/home?command=change_locale&locale=RU&from=${param.command}">RU</a> | <a href="/home?command=change_locale&locale=RN&from=${param.command}">EN</a></li>
                </ul>
            </nav>
            <c:if test="${not empty sessionScope.login}">
                <a href="home?command=cart"><img src="/images/cart.png" width="30px" height="30px"></a>
            </c:if>
            <img src="/images/menu-icon.png" class="menu-icon" onclick="menutoggle()">
        </div>
        <div class="row">
            <div class="col-2">
                <h1><fmt:message key="label.your_cozy_book_store" bundle="${lang}"/></h1>
                <p><fmt:message key="label.fancy_read_smth" bundle="${lang}"/></p>
                <a href="/home?command=books" class="btn"><fmt:message key="label.explore_now" bundle="${lang}"/> &#8594</a>
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
            <div class="col-4"><a href="/books?genre=ROMANCE">
                <img src="images/genres/romance.png">
                <h3><fmt:message key="label.ROMANCE" bundle="${lang}"/></h3>
            </a></div>
            <div class="col-4"><a href="/books?genre=ACTION_AND_ADVENTURE">
                <img src="images/genres/action_and_adventure.png">
                <h3><fmt:message key="label.ACTION_AND_ADVENTURE" bundle="${lang}"/></h3>
            </a></div>
            <div class="col-4"><a href="/books?genre=MYSTERY_AND_THRILLER">
                <img src="images/genres/mystery_and_thriller.png">
                <h3><fmt:message key="label.MYSTERY_AND_THRILLER" bundle="${lang}"/></h3>
            </a></div>
            <div class="col-4"><a href="/books?genre=BIOGRAPHIES_AND_HISTORY">
                <img src="images/genres/biographies_and_history.png">
                <h3><fmt:message key="label.BIOGRAPHIES_AND_HISTORY" bundle="${lang}"/></h3>
            </a></div>
        </div>
        <div class="row">
            <div class="col-4"><a href="/books?genre=CHILDREN">
                <img src="images/genres/children.png">
                <h3><fmt:message key="label.CHILDREN" bundle="${lang}"/></h3>
            </a></div>
            <div class="col-4"><a href="/books?genre=FANTASY">
                <img src="images/genres/fantasy.png">
                <h3><fmt:message key="label.FANTASY" bundle="${lang}"/></h3>
            </a></div>
            <div class="col-4"><a href="/books?genre=HISTORICAL_FICTION">
                <img src="images/genres/historical_fiction.png">
                <h3><fmt:message key="label.HISTORICAL_FICTION" bundle="${lang}"/></h3>
            </a></div>
            <div class="col-4"><a href="/books?genre=HORROR">
                <img src="images/genres/horror.png">
                <h3><fmt:message key="label.HORROR" bundle="${lang}"/></h3>
            </a></div>
        </div>
        <div class="row">
            <div class="col-3"><a href="/books?genre=LITERARY_FICTION">
                <img src="images/genres/literary_fiction.png">
                <h3><fmt:message key="label.LITERARY_FICTION" bundle="${lang}"/></h3>
            </a></div>
            <div class="col-3"><a href="/books?genre=NON-FICTION">
                <img src="images/genres/non_fiction.png">
                <h3><fmt:message key="label.NON-FICTION" bundle="${lang}"/></h3>
            </a></div>
            <div class="col-3"><a href="/books?genre=SCIENCE-FICTION">
                <img src="images/genres/science_fiction.png">
                <h3><fmt:message key="label.SCIENCE-FICTION" bundle="${lang}"/></h3>
            </a></div>
        </div>
    </div>
</div>

<!---------- bestsellers --------------->

<div class="small-container s">
    <h2 class="title"><fmt:message key="label.bestsellers" bundle="${lang}"/></h2>
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
                <p><fmt:message key="label.exclusive" bundle="${lang}"/></p>
                <h1>To Kill a Mockingbird</h1>
                <small><fmt:message key="label.read_this_book" bundle="${lang}"/></small>
                <a href="" class="btn"><fmt:message key="label.buy_now" bundle="${lang}"/> &#8594;</a>
            </div>
        </div>
    </div>
</div>

<!---------- latest books --------------->

<div class="small-container">
    <h2 class="title"><fmt:message key="label.latest_products" bundle="${lang}"/></h2>
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