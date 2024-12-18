<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="helper" scope="request" class="org.jlab.atlis.calendar.presentation.utility.CalendarHelper"/>
<jsp:useBean id="selectHelper" class="org.jlab.atlis.calendar.presentation.utility.SelectHelper" scope="request"/>
<c:set value="/view-week?year=${week.year}&week=${week.week}" var="viewPath"/>
<c:url value="${viewPath}" var="viewURL"/>
<c:url value="${helper.getAbsoluteBaseURL(pageContext.request)}${viewPath}" var="absViewURL"/>
<c:set var="title" value="Week ${week.week} ${week.year}"/>
<t:page title="${title}">
        <jsp:attribute name="stylesheets">
        </jsp:attribute>
    <jsp:attribute name="scripts">
        <script type="text/javascript" src="resources/v${initParam.releaseNumber}/js/order.js"></script>
        </jsp:attribute>
    <jsp:body>
        <div class="header">
            <%@include file="../fragments/site-left-header.jspf" %>
            <div class="center-header">
                    <%-- wkhtmltopdf doesn't allow anchors which match current url (or url param to wkhtmltopdf?) so we make the link slightly different --%>
                <h2>
                    <button class="timeline-scroller styled-button" title="Previous Week"
                            onclick="window.location.href = '${fn:escapeXml(helper.calculatePreviousWeekURL(calendarId, week.year, week.week))}'">
                        <span class="ui-icon ui-icon-seek-prev"></span></button>
                    <a id="pageLink" href="${fn:escapeXml(absViewURL)}&amp;link=pdf">Week ${week.week} ${week.year}</a>
                    <button class="timeline-scroller styled-button" title="Next Week"
                            onclick="window.location.href = '${fn:escapeXml(helper.calculateNextWeekURL(calendarId, week.year, week.week))}'">
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
        <c:if test="${pageContext.request.isUserInRole('calendar-admin')}">
            <div id="editable-control-panel-hook">
                <div id="editable-control-panel">
                    <form id="editable-form" method="get" action="view-week">
                        <input type="hidden" name="calendar" value="${fn:escapeXml(param.calendar)}"/>
                        <input type="hidden" name="year" value="${fn:escapeXml(param.year)}"/>
                        <input type="hidden" name="week" value="${fn:escapeXml(param.week)}"/>
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
                    <%@include file="../fragments/week-row.jspf" %>
                    </tbody>
                </table>
            </div>
        </div>
        <%@include file="../fragments/calendar-footer.jspf" %>
    </jsp:body>
</t:page>
