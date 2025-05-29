<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Send Skill Swap Request" scope="request" />
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="${title}" />
</jsp:include>

<section class="send-request-section">
    <div class="container">
        <div class="section-header">
            <h1>Send Skill Swap Request</h1>
            <p>Propose a skill exchange with ${receiver.fullName}</p>
        </div>
        
        <div class="send-request-container">
            <div class="send-request-form-container">
                <form action="${pageContext.request.contextPath}/send-request" method="post" class="send-request-form">
                    <input type="hidden" name="receiverId" value="${receiver.userId}">
                    <input type="hidden" name="wantedSkillId" value="${receiverSkill.skillId}">
                    
                    <div class="form-group">
                        <label>You want to learn:</label>
                        <div class="skill-preview">
                            <div class="skill-preview-header">
                                <h3>${receiverSkill.skillName}</h3>
                                <span class="skill-category">${receiverSkill.category}</span>
                            </div>
                            <div class="skill-preview-body">
                                <p>Offered by ${receiver.fullName}</p>
                            </div>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="offeredSkillId">In exchange, you'll teach:</label>
                        <c:choose>
                            <c:when test="${empty currentUserOfferedSkills}">
                                <div class="alert alert-warning">
                                    <i class="fas fa-exclamation-triangle"></i>
                                    You don't have any skills to offer. Please add skills to your profile first.
                                </div>
                                <a href="${pageContext.request.contextPath}/profile?action=edit" class="btn btn-primary">
                                    <i class="fas fa-plus"></i> Add Skills to Your Profile
                                </a>
                            </c:when>
                            <c:otherwise>
                                <select id="offeredSkillId" name="offeredSkillId" required class="form-control">
                                    <option value="">Select a skill to offer</option>
                                    <c:forEach var="skill" items="${currentUserOfferedSkills}">
                                        <option value="${skill.skillId}">${skill.skillName} (${skill.proficiencyLevel})</option>
                                    </c:forEach>
                                </select>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    
                    <div class="form-group">
                        <label for="message">Message (Optional):</label>
                        <textarea id="message" name="message" rows="4" placeholder="Introduce yourself and explain why you want to learn this skill..."></textarea>
                        <small class="form-text">This message will be sent to ${receiver.firstName} along with your request.</small>
                    </div>
                    
                    <div class="form-actions">
                        <c:choose>
                            <c:when test="${empty currentUserOfferedSkills}">
                                <button type="button" class="btn btn-primary" disabled>Send Request</button>
                            </c:when>
                            <c:otherwise>
                                <button type="submit" class="btn btn-primary">Send Request</button>
                            </c:otherwise>
                        </c:choose>
                        <a href="${pageContext.request.contextPath}/skill/${receiverSkill.skillId}" class="btn btn-outline">Cancel</a>
                    </div>
                </form>
            </div>
            
            <div class="send-request-info">
                <div class="info-card">
                    <div class="info-icon">
                        <i class="fas fa-info-circle"></i>
                    </div>
                    <h3>About Skill Swapping</h3>
                    <p>Skill swapping is a great way to learn new skills while sharing your own expertise.</p>
                    <h4>How it works:</h4>
                    <ol>
                        <li>You send a request to ${receiver.firstName} offering one of your skills in exchange for learning ${receiverSkill.skillName}.</li>
                        <li>${receiver.firstName} will review your request and either accept or decline it.</li>
                        <li>If accepted, you can exchange contact information and arrange your skill swap sessions.</li>
                    </ol>
                    <p>Be clear about your experience level and what you hope to learn. A good message increases your chances of getting your request accepted.</p>
                </div>
                
                <div class="user-preview">
                    <h3>About ${receiver.fullName}</h3>
                    <div class="user-preview-header">
                        <div class="user-avatar">
                            <c:choose>
                                <c:when test="${not empty receiver.profileImage}">
                                    <img src="${receiver.profileImage}" alt="${receiver.fullName}">
                                </c:when>
                                <c:otherwise>
                                    <div class="avatar-placeholder">
                                        <i class="fas fa-user"></i>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="user-info">
                            <h4>${receiver.fullName}</h4>
                            <p class="username">@${receiver.username}</p>
                        </div>
                    </div>
                    <div class="user-preview-body">
                        <c:if test="${not empty receiver.bio}">
                            <p>${receiver.bio}</p>
                        </c:if>
                        <a href="${pageContext.request.contextPath}/profile/${receiver.userId}" class="btn btn-outline btn-sm">
                            <i class="fas fa-user"></i> View Full Profile
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
