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

<div class="footer">
    <div class="container">
        <div class="row">
            <div class="footer-col-1">
                <img src="../../images/bookstore2.png">
            </div>
            <div class="footer-col-3">
                <h3><fmt:message key="label.useful_links" bundle="${lang}"/></h3>
                <ul>
                    <li><fmt:message key="label.coupons" bundle="${lang}"/></li>
                    <li><fmt:message key="label.blog_posts" bundle="${lang}"/></li>
                    <li><fmt:message key="label.return_policy" bundle="${lang}"/></li>
                    <li><fmt:message key="label.join_affiliate" bundle="${lang}"/></li>
                </ul>
            </div>
            <div class="footer-col-3">
                <h3><fmt:message key="label.genres" bundle="${lang}"/></h3>
                <ul>
                    <li><a href="/home?command=books&genre=ROMANCE"><fmt:message key="label.romance" bundle="${lang}"/></a></li>
                    <li><a href="/home?command=books&genre=ACTION_AND_ADVENTURE"><fmt:message key="label.action_and_adventure" bundle="${lang}"/></a></li>
                    <li><a href="/home?command=books&genre=MYSTERY_AND_THRILLER"><fmt:message key="label.mystery_and_thriller" bundle="${lang}"/></a></li>
                    <li><a href="/home?command=books&genre=BIOGRAPHIES_AND_HISTORY"><fmt:message key="label.biographies_and_history" bundle="${lang}"/></a></li>
                    <li><a href="/home?command=books&genre=CHILDREN"><fmt:message key="label.children" bundle="${lang}"/></a></li>
                    <li><a href="/home?command=books&genre=FANTASY"><fmt:message key="label.fantasy" bundle="${lang}"/></a></li>
                    <li><a href="/home?command=books&genre=HISTORICAL_FICTION"><fmt:message key="label.historical_fiction" bundle="${lang}"/></a></li>
                    <li><a href="/home?command=books&genre=HORROR"><fmt:message key="label.horror" bundle="${lang}"/></a></li>
                    <li><a href="/home?command=books&genre=LITERARY_FICTION"><fmt:message key="label.literary_fiction" bundle="${lang}"/></a></li>
                    <li><a href="/home?command=books&genre=NON-FICTION"><fmt:message key="label.non-fiction" bundle="${lang}"/></a></li>
                    <li><a href="/home?command=books&genre=SCIENCE-FICTION"><fmt:message key="label.science-fiction" bundle="${lang}"/></a></li>
                </ul>
            </div>
            <div class="footer-col-4">
                <a href="/home?command=contact_us"><h3><fmt:message key="label.contact_us" bundle="${lang}"/></h3></a>
                <!-- <ul>
                    <li>Facebook</li>
                    <li>Twitter</li>
                    <li>Instagram</li>
                    <li>Youtube</li>
                </ul> -->
            </div>
        </div>
        <hr>
        <p class="copyright">Copyright 2020 - Bookstore</p>
    </div>
</div>

<script type="text/javascript">
    var menuitems = $('#Menuitems');

    menuitems.css('max-height', '0px');

    function menutoggle() {
        if (menuitems.css('max-height') == '0px') {
            menuitems.css('max-height', '200px');
        } else {
            menuitems.css('max-height', '0px');
        }
    }
</script>















<%--<div class="footer">--%>
<%--    <div class="container">--%>
<%--        <div class="row">--%>
<%--&lt;%&ndash;            <div class="footer-col-1">&ndash;%&gt;--%>
<%--&lt;%&ndash;                <h3>Download app</h3>&ndash;%&gt;--%>
<%--&lt;%&ndash;                <p>some text</p>&ndash;%&gt;--%>
<%--&lt;%&ndash;                <div class="app-logo">&ndash;%&gt;--%>
<%--&lt;%&ndash;                    <img src="images/books/To Kill a Mockingbird.jpg">&ndash;%&gt;--%>
<%--&lt;%&ndash;                    <img src="images/books/To Kill a Mockingbird.jpg">&ndash;%&gt;--%>
<%--&lt;%&ndash;                </div>&ndash;%&gt;--%>
<%--&lt;%&ndash;            </div>&ndash;%&gt;--%>
<%--            <div class="footer-col-2">--%>
<%--                <img src="images/bookstore2.png">--%>
<%--                <p>some text</p>--%>
<%--            </div>--%>
<%--            <div class="footer-col-3">--%>
<%--                <h3>Useful links</h3>--%>
<%--                <ul>--%>
<%--                    <li>Coupons</li>--%>
<%--                    <li>Blog Posts</li>--%>
<%--                    <li>Return Policy</li>--%>
<%--                    <li>Join Affiliate</li>--%>
<%--                </ul>--%>
<%--            </div>--%>
<%--            <div class="footer-col-4">--%>
<%--                <h3>Follow us</h3>--%>
<%--                <ul>--%>
<%--                    <li>Facebook</li>--%>
<%--                    <li>Twitter</li>--%>
<%--                    <li>Instagram</li>--%>
<%--                    <li>Youtube</li>--%>
<%--                </ul>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--        <hr>--%>
<%--        <p class="copyright">Copyright 2020 - Bookstore</p>--%>
<%--    </div>--%>
<%--</div>--%>