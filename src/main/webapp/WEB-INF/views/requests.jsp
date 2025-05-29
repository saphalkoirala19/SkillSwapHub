<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="title" value="Skill Swap Requests" scope="request" />
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="${title}" />
</jsp:include>

<section class="requests-section">
    <div class="container">
        <div class="section-header">
            <h1>Skill Swap Requests</h1>
            <p>Manage your skill swap requests</p>
        </div>
        
        <div class="requests-tabs">
            <ul class="tabs">
                <li class="tab ${activeTab eq 'received' ? 'active' : ''}">
                    <a href="${pageContext.request.contextPath}/requests?tab=received">
                        <i class="fas fa-inbox"></i> Received Requests
                    </a>
                </li>
                <li class="tab ${activeTab eq 'sent' ? 'active' : ''}">
                    <a href="${pageContext.request.contextPath}/requests?tab=sent">
                        <i class="fas fa-paper-plane"></i> Sent Requests
                    </a>
                </li>
            </ul>
        </div>
        
        <div class="tab-content">
            <c:choose>
                <c:when test="${empty requests}">
                    <div class="empty-state">
                        <div class="empty-icon">
                            <c:choose>
                                <c:when test="${activeTab eq 'received'}">
                                    <i class="fas fa-inbox"></i>
                                </c:when>
                                <c:otherwise>
                                    <i class="fas fa-paper-plane"></i>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <h3>No requests found</h3>
                        <p>
                            <c:choose>
                                <c:when test="${activeTab eq 'received'}">
                                    You don't have any received skill swap requests.
                                </c:when>
                                <c:otherwise>
                                    You haven't sent any skill swap requests yet.
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <a href="${pageContext.request.contextPath}/skills" class="btn btn-primary">
                            <i class="fas fa-search"></i> Browse Skills
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="requests-list">
                        <c:forEach var="request" items="${requests}">
                            <div class="request-card ${request.status}">
                                <div class="request-header">
                                    <div class="request-status">
                                        <span class="status-badge ${request.status.toLowerCase()}">
                                            ${request.status}
                                        </span>
                                        <span class="request-date">
                                            <fmt:formatDate value="${request.createdAt}" pattern="MMM d, yyyy" />
                                        </span>
                                    </div>
                                    <c:if test="${activeTab eq 'received' && request.status eq 'Pending'}">
                                        <div class="request-actions">
                                            <form action="${pageContext.request.contextPath}/manage-request" method="post" class="inline-form">
                                                <input type="hidden" name="requestId" value="${request.requestId}">
                                                <input type="hidden" name="action" value="accept">
                                                <button type="submit" class="btn btn-success btn-sm">
                                                    <i class="fas fa-check"></i> Accept
                                                </button>
                                            </form>
                                            <form action="${pageContext.request.contextPath}/manage-request" method="post" class="inline-form">
                                                <input type="hidden" name="requestId" value="${request.requestId}">
                                                <input type="hidden" name="action" value="decline">
                                                <button type="submit" class="btn btn-danger btn-sm">
                                                    <i class="fas fa-times"></i> Decline
                                                </button>
                                            </form>
                                        </div>
                                    </c:if>
                                </div>
                                
                                <div class="request-body">
                                    <div class="request-users">
                                        <div class="request-user">
                                            <div class="user-avatar">
                                                <c:choose>
                                                    <c:when test="${activeTab eq 'received'}">
                                                        <c:choose>
                                                            <c:when test="${not empty request.sender.profileImage}">
                                                                <img src="${request.sender.profileImage}" alt="${request.sender.fullName}">
                                                            </c:when>
                                                            <c:otherwise>
                                                                <div class="avatar-placeholder">
                                                                    <i class="fas fa-user"></i>
                                                                </div>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:choose>
                                                            <c:when test="${not empty request.receiver.profileImage}">
                                                                <img src="${request.receiver.profileImage}" alt="${request.receiver.fullName}">
                                                            </c:when>
                                                            <c:otherwise>
                                                                <div class="avatar-placeholder">
                                                                    <i class="fas fa-user"></i>
                                                                </div>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div class="user-info">
                                                <h4>
                                                    <c:choose>
                                                        <c:when test="${activeTab eq 'received'}">
                                                            ${request.sender.fullName}
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${request.receiver.fullName}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </h4>
                                                <p class="username">
                                                    <c:choose>
                                                        <c:when test="${activeTab eq 'received'}">
                                                            @${request.sender.username}
                                                        </c:when>
                                                        <c:otherwise>
                                                            @${request.receiver.username}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </p>
                                            </div>
                                        </div>
                                        
                                        <div class="request-direction">
                                            <c:choose>
                                                <c:when test="${activeTab eq 'received'}">
                                                    <i class="fas fa-long-arrow-alt-right"></i>
                                                </c:when>
                                                <c:otherwise>
                                                    <i class="fas fa-long-arrow-alt-right"></i>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        
                                        <div class="request-user">
                                            <div class="user-avatar">
                                                <c:choose>
                                                    <c:when test="${activeTab eq 'received'}">
                                                        <c:choose>
                                                            <c:when test="${not empty request.receiver.profileImage}">
                                                                <img src="${request.receiver.profileImage}" alt="${request.receiver.fullName}">
                                                            </c:when>
                                                            <c:otherwise>
                                                                <div class="avatar-placeholder">
                                                                    <i class="fas fa-user"></i>
                                                                </div>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:choose>
                                                            <c:when test="${not empty request.sender.profileImage}">
                                                                <img src="${request.sender.profileImage}" alt="${request.sender.fullName}">
                                                            </c:when>
                                                            <c:otherwise>
                                                                <div class="avatar-placeholder">
                                                                    <i class="fas fa-user"></i>
                                                                </div>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div class="user-info">
                                                <h4>
                                                    <c:choose>
                                                        <c:when test="${activeTab eq 'received'}">
                                                            ${request.receiver.fullName}
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${request.sender.fullName}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </h4>
                                                <p class="username">
                                                    <c:choose>
                                                        <c:when test="${activeTab eq 'received'}">
                                                            @${request.receiver.username}
                                                        </c:when>
                                                        <c:otherwise>
                                                            @${request.sender.username}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </p>
                                            </div>
                                        </div>
                                    </div>
                                    
                                    <div class="request-skills">
                                        <div class="skill-exchange">
                                            <div class="skill-card">
                                                <h4>${request.offeredSkill.skillName}</h4>
                                                <span class="skill-category">${request.offeredSkill.category}</span>
                                                <p class="skill-owner">
                                                    <c:choose>
                                                        <c:when test="${activeTab eq 'received'}">
                                                            <i class="fas fa-share-alt"></i> Offered by ${request.sender.firstName}
                                                        </c:when>
                                                        <c:otherwise>
                                                            <i class="fas fa-share-alt"></i> Your offered skill
                                                        </c:otherwise>
                                                    </c:choose>
                                                </p>
                                            </div>
                                            
                                            <div class="exchange-icon">
                                                <i class="fas fa-exchange-alt"></i>
                                            </div>
                                            
                                            <div class="skill-card">
                                                <h4>${request.wantedSkill.skillName}</h4>
                                                <span class="skill-category">${request.wantedSkill.category}</span>
                                                <p class="skill-owner">
                                                    <c:choose>
                                                        <c:when test="${activeTab eq 'received'}">
                                                            <i class="fas fa-search"></i> Your offered skill
                                                        </c:when>
                                                        <c:otherwise>
                                                            <i class="fas fa-search"></i> Offered by ${request.receiver.firstName}
                                                        </c:otherwise>
                                                    </c:choose>
                                                </p>
                                            </div>
                                        </div>
                                    </div>
                                    
                                    <c:if test="${not empty request.message}">
                                        <div class="request-message">
                                            <h4>Message:</h4>
                                            <p>${request.message}</p>
                                        </div>
                                    </c:if>
                                </div>
                                
                                <div class="request-footer">
                                    <c:choose>
                                        <c:when test="${activeTab eq 'received'}">
                                            <a href="${pageContext.request.contextPath}/profile/${request.sender.userId}" class="btn btn-outline btn-sm">
                                                <i class="fas fa-user"></i> View Sender's Profile
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${pageContext.request.contextPath}/profile/${request.receiver.userId}" class="btn btn-outline btn-sm">
                                                <i class="fas fa-user"></i> View Receiver's Profile
                                            </a>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</section>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
