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
<fmt:setBundle basename="error_message" var="mes" />
<fmt:setBundle basename="message" var="m" />



<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>Search - Bookstore</title>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon">
    <link href="https://fonts.googleapis.com/css?family=Poppins:400,500,700" rel="stylesheet" />

    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <!-- jQuery library -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <!-- Latest compiled JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

    <link href="../../styles/home.css" rel="stylesheet" type="text/css">
    <link href="../../styles/search.css" rel="stylesheet" type="text/css">

    <noscript>
        For full functionality of this site it is necessary to enable JavaScript.
        Here are the <a href="https://www.enablejavascript.io/">
        instructions how to enable JavaScript in your web browser</a>.
    </noscript>

    <script src="../../js/search.js"></script>
</head>



<body>

<div id="searchgenre" style="display: none">${requestScope.genre}</div>
<div id="searchlocale" style="display: none">${sessionScope.locale}</div>

<!---------- header --------------->

<noscript>Need Javascript</noscript>

<div class="mcontainer">
    <div class="navbar">
        <div class="logo">
            <img src="../../images/bookstore2.png" width="125px">
        </div>
        <nav>
            <ul id="Menuitems">
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
                <li style="font-weight: bold">
                    <form method="post" action="/home">
                        <input type='hidden' name='command' value="change_locale">
                        <input type='hidden' name='locale' value="RU">
                        <input type='hidden' name='from' value="${param.command}">

                        <button class="localeBtn" type="submit">RU</button>
                    </form>
                    |
                    <form method="post" action="/home">
                        <input type='hidden' name='command' value="change_locale">
                        <input type='hidden' name='locale' value="US">
                        <input type='hidden' name='from' value="${param.command}">
                        <button class="localeBtn" type="submit">US</button>
                    </form>
                </li>
            </ul>
        </nav>
        <c:if test="${not empty sessionScope.login}">
            <a href="home?command=cart"><img src="../../images/cart.png" width="30px" height="30px"></a>
        </c:if>
        <img src="../../images/menu-icon.png" class="menu-icon" onclick="menutoggle()">
    </div>
</div>

<!---------- live search --------------->

<div class="small-container">

    <div class="row row-2">
        <h2><fmt:message key="label.advanced_search" bundle="${lang}"/></h2>
    </div>

    <div class="row row-2">
        <pre id="errorSearchMessage" style="color: #ff523b; display: none"></pre>
        <c:if test="${not empty error_search_message}">
            <pre style="color: #ff523b">${error_search_message}</pre>
            <c:remove var="error_search_message" scope="session" />
        </c:if>
    </div>

    <div class="mrow">
        <div class="row">
            <div class="col-xs-8 col-xs-offset-2">
                <div class="input-group">
                    <div class="input-group-btn search-panel">
                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                            <span id="search_concept"><fmt:message key="label.all" bundle="${lang}"/></span> <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu scrollable-dropdown" role="menu">
                            <li><a class="book" href="#"><fmt:message key="label.book" bundle="${lang}"/></a></li>
                            <li><a class="genre" href="#"><fmt:message key="label.genre" bundle="${lang}"/></a></li>
                            <li><a class="publisher" href="#"><fmt:message key="label.publisher" bundle="${lang}"/></a></li>
                            <li><a class="author" href="#"><fmt:message key="label.author" bundle="${lang}"/></a></li>
                        </ul>
                    </div>
                    <input type="hidden" name="search_param" value="all" id="search_param">
                    <input type="text" class="form-control" name="x" id="search" placeholder="<fmt:message key="label.search" bundle="${lang}"/>" onkeyup="loadBooks(this.value)">
<%--                    <span class="input-group-btn">--%>
<%--                        <button id="srch" class="btn btn-default" type="submit">--%>
<%--                            <span class="glyphicon glyphicon-search"></span>--%>
<%--                        </button>--%>
<%--                    </span>--%>
                    <form class="input-group-btn" method="post" action="/home?command=books">
                        <input type="hidden" name="customizedSearch" value="true" />
                        <input type="hidden" name="searchCriteria" id="scr" />
                        <input type="hidden" name="str" id="str" />
                        <button id="searchBtn" class="btn btn-default" type="submit" onclick="validateSearchInput(event)">
                            <span class="glyphicon glyphicon-search"></span>
                        </button>
                    </form>
                </div>

                <div id="livesearch"></div>

            </div>
        </div>
    </div>
</div>


<!---------- footer --------------->

<jsp:include page="footer.jsp" />

</body>
</html>

