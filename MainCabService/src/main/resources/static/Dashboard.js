$(document).ready(function() {
    const savedRole = localStorage.getItem("userRole");
    $('#sidebarRole').text(savedRole);

    const savedEmail = localStorage.getItem("userEmail");
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

    resetSectionFilterToAll(sectionName);
}

function resetSectionFilterToAll(sectionName) {
    const filterableSections = {
        'fleet': 'fleet-section',
        'bookings': 'bookings-section',
        'rentals': 'rentals-section',
        'returns': 'returns-section',
        'payments': 'payments-section',
        'maintenance': 'maintenance-section'
    };

    const sectionId = filterableSections[sectionName];
    if (!sectionId) return;

    const sectionEl = document.getElementById(sectionId);
    if (!sectionEl) return;

    const allButton = sectionEl.querySelector('.filter-bar .filter-btn');
    if (allButton) allButton.click();
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
        inputs.forEach(input => input.removeAttribute('disabled'));
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