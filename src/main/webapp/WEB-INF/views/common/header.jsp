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
                    <c:choose>
                        <c:when test="${empty sessionScope.user}">
                            <li><a href="${pageContext.request.contextPath}/login"><i class="fas fa-sign-in-alt"></i> Login</a></li>
                            <li><a href="${pageContext.request.contextPath}/register" class="btn btn-primary"><i class="fas fa-user-plus"></i> Register</a></li>
                        </c:when>
                        <c:otherwise>
                            <li><a href="${pageContext.request.contextPath}/profile"><i class="fas fa-user-circle"></i> Profile</a></li>
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
