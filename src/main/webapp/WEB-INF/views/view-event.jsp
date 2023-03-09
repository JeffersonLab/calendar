<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="helper" class="org.jlab.atlis.calendar.presentation.utility.CalendarHelper" scope="request"/>
<jsp:useBean id="selectHelper" class="org.jlab.atlis.calendar.presentation.utility.SelectHelper" scope="request"/>
<c:set var="title" value="Event ${event.eventId}"/>
<t:page title="${title}">
        <jsp:attribute name="stylesheets">
        <link type="text/css" rel="stylesheet"
              href="resources/v${initParam.resourceVersionNumber}/css/basic-table-print.css" media="print"/>
        <link type="text/css" rel="stylesheet" href="resources/v${initParam.resourceVersionNumber}/css/form.css"/>
        </jsp:attribute>
    <jsp:body>
        <div class="header">
            <%@include file="../fragments/site-left-header.jspf" %>
            <div class="center-header">
                <h2>Event ${event.eventId}</h2>
                <div class="nav">
                    <%@include file="../fragments/core-nav.jspf" %>
                </div>
            </div>
            <div class="right-header">
            </div>
        </div>
        <div class="content">
            <dl class="horizontal-fields">
                <dt>
                    <label>Calendar</label>
                </dt>
                <dd>
                    <c:out value="${event.calendar.name}"/>
                </dd>
                <dt>
                    <label>ATLis Task ID</label>
                </dt>
                <dd>
                    <c:out value="${event.taskId}"/>
                    <c:if test="${event.taskId ne null}">
                        <a class="alwaysblue" href="${helper.calculateAtlisURL(event.taskId)}">View Task</a>
                    </c:if>
                </dd>
            </dl>
            <div id="occurrences-section">
                <table id="occurrences-table">
                    <caption>Occurrences</caption>
                    <thead>
                    <tr>
                        <th>Day and Shift</th>
                        <th>Occurrence ID</th>
                        <th>Title</th>
                        <th>Description</th>
                        <th>Liaison</th>
                        <th>Display</th>
                        <th>Style</th>
                        <c:if test="${pageContext.request.isUserInRole('oability') or pageContext.request.isUserInRole('pd')}">
                            <th>Operability Comments</th>
                            <th class="action-column"></th>
                        </c:if>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${event.occurrenceList}" var="o">
                        <tr>
                            <td>${o.date}</td>
                            <td>
                                <c:out value="${o.occurrenceId}"/>
                            </td>
                            <td>
                                <c:out value="${o.title}"/>
                            </td>
                            <td class="linkify">
                                <c:out value="${o.description}"/>
                            </td>
                            <td>
                                <c:out value="${o.liaison}"/>
                            </td>
                            <td><c:out value="${o.display}"/></td>
                            <td>
                                <c:forEach items="${o.styles}" var="s">
                                    <span class="${s.occurrenceStyleChoice.cssClassName}"><c:out
                                            value="${s.occurrenceStyleChoice.name}"/></span>
                                </c:forEach>
                            </td>
                            <c:if test="${pageContext.request.isUserInRole('oability') or pageContext.request.isUserInRole('pd')}">
                                <td class="linkify"><c:out value="${o.remark}"/></td>
                                <td>
                                    <form class="deleteform" method="post" action="view-occurrence">
                                            <span class="column-button-group">
                                                <a class="styled-button" title="Edit Occurrence"
                                                   href="edit-occurrence?occurrenceId=${o.occurrenceId}">Edit</a>
                                                <a class="styled-button" title="Copy Occurrence"
                                                   href="copy-occurrence?copyId=${o.occurrenceId}">Copy</a>
                                                <input type="hidden" name="occurrenceId" value="${o.occurrenceId}"/>
                                                <input type="submit" title="Delete Occurrence" class="styled-button"
                                                       value="Delete"
                                                       onclick="return confirm('Are you sure you want to delete this occurrence?');"/>
                                            </span>
                                    </form>
                                </td>
                            </c:if>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <c:if test="${pageContext.request.isUserInRole('oability') or pageContext.request.isUserInRole('pd')}">
                    <a id="create-link" class="styled-button" href="edit-occurrence?eventId=${event.eventId}">Create</a>
                </c:if>
            </div>
            <form method="post" action="view-event">
                <input type="hidden" name="eventId" value="${event.eventId}"/>
                <div class="action-button-panel">
                    <c:if test="${pageContext.request.isUserInRole('oability') or pageContext.request.isUserInRole('pd')}">
                        <a class="styled-button" href="edit-event?eventId=${event.eventId}">Edit Event</a>
                        <input class="styled-button" type="submit" value="Delete Event"
                               onclick="return confirm('Are you sure you want to delete this event and all occurrences?');"/>
                    </c:if>
                </div>
            </form>
        </div>
    </jsp:body>
</t:page>
