package com.skillswaphub.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Model class representing a skill swap session between users
 */
public class Session {
    private int sessionId;
    private int requestId;
    private int teacherId;
    private int studentId;
    private int skillId;
    private LocalDateTime sessionDate;
    private int duration;
    private String location;
    private String notes;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Additional fields for display purposes
    private User teacher;
    private User student;
    private Skill skill;
    private SkillRequest request;
    
    // Default constructor
    public Session() {
    }
    
    // Constructor with basic fields
    public Session(int requestId, int teacherId, int studentId, int skillId, LocalDateTime sessionDate, int duration, String location, String notes) {
        this.requestId = requestId;
        this.teacherId = teacherId;
        this.studentId = studentId;
        this.skillId = skillId;
        this.sessionDate = sessionDate;
        this.duration = duration;
        this.location = location;
        this.notes = notes;
        this.status = "Scheduled";
    }
    
    // Full constructor
    public Session(int sessionId, int requestId, int teacherId, int studentId, int skillId, LocalDateTime sessionDate, 
                  int duration, String location, String notes, String status, Timestamp createdAt, Timestamp updatedAt) {
        this.sessionId = sessionId;
        this.requestId = requestId;
        this.teacherId = teacherId;
        this.studentId = studentId;
        this.skillId = skillId;
        this.sessionDate = sessionDate;
        this.duration = duration;
        this.location = location;
        this.notes = notes;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public int getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
    
    public int getRequestId() {
        return requestId;
    }
    
    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }
    
    public int getTeacherId() {
        return teacherId;
    }
    
    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }
    
    public int getStudentId() {
        return studentId;
    }
    
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    
    public int getSkillId() {
        return skillId;
    }
    
    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }
    
    public LocalDateTime getSessionDate() {
        return sessionDate;
    }
    
    public void setSessionDate(LocalDateTime sessionDate) {
        this.sessionDate = sessionDate;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
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
    
    public User getTeacher() {
        return teacher;
    }
    
    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }
    
    public User getStudent() {
        return student;
    }
    
    public void setStudent(User student) {
        this.student = student;
    }
    
    public Skill getSkill() {
        return skill;
    }
    
    public void setSkill(Skill skill) {
        this.skill = skill;
    }
    
    public SkillRequest getRequest() {
        return request;
    }
    
    public void setRequest(SkillRequest request) {
        this.request = request;
    }
    
    /**
     * Check if the session is scheduled
     * @return true if scheduled, false otherwise
     */
    public boolean isScheduled() {
        return "Scheduled".equals(status);
    }
    
    /**
     * Check if the session is completed
     * @return true if completed, false otherwise
     */
    public boolean isCompleted() {
        return "Completed".equals(status);
    }
    
    /**
     * Check if the session is cancelled
     * @return true if cancelled, false otherwise
     */
    public boolean isCancelled() {
        return "Cancelled".equals(status);
    }
    
    /**
     * Check if the session is in the past
     * @return true if in the past, false otherwise
     */
    public boolean isPast() {
        return sessionDate.isBefore(LocalDateTime.now());
    }
    
    /**
     * Check if the session is in the future
     * @return true if in the future, false otherwise
     */
    public boolean isFuture() {
        return sessionDate.isAfter(LocalDateTime.now());
    }
    
    @Override
    public String toString() {
        return "Session{" +
                "sessionId=" + sessionId +
                ", requestId=" + requestId +
                ", teacherId=" + teacherId +
                ", studentId=" + studentId +
                ", skillId=" + skillId +
                ", sessionDate=" + sessionDate +
                ", status='" + status + '\'' +
                '}';
    }
}
