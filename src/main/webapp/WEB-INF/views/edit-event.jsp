<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="helper" class="org.jlab.atlis.calendar.presentation.utility.CalendarHelper" scope="request"/>
<jsp:useBean id="selectHelper" class="org.jlab.atlis.calendar.presentation.utility.SelectHelper" scope="request"/>
<c:set var="title" value="${empty event.eventId and empty param.eventId ? 'Create' : 'Edit'} Event"/>
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
                <h2>${empty event.eventId and empty param.eventId ? 'Create' : 'Edit'} Event</h2>
            </div>
            <div class="right-header">
            </div>
        </div>
        <div class="content">
            <span class="error page-error"><c:out value="${messages.error}"/></span>
            <form method="post" action="edit-event">
                <div class="action-button-panel">
                    <input class="styled-button" type="button" value="Cancel"
                           onclick="window.location.href = '${returnPath}'"/>
                    <input class="styled-button" type="submit"
                           value="${empty event.eventId and empty param.eventId ? 'Create' : 'Edit'}"/>
                </div>
                <dl class="horizontal-fields">
                    <c:if test="${not (empty event.eventId and empty param.eventId)}">
                        <dt>
                            <label>Event ID</label>
                        </dt>
                        <dd>
                            <c:out value="${empty event.eventId ? param.eventId : event.eventId}"/>
                            <span class="error">${messages.eventId}</span>
                            <input type="hidden" name="eventId"
                                   value="${empty event.eventId ? param.eventId : event.eventId}"/>
                        </dd>
                    </c:if>
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
                    </dd>
                    <dt>
                        <label for="taskId">ATLis Task ID</label>
                    </dt>
                    <dd>
                        <input id="taskId" name="taskId" type="text"
                               value="${fn:escapeXml(empty event ? param.taskId : event.taskId)}" maxlength="10"/>
                        <span class="error">${messages.taskId}</span>
                    </dd>
                </dl>
                <div class="fieldset">
                    <c:if test="${not (empty event.eventId and empty param.eventId)}">
                        <label class="select-for-update-label">Select Fields for Update (<span id="field-all-link"
                                                                                               class="checkbox-helper">All</span>
                            | <span id="field-none-link" class="checkbox-helper">None</span>)</label>
                    </c:if>
                    <dl id="occurrence-fields" class="horizontal-fields">
                        <dt>
                            <c:if test="${not (empty event.eventId and empty param.eventId)}">
                                <input name="titleForUpdate" class="select-for-update" type="checkbox"
                                       value="Y"${param.titleForUpdate eq 'Y' ? 'checked="checked"' : ''}/>
                            </c:if>
                            <label class="required" for="title">Title</label>
                        </dt>
                        <dd>
                            <input id="title" name="title" type="text"
                                   value="${fn:escapeXml(empty occurrence ? param.title : occurrence.title)}"
                                   maxlength="128"/>
                            <span class="error">${messages.title}</span>
                        </dd>
                        <dt>
                            <c:if test="${not (empty event.eventId and empty param.eventId)}">
                                <input name="descriptionForUpdate" class="select-for-update" type="checkbox"
                                       value="Y"${param.descriptionForUpdate eq 'Y' ? 'checked="checked"' : ''}/>
                            </c:if>
                            <label for="description">Description</label>
                        </dt>
                        <dd>
                            <textarea id="description" name="description" maxlength="512"><c:out
                                    value="${empty occurrence ? param.description : occurrence.description}"/></textarea>
                            <span class="error">${messages.description}</span>
                        </dd>
                        <dt>
                            <c:if test="${not (empty event.eventId and empty param.eventId)}">
                                <input name="liaisonForUpdate" class="select-for-update" type="checkbox"
                                       value="Y"${param.liaisonForUpdate eq 'Y' ? 'checked="checked"' : ''}/>
                            </c:if>
                            <label for="liaison">Liaison</label>
                        </dt>
                        <dd>
                            <input id="liaison" name="liaison" type="text"
                                   value="${fn:escapeXml(empty occurrence ? param.liaison : occurrence.liaison)}"
                                   maxlength="64"/>
                            <span class="error">${messages.liaison}</span>
                        </dd>
                        <dt>
                            <c:if test="${not (empty event.eventId and empty param.eventId)}">
                                <input name="displayForUpdate" class="select-for-update" type="checkbox"
                                       value="Y"${param.displayForUpdate eq 'Y' ? 'checked="checked"' : ''}/>
                            </c:if>
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
                            <c:if test="${not (empty event.eventId and empty param.eventId)}">
                                <input name="styleForUpdate" class="select-for-update" type="checkbox"
                                       value="Y"${param.styleForUpdate eq 'Y' ? 'checked="checked"' : ''}/>
                            </c:if>
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
                            <c:if test="${not (empty event.eventId and empty param.eventId)}">
                                <input name="remarkForUpdate" class="select-for-update" type="checkbox"
                                       value="Y"${param.remarkForUpdate eq 'Y' ? 'checked="checked"' : ''}/>
                            </c:if>
                            <label for="remark">Operability Comments</label>
                        </dt>
                        <dd>
                            <textarea id="remark" name="remark" maxlength="512"><c:out
                                    value="${empty occurrence ? param.remark : occurrence.remark}"/></textarea>
                            <span class="error">${messages.remark}</span>
                        </dd>
                    </dl>
                </div>
                <div id="occurrences-section">
                    <table id="occurrences-table">
                        <caption>Occurrences</caption>
                        <thead>
                        <tr>
                            <c:choose>
                                <c:when test="${empty event.eventId and empty param.eventId}">
                                    <th>Day and Shift</th>
                                    <th></th>
                                </c:when>
                                <c:otherwise>
                                    <th>Select for Update (<span id="occurrence-all-link"
                                                                 class="checkbox-helper">All</span> | <span
                                            id="occurrence-none-link" class="checkbox-helper">None</span>)
                                    </th>
                                    <th>Day and Shift</th>
                                    <th>Display</th>
                                    <th>Template</th>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${empty event.eventId and empty param.eventId}">
                                <c:forEach items="${empty event ? paramValues.date : event.dates}" var="date">
                                    <tr>
                                        <td><input type="hidden" name="date" value="${date}"/>${date}</td>
                                        <td><input class="styled-button" type="button" value="Remove"/></td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <%-- This is dangerous because we have no guarantee that the correlated items order is maintained! --%>
                                <c:set value="${empty event ? paramValues.occurrenceId : event.occurrenceIds}"
                                       var="id"/>
                                <c:set value="${empty event ? paramValues.date : event.dates}" var="date"/>
                                <c:forEach var="i" begin="0" end="${(fn:length(id) > 0) ? (fn:length(id) - 1) : 0}">
                                    <tr>
                                        <td>
                                            <input name="occurrenceForUpdate" type="checkbox" class="select-for-update"
                                                   value="${id[i]}"${selectHelper.containsIdInArray(paramValues.occurrenceForUpdate, id[i]) ? 'checked="checked"' : ''}/>
                                        </td>
                                        <td><input type="hidden" name="occurrenceId" value="${id[i]}"/>
                                            <input type="hidden" name="date" value="${date[i]}"/>
                                                ${date[i]}
                                        </td>
                                        <td>
                                            <c:out value="${event.occurrenceList[i].display}"/>
                                        </td>
                                        <td>
                                            <a class="styled-button"
                                               href="edit-event?eventId=${empty event.eventId ? param.eventId : event.eventId}&amp;occurrenceId=${id[i]}">Load</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                    <c:if test="${not (empty event.eventId and empty param.eventId)}">
                        <input class="styled-button" type="submit" name="SubmitButton" value="Delete Selected"/>
                        <input class="styled-button" type="submit" name="SubmitButton" value="Hide Selected"/>
                        <input class="styled-button" type="submit" name="SubmitButton" value="Show Selected"/>
                    </c:if>
                    <span class="error">${messages.selectedOccurrences}</span>
                    <c:if test="${empty event.eventId and empty param.eventId}">
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
                                <input id="end" autocomplete="off" class="datepicker" type="text" value=""
                                       maxlength="10"/>
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
                    </c:if>
                </div>
                <div class="action-button-panel">
                    <input type="hidden" value="http://host/${returnPath}" name="referrer"/>
                    <input class="styled-button" type="button" value="Cancel"
                           onclick="window.location.href = '${returnPath}'"/>
                    <input class="styled-button" type="submit"
                           value="${empty event.eventId and empty param.eventId ? 'Create' : 'Edit'}"/>
                </div>
            </form>
        </div>
    </jsp:body>
</t:page>
