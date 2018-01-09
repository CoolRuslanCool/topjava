<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="jumbotron">
    <div class="container">
        <h3><spring:message code="meal.title"/></h3>

        <div class="container">
            <div class="row col-xs-5">
                <form method="post" id="filterForm">
                    <dl class="col-xs-6">
                        <dt><spring:message code="meal.startDate"/>:</dt>
                        <dd><input type="text" id="startDate" name="startDate" value=""></dd>
                    </dl>
                    <dl class="col-xs-6">
                        <dt><spring:message code="meal.startTime"/>:</dt>
                        <dd><input type="text" id="startTime" name="startTime" value=""></dd>
                    </dl>
                    <dl class="col-xs-6">
                        <dt><spring:message code="meal.endDate"/>:</dt>
                        <dd><input type="text" id="endDate" name="endDate" value=""></dd>
                    </dl>
                    <dl class="col-xs-6">
                        <dt><spring:message code="meal.endTime"/>:</dt>
                        <dd><input type="text" id="endTime" name="endTime" value=""></dd>
                    </dl>
                    <div class="pull-right">
                        <button type="button" class="btn btn-success" onclick="filter()"><i
                                class="glyphicon glyphicon-filter"></i>
                        </button>
                        <button type="button" class="btn btn-success" onclick="resetFilter()"><i
                                class="glyphicon glyphicon-refresh"></i></button>
                    </div>
                </form>
            </div>
        </div>
        <table class="table table-striped display" id="datatable">
            <a class="btn btn-primary" onclick="add()">
                <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
                <spring:message code="common.add"/>
            </a>
            <thead>
            <tr>
                <th><spring:message code="meal.dateTime"/></th>
                <th><spring:message code="meal.description"/></th>
                <th><spring:message code="meal.calories"/></th>
                <th></th>
                <th></th>
            </tr>
            </thead>
        </table>
    </div>
</div>
<div class="modal fade" id="editRow">
    <div class="modal-dialog">
        <div class="modal-content">

            <div class="modal-header">
                <button type="button" class="close" aria-hidden="true" data-dismiss="modal">&times;</button>
                <h2 class="modal-title"><spring:message code="meal.add"/>/<spring:message code="meal.edit"/></h2>
            </div>

            <div class="modal-body">
                <form class="form-horizontal" id="detailsForm">
                    <input type="hidden" name="id" id="id">
                    <div class="form-group">
                        <label for="datetime" class="control-label col-xs-3"><spring:message
                                code="meal.dateTime"/></label>
                        <div class="col-xs-9 input-group date" id='datetimepicker'>
                            <input type='text' class="form-control" id="datetime" name="datetime"
                                   placeholder="<spring:message code='meal.dateTimeAs'/>">
                            <span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"></span>
                    </span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="description" class="control-label col-xs-3"><spring:message
                                code="meal.description"/></label>
                        <div class="col-xs-9">
                            <input class="form-control data" id="description" name="description"
                                   placeholder="<spring:message code="meal.description"/>" value="jjj">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="calories" class="control-label col-xs-3"><spring:message
                                code="meal.calories"/></label>
                        <div class="col-xs-9">
                            <input class="form-control data" id="calories" name="calories"
                                   placeholder="<spring:message code="meal.calories"/>">
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-xs-offset-3 col-xs-9">
                            <button type="submit" class="btn btn-primary">
                                <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
<script type="text/javascript" src="resources/js/mealDatatables.js" defer></script>
<script type="text/javascript" src="resources/js/datatablesUtil.js" defer></script>
</body>
</html>