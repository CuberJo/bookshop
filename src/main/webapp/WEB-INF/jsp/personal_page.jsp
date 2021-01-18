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



<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Personal page - Bookstore</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/books.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/personal_page.css">
    <link rel="stylesheet" type="text/css" href='<c:url value="/styles/popup.css"/>'>
    <link href="https://fonts.googleapis.com/css?family=Montserrat:200,400,700" rel="stylesheet">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <link href="https://fonts.googleapis.com/css?family=Roboto:700" rel="stylesheet">


    <%--Bootstrap--%>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" integrity="sha384-HSMxcRTRxnN+Bdg0JdbxYKrThecOKuH5zCYotlSAcp1+c8xmyTe9GYg1l9a69psu" crossorigin="anonymous">
    <!-- Optional theme -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap-theme.min.css" integrity="sha384-6pzBo3FDv/PJ8r2KRkGHifhEocL+1X2rVCTTkUfGk7/0pbek5mMa1upzvWbrUbOZ" crossorigin="anonymous">
    <!-- Latest compiled and minified JavaScript -->
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js" integrity="sha384-aJ21OjlMXNL5UyIl/XNwTMqvzeRMZH2w8c5cRVpzpU8Y5bApTppSuUkhZXN0VxHd" crossorigin="anonymous"></script>
    <%--Bootstrap--%>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/home.css">
</head>


<body>

<!---------- header --------------->

<jsp:include page="header.jsp"/>

<!---------- popup --------------->

<div id="popup2" class="overlay">
    <div class="popup">
        <h2><fmt:message key="label.unbind_cart" bundle="${lang}"/></h2>
        <a class="close" href="#" <%--onclick="cls()"--%>>&times;</a>
        <form class="content" method="post" action="#">
            <fmt:message key="label.really_unbind_card" bundle="${lang}"/>
            <br/>
            <br/>
            <br/>
            <button type="submit" onclick="cls()" class="mmbutton cancelbtn"><fmt:message key="label.cancel" bundle="${lang}"/></button>
            <button type="submit" onclick="sbmt()" class="mmbutton unbindbtn"><fmt:message key="label.unbind" bundle="${lang}"/></button>
        </form>
    </div>
</div>

<div id="popup3" class="overlay">
    <div class="popup">
        <h2><fmt:message key="label.save_changes" bundle="${lang}"/></h2>
        <a class="close" href="#" <%--onclick="cls()"--%>>&times;</a>
        <form class="content" method="post" action="#">
            <fmt:message key="label.enter_pass_to_save" bundle="${lang}"/>
            <br/>
            <br/>
            <br/>
            <input type="password" placeholder="<fmt:message key="label.password" bundle="${lang}"/>" name="checkPassword" id="checkPassword">
            <button type="submit" onclick="cls2()" class="mmbutton cancelbtn"><fmt:message key="label.cancel" bundle="${lang}"/></button>
            <button type="submit" onclick="sbmt2()" class="mmbutton unbindbtn"><fmt:message key="label.save" bundle="${lang}"/></button>
        </form>
    </div>
</div>


<%--                <button type="button" onclick="document.getElementById('id01').style.display='none'" class="cancelbtn">Cancel</button>--%>





<div class="small-container">
    <div class="row">
        <div id="rowToReload" class="col-sm-10"><h1>${login}</h1></div>
    </div>

    <div class="row">

    <div class="col-sm-3"><!--left col--></div><!--/col-3-->

    <div class="col-sm-9">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#library"><fmt:message key="label.library" bundle="${lang}"/></a></li>
            <li><a data-toggle="tab" href="#settings"><fmt:message key="label.settings" bundle="${lang}"/></a></li>
            <li><a data-toggle="tab" href="#logout"><fmt:message key="label.log_out" bundle="${lang}"/></a></li>
            <li><a data-toggle="tab" href="#BankAccs"><fmt:message key="label.bank_accounts" bundle="${lang}"/></a></li>
            <li><a data-toggle="tab" href="#deleteAcc"><fmt:message key="label.delete_acc" bundle="${lang}"/></a></li>
        </ul>

        <div class="tab-content">
            <div class="tab-pane active" id="library">
                <hr>
                <c:forEach var="book" items="${library}">
                    <c:if test="${not empty book.base64Image}">
                        <div class="row">
                             <div class="col-4" style="flex-basis: 20%"><a href="/home?command=book-details&isbn=${book.ISBN}">
                                <%--                <img src="../../images/library.jpg">--%>
                                <img src="data:image/jpg;base64,${book.base64Image}">
                                <h4>${book.title}</h4>
                                <p>${book.price}$</p>
                            </a></div>
                        </div>
                    </c:if>
                </c:forEach>
                <c:if test="${empty library}">
                    <div class="mcontainer" style="box-shadow: none; border: none" align="center">
                        <br>
                        <h1><fmt:message key="empty_library" bundle="${mes}"/></h1>
                        <br>
                        <p><fmt:message key="no_books_in_library" bundle="${mes}"/></p>
                        <div style="height: 30px"></div>
                        <a href="/home?command=books" class="btn"><fmt:message key="go_to_store" bundle="${m}"/></a>
                        <div style="height: 50px"></div>
                    </div>
                </c:if>
