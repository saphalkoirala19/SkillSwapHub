-- Create database if not exists
CREATE DATABASE IF NOT EXISTS skillswaphub;

-- Use the database
USE skillswaphub;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    bio TEXT,
    profile_image VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create skills table
CREATE TABLE IF NOT EXISTS skills (
    skill_id INT AUTO_INCREMENT PRIMARY KEY,
    skill_name VARCHAR(100) NOT NULL UNIQUE,
    category VARCHAR(50) NOT NULL,
    image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create user_offered_skills table (skills that users can offer)
CREATE TABLE IF NOT EXISTS user_offered_skills (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    skill_id INT NOT NULL,
    proficiency_level ENUM('Beginner', 'Intermediate', 'Advanced', 'Expert') NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(skill_id) ON DELETE CASCADE,
    UNIQUE KEY user_skill_unique (user_id, skill_id)
);

-- Create user_wanted_skills table (skills that users want to learn)
CREATE TABLE IF NOT EXISTS user_wanted_skills (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    skill_id INT NOT NULL,
    current_level ENUM('None', 'Beginner', 'Intermediate', 'Advanced') NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(skill_id) ON DELETE CASCADE,
    UNIQUE KEY user_skill_unique (user_id, skill_id)
);

-- Insert some sample skill categories
INSERT INTO skills (skill_name, category) VALUES
('Java Programming', 'Technology'),
('Python Programming', 'Technology'),
('Web Development', 'Technology'),
('Graphic Design', 'Design'),
('Digital Marketing', 'Marketing'),
('Content Writing', 'Writing'),
('Photography', 'Arts'),
('Cooking', 'Culinary'),
('Guitar', 'Music'),
('Spanish Language', 'Languages'),
('French Language', 'Languages'),
('Public Speaking', 'Communication'),
('Yoga', 'Fitness'),
('Painting', 'Arts'),
('Data Analysis', 'Technology');
