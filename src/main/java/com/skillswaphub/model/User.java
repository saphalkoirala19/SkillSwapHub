package com.skillswaphub.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * User model class representing a user in the system
 */
public class User {
    private int userId;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String bio;
    private String profileImage;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<Skill> offeredSkills;
    private List<Skill> wantedSkills;
    
    // Default constructor
    public User() {
        this.offeredSkills = new ArrayList<>();
        this.wantedSkills = new ArrayList<>();
    }
    
    // Constructor with basic fields
    public User(String username, String email, String password, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.offeredSkills = new ArrayList<>();
        this.wantedSkills = new ArrayList<>();
    }
    
    // Full constructor
    public User(int userId, String username, String email, String password, String firstName, 
                String lastName, String bio, String profileImage, Timestamp createdAt, Timestamp updatedAt) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bio = bio;
        this.profileImage = profileImage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.offeredSkills = new ArrayList<>();
        this.wantedSkills = new ArrayList<>();
    }
    
    // Getters and Setters
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getBio() {
        return bio;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }
    
    public String getProfileImage() {
        return profileImage;
    }
    
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
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
    
    public List<Skill> getOfferedSkills() {
        return offeredSkills;
    }
    
    public void setOfferedSkills(List<Skill> offeredSkills) {
        this.offeredSkills = offeredSkills;
    }
    
    public List<Skill> getWantedSkills() {
        return wantedSkills;
    }
    
    public void setWantedSkills(List<Skill> wantedSkills) {
        this.wantedSkills = wantedSkills;
    }
    
    public void addOfferedSkill(Skill skill) {
        this.offeredSkills.add(skill);
    }
    
    public void addWantedSkill(Skill skill) {
        this.wantedSkills.add(skill);
    }
    
    // Helper methods
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
