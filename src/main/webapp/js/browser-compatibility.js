/**
 * Browser compatibility script for SkillSwapHub
 * Provides polyfills and workarounds for older browsers
 */

document.addEventListener('DOMContentLoaded', function() {
    // Polyfill for Element.matches
    if (!Element.prototype.matches) {
        Element.prototype.matches =
            Element.prototype.matchesSelector ||
            Element.prototype.mozMatchesSelector ||
            Element.prototype.msMatchesSelector ||
            Element.prototype.oMatchesSelector ||
            Element.prototype.webkitMatchesSelector ||
            function(s) {
                var matches = (this.document || this.ownerDocument).querySelectorAll(s),
                    i = matches.length;
                while (--i >= 0 && matches.item(i) !== this) {}
                return i > -1;
            };
    }

    // Polyfill for Element.closest
    if (!Element.prototype.closest) {
        Element.prototype.closest = function(s) {
            var el = this;
            do {
                if (el.matches(s)) return el;
                el = el.parentElement || el.parentNode;
            } while (el !== null && el.nodeType === 1);
            return null;
        };
    }

    // Polyfill for forEach on NodeList
    if (window.NodeList && !NodeList.prototype.forEach) {
        NodeList.prototype.forEach = Array.prototype.forEach;
    }

    // Polyfill for Object.assign
    if (typeof Object.assign !== 'function') {
        Object.assign = function(target) {
            if (target === null || target === undefined) {
                throw new TypeError('Cannot convert undefined or null to object');
            }

            var to = Object(target);

            for (var index = 1; index < arguments.length; index++) {
                var nextSource = arguments[index];

                if (nextSource !== null && nextSource !== undefined) {
                    for (var nextKey in nextSource) {
                        if (Object.prototype.hasOwnProperty.call(nextSource, nextKey)) {
                            to[nextKey] = nextSource[nextKey];
                        }
                    }
                }
            }
            return to;
        };
    }

    // Detect IE browser
    var isIE = !!document.documentMode;

    // Add IE-specific class to body for CSS targeting
    if (isIE) {
        document.body.classList.add('ie-browser');
    }

    // Fix for flexbox in IE
    if (isIE) {
        var flexElements = document.querySelectorAll('.flex-container, .user-card, .skill-card, .request-card');
        for (var i = 0; i < flexElements.length; i++) {
            var el = flexElements[i];
            if (window.getComputedStyle(el).display === 'flex') {
                el.style.display = 'block';
                el.style.display = 'flex';
            }
        }
    }

    // Fix for placeholder styling in IE
    if (isIE) {
        var inputs = document.querySelectorAll('input, textarea');
        for (var i = 0; i < inputs.length; i++) {
            var input = inputs[i];
            if (input.placeholder) {
                input.classList.add('has-placeholder');
            }
        }
    }

    // Fix for CSS variables in IE
    if (isIE) {
        // Fallback colors
        document.documentElement.style.setProperty('--primary-color', '#4a6fa5');
        document.documentElement.style.setProperty('--secondary-color', '#6c757d');
        document.documentElement.style.setProperty('--accent-color', '#28a745');
    }
});
