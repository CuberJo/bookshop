<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
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
<fmt:setBundle basename="error_message" var="mes" />
<fmt:setBundle basename="message" var="m" />


<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Books - Bookstore</title>
    <link rel="stylesheet" type="text/css" href="../../styles/books.css">
    <link rel="stylesheet" type="text/css" href="../../styles/home.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/choose_iban.css">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">
    <!--  bootstrap -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
    <!--  -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>


<body <%--onload="fetchBooksQuantity()"--%>>

<div id="genre" style="display: none">${requestScope.genre}</div>
<div id="locale" style="display: none">${sessionScope.locale}</div>
<%--<img src="data:image/jpg;base64,${img}" width="240" height="300"/>--%>
<!---------- header --------------->

<jsp:include page="header.jsp" />

<!---------- books --------------->

<div class="small-container">

    <div class="row row-2">
        <h2>
            <fmt:message key="label.all_books" bundle="${lang}" />
        </h2>
        <select>
            <option id="default"><fmt:message key="label.default_shorting" bundle="${lang}"/></option>
            <option id="price"><fmt:message key="label.short_by_price" bundle="${lang}"/></option>
            <option id="author"><fmt:message key="label.short_by_author" bundle="${lang}"/></option>
<%--            <option><fmt:message key="label.short_by_popularity" bundle="${lang}"/></option>--%>
<%--            <option><fmt:message key="label.short_by_rating" bundle="${lang}"/></option>--%>
<%--            <option><fmt:message key="label.short_by_sale" bundle="${lang}"/></option>--%>
        </select>
    </div>

    <div id="mydiv">

    </div>

    <div class="mrow">
        <div id="toInsert" class="row">
<%--            <c:forEach var="book" items="${sessionScope.books}">--%>
<%--                <c:if test="${not empty book.base64Image}">--%>
<%--                     <div class="col-4" style="flex-basis: 20%"><a href="/home?command=book_details&isbn=${book.ISBN}">--%>
<%--    &lt;%&ndash;                <img src="../../images/library.jpg">&ndash;%&gt;--%>
<%--                        <img src="data:image/jpg;base64,${book.base64Image}">--%>
<%--                        <h4>${book.title}</h4>--%>
<%--                        <p>${book.price}$</p>--%>
<%--                    </a></div>--%>
<%--                </c:if>--%>
<%--            </c:forEach>--%>
<%--            <c:if test="${empty books}">--%>
<%--                <div class="mcontainer" style="box-shadow: none; border: none" align="center">--%>
<%--                    <h1><fmt:message key="not_found" bundle="${mes}"/></h1>--%>
<%--                    <br>--%>
<%--                    <p><fmt:message key="no_books_with_genre" bundle="${mes}"/></p>--%>
<%--                    <br>--%>
<%--                    <a href="/home" class="btn"><fmt:message key="choose_another_genre" bundle="${m}"/></a>--%>
<%--                </div>--%>
<%--            </c:if>--%>
        </div>
        <div class="pagination">
            <div class="prev"><fmt:message key="label.prev" bundle="${lang}" /></div>
            <div class="page"><fmt:message key="label.page" bundle="${lang}"/><span class="page-num"></span></div>
            <div class="next"><fmt:message key="label.next" bundle="${lang}" /></div>
        </div>
    </div>
</div>

<!---------- footer --------------->

<jsp:include page="footer.jsp" />

<!---------- js for pagination --------------->

<script src="../../js/books.js" charset="UTF-8"></script>


<script>


    <%--const bookItems=document.querySelector(".mrow .row").children;--%>
    <%--const prev=document.querySelector(".prev");--%>
    <%--const next=document.querySelector(".next");--%>
    <%--const page=document.querySelector(".page-num");--%>
    <%--const maxItem=8;--%>
    <%--let index=1;--%>

    <%--&lt;%&ndash;const bookLength = ${requestScope.booksLength};&ndash;%&gt;--%>
    <%--const pagination = Math.ceil(bookItems.length/maxItem);--%>
    <%--console.log(pagination)--%>

    <%--prev.addEventListener("click",function(){--%>
    <%--    index--;--%>
    <%--    check();--%>
    <%--    showItems();--%>
    <%--})--%>
    <%--next.addEventListener("click",function(){--%>
    <%--    index++;--%>
    <%--    check();--%>
    <%--    showItems();--%>
    <%--})--%>

    <%--function check(){--%>
    <%--    if(index==pagination){--%>
    <%--        next.classList.add("disabled");--%>
    <%--    }--%>
    <%--    else{--%>
    <%--        next.classList.remove("disabled");--%>
    <%--    }--%>

    <%--    if(index==1){--%>
    <%--        prev.classList.add("disabled");--%>
    <%--    }--%>
    <%--    else{--%>
    <%--        prev.classList.remove("disabled");--%>
    <%--    }--%>
    <%--}--%>

    <%--function showItems() {--%>
    <%--    for(let i = 0;i < bookItems.length; i++){--%>
    <%--        bookItems[i].classList.remove("show");--%>
    <%--        bookItems[i].classList.add("hide");--%>


    <%--        if(i >= (index * maxItem) - maxItem && i < index * maxItem) {--%>


    <%--            bookItems[i].classList.remove("hide");--%>
    <%--            bookItems[i].classList.add("show");--%>
    <%--        }--%>
    <%--        page.innerHTML=index;--%>
    <%--    }--%>


    <%--}--%>

    <%--window.onload=function(){--%>
    <%--    showItems();--%>
    <%--    check();--%>
    <%--}--%>
</script>

</body>
</html>