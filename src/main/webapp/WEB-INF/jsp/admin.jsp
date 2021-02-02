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
    <title>Admin - Bookstore</title>
    <link rel="stylesheet" type="text/css" href="../../styles/admin.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon">

<%-------------------------     For file icon     ----------------------------------------%>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">


<%--    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">--%>
<%--    <!--  bootstrap -->--%>
<%--    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>--%>
<%--    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>--%>
<%--    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>--%>
<%--    <!--  -->--%>
<%--    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>--%>
</head>





<%--                  UNCOMMENT ONLOAD ATTR IF THERE WILL BE CYCLING OF PROGA             --%>
<body <%--onload="fetchBooksQuantity()"--%>>

<div id="genre" style="display: none">${requestScope.genre}</div>
<div id="locale" style="display: none">${sessionScope.locale}</div>

<svg style="display:none;">
    <symbol id="down" viewBox="0 0 16 16">
        <polygon points="3.81 4.38 8 8.57 12.19 4.38 13.71 5.91 8 11.62 2.29 5.91 3.81 4.38" />
    </symbol>
    <symbol id="users" viewBox="0 0 16 16">
        <path d="M8,0a8,8,0,1,0,8,8A8,8,0,0,0,8,0ZM8,15a7,7,0,0,1-5.19-2.32,2.71,2.71,0,0,1,1.7-1,13.11,13.11,0,0,0,1.29-.28,2.32,2.32,0,0,0,.94-.34,1.17,1.17,0,0,0-.27-.7h0A3.61,3.61,0,0,1,5.15,7.49,3.18,3.18,0,0,1,8,4.07a3.18,3.18,0,0,1,2.86,3.42,3.6,3.6,0,0,1-1.32,2.88h0a1.13,1.13,0,0,0-.27.69,2.68,2.68,0,0,0,.93.31,10.81,10.81,0,0,0,1.28.23,2.63,2.63,0,0,1,1.78,1A7,7,0,0,1,8,15Z" />
    </symbol>
    <symbol id="collection" viewBox="0 0 16 16">
        <rect width="7" height="7" />
        <rect y="9" width="7" height="7" />
        <rect x="9" width="7" height="7" />
        <rect x="9" y="9" width="7" height="7" />
    </symbol>
    <symbol id="charts" viewBox="0 0 16 16">
        <polygon points="0.64 7.38 -0.02 6.63 2.55 4.38 4.57 5.93 9.25 0.78 12.97 4.37 15.37 2.31 16.02 3.07 12.94 5.72 9.29 2.21 4.69 7.29 2.59 5.67 0.64 7.38" />
        <rect y="9" width="2" height="7" />
        <rect x="12" y="8" width="2" height="8" />
        <rect x="8" y="6" width="2" height="10" />
        <rect x="4" y="11" width="2" height="5" />
    </symbol>
    <symbol id="comments" viewBox="0 0 16 16">
        <path d="M0,16.13V2H15V13H5.24ZM1,3V14.37L5,12h9V3Z"/><rect x="3" y="5" width="9" height="1"/><rect x="3" y="7" width="7" height="1"/><rect x="3" y="9" width="5" height="1"/>
    </symbol>
    <symbol id="pages" viewBox="0 0 16 16">
        <rect x="4" width="12" height="12" transform="translate(20 12) rotate(-180)"/><polygon points="2 14 2 2 0 2 0 14 0 16 2 16 14 16 14 14 2 14"/>
    </symbol>
    <symbol id="appearance" viewBox="0 0 16 16">
        <path d="M3,0V7A2,2,0,0,0,5,9H6v5a2,2,0,0,0,4,0V9h1a2,2,0,0,0,2-2V0Zm9,7a1,1,0,0,1-1,1H9v6a1,1,0,0,1-2,0V8H5A1,1,0,0,1,4,7V6h8ZM4,5V1H6V4H7V1H9V4h1V1h2V5Z"/>
    </symbol>
    <symbol id="trends" viewBox="0 0 16 16">
        <polygon points="0.64 11.85 -0.02 11.1 2.55 8.85 4.57 10.4 9.25 5.25 12.97 8.84 15.37 6.79 16.02 7.54 12.94 10.2 9.29 6.68 4.69 11.76 2.59 10.14 0.64 11.85"/>
    </symbol>
    <symbol id="settings" viewBox="0 0 16 16">
        <rect x="9.78" y="5.34" width="1" height="7.97"/><polygon points="7.79 6.07 10.28 1.75 12.77 6.07 7.79 6.07"/><rect x="4.16" y="1.75" width="1" height="7.97"/><polygon points="7.15 8.99 4.66 13.31 2.16 8.99 7.15 8.99"/><rect x="1.28" width="1" height="4.97"/><polygon points="3.28 4.53 1.78 7.13 0.28 4.53 3.28 4.53"/><rect x="12.84" y="11.03" width="1" height="4.97"/><polygon points="11.85 11.47 13.34 8.88 14.84 11.47 11.85 11.47"/>
    </symbol>
    <symbol id="options" viewBox="0 0 16 16">
        <path d="M8,11a3,3,0,1,1,3-3A3,3,0,0,1,8,11ZM8,6a2,2,0,1,0,2,2A2,2,0,0,0,8,6Z"/><path d="M8.5,16h-1A1.5,1.5,0,0,1,6,14.5v-.85a5.91,5.91,0,0,1-.58-.24l-.6.6A1.54,1.54,0,0,1,2.7,14L2,13.3a1.5,1.5,0,0,1,0-2.12l.6-.6A5.91,5.91,0,0,1,2.35,10H1.5A1.5,1.5,0,0,1,0,8.5v-1A1.5,1.5,0,0,1,1.5,6h.85a5.91,5.91,0,0,1,.24-.58L2,4.82A1.5,1.5,0,0,1,2,2.7L2.7,2A1.54,1.54,0,0,1,4.82,2l.6.6A5.91,5.91,0,0,1,6,2.35V1.5A1.5,1.5,0,0,1,7.5,0h1A1.5,1.5,0,0,1,10,1.5v.85a5.91,5.91,0,0,1,.58.24l.6-.6A1.54,1.54,0,0,1,13.3,2L14,2.7a1.5,1.5,0,0,1,0,2.12l-.6.6a5.91,5.91,0,0,1,.24.58h.85A1.5,1.5,0,0,1,16,7.5v1A1.5,1.5,0,0,1,14.5,10h-.85a5.91,5.91,0,0,1-.24.58l.6.6a1.5,1.5,0,0,1,0,2.12L13.3,14a1.54,1.54,0,0,1-2.12,0l-.6-.6a5.91,5.91,0,0,1-.58.24v.85A1.5,1.5,0,0,1,8.5,16ZM5.23,12.18l.33.18a4.94,4.94,0,0,0,1.07.44l.36.1V14.5a.5.5,0,0,0,.5.5h1a.5.5,0,0,0,.5-.5V12.91l.36-.1a4.94,4.94,0,0,0,1.07-.44l.33-.18,1.12,1.12a.51.51,0,0,0,.71,0l.71-.71a.5.5,0,0,0,0-.71l-1.12-1.12.18-.33a4.94,4.94,0,0,0,.44-1.07l.1-.36H14.5a.5.5,0,0,0,.5-.5v-1a.5.5,0,0,0-.5-.5H12.91l-.1-.36a4.94,4.94,0,0,0-.44-1.07l-.18-.33L13.3,4.11a.5.5,0,0,0,0-.71L12.6,2.7a.51.51,0,0,0-.71,0L10.77,3.82l-.33-.18a4.94,4.94,0,0,0-1.07-.44L9,3.09V1.5A.5.5,0,0,0,8.5,1h-1a.5.5,0,0,0-.5.5V3.09l-.36.1a4.94,4.94,0,0,0-1.07.44l-.33.18L4.11,2.7a.51.51,0,0,0-.71,0L2.7,3.4a.5.5,0,0,0,0,.71L3.82,5.23l-.18.33a4.94,4.94,0,0,0-.44,1.07L3.09,7H1.5a.5.5,0,0,0-.5.5v1a.5.5,0,0,0,.5.5H3.09l.1.36a4.94,4.94,0,0,0,.44,1.07l.18.33L2.7,11.89a.5.5,0,0,0,0,.71l.71.71a.51.51,0,0,0,.71,0Z"/>
    </symbol>
    <symbol id="collapse" viewBox="0 0 16 16">
        <polygon points="11.62 3.81 7.43 8 11.62 12.19 10.09 13.71 4.38 8 10.09 2.29 11.62 3.81"/>
    </symbol>
    <symbol id="search" viewBox="0 0 16 16">
        <path d="M6.57,1A5.57,5.57,0,1,1,1,6.57,5.57,5.57,0,0,1,6.57,1m0-1a6.57,6.57,0,1,0,6.57,6.57A6.57,6.57,0,0,0,6.57,0Z"/><rect x="11.84" y="9.87" width="2" height="5.93" transform="translate(-5.32 12.84) rotate(-45)"/>
    </symbol>
