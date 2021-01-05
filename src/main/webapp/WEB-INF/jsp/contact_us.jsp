<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>About - Bookstore</title>
    <link rel="shortcut icon" href="/images/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" type="text/css" href="/styles/contact_us.css">
    <link rel="stylesheet" type="text/css" href="/styles/home.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>

<body>

<!---------- header --------------->

<jsp:include page="header.jsp"/>

<!---------- contact us page --------------->

<div class="small-container">
    <div class="row">
        <div class="mcontainer">
            <form action="action_page.php">

                <label for="name">Name</label>
                <input type="text" id="name" name="name" placeholder="Your name..">

                <label for="email">Email</label>
                <input type="email" id="email" name="email" placeholder="Your email..">

                <label for="subject">Subject</label>
                <textarea id="subject" name="subject" placeholder="Write something.." style="height:200px"></textarea>

                <input type="submit" value="Submit">

            </form>
        </div>
    </div>
</div>

<!---------- footer --------------->

<jsp:include page="footer.jsp" />

</body>
</html>