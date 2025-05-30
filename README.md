# SkillSwapHub

SkillSwapHub is a web application that allows users to share and exchange skills with others. Users can offer skills they are proficient in and request to learn skills from others.

## MVC Architecture

This application follows the Model-View-Controller (MVC) architectural pattern:

### Model Layer
- Located in `src/main/java/com/skillswaphub/model/`
- Contains Java classes that represent the data entities (User, Skill, SkillRequest, Message, etc.)
- Each model class encapsulates the data and provides getters and setters

### View Layer
- Located in `src/main/webapp/WEB-INF/views/`
- Contains JSP files that render the user interface
- Uses JSTL for dynamic content generation
- Styled with CSS files in `src/main/webapp/css/`
- Enhanced with JavaScript in `src/main/webapp/js/`

### Controller Layer
- Located in `src/main/java/com/skillswaphub/servlet/`
- Contains Servlet classes that handle HTTP requests and responses
- Processes user input, interacts with the model layer, and selects the appropriate view

### Data Access Layer
- Located in `src/main/java/com/skillswaphub/dao/`
- Contains DAO (Data Access Object) classes that handle database operations
- Provides an abstraction layer between the controllers and the database

### Utility Layer
- Located in `src/main/java/com/skillswaphub/util/`
- Contains utility classes for common operations (validation, error handling, etc.)

### Filter Layer
- Located in `src/main/java/com/skillswaphub/filter/`
- Contains filter classes for request/response preprocessing (authentication, session management, etc.)

## Features

1. **User Management**
   - Registration and login
   - Profile management
   - Session handling

2. **Skill Management**
   - Add and browse skills
   - Offer skills to teach
   - Request skills to learn

3. **Skill Swap Requests**
   - Send skill swap requests
   - Accept or decline requests
   - View pending, accepted, and completed requests

4. **Messaging System**
   - Send and receive messages
   - View conversation history
   - Unread message notifications

5. **Rating & Feedback**
   - Rate users after skill sessions
   - Leave feedback on teaching quality
   - View ratings and feedback

6. **Skill Session Scheduler**
   - Schedule skill swap sessions
   - Manage upcoming and past sessions

7. **Leaderboard**
   - View top-rated users
   - See most active users
   - Discover popular skills

8. **Admin Panel**
   - Manage users, skills, and requests
   - View system statistics

## Technical Implementation

### Security Measures
- Password hashing with BCrypt
- Input validation and sanitization
- Session management and timeout
- Authentication filters

### Cross-Browser Compatibility
- CSS vendor prefixes
- JavaScript polyfills
- Browser-specific fixes

### Exception Handling
- Comprehensive JDBC exception handling
- User-friendly error messages
- Logging for debugging

### Validation
- Client-side validation with JavaScript
- Server-side validation with Java
- Custom validation utility classes

## Testing

The application has been tested on:
1. Chrome (latest version)
2. Firefox (latest version)
3. Edge (latest version)
4. Safari (latest version)
5. Internet Explorer 11

## Setup Instructions

1. Clone the repository
2. Set up a MySQL database named `skillswaphub`
3. Update the database connection parameters in `DBConnection.java`
4. Deploy the application to a servlet container (e.g., Tomcat)
5. Access the application at `http://localhost:8080/skillswaphub`
