<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Edit Profile" scope="request" />
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="${title}" />
</jsp:include>

<section class="profile-edit-section">
    <div class="container">
        <div class="section-header">
            <h1>Edit Your Profile</h1>
            <p>Update your personal information and profile settings</p>
        </div>
        
        <div class="profile-edit-content">
            <div class="profile-edit-sidebar">
                <div class="profile-preview">
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
                    <h3>${user.fullName}</h3>
                    <p class="username">@${user.username}</p>
                </div>
                
                <ul class="profile-edit-nav">
                    <li class="active">
                        <a href="#personal-info">
                            <i class="fas fa-user"></i> Personal Information
                        </a>
                    </li>
                    <li>
                        <a href="#account-settings">
                            <i class="fas fa-cog"></i> Account Settings
                        </a>
                    </li>
                    <li>
                        <a href="#password">
                            <i class="fas fa-lock"></i> Change Password
                        </a>
                    </li>
                    <li>
                        <a href="#notifications">
                            <i class="fas fa-bell"></i> Notifications
                        </a>
                    </li>
                </ul>
                
                <div class="sidebar-actions">
                    <a href="${pageContext.request.contextPath}/profile" class="btn btn-outline btn-block">
                        <i class="fas fa-arrow-left"></i> Back to Profile
                    </a>
                </div>
            </div>
            
            <div class="profile-edit-main">
                <!-- Personal Information Form -->
                <div id="personal-info" class="edit-section active">
                    <div class="edit-section-header">
                        <h2>Personal Information</h2>
                        <p>Update your personal details</p>
                    </div>
                    
                    <form action="${pageContext.request.contextPath}/profile" method="post" class="edit-form">
                        <input type="hidden" name="action" value="updateProfile">
                        
                        <div class="form-row">
                            <div class="form-group">
                                <label for="firstName">First Name</label>
                                <input type="text" id="firstName" name="firstName" value="${not empty firstName ? firstName : user.firstName}" required>
                                <c:if test="${not empty firstNameError}">
                                    <span class="error-message">${firstNameError}</span>
                                </c:if>
                            </div>
                            
                            <div class="form-group">
                                <label for="lastName">Last Name</label>
                                <input type="text" id="lastName" name="lastName" value="${not empty lastName ? lastName : user.lastName}" required>
                                <c:if test="${not empty lastNameError}">
                                    <span class="error-message">${lastNameError}</span>
                                </c:if>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label for="bio">Bio</label>
                            <textarea id="bio" name="bio" rows="4">${not empty bio ? bio : user.bio}</textarea>
                            <small class="form-text">Tell others about yourself, your interests, and your experience.</small>
                        </div>
                        
                        <div class="form-group">
                            <label>Profile Picture</label>
                            <div class="profile-picture-upload">
                                <div class="current-picture">
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
                                <div class="upload-actions">
                                    <button type="button" class="btn btn-outline">
                                        <i class="fas fa-upload"></i> Upload New Picture
                                    </button>
                                    <button type="button" class="btn btn-text btn-danger">
                                        <i class="fas fa-trash-alt"></i> Remove
                                    </button>
                                    <input type="file" id="profileImage" name="profileImage" accept="image/*" style="display: none;">
                                </div>
                            </div>
                            <small class="form-text">Recommended size: 300x300 pixels. Max file size: 2MB.</small>
                        </div>
                        
                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary">Save Changes</button>
                            <a href="${pageContext.request.contextPath}/profile" class="btn btn-outline">Cancel</a>
                        </div>
                    </form>
                </div>
                
                <!-- Account Settings Form (Placeholder) -->
                <div id="account-settings" class="edit-section">
                    <div class="edit-section-header">
                        <h2>Account Settings</h2>
                        <p>Manage your account preferences</p>
                    </div>
                    
                    <div class="coming-soon">
                        <i class="fas fa-tools"></i>
                        <h3>Coming Soon</h3>
                        <p>This feature is currently under development.</p>
                    </div>
                </div>
                
                <!-- Change Password Form (Placeholder) -->
                <div id="password" class="edit-section">
                    <div class="edit-section-header">
                        <h2>Change Password</h2>
                        <p>Update your password to keep your account secure</p>
                    </div>
                    
                    <div class="coming-soon">
                        <i class="fas fa-tools"></i>
                        <h3>Coming Soon</h3>
                        <p>This feature is currently under development.</p>
                    </div>
                </div>
                
                <!-- Notifications Settings (Placeholder) -->
                <div id="notifications" class="edit-section">
                    <div class="edit-section-header">
                        <h2>Notification Settings</h2>
                        <p>Manage how you receive notifications</p>
                    </div>
                    
                    <div class="coming-soon">
                        <i class="fas fa-tools"></i>
                        <h3>Coming Soon</h3>
                        <p>This feature is currently under development.</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
