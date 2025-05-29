<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="title" value="Ratings & Feedback" scope="request" />
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="${title}" />
</jsp:include>

<section class="ratings-section">
    <div class="container">
        <div class="section-header">
            <h1>Ratings & Feedback</h1>
            <p>View and manage ratings and feedback</p>
        </div>
        
        <div class="user-rating-summary">
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
                    <h2>${user.fullName}</h2>
                    <p class="username">@${user.username}</p>
                    <c:if test="${not empty user.location}">
                        <p class="location"><i class="fas fa-map-marker-alt"></i> ${user.location}</p>
                    </c:if>
                </div>
            </div>
            
            <div class="rating-summary">
                <div class="average-rating">
                    <div class="rating-value">${averageRating > 0 ? String.format("%.1f", averageRating) : "N/A"}</div>
                    <div class="rating-stars">
                        <c:forEach begin="1" end="5" var="i">
                            <c:choose>
                                <c:when test="${i <= averageRating}">
                                    <i class="fas fa-star"></i>
                                </c:when>
                                <c:when test="${i <= averageRating + 0.5}">
                                    <i class="fas fa-star-half-alt"></i>
                                </c:when>
                                <c:otherwise>
                                    <i class="far fa-star"></i>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </div>
                    <div class="rating-count">${ratingsForUser.size()} ratings</div>
                </div>
            </div>
        </div>
        
        <div class="ratings-tabs">
            <ul class="tabs">
                <li class="tab active" data-tab="received">
                    <a href="#received">
                        <i class="fas fa-star"></i> Received Ratings
                    </a>
                </li>
                <li class="tab" data-tab="given">
                    <a href="#given">
                        <i class="fas fa-star-half-alt"></i> Given Ratings
                    </a>
                </li>
            </ul>
        </div>
        
        <div class="tab-content">
            <div id="received" class="tab-pane active">
                <c:choose>
                    <c:when test="${empty ratingsForUser}">
                        <div class="empty-state">
                            <div class="empty-icon">
                                <i class="fas fa-star"></i>
                            </div>
                            <h3>No ratings received yet</h3>
                            <p>When others rate your skills, they will appear here.</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="ratings-list">
                            <c:forEach var="rating" items="${ratingsForUser}">
                                <div class="rating-card">
                                    <div class="rating-header">
                                        <div class="rater-info">
                                            <div class="rater-avatar">
                                                <c:choose>
                                                    <c:when test="${not empty rating.rater.profileImage}">
                                                        <img src="${rating.rater.profileImage}" alt="${rating.rater.fullName}">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="avatar-placeholder">
                                                            <i class="fas fa-user"></i>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div class="rater-details">
                                                <h4>${rating.rater.fullName}</h4>
                                                <p class="rating-date">
                                                    <fmt:formatDate value="${rating.createdAt}" pattern="MMM d, yyyy" />
                                                </p>
                                            </div>
                                        </div>
                                        <div class="rating-value">
                                            <div class="rating-stars">
                                                <c:forEach begin="1" end="5" var="i">
                                                    <i class="${i <= rating.ratingValue ? 'fas' : 'far'} fa-star"></i>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="rating-body">
                                        <div class="rated-skill">
                                            <span class="skill-label">Skill:</span>
                                            <span class="skill-name">${rating.skill.skillName}</span>
                                            <span class="skill-category">${rating.skill.category}</span>
                                        </div>
                                        <c:if test="${not empty rating.feedback}">
                                            <div class="rating-feedback">
                                                <p>${rating.feedback}</p>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            
            <div id="given" class="tab-pane">
                <c:choose>
                    <c:when test="${empty ratingsByUser}">
                        <div class="empty-state">
                            <div class="empty-icon">
                                <i class="fas fa-star-half-alt"></i>
                            </div>
                            <h3>No ratings given yet</h3>
                            <p>When you rate others' skills, they will appear here.</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="ratings-list">
                            <c:forEach var="rating" items="${ratingsByUser}">
                                <div class="rating-card">
                                    <div class="rating-header">
                                        <div class="rater-info">
                                            <div class="rater-avatar">
                                                <c:choose>
                                                    <c:when test="${not empty rating.rated.profileImage}">
                                                        <img src="${rating.rated.profileImage}" alt="${rating.rated.fullName}">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="avatar-placeholder">
                                                            <i class="fas fa-user"></i>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div class="rater-details">
                                                <h4>${rating.rated.fullName}</h4>
                                                <p class="rating-date">
                                                    <fmt:formatDate value="${rating.createdAt}" pattern="MMM d, yyyy" />
                                                </p>
                                            </div>
                                        </div>
                                        <div class="rating-value">
                                            <div class="rating-stars">
                                                <c:forEach begin="1" end="5" var="i">
                                                    <i class="${i <= rating.ratingValue ? 'fas' : 'far'} fa-star"></i>
                                                </c:forEach>
                                            </div>
                                            <c:if test="${currentUser.userId eq rating.raterId}">
                                                <button class="btn btn-sm btn-outline edit-rating-btn" data-rating-id="${rating.ratingId}" data-rated-id="${rating.ratedId}" data-skill-id="${rating.skillId}" data-rating-value="${rating.ratingValue}" data-feedback="${rating.feedback}">
                                                    <i class="fas fa-edit"></i> Edit
                                                </button>
                                            </c:if>
                                        </div>
                                    </div>
                                    <div class="rating-body">
                                        <div class="rated-skill">
                                            <span class="skill-label">Skill:</span>
                                            <span class="skill-name">${rating.skill.skillName}</span>
                                            <span class="skill-category">${rating.skill.category}</span>
                                        </div>
                                        <c:if test="${not empty rating.feedback}">
                                            <div class="rating-feedback">
                                                <p>${rating.feedback}</p>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</section>

