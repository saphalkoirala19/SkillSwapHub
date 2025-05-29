package com.skillswaphub.model;

import java.sql.Timestamp;

/**
 * Model class representing a message between users
 */
public class Message {
    private int messageId;
    private int senderId;
    private int receiverId;
    private String content;
    private boolean isRead;
    private Timestamp createdAt;
    
    // Additional fields for display purposes
    private User sender;
    private User receiver;
    
    // Default constructor
    public Message() {
    }
    
    // Constructor with basic fields
    public Message(int senderId, int receiverId, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.isRead = false;
    }
    
    // Full constructor
    public Message(int messageId, int senderId, int receiverId, String content, boolean isRead, Timestamp createdAt) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getMessageId() {
        return messageId;
    }
    
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
    
    public int getSenderId() {
        return senderId;
    }
    
    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }
    
    public int getReceiverId() {
        return receiverId;
    }
    
    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public boolean isRead() {
        return isRead;
    }
    
    public void setRead(boolean read) {
        isRead = read;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public User getSender() {
        return sender;
    }
    
    public void setSender(User sender) {
        this.sender = sender;
    }
    
    public User getReceiver() {
        return receiver;
    }
    
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
    
    /**
     * Check if the current user is the sender of this message
     * @param userId User ID to check
     * @return true if the user is the sender, false otherwise
     */
    public boolean isSentByUser(int userId) {
        return senderId == userId;
    }
    
    /**
     * Check if the current user is the receiver of this message
     * @param userId User ID to check
     * @return true if the user is the receiver, false otherwise
     */
    public boolean isReceivedByUser(int userId) {
        return receiverId == userId;
    }
    
    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", content='" + content + '\'' +
                ", isRead=" + isRead +
                ", createdAt=" + createdAt +
                '}';
    }
}
