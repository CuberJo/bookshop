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


<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Add bank Account - Bookstore</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/credit-card.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/home.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/add_iban.css">
    <%--    <!--  -->--%>
    <%--    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>--%>
    <%--    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>--%>
    <%--    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>--%>
    <%--    <!--  -->--%>
    <link href="https://fonts.googleapis.com/css?family=Montserrat:200,400,700" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Roboto:700" rel="stylesheet">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>


<body>

<!---------- header --------------->

<jsp:include page="header.jsp"/>

<div class="small-container">
    <div class="row">
        <div class="mcontainer" align="center">
            <div class="mrow">
                <div class="col-75">
                    <div class="container">
                        <form action="/home?command=add_iban" method="post">

                            <div class="mrow">
                                <div class="col-50">
                                    <div id="errAddIBAN" style="display: none">
                                        <div style="background: #EACCCC; padding: 10px 15px">
                                            <div id="errorAddIBANMessage"<%-- style="color: #ff523b; height: 30px"--%>></div>
                                        </div>
                                    </div>
                                    <c:if test="${not empty error_add_iban_message}">
                                        <div style="background: #EACCCC; padding: 10px 15px">
                                            <br><pre style="color: #ff523b">${error_add_iban_message}</pre><br>
                                            <c:remove var="error_add_iban_message" scope="session" />
                                        </div>
                                    </c:if>
                                    <h3><fmt:message key="label.billing_address" bundle="${lang}"/></h3>
                                    <label for="fname"><i class="fa fa-user"></i> Full Name</label>
                                    <input type="text" id="fname" name="firstname" placeholder="John M. Doe" readonly>
                                    <label for="email"><i class="fa fa-envelope"></i> Email</label>
                                    <input type="text" id="email" name="email" placeholder="john@example.com" readonly>
                                    <label for="adr"><i class="fa fa-address-card-o"></i> Address</label>
                                    <input type="text" id="adr" name="address" placeholder="542 W. 15th Street" readonly>
                                    <label for="city"><i class="fa fa-institution"></i> City</label>
                                    <input type="text" id="city" name="city" placeholder="New York" readonly>

                                    <div class="mrow">
                                        <div class="col-50">
                                            <label for="state"><fmt:message key="label.state" bundle="${lang}"/></label>
                                            <input type="text" id="state" name="state" placeholder="NY" readonly>
                                        </div>
                                        <div class="col-50">
                                            <label for="zip"><fmt:message key="label.zip" bundle="${lang}"/></label>
                                            <input type="text" id="zip" name="zip" placeholder="10001" readonly>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-50">
                                    <h3><fmt:message key="label.payment" bundle="${lang}"/></h3>
                                    <label for="fname"><fmt:message key="label.accepted_cards" bundle="${lang}"/></label>
                                    <div class="icon-container">
                                        <i class="fa fa-cc-visa" style="color:navy;"></i>
                                        <i class="fa fa-cc-amex" style="color:blue;"></i>
                                        <i class="fa fa-cc-mastercard" style="color:red;"></i>
                                        <i class="fa fa-cc-discover" style="color:orange;"></i>
                                    </div>
                                    <label for="cname"><fmt:message key="label.name_on_card" bundle="${lang}"/></label>
                                    <input type="text" id="cname" name="cardname" placeholder="John More Doe" readonly>
                                    <div class="card-container">
                                        <label for="card"><fmt:message key="label.credit_card_no" bundle="${lang}"/></label>
<%--                                        <form>--%>
                                            <div name="card-container">
                                                <input type="text" id="card" name="iban" maxlength="19" placeholder="1111 1111 1111 1111">
<%--                                                <label class="card-label" for="">Credit Card</label>--%>
                                                <div id="logo"></div>
                                            </div>
<%--                                        </form>--%>
                                        <%--                                    <input type="text" id="ccnum" name="cardnumber" placeholder="1111-2222-3333-4444">--%>
                                    </div>
                                    <label for="expmonth"><fmt:message key="label.exp_month" bundle="${lang}"/></label>
                                    <input type="text" id="expmonth" name="expmonth" placeholder="September" readonly>

                                    <div class="mrow">
                                        <div class="col-50">
                                            <label for="expyear"><fmt:message key="label.exp_year" bundle="${lang}"/></label>
                                            <input type="text" id="expyear" name="expyear" placeholder="2018" readonly>
                                        </div>
                                        <div class="col-50">
                                            <label for="cvv">CVV</label>
                                            <input type="text" id="cvv" name="cvv" placeholder="352" readonly>
                                        </div>
                                    </div>
                                </div>

                            </div>
                            <%--                            <label>--%>
                            <%--                                <input type="checkbox" checked="checked" name="sameadr" readonly> Shipping address same as billing--%>
                            <%--                            </label>--%>
                            <button type="submit" onclick="return validateIBAN(event)" class="btn"><fmt:message key="label.bind" bundle="${lang}"/><%--"Continue to checkout"--%></button>
                        </form>
                    </div>
                </div>

                <%--                <div class="col-25">--%>
                <%--                    <div class="container">--%>
                <%--                        <h4>Cart--%>
                <%--                            <span class="price" style="color:black">--%>
                <%--                              <i class="fa fa-shopping-cart"></i>--%>
                <%--                              <b>4</b>--%>
                <%--                            </span>--%>
                <%--                        </h4>--%>
                <%--                        <p><a href="#">Product 1</a> <span class="price">$15</span></p>--%>
                <%--                        <p><a href="#">Product 2</a> <span class="price">$5</span></p>--%>
                <%--                        <p><a href="#">Product 3</a> <span class="price">$8</span></p>--%>
                <%--                        <p><a href="#">Product 4</a> <span class="price">$2</span></p>--%>
                <%--                        <hr>--%>
                <%--                        <p>Total <span class="price" style="color:black"><b>$30</b></span></p>--%>
                <%--                    </div>--%>
                <%--                </div>--%>
            </div>
        </div>
    </div>
</div>


<!---------- footer --------------->

<jsp:include page="footer.jsp"/>

<script src="https://cdnjs.cloudflare.com/ajax/libs/vanilla-masker/1.2.0/vanilla-masker.min.js"></script>
<script src="../../js/app.bundle.js"></script>

<script>
    function validateIBAN(event) {
        let error = "";

        iban = $('#card').val();

        let card_regex = /^[\d]{4}\s[\d]{4}\s[\d]{4}\s[\d]{4}$/;

        if (!card_regex.test(iban)) {
            event.preventDefault();
            console.log('ji')
            error = "<fmt:message key="iban_incorrect" bundle="${mes}"/>";
        }

        let whitespace_regex = /^[\s]{2,}$/;
        if (iban === "" || whitespace_regex.test(iban)) {
            event.preventDefault();
            console.log('ttttttt')
            error = "<fmt:message key="input_iban" bundle="${mes}"/>";
        }

        if (error !== "") {
            $("#errorAddIBANMessage").text(error);
            $('#errAddIBAN').css('display', 'block');
            return false;
        }

        return true;
    }
</script>

</body>
</html>
