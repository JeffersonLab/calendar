<%@tag description="The Site Page Template Tag" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@attribute name="title"%>
<%@attribute name="stylesheets" fragment="true"%>
<%@attribute name="scripts" fragment="true"%>
<c:url var="domainRelativeReturnUrl" scope="request" context="/" value="${requestScope['javax.servlet.forward.request_uri']}${requestScope['javax.servlet.forward.query_string'] ne null ? '?'.concat(requestScope['javax.servlet.forward.query_string']) : ''}"/>
<c:set var="currentPath" scope="request" value="${requestScope['javax.servlet.forward.servlet_path']}"/>
<!DOCTYPE html>
<html class="${initParam.notification ne null ? 'special-notification' : ''}">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><c:out value="${initParam.appShortName}"/> - ${title}</title>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/v${initParam.releaseNumber}/img/favicon.ico"/>
    <c:choose>
        <c:when test="${'CDN' eq resourceLocation}">
            <link type="text/css" rel="stylesheet" href="${cdnContextPath}/jquery-ui/1.10.3/theme/atlis/jquery-ui.min.css"/>
            <style>
                .ui-state-hover > .ui-button-text > .ui-icon {
                    background-image: url("${cdnContextPath}/jquery-ui/1.10.3/theme/atlis/images/ui-icons_ffff00_256x240.png") !important;
                }
            </style>
        </c:when>
        <c:otherwise><!-- LOCAL -->
            <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/jquery-ui/1.10.3/theme/atlis/jquery-ui.min.css"/>
            <style>
                .ui-state-hover > .ui-button-text > .ui-icon {
                    background-image: url("${pageContext.request.contextPath}/resources/jquery-ui/1.10.3/theme/atlis/images/ui-icons_ffff00_256x240.png") !important;
                }
            </style>
        </c:otherwise>
    </c:choose>
    <link type="text/css" rel="stylesheet" href="resources/v${initParam.releaseNumber}/css/site.css"/>
    <link type="text/css" rel="stylesheet" href="resources/v${initParam.releaseNumber}/css/calendar.css"/>
    <jsp:invoke fragment="stylesheets"/>
</head>
<body>
<c:if test="${initParam.notification ne null}">
    <div id="notification-bar"><c:out value="${initParam.notification}"/></div>
</c:if>
<jsp:doBody/>
<c:choose>
    <c:when test="${'CDN' eq resourceLocation}">
        <script type="text/javascript" src="${cdnContextPath}/jquery/1.10.2.min.js"></script>
        <script type="text/javascript" src="${cdnContextPath}/jquery-ui/1.10.3/jquery-ui.min.js"></script>
    </c:when>
    <c:otherwise><!-- LOCAL -->
        <script type="text/javascript" src="${pageContext.request.contextPath}/resources/jquery/1.10.2.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/resources/jquery-ui/1.10.3/jquery-ui.min.js"></script>
    </c:otherwise>
</c:choose>
<script type="text/javascript" src="resources/js/linkify-2.1.min.js"></script>
<script type="text/javascript" src="resources/js/linkify-jquery-2.1.min.js"></script>
<script type="text/javascript" src="resources/v${initParam.releaseNumber}/js/datepicker.js"></script>
<script type="text/javascript" src="resources/v${initParam.releaseNumber}/js/calendar.js"></script>
<script>
    jlab = jlab || {};
    jlab.keycloakHostname = '${env["KEYCLOAK_SERVER_FRONTEND"]}';
    <c:url var="iframeLoginUrl" value="https://${env['KEYCLOAK_HOSTNAME']}/auth/realms/${env['KEYCLOAK_REALM']}/protocol/openid-connect/auth">
        <c:param name="client_id" value="account"/>
        <c:param name="kc_idp_hint" value="${env['KEYCLOAK_HEADLESS_IDP']}"/>
        <c:param name="response_type" value="code"/>
        <c:param name="redirect_uri" value="https://${env['KEYCLOAK_SERVER_FRONTEND']}/auth/realms/${env['KEYCLOAK_REALM']}/account/"/>
    </c:url>
    jlab.iframeLoginUrl = '${empty env['KEYCLOAK_HEADLESS_IDP'] ? '' : iframeLoginUrl}';
    <c:url var="suLogoutUrl" value="https://${env['KEYCLOAK_SERVER_FRONTEND']}/auth/realms/${env['KEYCLOAK_REALM']}/protocol/openid-connect/logout">
        <c:param name="redirect_uri" value="https://${env['KEYCLOAK_SERVER_FRONTEND']}/auth/realms/${env['KEYCLOAK_REALM']}/account/"/>
    </c:url>
    jlab.suLogoutUrl = '${suULogoutUrl}';
</script>
<jsp:invoke fragment="scripts"/>
</body>
</html>