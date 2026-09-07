$(document).ready(function() {
    const savedRole = localStorage.getItem("userRole") || "STAFF";
    $('#sidebarRole').text(savedRole); 

    const savedEmail = localStorage.getItem("userEmail") || "admin@auracabs.com";
    $('#sidebarEmail').text(savedEmail);
});

function showSection(sectionName, element) {
    const sections = [
        'overview-section',
        'fleet-section',
        'bookings-section',
        'booking-customers-section',
        'normal-customers-section',
        'rentals-section',
        'returns-section',
        'payments-section',
        'maintenance-section',
        'settings-section',
        'register-customers-section',
        'update-tc-section',
        'update-faq-section',
        'website-settings-section',
        'customer-reviews-section'
    ];

    sections.forEach(id => {
        const section = document.getElementById(id);
        if (section) {
            section.style.display = (id === `${sectionName}-section`) ? 'block' : 'none';
        }
    });

    const menuItems = document.querySelectorAll('.sidebar-menu-scrollable .menu-item, .menu-group .menu-item');
    menuItems.forEach(item => item.classList.remove('active'));
    element.classList.add('active');
}

const themeToggleBtn = document.querySelector('.btn-theme-switch');
if (themeToggleBtn) {
    themeToggleBtn.addEventListener('click', () => {
        document.body.classList.toggle('dark-mode');
        if (document.body.classList.contains('dark-mode')) {
            themeToggleBtn.innerHTML = '<i class="ri-sun-line"></i> Light';
        } else {
            themeToggleBtn.innerHTML = '<i class="ri-moon-line"></i> Dark';
        }
    });
}

function scrollToSettingsSection() {
    const settingsMenuBtn = document.getElementById('menu-settings');
    if (settingsMenuBtn) {
        showSection('settings', settingsMenuBtn);
    }
}

function openWebsite() {
    localStorage.setItem('isAdminSession', 'true');
}

function switchLegalTab(type) {
    const termsSub = document.getElementById('terms-sub-section');
    const privacySub = document.getElementById('privacy-sub-section');
    const faqSub = document.getElementById('faq-sub-section');

    const termsBtn = document.getElementById('tab-terms-btn');
    const privacyBtn = document.getElementById('tab-privacy-btn');
    const faqBtn = document.getElementById('tab-faq-btn');

    termsSub.style.display = (type === 'terms') ? 'block' : 'none';
    privacySub.style.display = (type === 'privacy') ? 'block' : 'none';
    faqSub.style.display = (type === 'faq') ? 'block' : 'none';

    termsBtn.style.background = (type === 'terms') ? '#3b82f6' : 'transparent';
    termsBtn.style.color = (type === 'terms') ? 'white' : '#94a3b8';

    privacyBtn.style.background = (type === 'privacy') ? '#3b82f6' : 'transparent';
    privacyBtn.style.color = (type === 'privacy') ? 'white' : '#94a3b8';

    faqBtn.style.background = (type === 'faq') ? '#3b82f6' : 'transparent';
    faqBtn.style.color = (type === 'faq') ? 'white' : '#94a3b8';
}

function toggleEdit(id, type) {
    let headingInput, contentTextarea, editBtn;

    if (type === 'TERMS') {
        headingInput = document.getElementById(`heading_${id}`);
        contentTextarea = document.getElementById(`content_${id}`);
        editBtn = document.getElementById(`btn_${id}`);
    } else if (type === 'PRIVACY') {
        headingInput = document.getElementById(`heading_privacy_${id}`);
        contentTextarea = document.getElementById(`content_privacy_${id}`);
        editBtn = document.getElementById(`btn_privacy_${id}`);
    } else if (type === 'FAQ') {
        headingInput = document.getElementById(`heading_faq_${id}`);
        contentTextarea = document.getElementById(`content_faq_${id}`);
        editBtn = document.getElementById(`btn_faq_${id}`);
    }

    if (headingInput.disabled) {
        headingInput.disabled = false;
        contentTextarea.disabled = false;
        headingInput.focus();

        editBtn.innerText = 'Save';
        editBtn.style.background = '#10b981';
    } else {
        headingInput.disabled = true;
        contentTextarea.disabled = true;

        editBtn.innerText = 'Edit';
        editBtn.style.background = '#3b82f6';

        console.log(`Item ${id} (${type}) saved successfully!`);
    }
}

function approveReview(id) {
    const btn = document.getElementById(`btn_approve_${id}`);
    if (btn.innerText === 'Pending' || btn.innerText === 'Approve') {
        btn.innerText = 'Approved';
        btn.style.background = '#10b981';
        console.log(`Review #${id} Approved!`);
    } else {
        btn.innerText = 'Pending';
        btn.style.background = '#f59e0b';
        console.log(`Review #${id} set to Pending.`);
    }
}

function deleteReview(id) {
    if (confirm('Are you sure you want to delete this review?')) {
        console.log(`Review #${id} deleted.`);
    }
}

let isEditing = false;

function toggleEditMode() {
    const inputs = document.querySelectorAll('#websiteSettingsForm input, #websiteSettingsForm textarea');
    const actionBtn = document.getElementById('settingsActionBtn');

    if (!isEditing) {
        inputs.forEach(input => input.removeAttribute('disabled')); // Fields enable කිරීම
        actionBtn.innerText = "Save Changes";
        actionBtn.style.background = "linear-gradient(135deg, #00c853, #b9f6ca)";
        actionBtn.style.color = "#000";
        actionBtn.type = "submit";
        isEditing = true;
    } else {

        inputs.forEach(input => input.setAttribute('disabled', 'true'));
        actionBtn.innerText = "Edit Website Settings";
        actionBtn.style.background = "";
        actionBtn.style.color = "";
        actionBtn.type = "button";
        isEditing = false;

        alert("Website settings updated successfully!");
    }
}

function handleWebsiteSettingsAction(event) {
    event.preventDefault();
    if (isEditing) {
        toggleEditMode();
    }
}