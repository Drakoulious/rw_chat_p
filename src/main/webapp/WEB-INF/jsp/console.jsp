<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <link rel="stylesheet" href="resources/bootstrap/3.3.7/css/bootstrap.min.css">
</head>

<body>
<div class="container">
    <form:form action="logout" method="post">
        <sec:authorize access="isAuthenticated()">
            <input class="form-control btn btn-danger" type="submit" value="Logout">
        </sec:authorize>
    </form:form>
    <br>
    <table class="table table-bordered">
        <tr>
            <td><input class="form-control" type="email" name="login" placeholder="Логин Roswar"></td>
            <td><input class="form-control" type="password" name="password" placeholder="Пароль Roswar"></td>
            <td><button class="btn btn-default" onclick="sendAuth()">Залогиниться на Roswar</button></td>
        </tr>
        <tr>
            <td>Статус</td>
            <td class="status"></td>
            <td><button class="btn btn-default" onclick="getStatus()">Обновить</button></td>
        </tr>
        <tr>
            <td>Установленный логин</td>
            <td class="logn"></td>
            <td><button class="btn btn-default" onclick="getLogn()">Обновить</button></td>
        </tr>
    </table>
    <br>
    <button class="btn btn-default" onclick="tryOn()">Вкл парсер</button>
    <button class="btn btn-default" onclick="tryOff()">Выкл парсер</button>
    <br>
    <br>
    <br><label>SQL-команда:</label>
    <div class="btn-group">
        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
            <span class="caret"></span>
            <span class="sr-only">Toggle Dropdown</span>
        </button>
        <ul id="sql" class="dropdown-menu" role="menu">

        </ul>
    </div>


    <form:form method="post">
        <textarea class="form-control" rows="3" placeholder="SQL" name="sql"></textarea>
        <input class="btn btn-info" type="submit" value="Вывести в excel">
        <button class="btn btn-default" type="button" onclick="sendSql(); return false">Выполнить</button>
        <button type="button" class="btn btn-success" onclick="defineSql(); return false">Запомнить</button>
    </form:form>

    <br>
    Вывод:
    <hr>
</div>
<pre><span class="db_output"></span></pre>
</body>
<script type="text/javascript" src="resources/jquery/3.2.0/jquery.min.js"></script>
<script type="text/javascript" src="resources/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script type="text/javascript">

    $(document).ready(function() {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $(document).ajaxSend(function(e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });
        getLogn();
        getStatus();
        getSql();
    });
    
    function renderSql(sql) {
        var ul = $('#sql');
        ul.empty();
        for (var i = 0; i < sql.length; i++) {
            var obj = sql[i];
            var li = $("<li></li>");
            var alink = $("<a></a>").attr("val", obj.sql).text(obj.title);
            alink.click(function () {
                setSql($(this).attr('val'));
            });
            li.append(alink);
            ul.append(li);
        }
    }

    function setSql(val) {
        $("textarea").val(val)
    }
    
    function getSql() {
        $.ajax({
            url: 'ajax/command',
            type: 'GET',
            success: function (response) {
                renderSql(response);
            }
        })
    }

    function defineSql() {
        var sql = $("textarea").val();
        var title = prompt("Название команды:");
        console.log(title);
        if (title != false){
            if (title != ""){
                if (title != null)
                addSql(title, sql);
            }
        }
    }

    function addSql(title, sql) {
        $.ajax({
            url: 'ajax/command',
            type: 'POST',
            data: { title: title, sqlx: sql },
            success: function (response) {
                getSql();
            }
        })
    }

    function deleteSql(id) {
        $.ajax({
            url: 'ajax/command',
            type: 'DELETE',
            data: { id: id },
            success: function (response) {
                getSql();
            }
        })
    }

    function tryOn() {
        $.ajax({
            url: 'ajax/on',
            type: 'GET',
            success: function (response) {
                $('.status').text(response);
            }
        });
    }

    function tryOff() {
        $.ajax({
            url: 'ajax/off',
            type: 'GET',
            success: function (response) {
                $('.status').text(response);
            }
        });
    }

    function getLogn() {
        $.ajax({
            url: 'ajax/player',
            type: 'GET',
            success: function (response) {
                $('.logn').text(response);
            }
        });
    }

    function getStatus() {
        $.ajax({
            url: 'ajax/status',
            type: 'GET',
            success: function (response) {
                $('.status').text(response);
            }
        });
    }

    function sendAuth() {
        $.ajax({
            url: 'ajax/',
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
        var query = jQuery("textarea").val();
        console.log(query);
        $.ajax({
            url: 'ajax/sql',
            type: 'POST',
            success: function (response) {
                $("span.db_output").text(response);
            },
            data: { value : query }
        });
    }
</script>
</html>