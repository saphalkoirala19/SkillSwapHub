<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Profile" scope="request" />
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="${title}" />
</jsp:include>

<section class="profile-section">
    <div class="profile-header">
        <div class="profile-cover"></div>
        <div class="profile-info">
            <div class="profile-avatar">
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
            <div class="profile-details">
                <h1>${user.fullName}</h1>
                <p class="username">@${user.username}</p>
                <p class="bio">${user.bio}</p>
                <div class="profile-actions">
                    <c:choose>
                        <c:when test="${sessionScope.userId eq user.userId}">
                            <a href="${pageContext.request.contextPath}/profile?action=edit" class="btn btn-outline">
                                <i class="fas fa-edit"></i> Edit Profile
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/send-message?userId=${user.userId}" class="btn btn-primary">
                                <i class="fas fa-comments"></i> Send Message
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <div class="profile-content">
        <div class="profile-tabs">
            <ul class="tabs">
                <li class="tab active" data-tab="offered-skills">
                    <i class="fas fa-share-alt"></i> Skills I Offer
                </li>
                <li class="tab" data-tab="wanted-skills">
                    <i class="fas fa-search"></i> Skills I Want
                </li>
            </ul>
        </div>

        <div class="tab-content">
            <!-- Offered Skills Tab -->
            <div id="offered-skills" class="tab-pane active">
                <div class="skills-header">
                    <h2>Skills I Can Offer</h2>
                    <button class="btn btn-primary" id="add-offered-skill-btn">
                        <i class="fas fa-plus"></i> Add Skill
                    </button>
                </div>

                <c:choose>
                    <c:when test="${empty user.offeredSkills}">
                        <div class="empty-state">
                            <div class="empty-icon">
                                <i class="fas fa-share-alt"></i>
                            </div>
                            <h3>No skills added yet</h3>
                            <p>Add skills that you can teach or share with others.</p>
                            <button class="btn btn-primary" id="add-first-offered-skill-btn">
                                <i class="fas fa-plus"></i> Add Your First Skill
                            </button>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="skills-grid">
                            <c:forEach var="skill" items="${user.offeredSkills}">
                                <div class="skill-card">
                                    <div class="skill-header">
                                        <h3>${skill.skillName}</h3>
                                        <span class="skill-category">${skill.category}</span>
                                    </div>
                                    <div class="skill-level">
                                        <span class="level-label">Proficiency:</span>
                                        <span class="level-value ${skill.proficiencyLevel.toLowerCase()}">${skill.proficiencyLevel}</span>
                                    </div>
                                    <p class="skill-description">${skill.description}</p>
                                    <div class="skill-actions">
                                        <form action="${pageContext.request.contextPath}/profile" method="post">
                                            <input type="hidden" name="action" value="removeOfferedSkill">
                                            <input type="hidden" name="skillId" value="${skill.skillId}">
                                            <button type="submit" class="btn btn-text btn-danger">
                                                <i class="fas fa-trash-alt"></i> Remove
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>

                <!-- Add Offered Skill Modal -->
                <div id="add-offered-skill-modal" class="modal">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h3>Add Skill You Can Offer</h3>
                            <button class="close-modal">&times;</button>
                        </div>
                        <div class="modal-body">
                            <form action="${pageContext.request.contextPath}/profile" method="post">
                                <input type="hidden" name="action" value="addOfferedSkill">

                                <div class="form-group">
                                    <label for="offeredSkillId">Skill</label>
                                    <select id="offeredSkillId" name="skillId" required>
                                        <option value="">Select a skill</option>
                                        <c:forEach var="category" items="${categories}">
                                            <optgroup label="${category}">
                                                <c:forEach var="skill" items="${allSkills}">
                                                    <c:if test="${skill.category eq category}">
                                                        <option value="${skill.skillId}">${skill.skillName}</option>
                                                    </c:if>
                                                </c:forEach>
                                            </optgroup>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label for="offeredProficiencyLevel">Proficiency Level</label>
                                    <select id="offeredProficiencyLevel" name="proficiencyLevel" required>
                                        <option value="">Select your proficiency level</option>
                                        <option value="Beginner">Beginner</option>
                                        <option value="Intermediate">Intermediate</option>
                                        <option value="Advanced">Advanced</option>
                                        <option value="Expert">Expert</option>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label for="offeredDescription">Description</label>
                                    <textarea id="offeredDescription" name="description" rows="3" placeholder="Describe your experience with this skill"></textarea>
                                </div>

                                <div class="modal-actions">
                                    <button type="button" class="btn btn-outline cancel-modal">Cancel</button>
                                    <button type="submit" class="btn btn-primary">Add Skill</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Wanted Skills Tab -->
            <div id="wanted-skills" class="tab-pane">
                <div class="skills-header">
                    <h2>Skills I Want to Learn</h2>
                    <button class="btn btn-primary" id="add-wanted-skill-btn">
                        <i class="fas fa-plus"></i> Add Skill
                    </button>
                </div>

                <c:choose>
                    <c:when test="${empty user.wantedSkills}">
                        <div class="empty-state">
                            <div class="empty-icon">
                                <i class="fas fa-search"></i>
                            </div>
                            <h3>No skills added yet</h3>
                            <p>Add skills that you want to learn from others.</p>
                            <button class="btn btn-primary" id="add-first-wanted-skill-btn">
                                <i class="fas fa-plus"></i> Add Your First Skill
                            </button>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="skills-grid">
                            <c:forEach var="skill" items="${user.wantedSkills}">
                                <div class="skill-card">
                                    <div class="skill-header">
                                        <h3>${skill.skillName}</h3>
                                        <span class="skill-category">${skill.category}</span>
                                    </div>
                                    <div class="skill-level">
                                        <span class="level-label">Current Level:</span>
                                        <span class="level-value ${skill.currentLevel.toLowerCase()}">${skill.currentLevel}</span>
                                    </div>
                                    <p class="skill-description">${skill.description}</p>
                                    <div class="skill-actions">
                                        <form action="${pageContext.request.contextPath}/profile" method="post">
                                            <input type="hidden" name="action" value="removeWantedSkill">
                                            <input type="hidden" name="skillId" value="${skill.skillId}">
                                            <button type="submit" class="btn btn-text btn-danger">
                                                <i class="fas fa-trash-alt"></i> Remove
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>

                <!-- Add Wanted Skill Modal -->
                <div id="add-wanted-skill-modal" class="modal">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h3>Add Skill You Want to Learn</h3>
                            <button class="close-modal">&times;</button>
                        </div>
                        <div class="modal-body">
                            <form action="${pageContext.request.contextPath}/profile" method="post">
                                <input type="hidden" name="action" value="addWantedSkill">

                                <div class="form-group">
                                    <label for="wantedSkillId">Skill</label>
                                    <select id="wantedSkillId" name="skillId" required>
                                        <option value="">Select a skill</option>
                                        <c:forEach var="category" items="${categories}">
                                            <optgroup label="${category}">
                                                <c:forEach var="skill" items="${allSkills}">
                                                    <c:if test="${skill.category eq category}">
                                                        <option value="${skill.skillId}">${skill.skillName}</option>
                                                    </c:if>
                                                </c:forEach>
                                            </optgroup>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label for="wantedCurrentLevel">Current Level</label>
                                    <select id="wantedCurrentLevel" name="currentLevel" required>
                                        <option value="">Select your current level</option>
                                        <option value="None">None</option>
                                        <option value="Beginner">Beginner</option>
                                        <option value="Intermediate">Intermediate</option>
                                        <option value="Advanced">Advanced</option>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label for="wantedDescription">Description</label>
                                    <textarea id="wantedDescription" name="description" rows="3" placeholder="Describe what you want to learn about this skill"></textarea>
                                </div>

                                <div class="modal-actions">
                                    <button type="button" class="btn btn-outline cancel-modal">Cancel</button>
                                    <button type="submit" class="btn btn-primary">Add Skill</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
