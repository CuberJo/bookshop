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
    <link rel="stylesheet" type="text/css" href="../../styles/books.css">
    <link rel="stylesheet" type="text/css" href="../../styles/home.css">
    <link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <!--  bootstrap -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
    <!--  -->
</head>


<body>

<!---------- header --------------->

<jsp:include page="header.jsp" />

<!---------- books --------------->

<div class="small-container">

    <div class="row row-2">
        <h2>
            <fmt:message key="label.all_books" bundle="${lang}" />
        </h2>
        <select>
            <option><fmt:message key="label.default_shorting" bundle="${lang}"/></option>
            <option><fmt:message key="label.short_by_price" bundle="${lang}"/></option>
            <option><fmt:message key="label.short_by_popularity" bundle="${lang}"/></option>
            <option><fmt:message key="label.short_by_rating" bundle="${lang}"/></option>
            <option><fmt:message key="label.short_by_sale" bundle="${lang}"/></option>
        </select>
    </div>

    <div class="mrow">
        <div class="row">
            <c:forEach var="book" items="${requestScope.books}">
                 <div class="col-4" style="flex-basis: 20%">
                <img src="../../images/library.jpg">
                <h4>${book.title}</h4>
                <p>${book.price}</p>
                </div>
            </c:forEach>
        </div>
        <div class="pagination">
            <div class="prev"><fmt:message key="label.prev" bundle="${lang}" /></div>
            <div class="page"><fmt:message key="label.page" bundle="${lang}"/> <span class="page-num"></span></div>
            <div class="next"><fmt:message key="label.next" bundle="${lang}" /></div>
        </div>
    </div>

</div>

<!---------- footer --------------->

<jsp:include page="footer.jsp" />

<!---------- js for pagination --------------->

<script type="text/javascript">

    const bookItems=document.querySelector(".mrow .row").children;
    const prev=document.querySelector(".prev");
    const next=document.querySelector(".next");
    const page=document.querySelector(".page-num");
    const maxItem=12;
    let index=1;

    <%--const bookLength = ${requestScope.booksLength};--%>
    const pagination = Math.ceil(bookItems.length/maxItem);
    console.log(pagination)

    prev.addEventListener("click",function(){
        index--;
        check();
        showItems();
    })
    next.addEventListener("click",function(){
        index++;
        check();
        showItems();
    })

    function check(){
        if(index==pagination){
            next.classList.add("disabled");
        }
        else{
            next.classList.remove("disabled");
        }

        if(index==1){
            prev.classList.add("disabled");
        }
        else{
            prev.classList.remove("disabled");
        }
    }

    function showItems() {
        for(let i = 0;i < bookItems.length; i++){
            bookItems[i].classList.remove("show");
            bookItems[i].classList.add("hide");


            if(i >= (index * maxItem) - maxItem && i < index * maxItem) {


                bookItems[i].classList.remove("hide");
                bookItems[i].classList.add("show");
            }
            page.innerHTML=index;
        }


    }

    window.onload=function(){
        showItems();
        check();
    }
</script>

</body>
</html>