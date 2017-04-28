<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>

<body>
<form:form class="navbar-form" action="logout" method="post">
    <sec:authorize access="isAuthenticated()">
        <input type="submit" value="Logout">
    </sec:authorize>
</form:form>


</body>

</html>