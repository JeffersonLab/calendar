<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="helper" scope="request" class="org.jlab.atlis.calendar.presentation.utility.CalendarHelper"/>
<jsp:useBean id="selectHelper" class="org.jlab.atlis.calendar.presentation.utility.SelectHelper" scope="request"/>
<c:set var="title" value="Audit"/>
<t:page title="${title}">
    <jsp:body>
        <div class="header">
            <%@include file="../fragments/site-left-header.jspf" %>
            <div class="center-header">
                <h2>Audit</h2>
                <div class="nav">
                </div>
            </div>
            <div class="right-header">
            </div>
        </div>
        <div class="content">
            <c:choose>
                <c:when test="${messages.error ne null}">
                    <span class="error"><c:out value="${messages.error}"/></span>
                </c:when>
                <c:when test="${not empty revs}">
                    <table class="basic">
                        <thead>
                        <tr>
                            <th>Revision ID</th>
                            <th>Timestamp</th>
                            <th>Username</th>
                            <th>IP</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${revs}" var="revision">
                            <tr>
                                <td><c:out value="${revision.id}"/></td>
                                <td><f:formatDate value="${revision.revisionDate}" pattern="yyyy-MM-dd HH:mm"/></td>
                                <td><c:out value="${revision.username}"/></td>
                                <td><c:out value="${revision.address}"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <div>None</div>
                </c:otherwise>
            </c:choose>
        </div>
    </jsp:body>
</t:page>