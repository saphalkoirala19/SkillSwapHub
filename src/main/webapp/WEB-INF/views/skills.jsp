<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Browse Skills" scope="request" />
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="${title}" />
</jsp:include>

<section class="skills-hero">
    <div class="container">
        <div class="skills-hero-content">
            <h1>Discover Skills</h1>
            <p>Browse, search, and filter skills from our community</p>

            <form action="${pageContext.request.contextPath}/skills" method="get" class="search-form">
                <div class="search-input-container">
                    <input type="text" name="search" placeholder="Search skills..." value="${searchTerm}" class="search-input">
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-search"></i> Search
                    </button>
                </div>
            </form>
        </div>
    </div>
</section>

<section class="skills-section">
    <div class="container">
        <div class="skills-container">
            <div class="skills-sidebar">
                <div class="sidebar-section">
                    <h3>Categories</h3>
                    <ul class="category-list">
                        <li class="${empty selectedCategory ? 'active' : ''}">
                            <a href="${pageContext.request.contextPath}/skills">All Categories</a>
                        </li>
                        <c:forEach var="category" items="${categories}">
                            <li class="${category eq selectedCategory ? 'active' : ''}">
                                <a href="${pageContext.request.contextPath}/skills?category=${category}">${category}</a>
                            </li>
                        </c:forEach>
                    </ul>
                </div>

                <div class="sidebar-section">
                    <h3>Popular Skills</h3>
                    <ul class="popular-skills-list">
                        <c:forEach var="skill" items="${popularSkills}">
                            <li>
                                <a href="${pageContext.request.contextPath}/skill/${skill.skillId}">
                                    <span class="skill-name">${skill.skillName}</span>
                                    <span class="skill-count">${skill.offerCount + skill.wantCount} users</span>
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                </div>

                <div class="sidebar-section">
                    <h3>Add Your Skill</h3>
                    <p>Can't find the skill you're looking for?</p>
                    <a href="${pageContext.request.contextPath}/add-skill" class="btn btn-primary btn-block">
                        <i class="fas fa-plus"></i> Add New Skill
                    </a>
                </div>
            </div>

            <div class="skills-main">
                <div class="skills-header">
                    <div class="skills-count">
                        <c:choose>
                            <c:when test="${not empty searchTerm}">
                                <h2>Search Results for "${searchTerm}"</h2>
                                <p>${totalSkills} skills found</p>
                            </c:when>
                            <c:when test="${not empty selectedCategory}">
                                <h2>${selectedCategory}</h2>
                                <p>${totalSkills} skills found</p>
                            </c:when>
                            <c:otherwise>
                                <h2>All Skills</h2>
                                <p>${totalSkills} skills available</p>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="skills-sort">
                        <label for="sortBy">Sort by:</label>
                        <select id="sortBy" class="sort-select">
                            <option value="name" ${sortBy eq 'name' ? 'selected' : ''}>Name (A-Z)</option>
                            <option value="nameDesc" ${sortBy eq 'nameDesc' ? 'selected' : ''}>Name (Z-A)</option>
                            <option value="category" ${sortBy eq 'category' ? 'selected' : ''}>Category (A-Z)</option>
                            <option value="categoryDesc" ${sortBy eq 'categoryDesc' ? 'selected' : ''}>Category (Z-A)</option>
                            <option value="popularity" ${sortBy eq 'popularity' ? 'selected' : ''}>Popularity</option>
                            <option value="offers" ${sortBy eq 'offers' ? 'selected' : ''}>Most Offered</option>
                            <option value="wants" ${sortBy eq 'wants' ? 'selected' : ''}>Most Wanted</option>
                        </select>
                    </div>
                </div>

                <c:choose>
                    <c:when test="${empty skills}">
                        <div class="empty-state">
                            <div class="empty-icon">
                                <i class="fas fa-search"></i>
                            </div>
                            <h3>No skills found</h3>
                            <p>Try a different search term or category, or add a new skill.</p>
                            <a href="${pageContext.request.contextPath}/add-skill" class="btn btn-primary">
                                <i class="fas fa-plus"></i> Add New Skill
                            </a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="skills-grid">
                            <c:forEach var="skill" items="${skills}">
                                <div class="skill-card">
                                    <div class="skill-card-header">
                                        <span class="skill-category">${skill.category}</span>
                                        <c:if test="${skill.popular}">
                                            <span class="skill-popular">
                                                <i class="fas fa-fire"></i> Popular
                                            </span>
                                        </c:if>
                                    </div>
                                    <div class="skill-card-body">
                                        <h3 class="skill-name">${skill.skillName}</h3>
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
                                    <div class="skill-card-footer">
                                        <a href="${pageContext.request.contextPath}/skill/${skill.skillId}" class="btn btn-outline btn-sm">
                                            View Details
                                        </a>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>

                        <!-- Pagination -->
                        <c:if test="${totalPages > 1 && empty searchTerm && empty selectedCategory}">
                            <div class="pagination">
                                <c:if test="${currentPage > 1}">
                                    <a href="${pageContext.request.contextPath}/skills?page=${currentPage - 1}${not empty sortBy ? '&sortBy='.concat(sortBy) : ''}" class="pagination-item">
                                        <i class="fas fa-chevron-left"></i> Previous
                                    </a>
                                </c:if>

                                <c:forEach begin="1" end="${totalPages}" var="i">
                                    <c:choose>
                                        <c:when test="${i == currentPage}">
                                            <span class="pagination-item active">${i}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${pageContext.request.contextPath}/skills?page=${i}${not empty sortBy ? '&sortBy='.concat(sortBy) : ''}" class="pagination-item">${i}</a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>

                                <c:if test="${currentPage < totalPages}">
                                    <a href="${pageContext.request.contextPath}/skills?page=${currentPage + 1}${not empty sortBy ? '&sortBy='.concat(sortBy) : ''}" class="pagination-item">
                                        Next <i class="fas fa-chevron-right"></i>
                                    </a>
                                </c:if>
                            </div>
                        </c:if>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</section>

<script>
    // Handle sort change
    document.getElementById('sortBy').addEventListener('change', function() {
        const sortBy = this.value;

        // Get current URL and parameters
        const currentUrl = window.location.href;
        const url = new URL(currentUrl);

        // Update or add sortBy parameter
        url.searchParams.set('sortBy', sortBy);

        // Navigate to the new URL
        window.location.href = url.toString();
    });
</script>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
