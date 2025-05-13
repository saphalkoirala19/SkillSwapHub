<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Register" scope="request" />
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="${title}" />
</jsp:include>

<section class="auth-section">
    <div class="auth-container">
        <div class="auth-form-container">
            <h2>Create an Account</h2>
            <p>Join SkillSwapHub and start sharing your skills</p>
            
            <form action="${pageContext.request.contextPath}/register" method="post" class="auth-form">
                <div class="form-row">
                    <div class="form-group">
                        <label for="firstName">First Name</label>
                        <div class="input-with-icon">
                            <i class="fas fa-user"></i>
                            <input type="text" id="firstName" name="firstName" value="${firstName}" required>
                        </div>
                        <c:if test="${not empty firstNameError}">
                            <span class="error-message">${firstNameError}</span>
                        </c:if>
                    </div>
                    
                    <div class="form-group">
                        <label for="lastName">Last Name</label>
                        <div class="input-with-icon">
                            <i class="fas fa-user"></i>
                            <input type="text" id="lastName" name="lastName" value="${lastName}" required>
                        </div>
                        <c:if test="${not empty lastNameError}">
                            <span class="error-message">${lastNameError}</span>
                        </c:if>
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="username">Username</label>
                    <div class="input-with-icon">
                        <i class="fas fa-user-tag"></i>
                        <input type="text" id="username" name="username" value="${username}" required>
                    </div>
                    <c:if test="${not empty usernameError}">
                        <span class="error-message">${usernameError}</span>
                    </c:if>
                    <small class="form-text">Username must be 3-20 characters and contain only letters, numbers, and underscores.</small>
                </div>
                
                <div class="form-group">
                    <label for="email">Email</label>
                    <div class="input-with-icon">
                        <i class="fas fa-envelope"></i>
                        <input type="email" id="email" name="email" value="${email}" required>
                    </div>
                    <c:if test="${not empty emailError}">
                        <span class="error-message">${emailError}</span>
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
                    <small class="form-text">Password must be at least 8 characters and include at least one uppercase letter, one lowercase letter, and one number.</small>
                </div>
                
                <div class="form-group">
                    <label for="confirmPassword">Confirm Password</label>
                    <div class="input-with-icon">
                        <i class="fas fa-lock"></i>
                        <input type="password" id="confirmPassword" name="confirmPassword" required>
                    </div>
                    <c:if test="${not empty confirmPasswordError}">
                        <span class="error-message">${confirmPasswordError}</span>
                    </c:if>
                </div>
                
                <div class="form-group">
                    <label for="bio">Bio (Optional)</label>
                    <textarea id="bio" name="bio" rows="3">${bio}</textarea>
                    <small class="form-text">Tell us a bit about yourself.</small>
                </div>
                
                <div class="form-group terms">
                    <input type="checkbox" id="terms" name="terms" required>
                    <label for="terms">I agree to the <a href="${pageContext.request.contextPath}/terms" target="_blank">Terms of Service</a> and <a href="${pageContext.request.contextPath}/privacy" target="_blank">Privacy Policy</a></label>
                </div>
                
                <button type="submit" class="btn btn-primary btn-block">Create Account</button>
            </form>
            
            <div class="auth-divider">
                <span>OR</span>
            </div>
            
            <div class="social-login">
                <button class="btn btn-social btn-google">
                    <i class="fab fa-google"></i> Sign up with Google
                </button>
                <button class="btn btn-social btn-facebook">
                    <i class="fab fa-facebook-f"></i> Sign up with Facebook
                </button>
            </div>
            
            <div class="auth-footer">
                <p>Already have an account? <a href="${pageContext.request.contextPath}/login">Sign in</a></p>
            </div>
        </div>
        
        <div class="auth-image">
            <img src="${pageContext.request.contextPath}/images/register-image.svg" alt="Register">
            <div class="auth-image-text">
                <h3>Join Our Community</h3>
                <p>Connect with people who share your interests and passion for learning.</p>
            </div>
        </div>
    </div>
</section>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
