package com.skillswaphub.model;

import java.sql.Timestamp;

/**
 * Model class representing a rating given by one user to another for a specific skill
 */
public class Rating {
    private int ratingId;
    private int raterId;
    private int ratedId;
    private int skillId;
    private int ratingValue;
    private String feedback;
    private Timestamp createdAt;
    
    // Additional fields for display purposes
    private User rater;
    private User rated;
    private Skill skill;
    
    // Default constructor
    public Rating() {
    }
    
    // Constructor with basic fields
    public Rating(int raterId, int ratedId, int skillId, int ratingValue, String feedback) {
        this.raterId = raterId;
        this.ratedId = ratedId;
        this.skillId = skillId;
        this.ratingValue = ratingValue;
        this.feedback = feedback;
    }
    
    // Full constructor
    public Rating(int ratingId, int raterId, int ratedId, int skillId, int ratingValue, String feedback, Timestamp createdAt) {
        this.ratingId = ratingId;
        this.raterId = raterId;
        this.ratedId = ratedId;
        this.skillId = skillId;
        this.ratingValue = ratingValue;
        this.feedback = feedback;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getRatingId() {
        return ratingId;
    }
    
    public void setRatingId(int ratingId) {
        this.ratingId = ratingId;
    }
    
    public int getRaterId() {
        return raterId;
    }
    
    public void setRaterId(int raterId) {
        this.raterId = raterId;
    }
    
    public int getRatedId() {
        return ratedId;
    }
    
    public void setRatedId(int ratedId) {
        this.ratedId = ratedId;
    }
    
    public int getSkillId() {
        return skillId;
    }
    
    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }
    
    public int getRatingValue() {
        return ratingValue;
    }
    
    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }
    
    public String getFeedback() {
        return feedback;
    }
    
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public User getRater() {
        return rater;
    }
    
    public void setRater(User rater) {
        this.rater = rater;
    }
    
    public User getRated() {
        return rated;
    }
    
    public void setRated(User rated) {
        this.rated = rated;
    }
    
    public Skill getSkill() {
        return skill;
    }
    
    public void setSkill(Skill skill) {
        this.skill = skill;
    }
    
    @Override
    public String toString() {
        return "Rating{" +
                "ratingId=" + ratingId +
                ", raterId=" + raterId +
                ", ratedId=" + ratedId +
                ", skillId=" + skillId +
                ", ratingValue=" + ratingValue +
                ", createdAt=" + createdAt +
                '}';
    }
}
