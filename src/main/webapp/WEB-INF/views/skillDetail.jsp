<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="${skill.skillName}" scope="request" />
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="${title}" />
</jsp:include>

<section class="skill-detail-section">
    <div class="container">
        <div class="skill-detail-container">
            <div class="skill-detail-main">
                <div class="skill-detail-header">
                    <div class="skill-detail-info">
                        <div class="skill-breadcrumb">
                            <a href="${pageContext.request.contextPath}/skills">Skills</a>
                            <i class="fas fa-chevron-right"></i>
                            <a href="${pageContext.request.contextPath}/skills?category=${skill.category}">${skill.category}</a>
                        </div>
                        <h1>${skill.skillName}</h1>
                        <div class="skill-meta">
                            <span class="skill-category">${skill.category}</span>
                            <div class="skill-stats">
                                <div class="skill-stat">
                                    <i class="fas fa-share-alt"></i>
                                    <span>${skill.offerCount} offering</span>
                                </div>
                                <div class="skill-stat">
                                    <i class="fas fa-search"></i>
                                    <span>${skill.wantCount} wanting</span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <c:if test="${not empty skill.imageUrl}">
                        <div class="skill-image">
                            <img src="${skill.imageUrl}" alt="${skill.skillName}">
                        </div>
                    </c:if>
                </div>
                
                <div class="skill-actions">
                    <c:choose>
                        <c:when test="${empty sessionScope.user}">
                            <div class="login-prompt">
                                <p>Sign in to add this skill to your profile</p>
                                <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">
                                    <i class="fas fa-sign-in-alt"></i> Sign In
                                </a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="skill-action-buttons">
                                <c:choose>
                                    <c:when test="${userOffersSkill}">
                                        <form action="${pageContext.request.contextPath}/skill/${skill.skillId}" method="post">
                                            <input type="hidden" name="skillId" value="${skill.skillId}">
                                            <input type="hidden" name="action" value="removeOffer">
                                            <button type="submit" class="btn btn-outline">
                                                <i class="fas fa-minus-circle"></i> Remove from My Offered Skills
                                            </button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <button type="button" class="btn btn-primary" id="offerSkillBtn">
                                            <i class="fas fa-plus-circle"></i> I Can Offer This Skill
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                                
                                <c:choose>
                                    <c:when test="${userWantsSkill}">
                                        <form action="${pageContext.request.contextPath}/skill/${skill.skillId}" method="post">
                                            <input type="hidden" name="skillId" value="${skill.skillId}">
                                            <input type="hidden" name="action" value="removeWant">
                                            <button type="submit" class="btn btn-outline">
                                                <i class="fas fa-minus-circle"></i> Remove from My Wanted Skills
                                            </button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <button type="button" class="btn btn-secondary" id="wantSkillBtn">
                                            <i class="fas fa-plus-circle"></i> I Want to Learn This Skill
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                
                <div class="skill-tabs">
                    <ul class="tabs">
                        <li class="tab active" data-tab="offering-users">
                            <i class="fas fa-share-alt"></i> People Offering (${skill.offerCount})
                        </li>
                        <li class="tab" data-tab="wanting-users">
                            <i class="fas fa-search"></i> People Wanting (${skill.wantCount})
                        </li>
                    </ul>
                    
                    <div class="tab-content">
                        <div id="offering-users" class="tab-pane active">
                            <c:choose>
                                <c:when test="${empty offeringUsers}">
                                    <div class="empty-state">
                                        <p>No users are currently offering this skill.</p>
                                        <c:if test="${not userOffersSkill and not empty sessionScope.user}">
                                            <p>Be the first to offer this skill!</p>
                                            <button type="button" class="btn btn-primary" id="offerSkillBtnEmpty">
                                                <i class="fas fa-plus-circle"></i> I Can Offer This Skill
                                            </button>
                                        </c:if>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="users-grid">
                                        <c:forEach var="user" items="${offeringUsers}">
                                            <div class="user-card">
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
                                                <div class="user-info">
                                                    <h3>${user.fullName}</h3>
                                                    <p class="username">@${user.username}</p>
                                                </div>
                                                <div class="user-actions">
                                                    <a href="${pageContext.request.contextPath}/profile/${user.userId}" class="btn btn-outline btn-sm">
                                                        View Profile
                                                    </a>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        
                        <div id="wanting-users" class="tab-pane">
                            <c:choose>
                                <c:when test="${empty wantingUsers}">
                                    <div class="empty-state">
                                        <p>No users are currently wanting to learn this skill.</p>
                                        <c:if test="${not userWantsSkill and not empty sessionScope.user}">
                                            <p>Be the first to want to learn this skill!</p>
                                            <button type="button" class="btn btn-secondary" id="wantSkillBtnEmpty">
                                                <i class="fas fa-plus-circle"></i> I Want to Learn This Skill
                                            </button>
                                        </c:if>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="users-grid">
                                        <c:forEach var="user" items="${wantingUsers}">
                                            <div class="user-card">
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
                                                <div class="user-info">
                                                    <h3>${user.fullName}</h3>
                                                    <p class="username">@${user.username}</p>
                                                </div>
                                                <div class="user-actions">
                                                    <a href="${pageContext.request.contextPath}/profile/${user.userId}" class="btn btn-outline btn-sm">
                                                        View Profile
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
            
            <div class="skill-detail-sidebar">
                <div class="sidebar-section">
                    <h3>Related Skills</h3>
                    <c:choose>
                        <c:when test="${empty relatedSkills}">
                            <p>No related skills found.</p>
                        </c:when>
                        <c:otherwise>
                            <ul class="related-skills-list">
                                <c:forEach var="relatedSkill" items="${relatedSkills}">
                                    <li>
                                        <a href="${pageContext.request.contextPath}/skill/${relatedSkill.skillId}">
                                            <span class="skill-name">${relatedSkill.skillName}</span>
                                        </a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </c:otherwise>
                    </c:choose>
                </div>
                
                <div class="sidebar-section">
                    <h3>Add Your Skill</h3>
                    <p>Can't find the skill you're looking for?</p>
                    <a href="${pageContext.request.contextPath}/add-skill" class="btn btn-primary btn-block">
                        <i class="fas fa-plus"></i> Add New Skill
                    </a>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Offer Skill Modal -->