</svg>


<%---------------------------- Nav bar ----------------------------------------%>

<header class="page-header">
    <nav>
        <a href="/home">
            <img class="logo" src="../../images/bookstore2.png" alt="forecastr logo">
        </a>
        <button class="toggle-mob-menu" aria-expanded="false" aria-label="open menu">
            <svg width="20" height="20" aria-hidden="true">
                <use xlink:href="#down"></use>
            </svg>
        </button>
        <ul class="admin-menu" class="tabs__links">
            <li class="menu-heading">
                <h3><fmt:message key="label.admin" bundle="${lang}"/></h3>
            </li>
            <li>
                <a id="paymentsHref" href="#payments-1">
                    <svg><use xlink:href="#pages"></use></svg>
                    <span><fmt:message key="label.payments" bundle="${lang}"/></span>
                </a>
            </li>
            <li>
                <a id="usersHref" href="#users-1">
                    <svg><use xlink:href="#users"></use></svg>
                    <span><fmt:message key="label.users" bundle="${lang}"/></span>
                </a>
            </li>
<%--            <li>--%>
<%--                <a href="#trends-1">--%>
<%--                    <svg><use xlink:href="#trends"></use></svg>--%>
<%--                    <span>Trends</span>--%>
<%--                </a>--%>
<%--            </li>--%>
            <li>
                <a id="booksHref" href="#books-1">
                    <svg><use xlink:href="#collection"></use></svg>
                    <span><fmt:message key="label.books" bundle="${lang}"/></span>
                </a>
            </li>
            <li>
                <a id="addBookHref" href="#add-book">
                    <svg><use xlink:href="#collection"></use></svg>
                    <span><fmt:message key="label.add_book" bundle="${lang}"/></span>
                </a>
            </li>
