<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="title" value="Leaderboard" scope="request" />
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="${title}" />
</jsp:include>

<section class="leaderboard-section">
    <div class="container">
        <div class="section-header">
            <h1>Leaderboard</h1>
            <p>Discover our top community members</p>
        </div>
        
        <div class="leaderboard-container">
            <div class="leaderboard-card">
                <div class="leaderboard-header">
                    <h2><i class="fas fa-star"></i> Top Rated Users</h2>
                    <p>Users with the highest ratings</p>
                </div>
                
                <div class="leaderboard-body">
                    <c:choose>
                        <c:when test="${empty topRatedUsers}">
                            <div class="empty-state">
                                <p>No rated users yet</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="leaderboard-list">
                                <c:forEach var="user" items="${topRatedUsers}" varStatus="status">
                                    <div class="leaderboard-item">
                                        <div class="rank">${status.index + 1}</div>
                                        <div class="user-info">
                                            <div class="user-avatar">
                                                <c:choose>
                                                    <c:when test="${not empty user.profileImage}">
                                                        <img src="${user.profileImage}" alt="${user.fullName}">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="avatar-placeholder">
                                                            <i class="fas fa-user"></i>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div class="user-details">
                                                <h4>${user.fullName}</h4>
                                                <p class="username">@${user.username}</p>
                                            </div>
                                        </div>
                                        <div class="score">
                                            <div class="rating-value">${String.format("%.1f", user.averageRating)}</div>
                                            <div class="rating-stars">
                                                <c:forEach begin="1" end="5" var="i">
                                                    <c:choose>
                                                        <c:when test="${i <= user.averageRating}">
                                                            <i class="fas fa-star"></i>
                                                        </c:when>
                                                        <c:when test="${i <= user.averageRating + 0.5}">
                                                            <i class="fas fa-star-half-alt"></i>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <i class="far fa-star"></i>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:forEach>
                                            </div>
                                        </div>
                                        <div class="actions">
                                            <a href="${pageContext.request.contextPath}/profile/${user.userId}" class="btn btn-sm btn-outline">
                                                <i class="fas fa-user"></i> Profile
                                            </a>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            
            <div class="leaderboard-card">
                <div class="leaderboard-header">
                    <h2><i class="fas fa-graduation-cap"></i> Top Skill Providers</h2>
                    <p>Users offering the most skills</p>
                </div>
                
                <div class="leaderboard-body">
                    <c:choose>
                        <c:when test="${empty topSkillProviders}">
                            <div class="empty-state">
                                <p>No skill providers yet</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="leaderboard-list">
                                <c:forEach var="user" items="${topSkillProviders}" varStatus="status">
                                    <div class="leaderboard-item">
                                        <div class="rank">${status.index + 1}</div>
                                        <div class="user-info">
                                            <div class="user-avatar">
                                                <c:choose>
                                                    <c:when test="${not empty user.profileImage}">
                                                        <img src="${user.profileImage}" alt="${user.fullName}">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="avatar-placeholder">
                                                            <i class="fas fa-user"></i>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div class="user-details">
                                                <h4>${user.fullName}</h4>
                                                <p class="username">@${user.username}</p>
                                            </div>
                                        </div>
                                        <div class="score">
                                            <div class="skill-count">${user.offeredSkills.size()}</div>
                                            <div class="skill-label">skills</div>
                                        </div>
                                        <div class="actions">
                                            <a href="${pageContext.request.contextPath}/profile/${user.userId}" class="btn btn-sm btn-outline">
                                                <i class="fas fa-user"></i> Profile
                                            </a>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            
            <div class="leaderboard-card">
                <div class="leaderboard-header">
                    <h2><i class="fas fa-medal"></i> Most Active Users</h2>
                    <p>Users with the most completed sessions</p>
                </div>
                
                <div class="leaderboard-body">
                    <c:choose>
                        <c:when test="${empty mostActiveUsers}">
                            <div class="empty-state">
                                <p>No active users yet</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="leaderboard-list">
                                <c:forEach var="user" items="${mostActiveUsers}" varStatus="status">
                                    <div class="leaderboard-item">
                                        <div class="rank">${status.index + 1}</div>
                                        <div class="user-info">
                                            <div class="user-avatar">
                                                <c:choose>
                                                    <c:when test="${not empty user.profileImage}">
                                                        <img src="${user.profileImage}" alt="${user.fullName}">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="avatar-placeholder">
                                                            <i class="fas fa-user"></i>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div class="user-details">
                                                <h4>${user.fullName}</h4>
                                                <p class="username">@${user.username}</p>
                                            </div>
                                        </div>
                                        <div class="score">
                                            <div class="session-count">
                                                <i class="fas fa-calendar-check"></i>
                                            </div>
                                        </div>
                                        <div class="actions">
                                            <a href="${pageContext.request.contextPath}/profile/${user.userId}" class="btn btn-sm btn-outline">
                                                <i class="fas fa-user"></i> Profile
                                            </a>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
