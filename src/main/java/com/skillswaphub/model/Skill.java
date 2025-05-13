package com.skillswaphub.model;

import java.sql.Timestamp;

/**
 * Skill model class representing a skill in the system
 */
public class Skill {
    private int skillId;
    private String skillName;
    private String category;
    private Timestamp createdAt;

    // Proficiency level for offered skills
    private String proficiencyLevel;

    // Current level for wanted skills
    private String currentLevel;

    // Description for both offered and wanted skills
    private String description;

    // Additional properties for skill browsing
    private int offerCount;        // Number of users offering this skill
    private int wantCount;         // Number of users wanting to learn this skill
    private String imageUrl;       // URL to skill image
    private boolean isPopular;     // Flag for popular skills
    private int userId;            // User ID who added the skill (if applicable)

    // Default constructor
    public Skill() {
    }

    // Basic constructor
    public Skill(String skillName, String category) {
        this.skillName = skillName;
        this.category = category;
    }

    // Full constructor
    public Skill(int skillId, String skillName, String category, Timestamp createdAt) {
        this.skillId = skillId;
        this.skillName = skillName;
        this.category = category;
        this.createdAt = createdAt;
    }

    // Constructor for offered skills
    public Skill(int skillId, String skillName, String category, String proficiencyLevel, String description) {
        this.skillId = skillId;
        this.skillName = skillName;
        this.category = category;
        this.proficiencyLevel = proficiencyLevel;
        this.description = description;
    }

    // Constructor for wanted skills
    public Skill(int skillId, String skillName, String category, String currentLevel, String description, boolean isWanted) {
        this.skillId = skillId;
        this.skillName = skillName;
        this.category = category;
        this.currentLevel = currentLevel;
        this.description = description;
    }

    // Getters and Setters
    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getProficiencyLevel() {
        return proficiencyLevel;
    }

    public void setProficiencyLevel(String proficiencyLevel) {
        this.proficiencyLevel = proficiencyLevel;
    }

    public String getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(String currentLevel) {
        this.currentLevel = currentLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOfferCount() {
        return offerCount;
    }

    public void setOfferCount(int offerCount) {
        this.offerCount = offerCount;
    }

    public int getWantCount() {
        return wantCount;
    }

    public void setWantCount(int wantCount) {
        this.wantCount = wantCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isPopular() {
        return isPopular;
    }

    public void setPopular(boolean popular) {
        isPopular = popular;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "skillId=" + skillId +
                ", skillName='" + skillName + '\'' +
                ", category='" + category + '\'' +
                ", offerCount=" + offerCount +
                ", wantCount=" + wantCount +
                '}';
    }
}
