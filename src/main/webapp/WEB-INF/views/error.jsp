<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="title" value="${message}"/>
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
                <h2>Error</h2>
                <div class="nav">
                    <ul>
                        <li><a href="view-outlook">Current Outlook</a></li>
                    </ul>
                </div>
            </div>
            <div class="right-header">
            </div>
        </div>
        <div class="content">
            <h3><c:out value="${message}"/></h3>
        </div>
    </jsp:body>
</t:page>
