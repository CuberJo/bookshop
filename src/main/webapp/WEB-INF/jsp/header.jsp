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
                    <form action="">
                        <input type="search" style="outline: none">
<%--                        <i class="fa fa-search"></i>--%>
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
                <li style="font-weight: bold"><a id="ru" href="/home?command=change_locale&locale=RU&from=${param.command}">RU</a> | <a id="en" href="/home?command=change_locale&locale=RN&from=${param.command}">EN</a></li>
                </ul>
        </nav>
        <c:if test="${not empty sessionScope.role}">
            <a href="home?command=cart"><img src="../../images/cart.png" width="30px" height="30px"></a>
        </c:if>
        <img src="../../images/menu-icon.png" class="menu-icon" onclick="menutoggle()">
    </div>
</div>