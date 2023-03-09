<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="helper" class="org.jlab.atlis.calendar.presentation.utility.CalendarHelper" scope="request"/>
<jsp:useBean id="selectHelper" class="org.jlab.atlis.calendar.presentation.utility.SelectHelper" scope="request"/>
<c:set var="title" value="Copy Occurrence"/>
<t:page title="${title}">
        <jsp:attribute name="stylesheets">
        <link type="text/css" rel="stylesheet" href="resources/v${initParam.releaseNumber}/css/form.css"/>
        </jsp:attribute>
    <jsp:attribute name="scripts">
        <script type="text/javascript" src="resources/v${initParam.releaseNumber}/js/edit-event.js"></script>
                <script type="text/javascript"
                        src="resources/v${initParam.releaseNumber}/js/charCount.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                $("#description").charCount({
                    allowed: 512,
                    warning: 20,
                    counterText: 'Characters Remaining: '
                });
                $("#remark").charCount({
                    allowed: 512,
                    warning: 20,
                    counterText: 'Characters Remaining: '
                });
            });
        </script>
    </jsp:attribute>
    <jsp:body>

        <div class="header">
            <%@include file="../fragments/site-left-header.jspf" %>
            <div class="center-header">
                <h2>Copy Occurrence</h2>
            </div>
            <div class="right-header">
            </div>
        </div>
        <div class="content">
            <span class="error page-error"><c:out value="${messages.error}"/></span>
            <form method="post" action="copy-occurrence">
                <div class="action-button-panel">
                    <input class="styled-button" type="button" value="Cancel"
                           onclick="window.location.href = '${returnPath}'"/>
                    <input class="styled-button" type="submit" value="Copy"/>
                </div>
                <dl class="horizontal-fields">
                    <dt>
                        <label>Event ID</label>
                    </dt>
                    <dd>
                        <c:out value="${fn:escapeXml(empty occurrence ? param.eventId : occurrence.event.eventId)}"/>
                        <a class="alwaysblue"
                           href="edit-event?calendar=${empty occurrence ? '' : occurrence.event.calendar.calendarId}&eventId=${fn:escapeXml(empty occurrence ? param.eventId : occurrence.event.eventId)}">Edit
                            Event</a>
                        <input type="hidden" name="eventId"
                               value="${fn:escapeXml(empty occurrence ? param.eventId : occurrence.event.eventId)}"/>
                    </dd>
                    <dt>
                        <label>Calendar</label>
                    </dt>
                    <dd>
                        <c:out value="${selectedCalendar.name}"/>
                    </dd>
                    <c:if test="${not (empty occurrence.occurrenceId and empty param.occurrenceId)}">
                        <dt>
                            <label>Occurrence ID</label>
                        </dt>
                        <dd>
                            <c:out value="${empty occurrence ? param.occurrenceId : occurrence.occurrenceId}"/>
                            <input type="hidden" name="occurrenceId"
                                   value="${fn:escapeXml(empty occurrence ? param.occurrenceId : occurrence.occurrenceId)}"/>
                        </dd>
                    </c:if>
                    <dt>
                        <label class="required" for="title">Title</label>
                    </dt>
                    <dd>
                        <input id="title" name="title" type="text"
                               value="${fn:escapeXml(empty occurrence ? param.title : occurrence.title)}"
                               maxlength="128"/>
                        <span class="error">${messages.title}</span>
                    </dd>
                    <dt>
                        <label for="description">Description</label>
                    </dt>
                    <dd>
                        <textarea id="description" name="description" maxlength="512"><c:out
                                value="${empty occurrence ? param.description : occurrence.description}"/></textarea>
                        <span class="error">${messages.description}</span>
                    </dd>
                    <dt>
                        <label for="liaison">Liaison</label>
                    </dt>
                    <dd>
                        <input id="liaison" name="liaison" type="text"
                               value="${fn:escapeXml(empty occurrence ? param.liaison : occurrence.liaison)}"
                               maxlength="64"/>
                        <span class="error">${messages.liaison}</span>
                    </dd>
                    <dt>
                        <label class="required" for="display">Display</label>
                    </dt>
                    <dd>
                        <select id="display" name="display">
                            <option></option>
                            <c:forEach items="${selectHelper.displays}" var="d">
                                <option value="${d}"${empty occurrence ? (param.display eq d ? ' selected="selected"' : '') : (occurrence.display eq d ? ' selected="selected"' : '')}>${d}</option>
                            </c:forEach>
                        </select>
                        <span class="error">${messages.display}</span>
                    </dd>
                    <dt>
                        <label>Style</label>
                    </dt>
                    <dd>
                        <c:forEach items="${styleChoices}" var="c">
                            <span class="checkable">
                                <input type="checkbox" name="style"
                                       value="${fn:escapeXml(c.occurrenceStyleChoiceId)}"${empty occurrence ? (selectHelper.containsStyleInArray(paramValues.style, c.occurrenceStyleChoiceId) ? ' checked="checked"' : '') : (selectHelper.containsStyleInList(occurrence.styles, c.occurrenceStyleChoiceId) ? ' checked="checked"' : '')}/><span
                                    class="${c.cssClassName}"><c:out value="${c.name}"/></span>
                            </span>
                        </c:forEach>
                        <span class="error">${messages.style}</span>
                    </dd>
                    <dt>
                        <label for="remark">Operability Comments</label>
                    </dt>
                    <dd>
                        <div class="textarea-container">
                            <textarea id="remark" name="remark" maxlength="512"><c:out
                                    value="${empty occurrence ? param.remark : occurrence.remark}"/></textarea>
                        </div>
                        <span class="error">${messages.remark}</span>
                    </dd>
                </dl>
                <div id="calendar-section">
                    <dl class="horizontal-fields">
                        <dt>
                            <label>Calendar</label>
                        </dt>
                        <dd>
                            <select name="calendar" id="calendarselect">
                                <c:forEach items="${calendarList}" var="calendar">
                                    <option value="${calendar.calendarId}"${calendarId eq calendar.calendarId ? ' selected="selected"' : ''}>
                                        <c:out value="${calendar.name}"/></option>
                                </c:forEach>
                            </select>
                            <span style="font-style: italic">*Copying to a new calendar results in a new event</span>
                        </dd>
                    </dl>
                </div>
                <div id="occurrences-section">
                    <table id="occurrences-table">
                        <caption>Occurrences</caption>
                        <thead>
                        <tr>
                            <th>Day and Shift</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${paramValues.date}" var="date">
                            <tr>
                                <td><input type="hidden" name="date" value="${date}"/>${date}</td>
                                <td><input class="styled-button" type="button" value="Remove"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <span class="error">${messages.selectedOccurrences}</span>


                    <span class="error">${messages.date}</span>
                    <dl>
                        <dt>
                            <label class="required" for="start">Day (from)</label>
                        </dt>
                        <dd>
                            <f:formatDate pattern="yyyy-MM-dd" value="${occurrence.yearMonthDay}" var="startDate"/>
                            <input id="start" autocomplete="off" class="datepicker" type="text" value="${startDate}"
                                   maxlength="10"/>
                            <span class="format">(yyyy-mm-dd)</span>
                        </dd>
                        <dt>
                            <label for="end">Day (to)</label>
                        </dt>
                        <dd>
                            <input id="end" autocomplete="off" class="datepicker" type="text" value="" maxlength="10"/>
                            <span class="format">(yyyy-mm-dd)</span>
                        </dd>
                        <dt>
                            <label class="required" for="shift">Shift</label>
                        </dt>
                        <dd>
                            <select id="shift">
                                <c:forEach items="${selectHelper.shifts}" var="s">
                                    <option value="${s}"${s eq 'DAY' ? ' selected="selected"' : ''}>${s}</option>
                                </c:forEach>
                            </select>
                        </dd>
                    </dl>
                    <input id="add-occurrence-button" class="styled-button" type="button" value="Add"/>

                </div>
                <input type="hidden" value="http://host/${returnPath}" name="referrer"/>
                <div class="action-button-panel">
                    <input class="styled-button" type="button" value="Cancel"
                           onclick="window.location.href = '${returnPath}'"/>
                    <input class="styled-button" type="submit" value="Copy"/>
                </div>
            </form>
        </div>
    </jsp:body>
</t:page>
