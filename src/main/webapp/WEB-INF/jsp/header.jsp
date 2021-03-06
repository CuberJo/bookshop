<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<%--<fmt:setLocale value="en" scope="session" />--%>

<noscript>Need Javascript</noscript>

<div class="container">
    <div class="navbar">
        <div class="logo">
            <img src="../../images/bookstore2.png" width="125px">
        </div>
        <nav>
            <ul id="Menuitems">
                <li>
                    <form id="headerSearchForm" method="post" action="/home?command=book_search_results">
                        <input type="hidden" name="not_advanced_book_search" value="true">
                        <input type="search" name="str" style="outline: none; margin: 10px 0; padding: 0 10px; height: 30px;" placeholder="<fmt:message key="label.search" bundle="${lang}"/>" class="searchInput">
<%--                        <i class="fa fa-search"></i>--%>
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
<%--                <li style="font-weight: bold"><a id="ru" href="/home?command=change_locale&locale=RU&from=${param.command}">RU</a> | <a id="en" href="/home?command=change_locale&locale=US&from=${param.command}">US</a></li>--%>
                </ul>
        </nav>
        <c:if test="${not empty sessionScope.login and sessionScope.login != 'admin'}">
            <a href="home?command=cart"><img src="../../images/cart.png" width="30px" height="30px"></a>
        </c:if>
        <img src="../../images/menu-icon.png" class="menu-icon" onclick="menutoggle()">
    </div>
</div>


<script>
    $('#headerSearchForm').submit(function(event){

        let whitespace_regex = /^[\s]+$/;
        if ($('.searchInput').val() === "" || whitespace_regex.test($('.searchInput').val())) {
            event.preventDefault()
        }
        let malicious_regex = /[<>*;='#)+&("]+/;
        if (malicious_regex.test($('.searchInput').val())) {
            event.preventDefault()
        }
    });
</script>