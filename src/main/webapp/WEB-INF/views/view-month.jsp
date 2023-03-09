<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="helper" scope="request" class="org.jlab.atlis.calendar.presentation.utility.CalendarHelper"/>
<jsp:useBean id="selectHelper" class="org.jlab.atlis.calendar.presentation.utility.SelectHelper" scope="request"/>
<c:url value="${helper.getAbsoluteBaseURL(pageContext.request)}/${helper.calculateViewMonthURL(calendarId, calendar.yearMonth)}"
       var="absViewURL"/>
<f:formatDate value="${calendar.yearMonth}" pattern="MMMM yyyy" var="titleDate"/>
<c:set var="title" value="${titleDate}"/>
<t:page title="${title}">
        <jsp:attribute name="stylesheets">
        </jsp:attribute>
    <jsp:attribute name="scripts">
        <script type="text/javascript" src="resources/v${initParam.resourceVersionNumber}/js/order.js"></script>
        </jsp:attribute>
    <jsp:body>
        <div class="header">
            <%@include file="../fragments/site-left-header.jspf" %>
            <div class="center-header">
                    <%-- wkhtmltopdf doesn't allow anchors which match current url (or url param to wkhtmltopdf?) so we make the link slightly different --%>
                <h2>
                    <button class="timeline-scroller styled-button" title="Previous Month"
                            onclick="window.location.href = '${helper.calculatePreviousMonthURL(calendarId, calendar.yearMonth)}'">
                        <span class="ui-icon ui-icon-seek-prev"></span></button>
                    <a id="pageLink" href="${fn:escapeXml(absViewURL)}&amp;link=pdf"><f:formatDate
                            value="${calendar.yearMonth}" pattern="MMMM yyyy"/></a>
                    <button class="timeline-scroller styled-button" title="Next Month"
                            onclick="window.location.href = '${helper.calculateNextMonthURL(calendarId, calendar.yearMonth)}'">
                        <span class="ui-icon ui-icon-seek-next"></span></button>
                </h2>
                <div class="nav">
                    <%@include file="../fragments/core-nav.jspf" %>
                </div>
            </div>
            <div class="right-header">
                <%@include file="../fragments/key.jspf" %>
            </div>
        </div>
        <c:if test="${pageContext.request.isUserInRole('oability') or pageContext.request.isUserInRole('pd')}">
            <div id="editable-control-panel-hook">
                <div id="editable-control-panel">
                    <form id="editable-form" method="get" action="view-month">
                        <input type="hidden" name="calendar" value="${param.calendar}"/>
                        <input type="hidden" name="year" value="${param.year}"/>
                        <input type="hidden" name="month" value="${param.month}"/>
                        <label for="editable-checkbox">Edit:</label>
                        <input id="editable-checkbox" type="checkbox" name="editable"
                               value="true" ${param.editable ne null ? 'checked="checked"' : ''}
                               onclick="document.getElementById('editable-form').submit();"/>
                    </form>
                    <c:if test="${param.editable ne null}">
                        <a class="styled-button" id="move-opener-link" href="#">Move...</a>
                    </c:if>
                </div>
            </div>
        </c:if>
        <div class="content">
            <div class="table-wrapper">
                <table class="calendar">
                    <thead>
                    <%@include file="../fragments/header-row.jspf" %>
                    </thead>
                    <tbody>
                    <c:forEach items="${calendar.weeks}" var="week">
                        <%@include file="../fragments/week-row.jspf" %>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <%@include file="../fragments/calendar-footer.jspf" %>
    </jsp:body>
</t:page>
