<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="title" value="Messages" scope="request" />
<jsp:include page="/WEB-INF/views/common/header.jsp">
    <jsp:param name="title" value="${title}" />
</jsp:include>

<section class="messages-section">
    <div class="container">
        <div class="section-header">
            <h1>Messages</h1>
            <p>Communicate with other users</p>
        </div>
        
        <div class="messages-container">
            <div class="conversations-sidebar">
                <div class="conversations-header">
                    <h3>Conversations</h3>
                </div>
                
                <div class="conversations-list">
                    <c:choose>
                        <c:when test="${empty conversations}">
                            <div class="empty-conversations">
                                <p>No conversations yet</p>
                                <p class="empty-hint">Start a conversation by visiting a user's profile</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="entry" items="${conversations}">
                                <c:set var="userId" value="${entry.key}" />
                                <c:set var="message" value="${entry.value}" />
                                <c:set var="user" value="${conversationUsers[userId]}" />
                                <c:set var="unreadCount" value="${unreadCounts[userId]}" />
                                
                                <a href="${pageContext.request.contextPath}/messages?userId=${userId}" 
                                   class="conversation-item ${activeConversation eq userId ? 'active' : ''}">
                                    <div class="conversation-avatar">
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
                                    <div class="conversation-info">
                                        <div class="conversation-header">
                                            <h4 class="conversation-name">${user.fullName}</h4>
                                            <span class="conversation-time">
                                                <fmt:formatDate value="${message.createdAt}" pattern="MMM d" />
                                            </span>
                                        </div>
                                        <div class="conversation-preview">
                                            <p class="preview-text">
                                                <c:choose>
                                                    <c:when test="${message.senderId eq sessionScope.userId}">
                                                        <span class="you-prefix">You: </span>
                                                    </c:when>
                                                </c:choose>
                                                ${message.content.length() > 30 ? message.content.substring(0, 30).concat('...') : message.content}
                                            </p>
                                            <c:if test="${unreadCount > 0}">
                                                <span class="unread-badge">${unreadCount}</span>
                                            </c:if>
                                        </div>
                                    </div>
                                </a>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            
            <div class="conversation-main">
                <c:choose>
                    <c:when test="${empty activeConversation}">
                        <div class="empty-conversation">
                            <div class="empty-icon">
                                <i class="fas fa-comments"></i>
                            </div>
                            <h3>Select a conversation</h3>
                            <p>Choose a conversation from the sidebar or start a new one</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="conversation-header">
                            <div class="conversation-partner">
                                <div class="partner-avatar">
                                    <c:choose>
                                        <c:when test="${not empty partner.profileImage}">
                                            <img src="${partner.profileImage}" alt="${partner.fullName}">
                                        </c:when>
                                        <c:otherwise>
                                            <div class="avatar-placeholder">
                                                <i class="fas fa-user"></i>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="partner-info">
                                    <h3 class="partner-name">${partner.fullName}</h3>
                                    <p class="partner-username">@${partner.username}</p>
                                </div>
                            </div>
                            <div class="conversation-actions">
                                <a href="${pageContext.request.contextPath}/profile/${partner.userId}" class="btn btn-outline btn-sm">
                                    <i class="fas fa-user"></i> View Profile
                                </a>
                            </div>
                        </div>
                        
                        <div class="conversation-messages" id="messageContainer">
                            <c:forEach var="message" items="${conversation}">
                                <div class="message ${message.senderId eq sessionScope.userId ? 'sent' : 'received'}">
                                    <div class="message-content">
                                        <p>${message.content}</p>
                                        <span class="message-time">
                                            <fmt:formatDate value="${message.createdAt}" pattern="h:mm a" />
                                        </span>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                        
                        <div class="message-form-container">
                            <form action="${pageContext.request.contextPath}/messages" method="post" class="message-form" id="messageForm">
                                <input type="hidden" name="receiverId" value="${partner.userId}">
                                <div class="form-group">
                                    <textarea name="content" id="messageContent" placeholder="Type your message..." required></textarea>
                                </div>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-paper-plane"></i> Send
                                </button>
                            </form>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</section>

<c:if test="${not empty activeConversation}">
    <script>
        // Scroll to bottom of message container
        document.addEventListener('DOMContentLoaded', function() {
            const messageContainer = document.getElementById('messageContainer');
            if (messageContainer) {
                messageContainer.scrollTop = messageContainer.scrollHeight;
            }
            
            // Focus on message input
            const messageContent = document.getElementById('messageContent');
            if (messageContent) {
                messageContent.focus();
            }
            
            // Auto-resize textarea
            messageContent.addEventListener('input', function() {
                this.style.height = 'auto';
                this.style.height = (this.scrollHeight) + 'px';
            });
            
            // Optional: Auto-refresh messages every 10 seconds
            <c:if test="${not empty activeConversation}">
                setInterval(function() {
                    fetch('${pageContext.request.contextPath}/messages?userId=${activeConversation}&ajax=true')
                        .then(response => response.text())
                        .then(html => {
                            const parser = new DOMParser();
                            const doc = parser.parseFromString(html, 'text/html');
                            const newMessages = doc.querySelector('.conversation-messages');
                            
                            if (newMessages) {
                                const currentMessages = document.querySelector('.conversation-messages');
                                if (currentMessages.innerHTML !== newMessages.innerHTML) {
                                    currentMessages.innerHTML = newMessages.innerHTML;
                                    currentMessages.scrollTop = currentMessages.scrollHeight;
                                }
                            }
                        })
                        .catch(error => console.error('Error refreshing messages:', error));
                }, 10000);
            </c:if>
        });
    </script>
</c:if>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
