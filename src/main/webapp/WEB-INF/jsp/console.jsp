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
<br>
<table>
    <tr>
        <td><input type="email" name="login" placeholder="Логин Roswar"></td>
        <td><input type="password" name="password" placeholder="Пароль Roswar"></td>
        <td><button onclick="sendAuth()">Залогиниться на Roswar</button></td>
    </tr>
    <tr>
        <td>Статус</td>
        <td class="status"></td>
        <td><button onclick="getStatus()">Обновить</button></td>
    </tr>
    <tr>
        <td>Установленный логин</td>
        <td class="login"></td>
        <td><button onclick="getLogin()">Обновить</button></td>
    </tr>
</table>
<br>
<button onclick="tryOn()">Вкл парсер</button>
<button onclick="tryOff()">Выкл парсер</button>
</body>
<script type="text/javascript" src="resources/jquery-3.2.1.min.js"></script>
<script type="text/javascript">

    $(document).ready(function() {
        getLogin();
        getStatus();
    });

    function tryOn() {
        $.ajax({
            url: 'rest/on',
            type: 'GET',
            success: function (response) {
                $('.status').text(response);
            }
        });
    }

    function tryOff() {
        $.ajax({
            url: 'rest/off',
            type: 'GET',
            success: function (response) {
                $('.status').text(response);
            }
        });
    }

    function getLogin() {
        $.ajax({
            url: 'rest/player',
            type: 'GET',
            success: function (response) {
                $('.login').text(response);
            }
        });
    }

    function getStatus() {
        $.ajax({
            url: 'rest/status',
            type: 'GET',
            success: function (response) {
                $('.status').text(response);
            }
        });
    }

    function sendAuth() {
        $.ajax({
            url: 'rest/',
            type: 'POST',
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (response) {
                getLogin();
            },
            data: JSON.stringify({login:$("input[name='login']").val(), password:$("input[name='password']").val()}),
            beforeSend: function () {
                $('.status').text('Вход...');
            },
            complete: function () {
                getStatus();
            }
        });
    }
</script>
</html>