<%--            <li>--%>
<%--                <a href="#comments-1">--%>
<%--                    <svg><use xlink:href="#comments"></use></svg>--%>
<%--                    <span>Comments</span>--%>
<%--                </a>--%>
<%--            </li>--%>
<%--            <li>--%>
<%--                <a href="#appearance-1">--%>
<%--                    <svg><use xlink:href="#appearance"></use></svg>--%>
<%--                    <span>Appearance</span>--%>
<%--                </a>--%>
<%--            </li>--%>
<%--            <li class="menu-heading">--%>
<%--                <h3>Settings</h3>--%>
<%--            </li>--%>
<%--            <li>--%>
<%--                <a href="#0">--%>
<%--                    <svg><use xlink:href="#settings"></use></svg>--%>
<%--                    <span>Settings</span>--%>
<%--                </a>--%>
<%--            </li>--%>
<%--            <li>--%>
<%--                <a href="#0">--%>
<%--                    <svg><use xlink:href="#options"></use></svg>--%>
<%--                    <span>Options</span>--%>
<%--                </a>--%>
<%--            </li>--%>
<%--            <li>--%>
<%--                <a href="#0">--%>
<%--                    <svg><use xlink:href="#charts"></use></svg>--%>
<%--                    <span>Charts</span>--%>
<%--                </a>--%>
<%--            </li>--%>
            <li>
            <li class="menu-heading">
                <h3><a href="/home"><fmt:message key="label.website" bundle="${lang}"/></a></h3>
            </li>
            <li>
                <div style="display: flex; ">
                    <form method="post" action="/home">
                        <input type='hidden' name='command' value="change_locale">
                        <input type='hidden' name='locale' value="RU">
                        <input type='hidden' name='from' value="${param.command}">
                        <button class="localeBtn" type="submit">RU</button>
                    </form>
                    <span style="width: 20px"></span>
                    <form method="post" action="/home">
                        <input type='hidden' name='command' value="change_locale">
                        <input type='hidden' name='locale' value="US">
                        <input type='hidden' name='from' value="${param.command}">
                        <button class="localeBtn" type="submit">US</button>
                    </form>
                </div>
            </li>
            <button class="collapse-btn" aria-expanded="true" aria-label="collapse menu">
                <svg aria-hidden="true">
                    <use xlink:href="#collapse"></use>
                </svg>
                <span><fmt:message key="label.collapse" bundle="${lang}"/></span>
            </button>
            </li>
        </ul>
    </nav>
</header>

<%-------------------------------------------- Page content ---------------------------------------------%>

<section class="page-content">
    <section class="search-and-user">
