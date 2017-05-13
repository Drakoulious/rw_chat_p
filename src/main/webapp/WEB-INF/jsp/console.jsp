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
        <td class="logn"></td>
        <td><button onclick="getLogn()">Обновить</button></td>
    </tr>
</table>
<br>
<button onclick="tryOn()">Вкл парсер</button>
<button onclick="tryOff()">Выкл парсер</button>

<br>
<br>
<br><label>SQL-команда:</label>
<input type="text" placeholder="SQL" value="SELECT * FROM system_messages" name="sql">
<button onclick="sendSql()">Выполнить</button>

<br>
Вывод:
<hr>
<pre><span class="db_output"></span></pre>
</body>
<style>
    input[type='text'] {
        width: 50%;
        height: 50px;
        padding: 12px 20px;
        box-sizing: border-box;
        border: 2px solid #ccc;
        border-radius: 4px;
        background-color: #f8f8f8;
        resize: none;
    }
</style>
<script type="text/javascript" src="resources/jquery/3.2.0/jquery.min.js"></script>
<script type="text/javascript">

    $(document).ready(function() {
        getLogn();
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

    function getLogn() {
        $.ajax({
            url: 'rest/player',
            type: 'GET',
            success: function (response) {
                $('.logn').text(response);
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
            success: function (response) {
                getLogn();
            },
            data: { login: $("input[name='login']").val(), password:$("input[name='password']").val()},
            beforeSend: function () {
                $('.status').text('Вход...');
            },
            complete: function () {
                getStatus();
            }
        });
    }

    function sendSql() {
        $.ajax({
            url: 'rest/sql',
            type: 'POST',
            success: function (response) {
                $("span.db_output").text(response);
            },
            data: { sql : $("input[name='sql']").val() }
        });
    }
</script>
</html>