<%--                <form class="form" action="##" method="post" id="registrationForm">--%>
<%--                    <div class="form-group">--%>

<%--                        <div class="col-xs-6">--%>
<%--                            <label for="password2"><h4>Verify</h4></label>--%>
<%--                            <input type="password" class="form-control" name="password2" id="password2" placeholder="password2" title="enter your password2.">--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                </form>--%>
                <hr>
            </div>

            <div class="tab-pane" id="settings">
                <h2></h2>
                <hr>
<%--                <div id="errorAccMessage" style="color: #ff523b; height: 30px"></div>--%>
                <div id="merr" style="display: none">
                    <div style="background: #EACCCC; padding: 10px 15px">
                        <div id="errorAccMessage"<%-- style="color: #ff523b; height: 30px"--%>></div>
                    </div>
                </div>
                <div id="rowToReload2"><c:if test="${not empty error_acc_settings}">
                    <div style="background: #EACCCC; padding: 10px 15px">
                        ${error_acc_settings}
                        <c:remove var="error_acc_settings" scope="session" />
                    </div>
                </c:if></div>
<%--                <form class="form" action="/home?command=account_settings" method="post" id="registrationForm">--%>
                    <div class="form-group">
                        <div class="col-xs-6">
                            <label for="login"><h4><fmt:message key="label.login" bundle="${lang}"/></h4></label>
                            <input type="text" class="form-control" name="login" id="login" placeholder="<fmt:message key="label.login" bundle="${lang}"/>">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-xs-6">
                            <label for="email"><h4><fmt:message key="label.email" bundle="${lang}"/></h4></label>
                            <input type="email" class="form-control" name="email" id="email" placeholder="you@email.com">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-xs-6">
                            <label for="password"><h4><fmt:message key="label.password" bundle="${lang}"/></h4></label>
                            <input type="password" class="form-control" name="password" id="password" placeholder="<fmt:message key="label.password" bundle="${lang}"/>">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-xs-6">
                            <label for="verifyPassword"><h4><fmt:message key="label.verify_password" bundle="${lang}"/></h4></label>
                            <input type="password" class="form-control" name="verifyPassword" id="verifyPassword" placeholder="<fmt:message key="label.password" bundle="${lang}"/>">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-xs-12">
                            <br>
                            <button class="btn" type="submit" onclick="save()"><fmt:message key="label.update" bundle="${lang}"/></button>
                            <button class="btn" onclick="rst()" type="reset"><fmt:message key="label.reset" bundle="${lang}"/></button>
                        </div>
                    </div>
<%--                </form>--%>
            </div>

            <div class="tab-pane" id="logout">
                <hr>
                <form class="mcontainer" style="box-shadow: none; border: none" align="center" method="post" action="/home?command=logout">
                    <h2><fmt:message key="label.log_out" bundle="${lang}"/></h2>
                    <br>
                    <p><fmt:message key="really_logout" bundle="${m}"/></p>
                    <br>
                    <button class="btn"><fmt:message key="label.log_out" bundle="${lang}"/></button>
                    <div style="height: 80px"></div>
                </form>