<!-- Rating Modal -->
<div class="modal" id="ratingModal">
    <div class="modal-content">
        <span class="close">&times;</span>
        <h2>Edit Rating</h2>
        <form action="${pageContext.request.contextPath}/ratings" method="post" id="ratingForm">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="ratingId" id="ratingId">
            <input type="hidden" name="ratedId" id="ratedId">
            <input type="hidden" name="skillId" id="skillId">
            
            <div class="form-group">
                <label for="ratingValue">Rating:</label>
                <div class="star-rating">
                    <input type="radio" name="ratingValue" value="5" id="star5"><label for="star5"></label>
                    <input type="radio" name="ratingValue" value="4" id="star4"><label for="star4"></label>
                    <input type="radio" name="ratingValue" value="3" id="star3"><label for="star3"></label>
                    <input type="radio" name="ratingValue" value="2" id="star2"><label for="star2"></label>
                    <input type="radio" name="ratingValue" value="1" id="star1"><label for="star1"></label>
                </div>
            </div>
            
            <div class="form-group">
                <label for="feedback">Feedback (optional):</label>
                <textarea name="feedback" id="feedback" rows="4"></textarea>
            </div>
            
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Update Rating</button>
            </div>
        </form>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Tab switching
        const tabs = document.querySelectorAll('.tab');
        tabs.forEach(tab => {
            tab.addEventListener('click', function() {
                const tabId = this.getAttribute('data-tab');
                
                // Remove active class from all tabs and panes
                document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
                document.querySelectorAll('.tab-pane').forEach(p => p.classList.remove('active'));
                
                // Add active class to current tab and pane
                this.classList.add('active');
                document.getElementById(tabId).classList.add('active');
            });
        });
        
        // Modal functionality
        const modal = document.getElementById('ratingModal');
        const editButtons = document.querySelectorAll('.edit-rating-btn');
        const closeBtn = document.querySelector('.close');
        
        editButtons.forEach(button => {
            button.addEventListener('click', function() {
                const ratingId = this.getAttribute('data-rating-id');
                const ratedId = this.getAttribute('data-rated-id');
                const skillId = this.getAttribute('data-skill-id');
                const ratingValue = this.getAttribute('data-rating-value');
                const feedback = this.getAttribute('data-feedback');
                
                document.getElementById('ratingId').value = ratingId;
                document.getElementById('ratedId').value = ratedId;
                document.getElementById('skillId').value = skillId;
                document.getElementById('feedback').value = feedback || '';
                
                // Set rating value
                document.querySelector(`input[name="ratingValue"][value="${ratingValue}"]`).checked = true;
                
                modal.style.display = 'block';
            });
        });
        
        closeBtn.addEventListener('click', function() {
            modal.style.display = 'none';
        });
        
        window.addEventListener('click', function(event) {
            if (event.target == modal) {
                modal.style.display = 'none';
            }
        });
    });
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
