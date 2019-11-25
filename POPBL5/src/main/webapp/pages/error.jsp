<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="basePath" scope = "page" value="${pageContext.request.contextPath}"/>
<c:set var="errorCode" scope = "page" value="${not empty requestScope.errorCode ? errorCode : 404 }"/>

<c:set var="pageTitle" scope="request" value="error.${errorCode}.title"/>
<jsp:include page="/includes/header.jsp"/>


<main class="centered-content">
	<fmt:bundle basename="resources.Labels">
		<div class="card">
		<h2 class="card-title"><fmt:message key="error.${errorCode}.title"/></h2>
		<div class="card-body">
			<p><fmt:message key="error.${errorCode}.message"/></p>
		</div>
		</div>
	</fmt:bundle>
</main>
<jsp:include page="/includes/footer.jsp"/>