<%--                <form class="form" action="##" method="post" id="registrationForm">--%>
<%--                    <div class="form-group">--%>
<%--                        <div class="col-xs-6">--%>
<%--                            <label for="first_name"><h4>First name</h4></label>--%>
<%--                            <input type="text" class="form-control" name="first_name" id="first_name" placeholder="first name" title="enter your first name if any.">--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                    <div class="form-group">--%>
<%--                        <div class="col-xs-12">--%>
<%--                            <br>--%>
<%--                            <button class="btn btn-lg btn-success pull-right" type="submit"><i class="glyphicon glyphicon-ok-sign"></i> Save</button>--%>
<%--                            <!--<button class="btn btn-lg" type="reset"><i class="glyphicon glyphicon-repeat"></i> Reset</button>-->--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                </form>--%>
            </div>

            <div class="tab-pane" id="BankAccs">
                <hr>
                <div class="mcontainer">
                    <section>
                        <div>
                            <h2><fmt:message key="label.your_bank_accounts" bundle="${lang}"/></h2>
                            <ul id="mchlist" class="check-list">
                                <c:forEach var="iban" items="${ibans}">
                                    <div class="column col-xs-9">
                                        <li>${iban}</li>
                                    </div>
                                    <div class="column col-xs-3">
                                        <div style="cursor: pointer; color: #f44336;" onclick="setIBAN('${iban}')"><fmt:message key="label.unbind" bundle="${lang}"/></div>
                                    </div>
                                </c:forEach>
<%--                                <div class="column col-xs-9">--%>
<%--                                    <li id="rter">Head</li>--%>
<%--                                    <li>Shoulders</li>--%>
<%--                                    <li>Knees</li>--%>
<%--                                    <li>Toes</li>--%>
<%--                                </div>--%>
<%--                                <div class="column col-xs-3">--%>
<%--                                    <div onclick="setISBN(33333333333333333)">remove</div>--%>
<%--                                    <div onclick="setISBN(123)">remove</div>--%>
<%--                                    <div onclick="setISBN(444)">remove</div>--%>
<%--                                    <div>remove</div>--%>
<%--                                </div>--%>
                            </ul>
                        </div>
                    </section>
                    <div style="height: 50px"></div>
                    <a href="/home?command=add_iban&getAddIBANPage=getAddIBANPage&additional_iban=additional_iban" class="btn"><fmt:message key="label.add_bank_acc" bundle="${lang}"/></a>                    <div style="height: 50px"></div>
                    <div style="height: 50px"></div>
                </div>
            </div>

            <div class="tab-pane" id="deleteAcc">
                <hr>
                <form class="mcontainer" style="box-shadow: none; border: none" align="center" method="post" action="/home?command=delete_account">
                    <h2><fmt:message key="label.delete_acc" bundle="${lang}"/></h2>
                    <br>
                    <p><fmt:message key="label.really_like_to_delete_acc" bundle="${lang}"/></p>
                    <br>
                    <button class="btn"><fmt:message key="label.delete" bundle="${lang}"/></button>
                    <div style="height: 80px"></div>
                </form>
            </div>
        </div>
    </div>
</div>
</div>

<!---------- footer --------------->

<jsp:include page="footer.jsp"/>


<!---------- unbind bank account --------------->

<script>
    $(document).ready(function () {
        $('.popup .close').click(function(){
            $('.overlay').css({'visibility': 'hidden', 'opacity': '0'});
        })
    })
</script>

<script>
    $(document).ready(function getIBANs() {
        $.ajax({
            url: 'http://localhost:8080/load_ibans',
            type: 'GET',
            success: function () {
                $('#mchlist').load(' #mchlist');
            }
        });
    })
</script>

<script>
    let i;
    function setIBAN(isbn) {
        i = isbn;
        // document.getElementById('id01').style.display='block';
        $('#popup2.overlay').css({'visibility': 'visible', 'opacity': '1'});
    }

    function sbmt() {
        event.preventDefault()
        $('#popup2.overlay').css({'visibility': 'hidden', 'opacity': '0'});

        console.log(i);

        $.ajax({
            url: 'http://localhost:8080/unbind_iban',
            type: 'POST',
            data: ({iban_to_delete: i}),
            success: function () {
                $('#mchlist').load(' #mchlist');
                // $('#s').load('#s');
            }
        });

        i = undefined;
    }

    function cls() {
        i = undefined;
        event.preventDefault();
        $('#popup2.overlay').css({'visibility': 'hidden', 'opacity': '0'});
    }
