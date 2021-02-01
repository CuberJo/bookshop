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
    <title>Books - Bookstore</title>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" type="text/css" href='<c:url value="/styles/popup.css"/>'>
    <link rel="stylesheet" type="text/css" href="../../styles/book_details.css">
    <link rel="stylesheet" type="text/css" href="../../styles/book-preview.css">
    <link rel="stylesheet" type="text/css" href="../../styles/home.css">
    <link rel="stylesheet" type="text/css" href="../../styles/books.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="../../js/book-details.js"></script>
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
                    <form id="bookDetailsSearchForm" method="post" action="/home?command=books">
                        <input type="hidden" name="notAdvancedSearch" value="true">
                        <input type="search" name="str" style="outline: none" class="searchInput">
                    </form>
                </li>
                <li><a href="/home"><fmt:message key="label.home" bundle="${lang}"/></a></li>
                <li><a href="/home?command=books"><fmt:message key="label.store" bundle="${lang}"/></a></li>
                <li><a href="/home?command=contact_us"><fmt:message key="label.contact_us" bundle="${lang}"/></a></li>
                <li><a href="/home?command=search&from=${param.command}"><fmt:message key="label.search_book" bundle="${lang}"/></a></li>
                <c:choose>
                    <c:when test="${not empty sessionScope.role}">
                        <c:choose>
                            <c:when test="${sessionScope.role eq 'ADMIN'}">
                                <li><a href="/home?command=logout"><fmt:message key="label.log_out" bundle="${lang}"/></a></li>
                            </c:when>
                            <c:otherwise>
                                <li><a href="/home?command=personal_page"><fmt:message key="label.account" bundle="${lang}"/></a></li>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <li><a href="/home?command=account"><fmt:message key="label.log_in" bundle="${lang}"/></a></li>
                    </c:otherwise>
                </c:choose>
                <c:if test="${sessionScope.role eq 'ADMIN'}">
                    <li><a href="/home?command=admin"><fmt:message key="label.admin" bundle="${lang}"/></a></li>
                </c:if>
<%--                <li style="font-weight: bold"><a href="/home?command=change_locale&locale=RU&from=${param.command}&isbn=${book.ISBN}">RU</a> | <a href="/home?command=change_locale&locale=RN&from=${param.command}&isbn=${book.ISBN}">EN</a></li>--%>
                <li style="font-weight: bold">
                    <form method="post" action="/home">
                        <input type='hidden' name='command' value="change_locale">
                        <input type='hidden' name='locale' value="RU">
                        <input type='hidden' name='from' value="${param.command}">
                        <input type='hidden' name='isbn' value="${param.isbn}">
                        <button class="localeBtn" type="submit">RU</button>
                    </form>
                    |
                    <form method="post" action="/home">
                        <input type='hidden' name='command' value="change_locale">
                        <input type='hidden' name='locale' value="US">
                        <input type='hidden' name='from' value="${param.command}">
                        <input type='hidden' name='isbn' value="${param.isbn}">
                        <button class="localeBtn" type="submit">US</button>
                    </form>
                </li>
            </ul>
        </nav>
        <c:if test="${not empty sessionScope.login}">
            <a href="home?command=cart"><img src="images/cart.png" width="30px" height="30px"></a>
        </c:if>
        <img src="images/menu-icon.png" class="menu-icon" onclick="menutoggle()">
    </div>
</div>

<!---------- popup --------------->

<div id="popup1" class="overlay">
    <div class="popup">
        <h2><fmt:message key="label.book_added" bundle="${lang}"/></h2>
        <a class="close" href="#">&times;</a>
        <div class="content">
            <fmt:message key="label.check_book_in_cart" bundle="${lang}"/>
            <br/>
            <c:choose>
                <c:when test="${empty sessionScope.role}">
                    <c:set var="back_to_cart" scope="session"/>
                    <a href="/home?command=account" style="color: #F37326"><fmt:message key="label.check_out" bundle="${lang}"/></a>
                </c:when>
                <c:otherwise>
                    <a href="/home?command=cart" style="color: #F37326"><fmt:message key="label.check_out" bundle="${lang}"/></a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<!---------- preview-popup --------------->

