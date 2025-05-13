<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Add New Skill" scope="request" />
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="${title}" />
</jsp:include>

<section class="add-skill-section">
    <div class="container">
        <div class="section-header">
            <h1>Add New Skill</h1>
            <p>Contribute to our community by adding a new skill</p>
        </div>
        
        <div class="add-skill-container">
            <div class="add-skill-form-container">
                <form action="${pageContext.request.contextPath}/add-skill" method="post" class="add-skill-form">
                    <div class="form-group">
                        <label for="skillName">Skill Name</label>
                        <input type="text" id="skillName" name="skillName" value="${skillName}" required>
                        <c:if test="${not empty skillNameError}">
                            <span class="error-message">${skillNameError}</span>
                        </c:if>
                        <small class="form-text">Enter a clear and concise name for the skill.</small>
                    </div>
                    
                    <div class="form-group">
                        <label for="category">Category</label>
                        <select id="category" name="category" required>
                            <option value="">Select a category</option>
                            <c:forEach var="category" items="${categories}">
                                <option value="${category}" ${category eq selectedCategory ? 'selected' : ''}>${category}</option>
                            </c:forEach>
                            <option value="new" ${selectedCategory eq 'new' ? 'selected' : ''}>Add New Category</option>
                        </select>
                        <c:if test="${not empty categoryError}">
                            <span class="error-message">${categoryError}</span>
                        </c:if>
                    </div>
                    
                    <div id="newCategoryGroup" class="form-group" style="display: none;">
                        <label for="newCategory">New Category Name</label>
                        <input type="text" id="newCategory" name="newCategory" value="${newCategory}">
                        <small class="form-text">Enter a new category name.</small>
                    </div>
                    
                    <div class="form-group">
                        <label for="imageUrl">Image URL (Optional)</label>
                        <input type="url" id="imageUrl" name="imageUrl" value="${imageUrl}" placeholder="https://example.com/image.jpg">
                        <c:if test="${not empty imageUrlError}">
                            <span class="error-message">${imageUrlError}</span>
                        </c:if>
                        <small class="form-text">Enter a URL for an image that represents this skill.</small>
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">Add Skill</button>
                        <a href="${pageContext.request.contextPath}/skills" class="btn btn-outline">Cancel</a>
                    </div>
                </form>
            </div>
            
            <div class="add-skill-info">
                <div class="info-card">
                    <div class="info-icon">
                        <i class="fas fa-info-circle"></i>
                    </div>
                    <h3>Adding a New Skill</h3>
                    <p>Before adding a new skill, please check if it already exists in our database by searching on the skills page.</p>
                    <p>When adding a new skill:</p>
                    <ul>
                        <li>Use clear, concise names</li>
                        <li>Choose the most appropriate category</li>
                        <li>Only create a new category if necessary</li>
                    </ul>
                    <p>After adding a skill, you can add it to your profile as either a skill you offer or a skill you want to learn.</p>
                    <a href="${pageContext.request.contextPath}/skills" class="btn btn-outline btn-block">
                        <i class="fas fa-search"></i> Browse Existing Skills
                    </a>
                </div>
            </div>
        </div>
    </div>
</section>

<script>
    // Show/hide new category input based on selection
    const categorySelect = document.getElementById('category');
    const newCategoryGroup = document.getElementById('newCategoryGroup');
    
    // Check initial state
    if (categorySelect.value === 'new') {
        newCategoryGroup.style.display = 'block';
    }
    
    categorySelect.addEventListener('change', function() {
        if (this.value === 'new') {
            newCategoryGroup.style.display = 'block';
            document.getElementById('newCategory').setAttribute('required', 'required');
        } else {
            newCategoryGroup.style.display = 'none';
            document.getElementById('newCategory').removeAttribute('required');
        }
    });
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
