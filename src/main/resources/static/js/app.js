/**
 * Syllabix Core Application JavaScript
 * Global API helpers, Toast Notifications, Quick-Action Modals & Navigation
 */

const Syllabix = {
    // API Helper
    api: async function(url, options = {}) {
        const defaultHeaders = {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        };

        options.headers = { ...defaultHeaders, ...(options.headers || {}) };
        if (options.body && typeof options.body === 'object' && !(options.body instanceof FormData)) {
            options.body = JSON.stringify(options.body);
        } else if (options.body instanceof FormData) {
            delete options.headers['Content-Type']; // Let browser set multipart boundary
        }

        try {
            const response = await fetch(url, options);
            if (!response.ok) {
                const errData = await response.json().catch(() => ({ message: response.statusText }));
                throw new Error(errData.message || 'API request failed');
            }
            if (response.status === 204) return null;
            return await response.json();
        } catch (error) {
            console.error('API Error:', error);
            Syllabix.toast(error.message, 'danger');
            throw error;
        }
    },

    // Toast Notifications
    toast: function(message, type = 'info') {
        let container = document.getElementById('syllabix-toasts');
        if (!container) {
            container = document.createElement('div');
            container.id = 'syllabix-toasts';
            container.className = 'toast-syllabix';
            document.body.appendChild(container);
        }

        const toast = document.createElement('div');
        toast.className = 'toast-item';
        
        let icon = 'bi-info-circle-fill text-primary';
        let borderColor = 'var(--primary)';
        if (type === 'success') {
            icon = 'bi-check-circle-fill text-success';
            borderColor = 'var(--accent)';
        } else if (type === 'danger') {
            icon = 'bi-exclamation-triangle-fill text-danger';
            borderColor = 'var(--danger)';
        } else if (type === 'warning') {
            icon = 'bi-exclamation-circle-fill text-warning';
            borderColor = 'var(--warning)';
        }

        toast.style.borderLeftColor = borderColor;
        toast.innerHTML = `
            <i class="bi ${icon} fs-5"></i>
            <div class="flex-grow-1 text-sm font-medium">${message}</div>
            <button type="button" class="btn-close btn-close-sm" onclick="this.parentElement.remove()"></button>
        `;

        container.appendChild(toast);
        setTimeout(() => {
            toast.style.opacity = '0';
            toast.style.transition = 'opacity 0.4s ease';
            setTimeout(() => toast.remove(), 400);
        }, 4000);
    },

    // Highlight active sidebar navigation based on pathname
    initNavigation: function() {
        const path = window.location.pathname;
        const links = document.querySelectorAll('.sidebar-menu .menu-link');
        links.forEach(link => {
            const href = link.getAttribute('href');
            if (href === path || (path === '/' && href === '/') || (href !== '/' && path.startsWith(href))) {
                link.classList.add('active');
            } else {
                link.classList.remove('active');
            }
        });
    },

    // Check Current Auth User and populate top header
    initUserHeader: async function() {
        try {
            const profile = await Syllabix.api('/api/auth/me');
            if (profile && profile.fullName) {
                const nameElements = document.querySelectorAll('.js-student-name');
                nameElements.forEach(el => el.textContent = profile.fullName);

                const emailElements = document.querySelectorAll('.js-student-email');
                emailElements.forEach(el => el.textContent = profile.email);

                const avatarElements = document.querySelectorAll('.js-user-avatar');
                avatarElements.forEach(el => {
                    const initials = profile.fullName.split(' ').map(n => n[0]).join('').substring(0, 2).toUpperCase();
                    el.textContent = initials;
                });
            }
        } catch (e) {
            console.warn('Could not fetch user profile', e);
        }
    },

    // Global Logout
    logout: async function() {
        try {
            await Syllabix.api('/api/auth/logout', { method: 'POST' });
            Syllabix.toast('Logged out successfully', 'success');
            window.location.href = '/login';
        } catch (e) {
            window.location.href = '/login';
        }
    },

    // Populate Subject Select Dropdowns in Modals
    loadSubjectsDropdown: async function(selectElementId) {
        const select = document.getElementById(selectElementId);
        if (!select) return;
        try {
            const subjects = await Syllabix.api('/api/subjects');
            select.innerHTML = '<option value="">-- Choose Subject --</option>';
            subjects.forEach(s => {
                const opt = document.createElement('option');
                opt.value = s.id;
                opt.textContent = `${s.name} (${s.code})`;
                select.appendChild(opt);
            });
        } catch (e) {
            console.error('Failed loading subjects dropdown', e);
        }
    }
};

document.addEventListener('DOMContentLoaded', () => {
    Syllabix.initNavigation();
    Syllabix.initUserHeader();
});
