<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<section>
    <h3><a href="index.html"><spring:message code="meal.form.home"/></a></h3>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <h2><c:if test="${meal.id == null}"><spring:message code="meal.form.create"/></c:if>
        <c:if test="${meal.id != null}"><spring:message code="meal.form.edit"/></c:if></h2>
    <hr>

    <form method="post" action="meals/save">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt><spring:message code="meal.time"/>:</dt>
            <dd><input type="datetime-local" dataformatas="yyyy-MM-dd HH:mm" value="${meal.dateTime}" name="dateTime" required></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.description"/>:</dt>
            <dd><input type="text" value="${meal.description}" size=40 name="description" placeholder="required"
                       required></dd>
        </dl>
        <dl>
            <dt><spring:message code="meal.calories"/>:</dt>
            <dd><input type="number" value="${meal.calories}" name="calories" required></dd>
        </dl>
        <button type="submit"><c:if test="${meal.id == null}"><spring:message code="meal.form.create.save"/></c:if>
            <c:if test="${meal.id != null}"><spring:message code="meal.form.update"/></c:if></button>
        <button onclick="window.history.back()" type="button"><spring:message code="meal.form.cancel"/>:</button>
    </form>
</section>
</body>
</html>