<%--        <form>--%>
<%--            <input type="search" placeholder="Search Pages...">--%>
<%--            <button type="submit" aria-label="submit form">--%>
<%--                <svg aria-hidden="true">--%>
<%--                    <use xlink:href="#search"></use>--%>
<%--                </svg>--%>
<%--            </button>--%>
<%--        </form>--%>
        <div class="admin-profile">
            <span class="greeting"><fmt:message key="label.hello_admin" bundle="${lang}"/></span>
            <div class="notifications">
                <!-- <span class="badge">1</span> -->
                <svg>
                    <use xlink:href="#users"></use>
                </svg>
            </div>
        </div>
    </section>
    <section class="grid">
        <article id="payments-1">
            <h2><fmt:message key="label.payments" bundle="${lang}"/></h2>
            <table id="payments">
                <tr>
                    <th><fmt:message key="label.users_login" bundle="${lang}"/></th>
                    <th><fmt:message key="label.book_title" bundle="${lang}"/></th>
                    <th><fmt:message key="label.book_author" bundle="${lang}"/></th>
                    <th><fmt:message key="label.payment_time" bundle="${lang}"/></th>
                    <th><fmt:message key="label.price" bundle="${lang}"/></th>
                </tr>
            </table>

            <div id="paymentsPagination" class="pagination">
                <div class="prev"><fmt:message key="label.prev" bundle="${lang}" /></div>
                <div class="page"><fmt:message key="label.page" bundle="${lang}"/><span class="page-num"></span></div>
                <div class="next"><fmt:message key="label.next" bundle="${lang}" /></div>
            </div>
        </article>
        <article id="users-1">
            <h2><fmt:message key="label.users" bundle="${lang}"/></h2>
            <table id="users_">
                <tr>
                    <th><fmt:message key="label.name" bundle="${lang}"/></th>
                    <th><fmt:message key="label.login" bundle="${lang}"/></th>
                    <th><fmt:message key="label.email" bundle="${lang}"/></th>
                </tr>
            </table>

            <div id="usersPagination" class="pagination">
                <div class="prev"><fmt:message key="label.prev" bundle="${lang}" /></div>
                <div class="page"><fmt:message key="label.page" bundle="${lang}"/><span class="page-num"></span></div>
                <div class="next"><fmt:message key="label.next" bundle="${lang}" /></div>
            </div>
        </article>
        <article id="trends-1">trends</article>
        <article id="books-1">
            <h2><fmt:message key="label.books" bundle="${lang}"/></h2><div id="er" style="color: red"></div>
            <table id="books">
                <tr>
                    <th>ISBN</th>
                    <th><fmt:message key="label.title" bundle="${lang}"/></th>
                    <th><fmt:message key="label.author" bundle="${lang}"/></th>
                    <th><fmt:message key="label.price" bundle="${lang}"/></th>
                    <th><fmt:message key="label.publisher" bundle="${lang}"/></th>
                    <th><fmt:message key="label.genre" bundle="${lang}"/></th>
                    <th><fmt:message key="label.image" bundle="${lang}"/></th>
                    <th><fmt:message key="label.options" bundle="${lang}"/></th>
                </tr>

<%--                <input type="file" name="file" id="mf" class="input-file">--%>
<%--                <button id="b1">button</button>--%>

