/* main.js - Hash Rate Communications Main JavaScript */

(function() {
    'use strict';

    // DOM Content Loaded
    document.addEventListener('DOMContentLoaded', function() {
        // Initialize all components
        initializeNavigation();
        initializeScrollEffects();
        initializeAnimations();
        initializeForms();
        initializeModals();
        initializeCookieBanner();
        initializeLoadingScreen();
        initializeBackToTop();
        initializeLazyLoading();
        initializeTooltips();
        initializeDropdowns();
        initializeMobileMenu();
        initializeSearchFunctionality();
        initializeNewsletterSignup();
        initializeProductFilters();
        initializeCarousels();
        initializeAccordions();
        initializeTabs();
        
        // Remove loading screen after everything is loaded
        setTimeout(function() {
            hideLoadingScreen();
        }, 1000);
    });

    // Navigation Functionality
    function initializeNavigation() {
        const nav = document.getElementById('main-nav');
        const mobileToggle = document.getElementById('mobile-toggle');
        const mobileMenu = document.getElementById('mobile-menu');
        const mobileOverlay = document.getElementById('mobile-menu-overlay');
        const mobileClose = document.getElementById('mobile-close');

        if (!nav) return;

        // Scroll effect for navigation
        window.addEventListener('scroll', function() {
            if (window.scrollY > 50) {
                nav.classList.add('scrolled');
            } else {
                nav.classList.remove('scrolled');
            }
        });

        // Mobile menu functionality
        if (mobileToggle && mobileMenu && mobileOverlay) {
            mobileToggle.addEventListener('click', function() {
                openMobileMenu();
            });

            mobileClose?.addEventListener('click', function() {
                closeMobileMenu();
            });

            mobileOverlay.addEventListener('click', function() {
                closeMobileMenu();
            });

            // Handle mobile submenu toggles
            const submenuToggles = document.querySelectorAll('.mobile-nav-item.has-submenu > .mobile-nav-link');
            submenuToggles.forEach(toggle => {
                toggle.addEventListener('click', function(e) {
                    e.preventDefault();
                    const parent = this.closest('.mobile-nav-item');
                    parent.classList.toggle('active');
                });
            });
        }

        function openMobileMenu() {
            mobileMenu.classList.add('active');
            mobileOverlay.classList.add('active');
            mobileToggle.classList.add('active');
            document.body.style.overflow = 'hidden';
        }

        function closeMobileMenu() {
            mobileMenu.classList.remove('active');
            mobileOverlay.classList.remove('active');
            mobileToggle.classList.remove('active');
            document.body.style.overflow = '';
        }

        // Smooth scrolling for anchor links
        const anchorLinks = document.querySelectorAll('a[href^="#"]');
        anchorLinks.forEach(link => {
            link.addEventListener('click', function(e) {
                const href = this.getAttribute('href');
                if (href === '#') return;
                
                e.preventDefault();
                const target = document.querySelector(href);
                if (target) {
                    const headerHeight = nav.offsetHeight;
                    const targetPosition = target.offsetTop - headerHeight - 20;
                    
                    window.scrollTo({
                        top: targetPosition,
                        behavior: 'smooth'
                    });
                    
                    // Close mobile menu if open
                    closeMobileMenu();
                }
            });
        });
    }

    // Scroll Effects
    function initializeScrollEffects() {
        // Parallax effect for hero section
        const hero = document.querySelector('.hero-section');
        if (hero) {
            window.addEventListener('scroll', function() {
                const scrolled = window.pageYOffset;
                const rate = scrolled * -0.5;
                hero.style.transform = `translateY(${rate}px)`;
            });
        }

        // Reveal animations on scroll
        const observerOptions = {
            threshold: 0.1,
            rootMargin: '0px 0px -50px 0px'
        };

        const observer = new IntersectionObserver(function(entries) {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('animate-reveal');
                }
            });
        }, observerOptions);

        // Observe elements for animation
        const animatedElements = document.querySelectorAll('.service-card, .product-card, .department-card, .job-item, .testimonial-card');
        animatedElements.forEach(el => {
            observer.observe(el);
        });
    }

    // Animation System
    function initializeAnimations() {
        // Add CSS for reveal animation
        const style = document.createElement('style');
        style.textContent = `
            .animate-reveal {
                animation: revealAnimation 0.8s ease-out forwards;
            }
            
            @keyframes revealAnimation {
                from {
                    opacity: 0;
                    transform: translateY(30px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }
            
            .service-card, .product-card, .department-card, .job-item, .testimonial-card {
                opacity: 0;
                transform: translateY(30px);
                transition: none;
            }
            
            .animate-reveal {
                opacity: 1;
                transform: translateY(0);
            }
        `;
        document.head.appendChild(style);

        // Counter animation
        const counters = document.querySelectorAll('.stat-number');
        const counterObserver = new IntersectionObserver(function(entries) {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    animateCounter(entry.target);
                    counterObserver.unobserve(entry.target);
                }
            });
        });

        counters.forEach(counter => {
            counterObserver.observe(counter);
        });

        function animateCounter(element) {
            const target = parseInt(element.textContent.replace(/\D/g, ''));
            const suffix = element.textContent.replace(/\d/g, '');
            let current = 0;
            const increment = target / 50;
            const timer = setInterval(() => {
                current += increment;
                if (current >= target) {
                    current = target;
                    clearInterval(timer);
                }
                element.textContent = Math.floor(current) + suffix;
            }, 40);
        }
    }

    // Form Handling
    function initializeForms() {
        const forms = document.querySelectorAll('form');
        
        forms.forEach(form => {
            // Add floating labels
            const inputs = form.querySelectorAll('input, textarea, select');
            inputs.forEach(input => {
                if (input.value) {
                    input.classList.add('has-value');
                }

                input.addEventListener('blur', function() {
                    if (this.value) {
                        this.classList.add('has-value');
                    } else {
                        this.classList.remove('has-value');
                    }
                });

                input.addEventListener('focus', function() {
                    this.classList.add('focused');
                });

                input.addEventListener('blur', function() {
                    this.classList.remove('focused');
                });
            });

            // Form submission handling
            form.addEventListener('submit', function(e) {
                if (this.classList.contains('ajax-form')) {
                    e.preventDefault();
                    handleAjaxForm(this);
                }
            });
        });

        // Real-time validation
        const requiredInputs = document.querySelectorAll('input[required], textarea[required]');
        requiredInputs.forEach(input => {
            input.addEventListener('blur', function() {
                validateField(this);
            });
        });

        function validateField(field) {
            const value = field.value.trim();
            const isValid = field.checkValidity();
            
            field.classList.toggle('is-valid', isValid && value !== '');
            field.classList.toggle('is-invalid', !isValid && value !== '');
        }

        function handleAjaxForm(form) {
            const formData = new FormData(form);
            const submitBtn = form.querySelector('button[type="submit"]');
            const originalText = submitBtn.textContent;
            
            // Show loading state
            submitBtn.textContent = 'Sending...';
            submitBtn.disabled = true;
            
            fetch(form.action, {
                method: 'POST',
                body: formData
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    showNotification('Message sent successfully!', 'success');
                    form.reset();
                } else {
                    showNotification('Error sending message. Please try again.', 'error');
                }
            })
            .catch(error => {
                showNotification('Error sending message. Please try again.', 'error');
            })
            .finally(() => {
                submitBtn.textContent = originalText;
                submitBtn.disabled = false;
            });
        }
    }

    // Modal System
    function initializeModals() {
        const modalTriggers = document.querySelectorAll('[data-modal]');
        const modals = document.querySelectorAll('.modal');
        
        modalTriggers.forEach(trigger => {
            trigger.addEventListener('click', function(e) {
                e.preventDefault();
                const modalId = this.getAttribute('data-modal');
                const modal = document.getElementById(modalId);
                if (modal) {
                    openModal(modal);
                }
            });
        });

        modals.forEach(modal => {
            const closeBtn = modal.querySelector('.modal-close');
            const overlay = modal.querySelector('.modal-overlay');
            
            closeBtn?.addEventListener('click', () => closeModal(modal));
            overlay?.addEventListener('click', () => closeModal(modal));
        });

        function openModal(modal) {
            modal.classList.add('active');
            document.body.style.overflow = 'hidden';
            
            // Focus first focusable element
            const focusable = modal.querySelector('input, button, textarea, select, [tabindex]:not([tabindex="-1"])');
            if (focusable) {
                setTimeout(() => focusable.focus(), 100);
            }
        }

        function closeModal(modal) {
            modal.classList.remove('active');
            document.body.style.overflow = '';
        }

        // Close modal on Escape key
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') {
                const activeModal = document.querySelector('.modal.active');
                if (activeModal) {
                    closeModal(activeModal);
                }
            }
        });
    }

    // Cookie Banner
    function initializeCookieBanner() {
        const cookieBanner = document.getElementById('cookie-banner');
        const acceptBtn = document.getElementById('accept-cookies');
        const rejectBtn = document.getElementById('reject-cookies');
        
        if (!cookieBanner) return;

        // Check if user has already made a choice
        if (!localStorage.getItem('cookieConsent')) {
            setTimeout(() => {
                cookieBanner.style.display = 'block';
                cookieBanner.classList.add('visible');
            }, 2000);
        }

        acceptBtn?.addEventListener('click', function() {
            localStorage.setItem('cookieConsent', 'accepted');
            hideCookieBanner();
            // Initialize analytics or other tracking
            initializeAnalytics();
        });

        rejectBtn?.addEventListener('click', function() {
            localStorage.setItem('cookieConsent', 'rejected');
            hideCookieBanner();
        });

        function hideCookieBanner() {
            cookieBanner.classList.remove('visible');
            setTimeout(() => {
                cookieBanner.style.display = 'none';
            }, 300);
        }
    }

    // Loading Screen
    function initializeLoadingScreen() {
        const loadingOverlay = document.getElementById('loading-overlay');
        
        if (loadingOverlay) {
            // Hide loading screen when page is fully loaded
            window.addEventListener('load', function() {
                setTimeout(hideLoadingScreen, 500);
            });
        }
    }

    function hideLoadingScreen() {
        const loadingOverlay = document.getElementById('loading-overlay');
        if (loadingOverlay) {
            loadingOverlay.classList.add('hidden');
            setTimeout(() => {
                loadingOverlay.style.display = 'none';
            }, 300);
        }
    }

    // Back to Top Button
    function initializeBackToTop() {
        const backToTopBtn = document.getElementById('back-to-top');
        
        if (!backToTopBtn) return;

        window.addEventListener('scroll', function() {
            if (window.scrollY > 300) {
                backToTopBtn.classList.add('visible');
            } else {
                backToTopBtn.classList.remove('visible');
            }
        });

        backToTopBtn.addEventListener('click', function() {
            window.scrollTo({
                top: 0,
                behavior: 'smooth'
            });
        });
    }

    // Lazy Loading for Images
    function initializeLazyLoading() {
        const images = document.querySelectorAll('img[data-src]');
        
        const imageObserver = new IntersectionObserver(function(entries) {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const img = entry.target;
                    img.src = img.dataset.src;
                    img.classList.remove('lazy');
                    imageObserver.unobserve(img);
                }
            });
        });

        images.forEach(img => {
            imageObserver.observe(img);
        });
    }

    // Tooltips
    function initializeTooltips() {
        const tooltipTriggers = document.querySelectorAll('[data-tooltip]');
        
        tooltipTriggers.forEach(trigger => {
            let tooltip;
            
            trigger.addEventListener('mouseenter', function() {
                const text = this.getAttribute('data-tooltip');
                tooltip = createTooltip(text);
                document.body.appendChild(tooltip);
                positionTooltip(tooltip, this);
                
                setTimeout(() => {
                    tooltip.classList.add('visible');
                }, 10);
            });

            trigger.addEventListener('mouseleave', function() {
                if (tooltip) {
                    tooltip.classList.remove('visible');
                    setTimeout(() => {
                        if (tooltip && tooltip.parentNode) {
                            document.body.removeChild(tooltip);
                        }
                    }, 200);
                }
            });
        });

        function createTooltip(text) {
            const tooltip = document.createElement('div');
            tooltip.className = 'tooltip';
            tooltip.textContent = text;
            tooltip.style.cssText = `
                position: fixed;
                background: var(--gray-900);
                color: white;
                padding: 0.5rem 0.75rem;
                border-radius: 0.375rem;
                font-size: 0.875rem;
                z-index: 1070;
                opacity: 0;
                transition: opacity 0.2s ease;
                pointer-events: none;
            `;
            return tooltip;
        }

        function positionTooltip(tooltip, trigger) {
            const triggerRect = trigger.getBoundingClientRect();
            const tooltipRect = tooltip.getBoundingClientRect();
            
            const top = triggerRect.top - tooltipRect.height - 10;
            const left = triggerRect.left + (triggerRect.width / 2) - (tooltipRect.width / 2);
            
            tooltip.style.top = top + 'px';
            tooltip.style.left = left + 'px';
        }
    }

    // Dropdown Functionality
    function initializeDropdowns() {
        const dropdowns = document.querySelectorAll('.dropdown');
        
        dropdowns.forEach(dropdown => {
            const trigger = dropdown.querySelector('.dropdown-toggle');
            const menu = dropdown.querySelector('.dropdown-menu');
            
            if (!trigger || !menu) return;

            // Handle hover for desktop
            if (window.innerWidth > 1024) {
                dropdown.addEventListener('mouseenter', function() {
                    menu.classList.add('show');
                });

                dropdown.addEventListener('mouseleave', function() {
                    menu.classList.remove('show');
                });
            } else {
                // Handle click for mobile
                trigger.addEventListener('click', function(e) {
                    e.preventDefault();
                    menu.classList.toggle('show');
                });
            }
        });

        // Close dropdowns when clicking outside
        document.addEventListener('click', function(e) {
            if (!e.target.closest('.dropdown')) {
                document.querySelectorAll('.dropdown-menu.show').forEach(menu => {
                    menu.classList.remove('show');
                });
            }
        });
    }

    // Mobile Menu Enhancement
    function initializeMobileMenu() {
        // Add swipe gestures for mobile menu
        let startX, startY;
        const mobileMenu = document.getElementById('mobile-menu');
        
        if (!mobileMenu) return;

        mobileMenu.addEventListener('touchstart', function(e) {
            startX = e.touches[0].clientX;
            startY = e.touches[0].clientY;
        });

        mobileMenu.addEventListener('touchmove', function(e) {
            if (!startX || !startY) return;
            
            const currentX = e.touches[0].clientX;
            const currentY = e.touches[0].clientY;
            
            const diffX = startX - currentX;
            const diffY = startY - currentY;
            
            // Swipe right to close
            if (Math.abs(diffX) > Math.abs(diffY) && diffX < -50) {
                closeMobileMenu();
            }
        });

        mobileMenu.addEventListener('touchend', function() {
            startX = null;
            startY = null;
        });

        function closeMobileMenu() {
            const mobileToggle = document.getElementById('mobile-toggle');
            const mobileOverlay = document.getElementById('mobile-menu-overlay');
            
            mobileMenu.classList.remove('active');
            mobileOverlay?.classList.remove('active');
            mobileToggle?.classList.remove('active');
            document.body.style.overflow = '';
        }
    }

    // Search Functionality
    function initializeSearchFunctionality() {
        const searchTriggers = document.querySelectorAll('[data-search-trigger]');
        const searchOverlay = document.getElementById('search-overlay');
        const searchInput = document.getElementById('search-input');
        const searchResults = document.getElementById('search-results');
        const searchClose = document.getElementById('search-close');
        
        if (!searchOverlay) return;

        searchTriggers.forEach(trigger => {
            trigger.addEventListener('click', function(e) {
                e.preventDefault();
                openSearch();
            });
        });

        searchClose?.addEventListener('click', closeSearch);
        
        searchOverlay.addEventListener('click', function(e) {
            if (e.target === searchOverlay) {
                closeSearch();
            }
        });

        // Search input handling
        let searchTimeout;
        searchInput?.addEventListener('input', function() {
            clearTimeout(searchTimeout);
            const query = this.value.trim();
            
            if (query.length > 2) {
                searchTimeout = setTimeout(() => {
                    performSearch(query);
                }, 300);
            } else {
                searchResults.innerHTML = '';
            }
        });

        function openSearch() {
            searchOverlay.classList.add('active');
            document.body.style.overflow = 'hidden';
            setTimeout(() => {
                searchInput?.focus();
            }, 100);
        }

        function closeSearch() {
            searchOverlay.classList.remove('active');
            document.body.style.overflow = '';
            if (searchInput) searchInput.value = '';
            if (searchResults) searchResults.innerHTML = '';
        }

        function performSearch(query) {
            // Mock search functionality - replace with actual search API
            const mockResults = [
                { title: 'Servers & Workstations', url: '/products/servers', type: 'Product' },
                { title: 'Datacenter Hardware', url: '/products/datacenter-hardware', type: 'Product' },
                { title: 'Building Management Systems', url: '/products/bms', type: 'Product' },
                { title: 'Technical Support Services', url: '/services', type: 'Service' },
                { title: 'Software Engineer Position', url: '/careers', type: 'Career' }
            ].filter(item => 
                item.title.toLowerCase().includes(query.toLowerCase())
            );

            displaySearchResults(mockResults);
        }

        function displaySearchResults(results) {
            if (!searchResults) return;
            
            if (results.length === 0) {
                searchResults.innerHTML = '<div class="search-no-results">No results found</div>';
                return;
            }

            const resultsHTML = results.map(result => `
                <a href="${result.url}" class="search-result-item">
                    <div class="search-result-type">${result.type}</div>
                    <div class="search-result-title">${result.title}</div>
                </a>
            `).join('');

            searchResults.innerHTML = resultsHTML;
        }

        // Keyboard shortcuts
        document.addEventListener('keydown', function(e) {
            // Ctrl/Cmd + K to open search
            if ((e.ctrlKey || e.metaKey) && e.key === 'k') {
                e.preventDefault();
                openSearch();
            }
            
            // Escape to close search
            if (e.key === 'Escape' && searchOverlay?.classList.contains('active')) {
                closeSearch();
            }
        });
    }

    // Newsletter Signup
    function initializeNewsletterSignup() {
        const newsletterForms = document.querySelectorAll('.newsletter-form');
        
        newsletterForms.forEach(form => {
            form.addEventListener('submit', function(e) {
                e.preventDefault();
                
                const email = this.querySelector('input[type="email"]')?.value;
                const button = this.querySelector('button[type="submit"]');
                const originalText = button?.innerHTML;
                
                if (!button || !email) return;
                
                // Show loading state
                button.innerHTML = '<i class="fas fa-spinner fa-spin"></i>';
                button.disabled = true;
                
                // Simulate API call
                setTimeout(() => {
                    showNotification('Thank you for subscribing!', 'success');
                    this.reset();
                    button.innerHTML = originalText;
                    button.disabled = false;
                }, 1000);
            });
        });
    }

    // Product Filters
    function initializeProductFilters() {
        const filterButtons = document.querySelectorAll('.filter-btn');
        const productItems = document.querySelectorAll('.product-item, .category-card');
        
        filterButtons.forEach(btn => {
            btn.addEventListener('click', function(e) {
                e.preventDefault();
                
                // Update active button
                filterButtons.forEach(b => b.classList.remove('active'));
                this.classList.add('active');
                
                const filter = this.getAttribute('data-filter');
                
                // Filter items
                productItems.forEach(item => {
                    const category = item.getAttribute('data-category');
                    const shouldShow = filter === 'all' || category === filter;
                    
                    if (shouldShow) {
                        item.style.display = 'block';
                        setTimeout(() => {
                            item.style.opacity = '1';
                            item.style.transform = 'translateY(0)';
                        }, 50);
                    } else {
                        item.style.opacity = '0';
                        item.style.transform = 'translateY(20px)';
                        setTimeout(() => {
                            item.style.display = 'none';
                        }, 300);
                    }
                });
            });
        });
    }

    // Carousels
    function initializeCarousels() {
        const carousels = document.querySelectorAll('.carousel');
        
        carousels.forEach(carousel => {
            const slides = carousel.querySelectorAll('.carousel-slide');
            const prevBtn = carousel.querySelector('.carousel-prev');
            const nextBtn = carousel.querySelector('.carousel-next');
            const indicators = carousel.querySelectorAll('.carousel-indicator');
            
            if (slides.length === 0) return;
            
            let currentSlide = 0;
            
            function showSlide(index) {
                slides.forEach((slide, i) => {
                    slide.classList.toggle('active', i === index);
                });
                
                indicators.forEach((indicator, i) => {
                    indicator.classList.toggle('active', i === index);
                });
                
                currentSlide = index;
            }
            
            function nextSlide() {
                const next = (currentSlide + 1) % slides.length;
                showSlide(next);
            }
            
            function prevSlide() {
                const prev = (currentSlide - 1 + slides.length) % slides.length;
                showSlide(prev);
            }
            
            nextBtn?.addEventListener('click', nextSlide);
            prevBtn?.addEventListener('click', prevSlide);
            
            indicators.forEach((indicator, index) => {
                indicator.addEventListener('click', () => showSlide(index));
            });
            
            // Auto-play
            const autoPlay = carousel.getAttribute('data-autoplay');
            if (autoPlay !== 'false') {
                setInterval(nextSlide, 5000);
            }
            
            // Initialize first slide
            showSlide(0);
        });
    }

    // Accordions
    function initializeAccordions() {
        const accordions = document.querySelectorAll('.accordion');
        
        accordions.forEach(accordion => {
            const items = accordion.querySelectorAll('.accordion-item');
            
            items.forEach(item => {
                const trigger = item.querySelector('.accordion-trigger');
                const content = item.querySelector('.accordion-content');
                
                if (!trigger || !content) return;
                
                trigger.addEventListener('click', function() {
                    const isActive = item.classList.contains('active');
                    
                    // Close all items (if single-open accordion)
                    if (accordion.classList.contains('single-open')) {
                        items.forEach(otherItem => {
                            otherItem.classList.remove('active');
                            const otherContent = otherItem.querySelector('.accordion-content');
                            if (otherContent) {
                                otherContent.style.maxHeight = null;
                            }
                        });
                    }
                    
                    // Toggle current item
                    if (!isActive) {
                        item.classList.add('active');
                        content.style.maxHeight = content.scrollHeight + 'px';
                    } else {
                        item.classList.remove('active');
                        content.style.maxHeight = null;
                    }
                });
            });
        });
    }

    // Tabs
    function initializeTabs() {
        const tabContainers = document.querySelectorAll('.tabs');
        
        tabContainers.forEach(container => {
            const tabButtons = container.querySelectorAll('.tab-button');
            const tabContents = container.querySelectorAll('.tab-content');
            
            tabButtons.forEach((button, index) => {
                button.addEventListener('click', function() {
                    // Remove active class from all tabs and contents
                    tabButtons.forEach(btn => btn.classList.remove('active'));
                    tabContents.forEach(content => content.classList.remove('active'));
                    
                    // Add active class to clicked tab and corresponding content
                    button.classList.add('active');
                    if (tabContents[index]) {
                        tabContents[index].classList.add('active');
                    }
                });
            });
        });
    }

    // Notification System
    function showNotification(message, type = 'info') {
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.innerHTML = `
            <div class="notification-content">
                <span class="notification-message">${message}</span>
                <button class="notification-close">&times;</button>
            </div>
        `;
        
        // Add notification styles
        notification.style.cssText = `
            position: fixed;
            top: 20px;
            right: 20px;
            background: ${type === 'success' ? 'var(--success-500)' : type === 'error' ? 'var(--error-500)' : 'var(--info-500)'};
            color: white;
            padding: 1rem 1.5rem;
            border-radius: 0.5rem;
            box-shadow: var(--shadow-lg);
            z-index: 1080;
            transform: translateX(100%);
            transition: transform 0.3s ease;
            max-width: 400px;
        `;
        
        document.body.appendChild(notification);
        
        // Show notification
        setTimeout(() => {
            notification.style.transform = 'translateX(0)';
        }, 100);
        
        // Auto hide after 5 seconds
        setTimeout(() => {
            hideNotification(notification);
        }, 5000);
        
        // Close button functionality
        notification.querySelector('.notification-close')?.addEventListener('click', () => {
            hideNotification(notification);
        });
        
        function hideNotification(notif) {
            notif.style.transform = 'translateX(100%)';
            setTimeout(() => {
                if (notif.parentNode) {
                    document.body.removeChild(notif);
                }
            }, 300);
        }
    }

    // Analytics Initialization
    function initializeAnalytics() {
        // Only initialize if user has consented to cookies
        if (localStorage.getItem('cookieConsent') === 'accepted') {
            // Initialize Google Analytics or other tracking
            console.log('Analytics initialized');
            
            // Track page views
            trackPageView();
            
            // Track form submissions
            document.addEventListener('submit', function(e) {
                const form = e.target;
                if (form.classList.contains('track-form')) {
                    trackEvent('form_submit', {
                        form_name: form.getAttribute('data-form-name') || 'unknown'
                    });
                }
            });
            
            // Track button clicks
            document.addEventListener('click', function(e) {
                const button = e.target.closest('[data-track]');
                if (button) {
                    trackEvent('button_click', {
                        button_name: button.getAttribute('data-track')
                    });
                }
            });
        }
    }

    function trackPageView() {
        // Track page view
        console.log('Page view tracked:', window.location.pathname);
    }

    function trackEvent(eventName, parameters = {}) {
        // Track custom event
        console.log('Event tracked:', eventName, parameters);
    }

    // Performance Monitoring
    function initializePerformanceMonitoring() {
        // Measure page load time
        window.addEventListener('load', function() {
            const perfData = performance.timing;
            const pageLoadTime = perfData.loadEventEnd - perfData.navigationStart;
            
            if (pageLoadTime > 3000) {
                console.warn('Page load time is slow:', pageLoadTime + 'ms');
            }
        });

        // Monitor largest contentful paint
        if ('PerformanceObserver' in window) {
            try {
                const observer = new PerformanceObserver((list) => {
                    const entries = list.getEntries();
                    const lastEntry = entries[entries.length - 1];
                    console.log('LCP:', lastEntry.startTime);
                });
                
                observer.observe({ entryTypes: ['largest-contentful-paint'] });
            } catch (e) {
                console.log('Performance observer not supported');
            }
        }
    }

    // Error Handling
    window.addEventListener('error', function(e) {
        console.error('JavaScript error:', e.error);
        
        // In production, you might want to send this to an error tracking service
        // trackError(e.error);
    });

    // Accessibility Enhancements
    function enhanceAccessibility() {
        // Skip link functionality
        const skipLink = document.querySelector('.skip-link');
        if (skipLink) {
            skipLink.addEventListener('click', function(e) {
                e.preventDefault();
                const target = document.querySelector(this.getAttribute('href'));
                if (target) {
                    target.focus();
                    target.scrollIntoView();
                }
            });
        }

        // Focus management for dropdowns
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') {
                // Close any open dropdowns
                document.querySelectorAll('.dropdown-menu.show').forEach(menu => {
                    menu.classList.remove('show');
                });
            }
        });

        // Announce page changes for screen readers
        const pageTitle = document.title;
        if (pageTitle) {
            const announcement = document.createElement('div');
            announcement.setAttribute('aria-live', 'polite');
            announcement.setAttribute('aria-atomic', 'true');
            announcement.className = 'sr-only';
            announcement.textContent = 'Page loaded: ' + pageTitle;
            announcement.style.cssText = `
                position: absolute;
                width: 1px;
                height: 1px;
                padding: 0;
                margin: -1px;
                overflow: hidden;
                clip: rect(0, 0, 0, 0);
                white-space: nowrap;
                border: 0;
            `;
            document.body.appendChild(announcement);
        }
    }

    // Initialize performance monitoring and accessibility
    initializePerformanceMonitoring();
    enhanceAccessibility();

    // Export functions for external use
    window.HashRateApp = {
        showNotification: showNotification,
        trackEvent: trackEvent,
        hideLoadingScreen: hideLoadingScreen
    };

})();