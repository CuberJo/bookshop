<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>JavaMail</title>
</head>
<body>
<form action="MailController" method="post">
    <table>
        <tr>
            <td>Send to:</td>
            <td>
                <input type="text" name="to" />
            </td>
        </tr>
        <tr>
            <td>Subject:</td>
            <td>
                <input type="text" name="subject" />
            </td>
        </tr>
    </table>
    <hr />
    <textarea type="text" name="body" cols="35" rows="5">
        Message text
    </textarea>
    <br/>
    <br/>
    <input type="submit" value="send" />
</form>
</body>
</html>