<div id="offer-skill-modal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Offer Skill: ${skill.skillName}</h3>
            <button class="close-modal">&times;</button>
        </div>
        <div class="modal-body">
            <form action="${pageContext.request.contextPath}/skill/${skill.skillId}" method="post">
                <input type="hidden" name="skillId" value="${skill.skillId}">
                <input type="hidden" name="action" value="addOffer">
                
                <div class="form-group">
                    <label for="proficiencyLevel">Your Proficiency Level</label>
                    <select id="proficiencyLevel" name="proficiencyLevel" required>
                        <option value="">Select your proficiency level</option>
                        <option value="Beginner">Beginner</option>
                        <option value="Intermediate">Intermediate</option>
                        <option value="Advanced">Advanced</option>
                        <option value="Expert">Expert</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="offerDescription">Description (Optional)</label>
                    <textarea id="offerDescription" name="description" rows="3" placeholder="Describe your experience with this skill"></textarea>
                </div>
                
                <div class="modal-actions">
                    <button type="button" class="btn btn-outline cancel-modal">Cancel</button>
                    <button type="submit" class="btn btn-primary">Add to My Offered Skills</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Want Skill Modal -->
<div id="want-skill-modal" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Want to Learn: ${skill.skillName}</h3>
            <button class="close-modal">&times;</button>
        </div>
        <div class="modal-body">
            <form action="${pageContext.request.contextPath}/skill/${skill.skillId}" method="post">
                <input type="hidden" name="skillId" value="${skill.skillId}">
                <input type="hidden" name="action" value="addWant">
                
                <div class="form-group">
                    <label for="currentLevel">Your Current Level</label>
                    <select id="currentLevel" name="currentLevel" required>
                        <option value="">Select your current level</option>
                        <option value="None">None</option>
                        <option value="Beginner">Beginner</option>
                        <option value="Intermediate">Intermediate</option>
                        <option value="Advanced">Advanced</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="wantDescription">Description (Optional)</label>
                    <textarea id="wantDescription" name="description" rows="3" placeholder="Describe what you want to learn about this skill"></textarea>
                </div>
                
                <div class="modal-actions">
                    <button type="button" class="btn btn-outline cancel-modal">Cancel</button>
                    <button type="submit" class="btn btn-secondary">Add to My Wanted Skills</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    // Initialize tabs
    document.querySelectorAll('.tab').forEach(tab => {
        tab.addEventListener('click', function() {
            const tabId = this.getAttribute('data-tab');
            
            // Remove active class from all tabs and tab panes
            document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
            document.querySelectorAll('.tab-pane').forEach(p => p.classList.remove('active'));
            
            // Add active class to current tab and tab pane
            this.classList.add('active');
            document.getElementById(tabId).classList.add('active');
        });
    });
    
    // Initialize offer skill modal
    const offerSkillModal = document.getElementById('offer-skill-modal');
    const offerSkillBtn = document.getElementById('offerSkillBtn');
    const offerSkillBtnEmpty = document.getElementById('offerSkillBtnEmpty');
    
    if (offerSkillBtn) {
        offerSkillBtn.addEventListener('click', function() {
            offerSkillModal.classList.add('active');
            document.body.style.overflow = 'hidden';
        });
    }
    
    if (offerSkillBtnEmpty) {
        offerSkillBtnEmpty.addEventListener('click', function() {
            offerSkillModal.classList.add('active');
            document.body.style.overflow = 'hidden';
        });
    }
    
    // Initialize want skill modal
    const wantSkillModal = document.getElementById('want-skill-modal');
    const wantSkillBtn = document.getElementById('wantSkillBtn');
    const wantSkillBtnEmpty = document.getElementById('wantSkillBtnEmpty');
    
    if (wantSkillBtn) {
        wantSkillBtn.addEventListener('click', function() {
            wantSkillModal.classList.add('active');
            document.body.style.overflow = 'hidden';
        });
    }
    
    if (wantSkillBtnEmpty) {
        wantSkillBtnEmpty.addEventListener('click', function() {
            wantSkillModal.classList.add('active');
            document.body.style.overflow = 'hidden';
        });
    }
    
    // Close modals
    document.querySelectorAll('.close-modal, .cancel-modal').forEach(btn => {
        btn.addEventListener('click', function() {
            offerSkillModal.classList.remove('active');
            wantSkillModal.classList.remove('active');
            document.body.style.overflow = '';
        });
    });
    
    // Close modal when clicking outside
    window.addEventListener('click', function(event) {
        if (event.target === offerSkillModal) {
            offerSkillModal.classList.remove('active');
            document.body.style.overflow = '';
        }
        if (event.target === wantSkillModal) {
            wantSkillModal.classList.remove('active');
            document.body.style.overflow = '';
        }
    });
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
