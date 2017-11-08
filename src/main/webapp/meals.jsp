<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>

<form method="post" action="meals">
    <label for="add_descr">Описание</label>
    <input id="add_descr" name="description" value="Оббед" type="text">
    <label for="add_date">Дата</label>
    <input id="add_date" name="date" value="22/3/1980 10:04" type="text">
    <label for="add_calorie">Калории</label>
    <input id="add_calorie" name="calorie" value="2010" type="text">
    <button type="submit" name="add" value="true">Add</button>
</form>

<table style="border: medium solid #b8b8b8; border-radius: 12px; padding: 10px">

    <thead>
    <tr>
        <th width="120">Описание</th>
        <th width="120">Дата</th>
        <th width="80">Колории</th>
        <th width="80">ID</th>
    </tr>
    </thead>

    <tbody>
    <c:forEach items="${meals}" var="m">
        <tr style="color: ${m.isExceed() ? 'red' : 'green'}">
            <form name="form${m.getId()}" action="meals" method="get">
                <td>
                    <input class="meal${m.getId()}" style="border: none; color: ${m.isExceed() ? 'red' : 'green'}"
                           readonly name="description" value="${m.getDescription()}"/>
                </td>
                <td align="center">
                    <input class="meal${m.getId()}" style="border: none; color: ${m.isExceed() ? 'red' : 'green'}"
                           readonly name="date" value="${m.getDateTime().format(formatter)}"/>
                </td>
                <td align="center">
                    <input class="meal${m.getId()}" style="border: none; color: ${m.isExceed() ? 'red' : 'green'}"
                           readonly name="calorie" value="${m.getCalories()}"/>
                </td>
                <td align="center">
                    <button id="editbtn${m.getId()}" onclick="edit(${m.getId()})" type="button"
                            value="edit-${m.getId()}" name="action">Редактировать</button>
                </td>
                <td align="center">
                    <button value="delete-${m.getId()}" name="action">Удалить</button>
                </td>
            </form>
        </tr>
    </c:forEach>
    </tbody>

</table>
<script>
    function edit(id) {
        var el = 'meal' + id;
        if (document.getElementById('editbtn' + id).innerHTML === 'Редактировать') {
            document.getElementById('editbtn' + id).innerHTML = 'Сохранить';
            var els = document.getElementsByClassName(el);
            for (var i = 0; els.length; i++) {
                els[i].removeAttribute('readonly');
                els[i].style.border = 'solid';
            }
        } else {
            var f = 'editbtn' + id;
            document.forms.namedItem('form' + id).setAttribute('method', 'post');
            document.getElementById(f).setAttribute("type", "submit").click();
        }
    }
</script>
</body>
</html>