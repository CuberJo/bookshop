<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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
                        <%--                        <i class="fa fa-search"></i>--%>
                    </form>
                </li>
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
            <a href="home?command=cart"><img src="images/cart.png" width="30px" height="30px"></a>
        </c:if>
        <img src="images/menu-icon.png" class="menu-icon" onclick="menutoggle()">
    </div>
</div>