<%--                <script>--%>
<%--                    $('#b1').click(function () {--%>

<%--                        console.log('done click')--%>

<%--                        let fileSelect = document.getElementById('mf').files;--%>
<%--                        // if (fileSelect.length > 0) {--%>
<%--                        //     fileSelect = fileSelect[0];--%>
<%--                        // }--%>
<%--                        var fd = new FormData();--%>
<%--                        fd.append( 'file', fileSelect[0] );--%>
<%--    --%>
<%--                        $.ajax({--%>
<%--                            url: 'http://localhost:8080/admin',--%>
<%--                            data: fd,--%>
<%--                            processData: false,--%>
<%--                            contentType: false,--%>
<%--                            type: 'POST',--%>
<%--                            success: function(data){--%>
<%--                                alert(data);--%>
<%--                            }--%>
<%--                        });--%>
<%--                    })--%>
<%--                </script>--%>


                <div style="display: none" class="example-2">
                    <div class="form-group">
                        <input type="file" name="file" id="file" class="input-file">
                        <label for="file" class="btn btn-tertiary js-labelFile">
                            <i class="icon fa fa-check"></i>
                            <span class="js-fileName">Загрузить файл</span>
                        </label>
                    </div>
                </div>
            </table>

            <div id="booksPagination" class="pagination">
                <div class="prev"><fmt:message key="label.prev" bundle="${lang}" /></div>
                <div class="page"><fmt:message key="label.page" bundle="${lang}"/><span class="page-num"></span></div>
                <div class="next"><fmt:message key="label.next" bundle="${lang}" /></div>
            </div>
        </article>
        <article id="add-book">
            <h2><fmt:message key="label.add_book" bundle="${lang}"/></h2><div id="erAddBookMesToLoad" style="color: red">${sessionScope.erAddBookMes}</div><c:remove var="erAddBookMes" scope="session"/><div id="er2" style="color: red"></div>

            <form id="addBookForm" action="admin" method="post">
            <label class="inp">
                <input name="isbn" pattern="^[\d]+-[\d]+-[\d]+-[\d]+-[\d]+$" type="text" size="17" placeholder="<fmt:message key="label.isbn" bundle="${lang}"/>"/>
                <input name="title" pattern="^[-(),!\d\s.\p{L}]{1,70}$" type="text" placeholder="<fmt:message key="label.title" bundle="${lang}"/>"/>
                <input name="author" type="text" pattern="^[-\s.\p{L}]{1,50}$" placeholder="<fmt:message key="label.author" bundle="${lang}"/>"/>
                <input name="price" type="text" pattern="^[0-9]+(\.[0-9]+)?$" placeholder="<fmt:message key="label.price" bundle="${lang}"/>"/>
                <input name="publisher" type="text" pattern="^[-&\p{L}\s]{1,50}$" placeholder="<fmt:message key="label.publisher" bundle="${lang}"/>"/>
                <select id="addBookFormGenre" class="genres" style="margin: 25px 0"> +
                    <option selected value="CHOOSE_GENRE"><fmt:message key="label.choose_genre" bundle="${lang}" /></option>
                    <option value="ROMANCE"><fmt:message key="label.romance" bundle="${lang}" /></option>
                    <option value="ACTION_AND_ADVENTURE"><fmt:message key="label.action_and_adventure" bundle="${lang}" /></option>
                    <option value="MYSTERY_AND_THRILLER"><fmt:message key="label.mystery_and_thriller" bundle="${lang}" /></option>
                    <option value="BIOGRAPHIES_AND_HISTORY"><fmt:message key="label.biographies_and_history" bundle="${lang}" /></option>
                    <option value="CHILDREN"><fmt:message key="label.children" bundle="${lang}" /></option>
                    <option value="FANTASY"><fmt:message key="label.fantasy" bundle="${lang}" /></option>
                    <option value="HISTORICAL_FICTION"><fmt:message key="label.historical_fiction" bundle="${lang}" /></option>
                    <option value="HORROR"><fmt:message key="label.horror" bundle="${lang}" /></option>
                    <option value="LITERARY_FICTION"><fmt:message key="label.literary_fiction" bundle="${lang}" /></option>
                    <option value="NON-FICTION"><fmt:message key="label.non-fiction" bundle="${lang}" /></option>
                    <option value="SCIENCE-FICTION"><fmt:message key="label.science-fiction" bundle="${lang}" /></option>
                    <option value="DETECTIVE"><fmt:message key="label.detective" bundle="${lang}" /></option>
                    <option value="PSYCHOLOGY"><fmt:message key="label.psychology" bundle="${lang}" /></option>
                </select>
                <input name="preview" type="text" pattern="[^<>]+" placeholder="<fmt:message key="label.book_description" bundle="${lang}"/>"/>

                <div class="example-1">
                    <div class="form-group" style="display: flex;">
                        <label class="label">
                            <i class="material-icons">attach_file</i>
                            <span class="title"><fmt:message key="label.add_file" bundle="${lang}" /></span>
                            <input type="file" id="fileBookToAdd">
                        </label>

                        <label class="label" style="margin-left: 30px">
                            <i class="material-icons">attach_file</i>
                            <span class="title"><fmt:message key="label.add_img" bundle="${lang}" /></span>
                            <input type="file" id="imgBookToAdd">
                        </label>
                    </div>
                </div>

                <input type="submit" id="addBtn" value="<fmt:message key="label.save" bundle="${lang}"/>">
            </label>
            </form>
        </article>
        <article id="comments-1">comments</article>
        <article id="appearance-1">appearance</article>
        <article></article>
        <article></article>
    </section>
    <footer class="page-footer">

    </footer>
</section>

<!---------- js for pagination --------------->

<script src="../../js/admin.js" charset="UTF-8"></script>




</body>
</html>