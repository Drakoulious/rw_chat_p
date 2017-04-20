<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<form:form role="form" action="spring_security_check" method="post">
    <div>
        <input type="text" placeholder="Email" name='username'>
    </div>
    <div>
        <input type="password" placeholder="Password" name='password'>
    </div>
    <button type="submit">LOGIN</button>
</form:form>
</html>