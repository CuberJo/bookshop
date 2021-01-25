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
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon">
    <link href="https://fonts.googleapis.com/css?family=Poppins:400,500,700" rel="stylesheet" />

    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <!-- jQuery library -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <!-- Latest compiled JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>


    <link rel="stylesheet" type="text/css" href="../../styles/home.css">


    <style>

        .input-group .form-control {
           height: 35px;
            margin-top: 30px;
        }

        .btn {
            height: 35px;
        }


        /*что-то такое*/
        .row {
            text-align: center;
        }
        .col-2, .col-3, .col-4 {
            flex-basis: 100%;
        }

        }
        .col-xs-offset-2 {
            margin-left:auto
            margin-right: 22px;
        }
        .mrow {
            margin-left: auto;
            margin-right: 22px;
        }
        /****************/

        /*.col-xs-offset-2 {*/
        /*     margin-left: 220px;*/
        /*}*/


        /*#search.form-control {*/
        /*    min-width: 150px;*/
        /*}*/

        .container {
            margin-top: 2%;
        }

        /* Style to create scroll bar in dropdown */
        .scrollable-dropdown {
            height: auto;
            max-height: 320px;
            /* Increase / Decrease value as per your need */
            overflow-x: hidden;
        }
    </style>
    <noscript>
        For full functionality of this site it is necessary to enable JavaScript.
        Here are the <a href="https://www.enablejavascript.io/">
        instructions how to enable JavaScript in your web browser</a>.
    </noscript>
    <script>
        $(document).ready(function(e) {
            $('.search-panel .dropdown-menu').find('a').click(function(e) {
                e.preventDefault();
                var param = $(this).attr("href").replace("#", "");
                var concept = $(this).text();
                $('.search-panel span#search_concept').text(concept);
                $('.input-group #search_param').val(param);
            });
        });
        var a = document.getElementsByTagName('a').item(0);
        // $(a).on('keyup', function(evt) {
        //     console.log(evt);
        //     if (evt.keycode === 13) {

        //         alert('search?');
        //     }
        // });
        $(function(){ // this will be called when the DOM is ready
            // var t = $('a');
            $(a).item(0).onclick(function() {
                console.log($('a')[0]);
                alert('Handler for .keyup() called.');
            });
        });
    </script>
</head>



<body>
<!---------- header --------------->

<jsp:include page="header.jsp" />

<!---------- books --------------->

<div style="margin: 0; padding: 0" class="small-container">

    <div class="row row-2">
        <h2>

        </h2>

    </div>


    <div style="margin: auto;" class="mrow">
        <div class="row">
<%--            <div class="container">--%>
<%--                <div class="row">--%>
                    <div class="col-xs-8 col-xs-offset-2">
                        <div class="input-group">
                            <div class="input-group-btn search-panel">
                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                                    <span id="search_concept">All</span> <span class="caret"></span>
                                </button>
                                <ul class="dropdown-menu scrollable-dropdown" role="menu">
                                    <li><a href="#">Automotive Accesories</a></li>
                                    <li><a href="#">Cell Phone Accesories</a></li>
                                    <li><a href="#">Computer Accesories</a></li>
                                    <li><a href="#">Health and Personal Care</a></li>
                                    <li><a href="#">Automotive Accesories</a></li>
                                    <li><a href="#">Cell Phone Accesories</a></li>
                                    <li><a href="#">Computer Accesories</a></li>
                                    <li><a href="#">Health and Personal Care</a></li>
                                    <li><a href="#">Automotive Accesories</a></li>
                                    <li><a href="#">Cell Phone Accesories</a></li>
                                    <li><a href="#">Computer Accesories</a></li>
                                    <li><a href="#">Health and Personal Care</a></li>
                                    <li><a href="#">Automotive Accesories</a></li>
                                    <li><a href="#">Cell Phone Accesories</a></li>
                                    <li><a href="#">Computer Accesories</a></li>
                                    <li><a href="#">Health and Personal Care</a></li>
                                </ul>
                            </div>
                            <input type="hidden" name="search_param" value="all" id="search_param">
                            <input type="text" class="form-control" name="x" id="search" placeholder="Search">
                            <span class="input-group-btn">
                                <button class="btn btn-default" type="button">
                                    <span class="glyphicon glyphicon-search"></span>
                                </button>
                            </span>
                        </div>
                    </div>
<%--                </div>--%>
<%--            </div>--%>
        </div>
    </div>
</div>

<!---------- footer --------------->

<jsp:include page="footer.jsp" />

<script type="text/javascript">
    $(document).ready(function() {
        $('#back').click(function (){
            window.location = "index.jsp"
        });
    });
</script>

</body>
</html>

