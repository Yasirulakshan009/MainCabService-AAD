const SITE_SETTINGS_API_URL = "http://localhost:8080/v1/website-settings";

function applyWebsiteSettings(settings) {
    if (!settings) {
        return;
    }

    document.querySelectorAll('[data-field-text="companyName"]').forEach(el => {
        el.textContent = settings.companyName;
    });

    document.querySelectorAll('[data-field-text="phone"]').forEach(el => {
        el.textContent = settings.phoneNumber;
    });

    document.querySelectorAll('[data-field-href="phone"]').forEach(el => {
        el.setAttribute("href", "tel:" + settings.phoneNumber.replace(/\s+/g, ""));
    });

    document.querySelectorAll('[data-field-text="email"]').forEach(el => {
        el.textContent = settings.email;
    });

    document.querySelectorAll('[data-field-href="email"]').forEach(el => {
        el.setAttribute("href", "mailto:" + settings.email);
    });

    document.querySelectorAll('[data-field-text="address"]').forEach(el => {
        el.textContent = settings.address;
    });

    document.querySelectorAll('[data-field-href="whatsapp"]').forEach(el => {
        const digitsOnly = (settings.whatsappNumber || "").replace(/\D/g, "");
        const message = "Hello " + settings.companyName + ", I want to book a cab.";
        el.setAttribute("href", "https://wa.me/" + digitsOnly + "?text=" + encodeURIComponent(message));
    });

    document.querySelectorAll('[data-field-href="facebook"]').forEach(el => {
        if (settings.facebookUrl) {
            el.setAttribute("href", settings.facebookUrl);
        }
    });

    document.querySelectorAll('[data-field-href="instagram"]').forEach(el => {
        if (settings.instagramUrl) {
            el.setAttribute("href", settings.instagramUrl);
        }
    });
}

$(document).ready(function () {
    $.ajax({
        url: SITE_SETTINGS_API_URL,
        type: "GET",
        success: function (response) {
            applyWebsiteSettings(response.body);
        },
        error: function (xhr) {
            console.error("Failed to load website settings:", xhr);
        }
    });
});