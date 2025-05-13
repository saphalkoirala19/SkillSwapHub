/**
 * SkillSwapHub JavaScript
 * Handles interactive functionality for the application
 */

document.addEventListener('DOMContentLoaded', function() {
    // Initialize all components
    initTabs();
    initModals();
    initPasswordToggle();
    initProfileEditTabs();
    initFormValidation();
    
    // Show success/error messages and hide after 5 seconds
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.opacity = '0';
            setTimeout(() => {
                alert.style.display = 'none';
            }, 500);
        }, 5000);
    });
});

/**
 * Initialize tab functionality
 */
function initTabs() {
    const tabs = document.querySelectorAll('.tab');
    if (!tabs.length) return;
    
    tabs.forEach(tab => {
        tab.addEventListener('click', function() {
            // Get the tab ID
            const tabId = this.getAttribute('data-tab');
            
            // Remove active class from all tabs and tab panes
            document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
            document.querySelectorAll('.tab-pane').forEach(p => p.classList.remove('active'));
            
            // Add active class to current tab and tab pane
            this.classList.add('active');
            document.getElementById(tabId).classList.add('active');
        });
    });
}

/**
 * Initialize modal functionality
 */
function initModals() {
    // Add Offered Skill Modal
    initModal('add-offered-skill-btn', 'add-offered-skill-modal');
    initModal('add-first-offered-skill-btn', 'add-offered-skill-modal');
    
    // Add Wanted Skill Modal
    initModal('add-wanted-skill-btn', 'add-wanted-skill-modal');
    initModal('add-first-wanted-skill-btn', 'add-wanted-skill-modal');
}

/**
 * Initialize a specific modal
 * @param {string} buttonId - ID of the button that opens the modal
 * @param {string} modalId - ID of the modal to open
 */
function initModal(buttonId, modalId) {
    const button = document.getElementById(buttonId);
    const modal = document.getElementById(modalId);
    
    if (!button || !modal) return;
    
    // Open modal when button is clicked
    button.addEventListener('click', function() {
        modal.classList.add('active');
        document.body.style.overflow = 'hidden'; // Prevent scrolling
    });
    
    // Close modal when close button is clicked
    const closeButtons = modal.querySelectorAll('.close-modal, .cancel-modal');
    closeButtons.forEach(btn => {
        btn.addEventListener('click', function() {
            modal.classList.remove('active');
            document.body.style.overflow = ''; // Restore scrolling
        });
    });
    
    // Close modal when clicking outside the modal content
    modal.addEventListener('click', function(event) {
        if (event.target === modal) {
            modal.classList.remove('active');
            document.body.style.overflow = ''; // Restore scrolling
        }
    });
}

/**
 * Initialize password toggle functionality
 */
function initPasswordToggle() {
    const toggleButtons = document.querySelectorAll('.toggle-password');
    
    toggleButtons.forEach(button => {
        button.addEventListener('click', function() {
            const input = this.parentElement.querySelector('input');
            const icon = this.querySelector('i');
            
            // Toggle password visibility
            if (input.type === 'password') {
                input.type = 'text';
                icon.classList.remove('fa-eye');
                icon.classList.add('fa-eye-slash');
            } else {
                input.type = 'password';
                icon.classList.remove('fa-eye-slash');
                icon.classList.add('fa-eye');
            }
        });
    });
}

/**
 * Initialize profile edit tabs
 */
function initProfileEditTabs() {
    const navLinks = document.querySelectorAll('.profile-edit-nav a');
    
    navLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            
            // Get the section ID from the href
            const sectionId = this.getAttribute('href').substring(1);
            
            // Remove active class from all nav items and sections
            document.querySelectorAll('.profile-edit-nav li').forEach(item => {
                item.classList.remove('active');
            });
            
            document.querySelectorAll('.edit-section').forEach(section => {
                section.classList.remove('active');
            });
            
            // Add active class to current nav item and section
            this.parentElement.classList.add('active');
            document.getElementById(sectionId).classList.add('active');
        });
    });
}

/**
 * Initialize form validation
 */
function initFormValidation() {
    const forms = document.querySelectorAll('form');
    
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            let isValid = true;
            
            // Validate required fields
            const requiredFields = form.querySelectorAll('[required]');
            requiredFields.forEach(field => {
                if (!field.value.trim()) {
                    isValid = false;
                    showFieldError(field, 'This field is required');
                } else {
                    clearFieldError(field);
                }
            });
            
            // Validate email fields
            const emailFields = form.querySelectorAll('input[type="email"]');
            emailFields.forEach(field => {
                if (field.value.trim() && !isValidEmail(field.value)) {
                    isValid = false;
                    showFieldError(field, 'Please enter a valid email address');
                }
            });
            
            // Validate password fields
            const passwordField = form.querySelector('input[name="password"]');
            const confirmPasswordField = form.querySelector('input[name="confirmPassword"]');
            
            if (passwordField && passwordField.value.trim()) {
                if (!isValidPassword(passwordField.value)) {
                    isValid = false;
                    showFieldError(passwordField, 'Password must be at least 8 characters and include at least one uppercase letter, one lowercase letter, and one number');
                }
                
                // Check if passwords match
                if (confirmPasswordField && confirmPasswordField.value.trim() && 
                    passwordField.value !== confirmPasswordField.value) {
                    isValid = false;
                    showFieldError(confirmPasswordField, 'Passwords do not match');
                }
            }
            
            // Prevent form submission if validation fails
            if (!isValid) {
                e.preventDefault();
            }
        });
    });
}

/**
 * Show error message for a form field
 * @param {Element} field - The form field
 * @param {string} message - Error message to display
 */
function showFieldError(field, message) {
    // Clear any existing error
    clearFieldError(field);
    
    // Create error message element
    const errorElement = document.createElement('span');
    errorElement.className = 'error-message';
    errorElement.textContent = message;
    
    // Insert error message after the field
    field.parentNode.insertBefore(errorElement, field.nextSibling);
    
    // Add error class to the field
    field.classList.add('error');
}

/**
 * Clear error message for a form field
 * @param {Element} field - The form field
 */
function clearFieldError(field) {
    // Remove error class from the field
    field.classList.remove('error');
    
    // Remove any existing error message
    const parent = field.parentNode;
    const errorElement = parent.querySelector('.error-message');
    if (errorElement) {
        parent.removeChild(errorElement);
    }
}

/**
 * Validate email format
 * @param {string} email - Email to validate
 * @returns {boolean} True if valid, false otherwise
 */
function isValidEmail(email) {
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return emailRegex.test(email);
}

/**
 * Validate password strength
 * @param {string} password - Password to validate
 * @returns {boolean} True if valid, false otherwise
 */
function isValidPassword(password) {
    // At least 8 characters, one uppercase, one lowercase, one number
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/;
    return passwordRegex.test(password);
}

/**
 * Validate username format
 * @param {string} username - Username to validate
 * @returns {boolean} True if valid, false otherwise
 */
function isValidUsername(username) {
    // 3-20 characters, letters, numbers, and underscores only
    const usernameRegex = /^[a-zA-Z0-9_]{3,20}$/;
    return usernameRegex.test(username);
}
