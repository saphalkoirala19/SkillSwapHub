package com.skillswaphub.model;

import java.sql.Timestamp;

/**
 * Model class representing a skill swap request between users
 */
public class SkillRequest {
    private int requestId;
    private int senderId;
    private int receiverId;
    private int offeredSkillId;
    private int wantedSkillId;
    private String status;
    private String message;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Additional fields for display purposes
    private User sender;
    private User receiver;
    private Skill offeredSkill;
    private Skill wantedSkill;
    
    // Default constructor
    public SkillRequest() {
    }
    
    // Constructor with basic fields
    public SkillRequest(int senderId, int receiverId, int offeredSkillId, int wantedSkillId, String message) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.offeredSkillId = offeredSkillId;
        this.wantedSkillId = wantedSkillId;
        this.message = message;
        this.status = "Pending";
    }
    
    // Full constructor
    public SkillRequest(int requestId, int senderId, int receiverId, int offeredSkillId, int wantedSkillId, 
                        String status, String message, Timestamp createdAt, Timestamp updatedAt) {
        this.requestId = requestId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.offeredSkillId = offeredSkillId;
        this.wantedSkillId = wantedSkillId;
        this.status = status;
        this.message = message;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public int getRequestId() {
        return requestId;
    }
    
    public void setRequestId(int requestId) {
        this.requestId = requestId;
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
    
    public int getOfferedSkillId() {
        return offeredSkillId;
    }
    
    public void setOfferedSkillId(int offeredSkillId) {
        this.offeredSkillId = offeredSkillId;
    }
    
    public int getWantedSkillId() {
        return wantedSkillId;
    }
    
    public void setWantedSkillId(int wantedSkillId) {
        this.wantedSkillId = wantedSkillId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
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
    
    public Skill getOfferedSkill() {
        return offeredSkill;
    }
    
    public void setOfferedSkill(Skill offeredSkill) {
        this.offeredSkill = offeredSkill;
    }
    
    public Skill getWantedSkill() {
        return wantedSkill;
    }
    
    public void setWantedSkill(Skill wantedSkill) {
        this.wantedSkill = wantedSkill;
    }
    
    /**
     * Check if the request is pending
     * @return true if pending, false otherwise
     */
    public boolean isPending() {
        return "Pending".equals(status);
    }
    
    /**
     * Check if the request is accepted
     * @return true if accepted, false otherwise
     */
    public boolean isAccepted() {
        return "Accepted".equals(status);
    }
    
    /**
     * Check if the request is declined
     * @return true if declined, false otherwise
     */
    public boolean isDeclined() {
        return "Declined".equals(status);
    }
    
    /**
     * Check if the request is completed
     * @return true if completed, false otherwise
     */
    public boolean isCompleted() {
        return "Completed".equals(status);
    }
    
    @Override
    public String toString() {
        return "SkillRequest{" +
                "requestId=" + requestId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", offeredSkillId=" + offeredSkillId +
                ", wantedSkillId=" + wantedSkillId +
                ", status='" + status + '\'' +
                '}';
    }
}
