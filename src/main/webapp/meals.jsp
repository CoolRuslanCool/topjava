<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .exceeded {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <h2>Meals</h2>
    <a href="meals?action=create">Add Meal</a>
    <hr/>
    <div style="width: 250px; height: 120px">
        <form action="meals" method="post">
            <div style="width: 150px; float: left">
                <label for="dateFrom">Дата с:</label>
                <input id="dateFrom" type="date" name="dateFrom">
                <label for="dateTo">Дата до:</label>
                <input id="dateTo" type="date" name="dateTo">
            </div>
            <div style="width: 80px; float: left; margin-left: 20px;">
                <label for="timeFrom">Время с:</label>
                <input id="timeFrom" type="time" name="timeFrom">
                <label for="timeTo">Время до:</label>
                <input id="timeTo" type="time" name="timeTo">
            </div>
            <div style="width: 100%">
                <select name="user">
                    <option value="1">1</option>
                    <option value="2">2</option>
                </select>
                <button style="float: right; margin-top: 10px" type="submit" name="action" value="filter">Submit</button>
            </div>
        </form>
    </div>
    <hr/>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealWithExceed"/>
            <tr class="${meal.exceed ? 'exceeded' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>