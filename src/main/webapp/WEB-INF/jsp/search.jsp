<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="author" content="colorlib.com">
    <link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon">
    <link href="https://fonts.googleapis.com/css?family=Poppins:400,500,700" rel="stylesheet" />
    <link href="../../styles/main.css" rel="stylesheet" />
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

</head>

<body>
<div class="s013">
    <form action="page.html">
        <fieldset>
            <legend>FIND BOOK YOU WANT</legend>
        </fieldset>
        <div class="inner-form">
            <button id="back" class="btn-search" type="button">BACK</button>
            <div class="left">
                <div class="input-wrap first">
                    <div class="input-field first">
                        <!-- <label>SEARCH BY</label> -->
                        <input type="text" placeholder="title, author, ISBN, genre" />
                    </div>
                </div>
                <div class="input-wrap second">
                    <div class="input-field second">
                        <!-- <label>ALL</label> -->
                        <div class="input-select">
                            <select data-trigger="" name="choices-single-defaul">
                                <option placeholder="">1 adult</option>
                                <option>2 adults</option>
                                <option>3 adults</option>
                                <option>4 adults</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
            <button class="btn-search" type="button">SEARCH</button>
        </div>
    </form>
</div>
<script src="../../js/extention/choices.js"></script>
<script>
    const choices = new Choices('[data-trigger]',
        {
            searchEnabled: false,
            itemSelectText: '',
        });

</script>

<script type="text/javascript">
    $(document).ready(function() {
        $('#back').click(function (){
            window.location = "index.jsp"
        });
    });
</script>

</body>
</html>