<%--style="width: 100%; height: 100%"--%>

<div id="popup2" class="overlay">
    <div class="popup" id="preview">
<%--        <h2><fmt:message key="label.book_added" bundle="${lang}"/></h2>--%>
        <a class="close" href="#">&times;</a>
        <div class="content">
            <div class="cover">
                <div class="book">
                    <label for="page-1"  class="book__page book__page--1">
<%--                        <img src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/193203/1111.jpg" alt="">--%>
                        <img src="data:image/jpg;base64,${book.base64Image}" width="100%" id="book-img">
                    </label>

                    <label for="page-2" class="book__page book__page--4">
                        <div class="page__content">
                            <h1 class="page__content-title">I</h1>
                            <div class="page__content-blockquote" style="font-size: 14px">
                                <p class="page__content-blockquote-text" style="font-size: 14px">HARI SELDON — . . . born in the 11,988th year of the Galactic Era; died 12,069. The dates are more commonly given in terms of the current Foundational Era as -79 to the year 1 F.E. Born to middle-class parents on Helicon, Arcturus sector (where his father, in a legend of doubtful authenticity, was a tobacco grower in the hydroponic plants of the planet), he early showed amazing ability in mathematics. Anecdotes concerning his ability are innumerable, and some are contradictory. At the age of two, he is said to have. . . </p>
                                <p class="page__content-blockquote-text" style="font-size: 14px">. . . Undoubtedly his greatest contributions were in the field of psychohistory. Seldon found the field little more than a set of vague axioms; he left it a profound statistical science. . . . </p>
                                <p class="page__content-blockquote-text" style="font-size: 14px">. . . The best existing authority we have for the details of his life is the biography written by Gaal Dornick who, as a young man, met Seldon two years before the great mathematician's death. The story of the meeting . . .</p>
                                <span class="page__content-blockquote-reference" style="font-size: 14px">Encyclopedia Galactica*</span>
                            </div>
                            <div class="page__content-text" style="font-size: 14px">
                                <p>His name was Gaal Dornick and he was just a country boy who had never seen Trantor before. That is, not in real life. He had seen it many times on the hyper-video, and occasionally in tremendous three-dimensional newscasts covering an Imperial Coronation or the opening of a Galactic Council. Even though he had lived all his life on the world of Synnax, which circled a star at the edges of the Blue Drift, he was not cut off from civilization, you see. At that time, no place in the Galaxy was. </p>

                                <p>There were nearly twenty-five million inhabited planets in the Galaxy then, and not one but owed allegiance to the Empire whose seat was on Trantor. It was the last half-century in which that could be said. </p>
                                <p>To Gaal, this trip was the undoubted climax of his young, scholarly life. He had been in space before so that the trip, as a voyage and nothing more, meant little to him. To be sure, he had traveled previously only as far as Synnax's only satellite in order to get the data on the mechanics of meteor driftage which he needed for his dissertation, but space-travel was all one whether one travelled half a million miles, or as many light years. </p>
                            </div>
                            <div class="page__number">3</div>
                        </div>
                    </label>

                    <!-- Resets the page -->
                    <input type="radio" name="page" id="page-1"/>

                    <!-- Goes to the second page -->
                    <input type="radio" name="page" id="page-2"/>
                    <label class="book__page book__page--2">
                        <div class="book__page-front">
                            <div class="page__content">
                                <h1 class="page__content-book-title">Foundation</h1>
                                <h2 class="page__content-author">Isaac Asimov</h2>

                                <p class="page__content-credits">
                                    Introduction by
                                    <span>Paul Krugman</span>
                                </p>

                                <p class="page__content-credits">
                                    Illustrations by
                                    <span>Alex Wells</span>
                                </p>

                                <div class="page__content-copyright">
                                    <p>The Folio Society</p>
                                    <p>London - MMXII</p>
                                </div>
                            </div>
                        </div>
                        <div class="book__page-back">
                            <div class="page__content">
                                <h1 class="page__content-title">Contents</h1>
                                <table class="page__content-table">
                                    <tr>
                                        <td align="left">Part I</td><td align="left">The Psycohistorians</td><td align="right">3</td>
                                    </tr>
                                    <tr>
                                        <td align="left">Part II</td><td align="left">The Encyclopedists</td><td align="right">43</td>
                                    </tr>
                                    <tr>
                                        <td align="left">Part III</td><td align="left">The Mayors</td><td align="right">87</td>
                                    </tr>
                                    <tr>
                                        <td align="left">Part IV</td><td align="left">The Traders</td><td align="right">147</td>
                                    </tr>
                                    <tr>
                                        <td align="left">Part V</td><td align="left">The Merchant Princes</td><td align="right">173</td>
                                    </tr>
                                </table>

                                <div class="page__number">2</div>
                            </div>
                        </div>
                    </label>
                </div>
            </div>
        </div>
    </div>
