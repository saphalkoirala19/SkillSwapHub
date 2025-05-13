<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Login" scope="request" />
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="${title}" />
</jsp:include>

<section class="auth-section">
    <div class="auth-container">
        <div class="auth-form-container">
            <h2>Welcome Back</h2>
            <p>Sign in to your SkillSwapHub account</p>
            
            <!-- Display logout message -->
            <c:if test="${param.logout == 'true'}">
                <div class="alert alert-success">
                    <i class="fas fa-check-circle"></i> You have been successfully logged out.
                </div>
            </c:if>
            
            <!-- Display unauthorized message -->
            <c:if test="${param.unauthorized == 'true'}">
                <div class="alert alert-error">
                    <i class="fas fa-exclamation-circle"></i> Please log in to access that page.
                </div>
            </c:if>
            
            <form action="${pageContext.request.contextPath}/login" method="post" class="auth-form">
                <div class="form-group">
                    <label for="usernameOrEmail">Username or Email</label>
                    <div class="input-with-icon">
                        <i class="fas fa-user"></i>
                        <input type="text" id="usernameOrEmail" name="usernameOrEmail" value="${usernameOrEmail}" required>
                    </div>
                    <c:if test="${not empty usernameOrEmailError}">
                        <span class="error-message">${usernameOrEmailError}</span>
                    </c:if>
                </div>
                
                <div class="form-group">
                    <label for="password">Password</label>
                    <div class="input-with-icon">
                        <i class="fas fa-lock"></i>
                        <input type="password" id="password" name="password" required>
                        <button type="button" class="toggle-password" tabindex="-1">
                            <i class="fas fa-eye"></i>
                        </button>
                    </div>
                    <c:if test="${not empty passwordError}">
                        <span class="error-message">${passwordError}</span>
                    </c:if>
                </div>
                
                <div class="form-group remember-forgot">
                    <div class="remember-me">
                        <input type="checkbox" id="remember" name="remember">
                        <label for="remember">Remember me</label>
                    </div>
                    <a href="${pageContext.request.contextPath}/forgot-password" class="forgot-password">Forgot password?</a>
                </div>
                
                <button type="submit" class="btn btn-primary btn-block">Sign In</button>
            </form>
            
            <div class="auth-divider">
                <span>OR</span>
            </div>
            
            <div class="social-login">
                <button class="btn btn-social btn-google">
                    <i class="fab fa-google"></i> Sign in with Google
                </button>
                <button class="btn btn-social btn-facebook">
                    <i class="fab fa-facebook-f"></i> Sign in with Facebook
                </button>
            </div>
            
            <div class="auth-footer">
                <p>Don't have an account? <a href="${pageContext.request.contextPath}/register">Sign up</a></p>
            </div>
        </div>
        
        <div class="auth-image">
            <img src="${pageContext.request.contextPath}/images/login-image.svg" alt="Login">
            <div class="auth-image-text">
                <h3>Exchange Skills, Expand Knowledge</h3>
                <p>Connect with people who want to learn what you know and teach what you want to learn.</p>
            </div>
        </div>
    </div>
</section>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
