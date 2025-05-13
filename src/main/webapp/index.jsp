<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Home" scope="request" />
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="${title}" />
</jsp:include>

<section class="hero">
    <div class="hero-content">
        <h1>Share Skills, Grow Together</h1>
        <p>Connect with people who want to learn what you know and teach what you want to learn.</p>
        <div class="hero-buttons">
            <c:choose>
                <c:when test="${empty sessionScope.user}">
                    <a href="${pageContext.request.contextPath}/register" class="btn btn-primary btn-large">Get Started</a>
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-outline btn-large">Sign In</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/profile" class="btn btn-primary btn-large">My Profile</a>
                    <a href="${pageContext.request.contextPath}/skills" class="btn btn-outline btn-large">Browse Skills</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <div class="hero-image">
        <img src="${pageContext.request.contextPath}/images/hero-image.svg" alt="People sharing skills">
    </div>
</section>

<section class="features">
    <div class="section-header">
        <h2>How It Works</h2>
        <p>SkillSwapHub makes it easy to connect with others and exchange knowledge</p>
    </div>
    <div class="feature-cards">
        <div class="feature-card">
            <div class="feature-icon">
                <i class="fas fa-user-plus"></i>
            </div>
            <h3>Create Your Profile</h3>
            <p>Sign up and list the skills you can offer and the ones you want to learn.</p>
        </div>
        <div class="feature-card">
            <div class="feature-icon">
                <i class="fas fa-search"></i>
            </div>
            <h3>Find Matches</h3>
            <p>Discover people who want to learn what you know and can teach what you want to learn.</p>
        </div>
        <div class="feature-card">
            <div class="feature-icon">
                <i class="fas fa-handshake"></i>
            </div>
            <h3>Connect & Learn</h3>
            <p>Reach out, schedule sessions, and start exchanging knowledge.</p>
        </div>
    </div>
</section>

<section class="categories">
    <div class="section-header">
        <h2>Popular Skill Categories</h2>
        <p>Explore the most popular skill categories on our platform</p>
    </div>
    <div class="category-cards">
        <div class="category-card">
            <div class="category-icon">
                <i class="fas fa-laptop-code"></i>
            </div>
            <h3>Technology</h3>
            <p>Programming, Web Development, Data Science, and more</p>
        </div>
        <div class="category-card">
            <div class="category-icon">
                <i class="fas fa-palette"></i>
            </div>
            <h3>Arts & Design</h3>
            <p>Graphic Design, Photography, Drawing, Painting</p>
        </div>
        <div class="category-card">
            <div class="category-icon">
                <i class="fas fa-language"></i>
            </div>
            <h3>Languages</h3>
            <p>Spanish, French, German, Japanese, and more</p>
        </div>
        <div class="category-card">
            <div class="category-icon">
                <i class="fas fa-music"></i>
            </div>
            <h3>Music</h3>
            <p>Guitar, Piano, Singing, Music Production</p>
        </div>
    </div>
</section>

<section class="testimonials">
    <div class="section-header">
        <h2>Success Stories</h2>
        <p>Hear from our community members who have successfully exchanged skills</p>
    </div>
    <div class="testimonial-slider">
        <div class="testimonial">
            <div class="testimonial-content">
                <p>"I taught web development to Sarah while she taught me graphic design. Now I can create beautiful websites from scratch!"</p>
            </div>
            <div class="testimonial-author">
                <img src="${pageContext.request.contextPath}/images/testimonial-1.jpg" alt="Michael">
                <div>
                    <h4>Michael Johnson</h4>
                    <p>Web Developer</p>
                </div>
            </div>
        </div>
        <div class="testimonial">
            <div class="testimonial-content">
                <p>"I've always wanted to learn Spanish, and through SkillSwapHub, I found someone who wanted to learn English. It's been a fantastic exchange!"</p>
            </div>
            <div class="testimonial-author">
                <img src="${pageContext.request.contextPath}/images/testimonial-2.jpg" alt="Emily">
                <div>
                    <h4>Emily Chen</h4>
                    <p>Language Enthusiast</p>
                </div>
            </div>
        </div>
    </div>
</section>

<section class="cta">
    <div class="cta-content">
        <h2>Ready to Start Sharing Skills?</h2>
        <p>Join our community today and begin your skill-sharing journey.</p>
        <c:choose>
            <c:when test="${empty sessionScope.user}">
                <a href="${pageContext.request.contextPath}/register" class="btn btn-primary btn-large">Sign Up Now</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/profile" class="btn btn-primary btn-large">Update Your Profile</a>
            </c:otherwise>
        </c:choose>
    </div>
</section>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
