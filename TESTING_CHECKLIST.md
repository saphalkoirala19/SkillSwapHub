# SkillSwapHub Testing Checklist

Use this checklist to verify that the application works correctly on different PCs.

## Setup Instructions

1. Clone the repository
2. Set up a MySQL database named `skillswaphub`
3. Run the SQL scripts in `src/main/resources/db/init.sql` to create the database schema
4. Update the database connection parameters in `src/main/java/com/skillswaphub/dao/DBConnection.java` if needed
5. Deploy the application to a servlet container (e.g., Tomcat)
6. Access the application at `http://localhost:8080/skillswaphub`

## Test Environment

| Test PC | Operating System | Browser | Java Version | Servlet Container | Database |
|---------|------------------|---------|--------------|-------------------|----------|
| PC 1    |                  |         |              |                   |          |
| PC 2    |                  |         |              |                   |          |

## Functionality Tests

### User Management

- [ ] User can register a new account
- [ ] User can log in with valid credentials
- [ ] User cannot log in with invalid credentials
- [ ] User can log out
- [ ] User can view their profile
- [ ] User can edit their profile
- [ ] User can upload a profile image

### Skill Management

- [ ] User can add a new skill to offer
- [ ] User can add a new skill they want to learn
- [ ] User can browse skills offered by others
- [ ] User can search for skills by keyword
- [ ] User can filter skills by category
- [ ] User can view skill details

### Skill Swap Requests

- [ ] User can send a skill swap request
- [ ] User can view pending requests
- [ ] User can accept a request
- [ ] User can decline a request
- [ ] User can view accepted requests
- [ ] User can view declined requests

### Messaging System

- [ ] User can send a message to another user
- [ ] User can view conversation history
- [ ] User can see unread message notifications
- [ ] Messages are displayed in chronological order
- [ ] User can navigate between different conversations

### Rating & Feedback

- [ ] User can rate another user after a skill session
- [ ] User can leave feedback for another user
- [ ] User can view ratings and feedback they've received
- [ ] User can view ratings and feedback they've given
- [ ] Average rating is calculated correctly

### Skill Session Scheduler

- [ ] User can schedule a skill session
- [ ] User can view upcoming sessions
- [ ] User can view past sessions
- [ ] User can mark a session as completed
- [ ] User can cancel a session

### Leaderboard

- [ ] User can view the leaderboard
- [ ] Top rated users are displayed correctly
- [ ] Most active users are displayed correctly
- [ ] Users with most skills are displayed correctly

### Admin Panel

- [ ] Admin can view all users
- [ ] Admin can view all skills
- [ ] Admin can view all requests
- [ ] Admin can view all sessions
- [ ] Admin can delete users
- [ ] Admin can delete skills
- [ ] Admin can delete requests
- [ ] Admin can delete sessions

## Cross-Browser Tests

- [ ] Application works in Chrome
- [ ] Application works in Firefox
- [ ] Application works in Edge
- [ ] Application works in Safari
- [ ] Application works in Internet Explorer 11

## Responsive Design Tests

- [ ] Application is usable on desktop
- [ ] Application is usable on tablet
- [ ] Application is usable on mobile

## Security Tests

- [ ] Passwords are stored securely (hashed)
- [ ] User cannot access protected pages without logging in
- [ ] User cannot access admin pages without admin privileges
- [ ] User cannot access other users' private information
- [ ] Form inputs are validated on the client side
- [ ] Form inputs are validated on the server side
- [ ] SQL injection is prevented
- [ ] XSS attacks are prevented
- [ ] CSRF attacks are prevented

## Performance Tests

- [ ] Pages load within acceptable time
- [ ] Database queries are optimized
- [ ] Images are optimized for web
- [ ] CSS and JavaScript files are minified

## Notes

Use this section to document any issues or observations during testing:

- 
- 
- 

## Test Results

| Test PC | Test Date | Tester | Result | Notes |
|---------|-----------|--------|--------|-------|
| PC 1    |           |        |        |       |
| PC 2    |           |        |        |       |
