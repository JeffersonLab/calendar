<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="helper" scope="request" class="org.jlab.atlis.calendar.presentation.utility.CalendarHelper"/>
<jsp:useBean id="selectHelper" class="org.jlab.atlis.calendar.presentation.utility.SelectHelper" scope="request"/>
<c:set var="title" value="Search ATLis"/>
<t:page title="${title}">
        <jsp:attribute name="stylesheets">
        <link type="text/css" rel="stylesheet" href="resources/v${initParam.resourceVersionNumber}/css/form.css"/>
        </jsp:attribute>
    <jsp:attribute name="scripts">
<script type="text/javascript">
    function next() {
        document.getElementById("startIndex").value = "${paginator.nextStartIndex}";
        document.getElementById("filter-form").submit();
    }

    function previous() {
        document.getElementById("startIndex").value = "${paginator.previousStartIndex}";
        document.getElementById("filter-form").submit();
    }

    function getReset() {
        document.getElementById("startIndex").value = "0";
    }
</script>
        </jsp:attribute>
    <jsp:body>
        <div class="header">
            <%@include file="../fragments/site-left-header.jspf" %>
            <div class="center-header">
                <h2>Search ATLis</h2>
                <div class="nav">
                    <ul>
                        <li><a href="view-outlook">Current View</a></li>
                        <li><a href="${helper.calculateViewMonthURLFromString(param.start)}">Month View</a></li>
                    </ul>
                </div>
            </div>
            <div class="right-header">
            </div>
        </div>
        <div class="content">
            <span class="error"><c:out value="${messages.error}"/></span>
            <div id="filter-pane">
                <form id="filter-form" method="get" action="search-atlis">
                    <div class="fieldset">
                        <div class="filter-block">
                            <fieldset>
                                <legend>Scheduled Date Range</legend>
                                <dl class="horizontal-fields">
                                    <dt>
                                        <label for="start">Start</label>
                                    </dt>
                                    <dd>
                                        <input id="start" class="datepicker" name="start"
                                               value="${fn:escapeXml(param.start)}"/>
                                        <span class="error">${messages.start}</span>
                                        <span class="format">(yyyy-mm-dd)</span>
                                    </dd>
                                    <dt>
                                        <label for="end">End</label>
                                    </dt>
                                    <dd>
                                        <input id="end" class="datepicker" name="end"
                                               value="${fn:escapeXml(param.end)}"/>
                                        <span class="error">${messages.end}</span>
                                        <span class="format">(yyyy-mm-dd)</span>
                                    </dd>
                                </dl>
                            </fieldset>
                        </div>
                        <div class="filter-block">
                            <fieldset>
                                <legend>Containing Title Text</legend>
                                <dl class="horizontal-fields">
                                    <dt>
                                        <label for="titlePhrase">Text</label>
                                    </dt>
                                    <dd>
                                        <input id="titlePhrase" name="titlePhrase" type="text"
                                               value="${fn:escapeXml(param.titlePhrase)}" maxlength="32"/>
                                    </dd>
                                </dl>
                            </fieldset>
                        </div>
                        <div class="filter-block">
                            <fieldset>
                                <legend>Containing Liaison Text</legend>
                                <dl class="horizontal-fields">
                                    <dt>
                                        <label for="liaisonPhrase">Text</label>
                                    </dt>
                                    <dd>
                                        <input id="liaisonPhrase" name="liaisonPhrase" type="text"
                                               value="${fn:escapeXml(param.liaisonPhrase)}" maxlength="32"/>
                                    </dd>
                                </dl>
                            </fieldset>
                        </div>
                    </div>
                    <div class="action-button-panel">
                        <input class="styled-button" type="submit" value="Search" onclick="getReset()"/>
                    </div>
                    <input type="hidden" id="startIndex" name="startIndex" value="${paginator.startIndex}"/>
                </form>
            </div>
            <span class="info"><c:out value="${messages.noresults}"/></span>
            <c:if test="${not empty tasks}">
                <div class="paginator">
                    <c:if test="${paginator.previous}">
                        <span><a class="alwaysblue" title="Previous" href="#" onclick="previous();">&lt;</a></span>
                    </c:if>
                    <span>Showing tasks ${paginator.firstVisible} - ${paginator.lastVisible} of ${paginator.count}</span>
                    <c:if test="${paginator.next}">
                        <span><a class="alwaysblue" title="Next" href="#" onclick="next();">&gt;</a></span>
                    </c:if>
                </div>
                <table class="basic">
                    <thead>
                    <tr>
                        <th>Task ID</th>
                        <th>Title</th>
                        <th>Liaison</th>
                        <th>Scheduled Date</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${tasks}" var="task">
                        <tr>
                            <td><a class="alwaysblue" href="${helper.calculateAtlisURL(task.taskId)}">${task.taskId}</a>
                            </td>
                            <td><c:out value="${task.title}"/></td>
                            <td><c:out value="${task.contactInfo}"/></td>
                            <td><f:formatDate value="${task.scheduleDate}" pattern="yyyy-MM-dd"/></td>
                            <td><a class="alwaysblue" href="${helper.calculateAddEventURLFromAtlis(task.taskId)}">Add To
                                Calendar</a></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </div>
    </jsp:body>
</t:page>
