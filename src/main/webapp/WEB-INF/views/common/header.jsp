<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SkillSwapHub - ${param.title}</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/skill-styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/request-styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/message-styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/rating-styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/leaderboard-styles.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/browser-fixes.css">

    <!-- Browser compatibility script -->
    <script src="${pageContext.request.contextPath}/js/browser-compatibility.js" defer></script>

    <!-- HTML5 shim and Respond.js for IE8 support -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
    <header class="main-header">
        <div class="container">
            <div class="logo">
                <a href="${pageContext.request.contextPath}/">
                    <span class="logo-text">SkillSwapHub</span>
                </a>
            </div>
            <nav class="main-nav">
                <ul>
                    <li><a href="${pageContext.request.contextPath}/"><i class="fas fa-home"></i> Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/skills"><i class="fas fa-th-list"></i> Skills</a></li>
                    <li><a href="${pageContext.request.contextPath}/leaderboard"><i class="fas fa-trophy"></i> Leaderboard</a></li>
                    <c:choose>
                        <c:when test="${empty sessionScope.user && empty sessionScope.userId}">
                            <li><a href="${pageContext.request.contextPath}/login"><i class="fas fa-sign-in-alt"></i> Login</a></li>
                            <li><a href="${pageContext.request.contextPath}/register" class="btn btn-primary"><i class="fas fa-user-plus"></i> Register</a></li>
                        </c:when>
                        <c:otherwise>
                            <li><a href="${pageContext.request.contextPath}/profile"><i class="fas fa-user-circle"></i> Profile</a></li>
                            <li>
                                <a href="${pageContext.request.contextPath}/requests">
                                    <i class="fas fa-exchange-alt"></i> Requests
                                    <c:if test="${not empty sessionScope.userId}">
                                        <c:set var="requestDAO" value="<%= new com.skillswaphub.dao.SkillRequestDAO() %>" />
                                        <c:set var="pendingCount" value="${requestDAO.getPendingRequestCount(sessionScope.userId)}" />
                                        <c:if test="${pendingCount > 0}">
                                            <span class="notification-badge">${pendingCount}</span>
                                        </c:if>
                                    </c:if>
                                </a>
                            </li>
                            <li>
                                <a href="${pageContext.request.contextPath}/messages">
                                    <i class="fas fa-comments"></i> Messages
                                    <c:if test="${not empty sessionScope.userId}">
                                        <c:set var="messageDAO" value="<%= new com.skillswaphub.dao.MessageDAO() %>" />
                                        <c:set var="unreadCount" value="${messageDAO.getUnreadMessageCount(sessionScope.userId)}" />
                                        <c:if test="${unreadCount > 0}">
                                            <span class="notification-badge">${unreadCount}</span>
                                        </c:if>
                                    </c:if>
                                </a>
                            </li>
                            <li><a href="${pageContext.request.contextPath}/sessions"><i class="fas fa-calendar-alt"></i> Sessions</a></li>
                            <li><a href="${pageContext.request.contextPath}/ratings"><i class="fas fa-star"></i> Ratings</a></li>
                            <c:if test="${not empty sessionScope.userId}">
                                <c:set var="userDAO" value="<%= new com.skillswaphub.dao.UserDAO() %>" />
                                <c:set var="currentUser" value="${userDAO.getUserById(sessionScope.userId)}" />
                                <c:if test="${currentUser.admin}">
                                    <li><a href="${pageContext.request.contextPath}/admin"><i class="fas fa-cog"></i> Admin</a></li>
                                </c:if>
                            </c:if>
                            <li><a href="${pageContext.request.contextPath}/logout" class="btn btn-outline"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </nav>
        </div>
    </header>

    <main class="main-content">
        <div class="container">
            <!-- Display success message if available -->
            <c:if test="${not empty sessionScope.message}">
                <div class="alert alert-success">
                    <i class="fas fa-check-circle"></i> ${sessionScope.message}
                    <c:remove var="message" scope="session" />
                </div>
            </c:if>

            <!-- Display error message if available -->
            <c:if test="${not empty sessionScope.errorMessage}">
                <div class="alert alert-error">
                    <i class="fas fa-exclamation-circle"></i> ${sessionScope.errorMessage}
                    <c:remove var="errorMessage" scope="session" />
                </div>
            </c:if>

            <!-- Display error message if available (request scope) -->
            <c:if test="${not empty requestScope.errorMessage}">
                <div class="alert alert-error">
                    <i class="fas fa-exclamation-circle"></i> ${requestScope.errorMessage}
                </div>
            </c:if>