</div>
<%----------------------------------------------------------------------------------------------------------------------------------------%>
<!---------- single book details --------------->

<div class="small-container single-product">
    <div class="row">
        <div class="col-2">
            <img src="data:image/jpg;base64,${book.base64Image}" width="100%" id="book-img">
        </div>
        <div class="col-2">
            <p><fmt:message key="label.genre" bundle="${lang}"/> / <a href="/home?command=books&genre=${book.genre.genre}&page=1"><fmt:message key="label.${book.genre.genre}" bundle="${lang}"/></a></p>
            <h1>${book.title}</h1>
            <h4>$${book.price}</h4>
            <c:set var="book_to_cart" scope="session" value="${book}"/>
<%--            <form action="/home?command=add_to_cart" method="post">--%>
            <ex:checkBookPresence library="${library}" bookToCheck="${book}"/>
<%--            <c:choose>--%>
<%--                <c:when test="${not empty library}">--%>
<%--                    <c:set var="contains" value="false" />--%>
<%--                    <c:forEach var="book" items="${library}">--%>
<%--                        <c:if test="${item eq myValue}">--%>
<%--                            <c:set var="contains" value="true" />--%>
<%--                        </c:if>--%>
<%--                    </c:forEach>--%>
<%--                    <c:choose>--%>
<%--                        <c:when test=""></c:when>--%>
<%--                    </c:choose>--%>
<%--                </c:when>--%>
<%--            </c:choose>--%>
            <c:choose>
                <c:when test="${contains}">
                    <a href="read_book?isbn=${book.ISBN}" class="btn"><fmt:message key="label.read" bundle="${lang}"/></a>
                </c:when>
                <c:when test="${sessionScope.role eq 'ADMIN'}">
                    <a href="read_book?isbn=${book.ISBN}" class="btn"><fmt:message key="label.read" bundle="${lang}"/></a>
                </c:when>
                <c:otherwise>
                    <button id="add" style="cursor: pointer; outline: none; border: none" type="submit" class="btn"><fmt:message key="label.add_to_cart" bundle="${lang}"/></button>
                </c:otherwise>
            </c:choose>
            <button id="previewBtn" style="cursor: pointer; outline: none; border: none" type="submit" class="btn"><fmt:message key="label.preview" bundle="${lang}"/></button>
<%--            </form>--%>
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
    <div id="relatedBooks" class="row">
