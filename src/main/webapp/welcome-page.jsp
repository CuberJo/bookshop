<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
    <script src="https://apis.google.com/js/platform.js?onload=init" async defer></script>


    <meta name="google-signin-scope" content="profile email">
    <meta name="google-signin-client_id"
          content="90328965003-3g6segp8runjt9gf0bnonhfh1mqjl24t">

    <title>Welcome</title>

    <script type="text/javascript">
        let GoogleAuth;

        function init() {
            gapi.load('auth2', function() {
                let obj = {
                    client_id: '90328965003-3g6segp8runjt9gf0bnonhfh1mqjl24t.apps.googleusercontent.com'
                }
                GoogleAuth = gapi.auth2.init(obj);
            });
        }
    </script>
</head>
<body>


<!--  forward to index page for login if user info is not in session  -->
<% if (session.getAttribute("userName") == null) {%>
<jsp:forward page="/index.jsp"/>
<% } %>

<h3>Welcome  ${userName}</h3>


<a href="#" onclick="signOut();">Sign out</a>
<script>
    function signOut() {
        var auth2 = gapi.auth2.getAuthInstance();
        auth2.signOut().then(function () {
            console.log('User signed out.');
        });
    }
</script>

</body>
</html>