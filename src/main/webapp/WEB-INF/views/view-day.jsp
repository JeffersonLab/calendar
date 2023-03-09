<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="helper" scope="request" class="org.jlab.atlis.calendar.presentation.utility.CalendarHelper"/>
<jsp:useBean id="selectHelper" class="org.jlab.atlis.calendar.presentation.utility.SelectHelper" scope="request"/>
<f:formatDate value="${day.yearMonthDay}" pattern="MMMM d yyyy" var="titleDate"/>
<c:set var="title" value="${titleDate}"/>
<t:page title="${title}">
    <jsp:body>
        <div class="header">
            <%@include file="../fragments/site-left-header.jspf" %>
            <div class="center-header">
                <h2>
                    <button class="timeline-scroller styled-button" title="Previous Day"
                            onclick="window.location.href='${helper.calculatePreviousDayURL(calendarId, day.yearMonthDay)}'">
                        <span class="ui-icon ui-icon-seek-prev"></span></button>
                    <f:formatDate value="${day.yearMonthDay}" pattern="MMMM d yyyy"/>
                    <button class="timeline-scroller styled-button" title="Next Day"
                            onclick="window.location.href='${helper.calculateNextDayURL(calendarId, day.yearMonthDay)}'">
                        <span class="ui-icon ui-icon-seek-next"></span></button>
                </h2>
                <div class="nav">
                    <%@include file="../fragments/core-nav.jspf" %>
                </div>
            </div>
            <div class="right-header">
            </div>
        </div>
        <div class="content">
            <h3>Event Occurrences</h3>
            <c:choose>
                <c:when test="${not empty day.allOccurrences}">
                    <h4>OWL</h4>
                    <c:set var="fragmentOccurrences" value="${day.getOccurrences('OWL', null, true, true)}"
                           scope="request"/>
                    <%@include file="../fragments/shift-day-table.jspf" %>
                    <h4>DAY</h4>
                    <c:set var="fragmentOccurrences" value="${day.getOccurrences('DAY', null, true, true)}"
                           scope="request"/>
                    <%@include file="../fragments/shift-day-table.jspf" %>
                    <h4>SWING</h4>
                    <c:set var="fragmentOccurrences" value="${day.getOccurrences('SWING', null, true, true)}"
                           scope="request"/>
                    <%@include file="../fragments/shift-day-table.jspf" %>
                </c:when>
                <c:otherwise>
                    <div>None</div>
                </c:otherwise>
            </c:choose>
        </div>
    </jsp:body>
</t:page>