<%--        <div class="col-4">--%>
<%--            <img src="images/books/The Three Musketeers Paperback.jpg">--%>
<%--            <h4>The Three Musketeers Paperback</h4>--%>
<%--            <div class="rating">--%>
<%--                <i class="fa fa-star"></i>--%>
<%--                <i class="fa fa-star"></i>--%>
<%--                <i class="fa fa-star"></i>--%>
<%--                <i class="fa fa-star"></i>--%>
<%--                <i class="fa fa-star-o"></i>--%>
<%--            </div>--%>
<%--            <p>$11.79</p>--%>
<%--        </div>--%>
<%--        <div class="col-4">--%>
<%--            <img src="images/books/Brazen and the Beast.jpg">--%>
<%--            <h4>Brazen and the Beast</h4>--%>
<%--            <div class="rating">--%>
<%--                <i class="fa fa-star"></i>--%>
<%--                <i class="fa fa-star"></i>--%>
<%--                <i class="fa fa-star"></i>--%>
<%--                <i class="fa fa-star"></i>--%>
<%--                <i class="fa fa-star-half-o"></i>--%>
<%--            </div>--%>
<%--            <p>$8.25</p>--%>
<%--        </div>--%>
<%--        <div class="col-4">--%>
<%--            <img src="images/books/Circe.jpg">--%>
<%--            <h4>Circe</h4>--%>
<%--            <div class="rating">--%>
<%--                <i class="fa fa-star"></i>--%>
<%--                <i class="fa fa-star"></i>--%>
<%--                <i class="fa fa-star"></i>--%>
<%--                <i class="fa fa-star-half-o"></i>--%>
<%--                <i class="fa fa-star-o"></i>--%>
<%--            </div>--%>
<%--            <p>$22.00</p>--%>
<%--        </div>--%>
<%--        <div class="col-4">--%>
<%--            <img src="images/books/To Kill a Mockingbird.jpg">--%>
<%--            <h4>To Kill a Mockingbird</h4>--%>
<%--            <div class="rating">--%>
<%--                <i class="fa fa-star"></i>--%>
<%--                <i class="fa fa-star"></i>--%>
<%--                <i class="fa fa-star"></i>--%>
<%--                <i class="fa fa-star-half-o"></i>--%>
<%--                <i class="fa fa-star-o"></i>--%>
<%--            </div>--%>
<%--            <p>$12.65</p>--%>
<%--        </div>--%>
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

<script>
    $(document).ready(function () {
        $('#add').bind('click', function () {
            $.ajax({
                // url: 'http://localhost:8080/home?command=add_to_cart',
                url: 'http://localhost:8080/add_to_cart',
                type: 'POST',
<%--                <c:if test= "${empty sessionScope.role}">--%>
<%--                    data: ({back_to_cart: "back_to_cart"}),--%>
<%--                </c:if>--%>
                success: function() {
                    $('#popup1.overlay').css({'visibility': 'visible', 'opacity': '1'});
                    $('.popup .close').click(function(){
                        $('#popup1.overlay').css({'visibility': 'hidden', 'opacity': '0'});
                    })
                }
            });
        })
        $('#previewBtn').bind('click', function () {
            $('#popup2.overlay').css({'visibility': 'visible', 'opacity': '1'});
            $('.popup .close').click(function(){
                $('#popup2.overlay').css({'visibility': 'hidden', 'opacity': '0', 'cursor': 'default'});
                $('html').css({'cursor': 'default'});
            })
        })
    })
</script>

<%--This code will disable mouse click--%>

<script>
    $(document).ready(function () {
        $("#popup2").on("contextmenu",function(e){
            return false;
        });
    });
    $(document).ready(function () {
        $('#popup2').bind('cut copy paste', function (e) {
            e.preventDefault();
        });
    });
</script>

<script>
    $(function () {
        $('#bookDetailsSearchForm').submit(function(event) {

            let whitespace_regex = /^[\s]+$/;
            if ($('.searchInput').val() === "" || whitespace_regex.test($('.searchInput').val())) {
                event.preventDefault()
            }
            let malicious_regex = /[<>*;='#)+&("]+/;
            if (malicious_regex.test($('.searchInput').val())) {
                event.preventDefault()
            }
        });
    });
</script>

</body>
</html>