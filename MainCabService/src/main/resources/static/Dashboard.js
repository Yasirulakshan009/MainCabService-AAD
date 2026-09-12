$(document).ready(function() {
    const savedRole = localStorage.getItem("userRole");
    $('#sidebarRole').text(savedRole);

    const savedEmail = localStorage.getItem("userEmail");
    $('#sidebarEmail').text(savedEmail);
});

function handleSignOut() {
    Swal.fire({
        title: "Sign out?",
        text: "Are you sure you want to sign out of the dashboard?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Yes, sign out",
        cancelButtonText: "Cancel",
        confirmButtonColor: "#ff4d4d"
    }).then(function (result) {
        if (!result.isConfirmed) {
            return;
        }

        localStorage.removeItem("jwtToken");
        localStorage.removeItem("userRole");
        localStorage.removeItem("userEmail");
        localStorage.removeItem("userPermissions");
        localStorage.removeItem("isAdminSession");

        window.location.href = "Login.html";
    });
}

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

    if (sectionName === 'website-settings') {
        loadWebsiteSettings();
    }
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

let isEditing = false;

const WEBSITE_SETTINGS_API_URL = "http://localhost:8080/v1/website-settings";

function loadWebsiteSettings() {
    $.ajax({
        url: WEBSITE_SETTINGS_API_URL,
        type: "GET",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("jwtToken")
        },
        success: function (response) {
            const settings = response.body;
            if (!settings) {
                return;
            }

            document.getElementById("companyName").value = settings.companyName || "";
            document.getElementById("phoneNumber").value = settings.phoneNumber || "";
            document.getElementById("whatsappNumber").value = settings.whatsappNumber || "";
            document.getElementById("email").value = settings.email || "";
            document.getElementById("address").value = settings.address || "";
            document.getElementById("facebookUrl").value = settings.facebookUrl || "";
            document.getElementById("instagramUrl").value = settings.instagramUrl || "";
        },
        error: function (xhr) {
            console.error("Failed to load website settings:", xhr);
            Swal.fire({
                icon: "error",
                title: "Load Failed",
                text: "Could not load current website settings."
            });
        }
    });
}

function toggleEditMode() {
    const inputs = document.querySelectorAll('#websiteSettingsForm input, #websiteSettingsForm textarea');
    const actionBtn = document.getElementById('settingsActionBtn');

    if (!isEditing) {
        inputs.forEach(input => input.removeAttribute('disabled'));
        actionBtn.innerText = "Save Changes";
        actionBtn.style.background = "linear-gradient(135deg, #00c853, #b9f6ca)";
        actionBtn.style.color = "#000";
        isEditing = true;
        return;
    }

    saveWebsiteSettings(inputs, actionBtn);
}

function saveWebsiteSettings(inputs, actionBtn) {
    const settingsData = {
        companyName: document.getElementById("companyName").value.trim(),
        phoneNumber: document.getElementById("phoneNumber").value.trim(),
        whatsappNumber: document.getElementById("whatsappNumber").value.trim(),
        email: document.getElementById("email").value.trim(),
        address: document.getElementById("address").value.trim(),
        facebookUrl: document.getElementById("facebookUrl").value.trim(),
        instagramUrl: document.getElementById("instagramUrl").value.trim()
    };

    actionBtn.disabled = true;
    actionBtn.innerText = "Saving...";

    $.ajax({
        url: WEBSITE_SETTINGS_API_URL,
        type: "PUT",
        contentType: "application/json",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("jwtToken")
        },
        data: JSON.stringify(settingsData),

        success: function () {
            inputs.forEach(input => input.setAttribute('disabled', 'true'));
            actionBtn.disabled = false;
            actionBtn.innerText = "Edit Website Settings";
            actionBtn.style.background = "";
            actionBtn.style.color = "";
            isEditing = false;

            Swal.fire({
                icon: "success",
                title: "Saved!",
                text: "Website settings updated successfully. Changes are now live on the main website.",
                confirmButtonColor: "#28a745"
            });
        },

        error: function (xhr) {
            actionBtn.disabled = false;
            actionBtn.innerText = "Save Changes";

            let message = "Failed to update website settings.";
            if (xhr.responseJSON && xhr.responseJSON.message) {
                message = xhr.responseJSON.message;
            }

            Swal.fire({
                icon: "error",
                title: "Update Failed",
                text: message
            });
            console.error("Website settings update error:", xhr);
        }
    });
}

function handleWebsiteSettingsAction(event) {
    event.preventDefault();
}