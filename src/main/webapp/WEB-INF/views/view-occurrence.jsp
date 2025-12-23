<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="f" uri="jakarta.tags.fmt" %>
<%@taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="helper" class="org.jlab.atlis.calendar.presentation.utility.CalendarHelper" scope="request"/>
<jsp:useBean id="selectHelper" class="org.jlab.atlis.calendar.presentation.utility.SelectHelper" scope="request"/>
<c:set var="title" value="Occurrence ${occurrence.occurrenceId}"/>
<t:page title="${title}">
    <jsp:attribute name="stylesheets">
                <link type="text/css" rel="stylesheet"
                      href="resources/v${initParam.releaseNumber}/css/form.css"/>
    </jsp:attribute>
    <jsp:body>
        <div class="header">
            <%@include file="../fragments/site-left-header.jspf" %>
            <div class="center-header">
                <h2>Occurrence ${occurrence.occurrenceId}</h2>
                <div class="nav">
                    <ul>
                        <li><a href="view-outlook">Outlook</a></li>
                        <li>
                            <a href="${fn:escapeXml(helper.calculateViewMonthURL(calendarId, occurrence.yearMonthDay))}">Month</a>
                        </li>
                        <li><a href="${fn:escapeXml(helper.calculateViewWeekURL(calendarId, occurrence.yearMonthDay))}">Week</a>
                        </li>
                        <li><a href="${fn:escapeXml(helper.calculateViewDayURL(calendarId, occurrence.yearMonthDay))}">Day</a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="right-header">
            </div>
        </div>
        <div class="content">
            <div class="top-left-nav">
                <c:if test="${pageContext.request.isUserInRole('calendar-admin')}">
                    <a class="alwaysblue" href="edit-occurrence?occurrenceId=${occurrence.occurrenceId}">Edit
                        Occurrence</a>
                    <a class="alwaysblue" href="copy-occurrence?copyId=${occurrence.occurrenceId}">Copy Occurrence</a>
                    <a class="alwaysblue" href="edit-event?eventId=${occurrence.event.eventId}">Edit Event</a>
                </c:if>
            </div>
            <form method="post" action="view-occurrence">
                <dl class="horizontal-fields">
                    <dt>
                        <label>Event ID</label>
                    </dt>
                    <dd>
                        <c:out value="${occurrence.event.eventId}"/>
                        <a class="alwaysblue" href="view-event?eventId=${occurrence.event.eventId}">View Event</a>
                    </dd>
                    <dt>
                        <label>Day</label>
                    </dt>
                    <dd>
                        <f:formatDate value="${occurrence.yearMonthDay}" var="yearMonthDay" pattern="yyyy-MM-dd"/>
                        <c:out value="${yearMonthDay}"/>
                    </dd>
                    <dt>
                        <label>Shift</label>
                    </dt>
                    <dd>
                        <c:out value="${occurrence.shift}"/>
                    </dd>
                    <dt>
                        <label>Title</label>
                    </dt>
                    <dd>
                        <c:out value="${occurrence.title}"/>
                    </dd>
                    <dt>
                        <label>Description</label>
                    </dt>
                    <dd>
                        <c:out value="${occurrence.description}"/>
                    </dd>
                    <dt>
                        <label>Liaison</label>
                    </dt>
                    <dd>
                        <c:out value="${occurrence.liaison}"/>
                    </dd>
                    <dt>
                        <label>Display</label>
                    </dt>
                    <dd>
                        <c:out value="${occurrence.display}"/>
                    </dd>
                    <dt>
                        <label>Style</label>
                    </dt>
                    <dd>
                        <c:forEach items="${occurrence.styles}" var="s">
                            <span class="${s.occurrenceStyleChoice.cssClassName}"><c:out
                                    value="${s.occurrenceStyleChoice.name}"/></span>
                        </c:forEach>
                    </dd>
                    <c:if test="${pageContext.request.isUserInRole('calendar-admin')}">
                        <dt>
                            <label>Operability Comments</label>
                        </dt>
                        <dd>
                            <c:out value="${occurrence.remark}"/>
                        </dd>
                    </c:if>
                </dl>
                <input type="hidden" name="occurrenceId" value="${occurrence.occurrenceId}"/>
                <div class="action-button-panel">
                    <c:if test="${pageContext.request.isUserInRole('calendar-admin')}">
                        <input class="styled-button" type="submit" value="Delete"
                               onclick="return confirm('Are you sure you want to delete this occurrence?');"/>
                    </c:if>
                </div>
            </form>
        </div>
    </jsp:body>
</t:page>