</script>

<script>
    function save(isbn) {
        $('#popup3.overlay').css({'visibility': 'visible', 'opacity': '1'});
    }

    function sbmt2() {
        event.preventDefault()

        if (validateInput(event) == false) {
            return;
        }

        $('#popup3.overlay').css({'visibility': 'hidden', 'opacity': '0'});

        var inputLogin = $("#login").val();
        var inputEmail = $("#email").val();
        var inputPass = $("#password").val();
        var inputVerifyPass = $("#verifyPassword").val();
        var inputCheckPass = $("#checkPassword").val();

        $("#login").val('');
        $("#email").val('');
        $("#password").val('');
        $("#verifyPassword").val('');
        $("#checkPassword").val('');

        $.ajax({
            url: 'http://localhost:8080/account_settings',
            type: 'POST',
            data: ({login: inputLogin,
                email: inputEmail,
                password: inputPass,
                verifyPassword: inputVerifyPass,
                checkPassword: inputCheckPass}),
            success: function () {
                $('#rowToReload').load(' #rowToReload');
                $('#rowToReload2').load(' #rowToReload2');
                // $('.small-container').load(' .small-container');
            }
        });
    }

    function cls2() {
        event.preventDefault();
        $('#popup3.overlay').css({'visibility': 'hidden', 'opacity': '0'});
    }
</script>

<script>
    function rst() {
        $("#login").val('');
        $("#email").val('');
        $("#password").val('');
        $("#verifyPassword").val('');
        $("#checkPassword").val('');
    }
</script>

<script>
    function validateInput(event) {
        let loginField = $("#login").val();
        let emailField = $("#email").val();
        let passField = $("#password").val();
        let verifyPassField = $("#verifyPassword").val();
        let checkPassField = $("#checkPassword").val();

        let error = "";

        let email_regex = /[\w-]+@[\w-]+\.[a-z]{2,5}/;
        let malicious_regex = /^[-<>*;='#)+&("]+$/;
        if (malicious_regex.test(checkPassField)) {
            event.preventDefault();
            error = "<fmt:message key="incorrect_pass" bundle="${mes}"/>";
        }
        if (malicious_regex.test(verifyPassField)) {
            event.preventDefault();
            error = "<fmt:message key="incorrect_pass" bundle="${mes}"/>";
        }
        if (malicious_regex.test(passField)) {
            event.preventDefault();
            error = "<fmt:message key="incorrect_pass" bundle="${mes}"/>";
        }
        if (malicious_regex.test(emailField) || !email_regex.test(emailField)) {
            event.preventDefault();
            error = "<fmt:message key="incorrect_email" bundle="${mes}"/>";
        }
        if (malicious_regex.test(loginField)) {
            event.preventDefault();
            error = "<fmt:message key="incorrect_login" bundle="${mes}"/>";
        }

        let whitespace_regex = /[\s]+/;
        if (checkPassField == "" || whitespace_regex.test(checkPassField)) {
            event.preventDefault();
            error = "<fmt:message key="input_pass" bundle="${mes}"/>";
        }
        if (verifyPassField == "" || whitespace_regex.test(verifyPassField)) {
            event.preventDefault();
            error = "<fmt:message key="input_pass" bundle="${mes}"/>";
        }
        if (passField == "" || whitespace_regex.test(passField)) {
            event.preventDefault();
            error = "<fmt:message key="input_pass" bundle="${mes}"/>";
        }
        if (emailField == "" || whitespace_regex.test(emailField)) {
            event.preventDefault();
            error = "<fmt:message key="input_email" bundle="${mes}"/>";
        }
        if (loginField == "" || whitespace_regex.test(loginField)) {
            event.preventDefault();
            error = "<fmt:message key="input_login" bundle="${mes}"/>";
        }

        if (passField != verifyPassField) {
            event.preventDefault();
            error = "<fmt:message key="passes_not_equal" bundle="${mes}"/>";
        }

        if (error != "") {
            $('#merr').css("display", "block");
            $("#errorAccMessage").text(error);
            return false;
        }

        // window.location = "/home?command=register"
        return true;
    }
</script>

</body>
</html>
