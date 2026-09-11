const LEGAL_API_BASE = "http://localhost:8080";

const LEGAL_ENDPOINTS = {
    TERMS: LEGAL_API_BASE + "/v1/terms-condition",
    PRIVACY: LEGAL_API_BASE + "/v1/privacy-policy",
    FAQ: LEGAL_API_BASE + "/v1/faqs"
};

function loadTermsAndConditions() {
    const termsList = $(".terms-list");

    if (termsList.length === 0) {
        return;
    }

    $.ajax({
        url: LEGAL_ENDPOINTS.TERMS,
        type: "GET",
        dataType: "json",
        success: function (result) {
            console.log("Terms API Response:", result);
            const terms = result.body || [];
            termsList.empty();

            if (terms.length === 0) {
                termsList.html(`
                    <div class="term-item">
                        <h3>No Terms & Conditions Available</h3>
                        <p>Terms & Conditions have not been added yet.</p>
                    </div>
                `);
                return;
            }

            $.each(terms, function (index, term) {
                termsList.append(`
                    <div class="term-item">
                        <h3>${index + 1}. ${escapeHtml(term.heading)}</h3>
                        <p>${escapeHtml(term.content)}</p>
                    </div>
                `);
            });
        },
        error: function (xhr, status, error) {
            console.error("Error loading Terms & Conditions:", error);
            console.error("Status:", status);
            console.error("Response:", xhr.responseText);

            termsList.html(`
                <div class="term-item">
                    <h3>Unable to load Terms & Conditions</h3>
                    <p>Please try again later.</p>
                </div>
            `);
        }
    });
}

function loadPrivacyPolicy() {
    const privacySection = $("#privacy-policy");

    if (privacySection.length === 0) {
        return;
    }

    const privacyList = privacySection.find(".terms-list");

    if (privacyList.length === 0) {
        return;
    }

    $.ajax({
        url: LEGAL_ENDPOINTS.PRIVACY,
        type: "GET",
        dataType: "json",
        success: function (result) {
            console.log("Privacy API Response:", result);
            const privacyPolicies = result.body || [];
            privacyList.empty();

            if (privacyPolicies.length === 0) {
                privacyList.html(`
                    <div class="term-item">
                        <h3>No Privacy Policy Available</h3>
                        <p>Privacy Policy information has not been added yet.</p>
                    </div>
                `);
                return;
            }

            $.each(privacyPolicies, function (index, policy) {
                privacyList.append(`
                    <div class="term-item">
                        <h3>${index + 1}. ${escapeHtml(policy.heading)}</h3>
                        <p>${escapeHtml(policy.content)}</p>
                    </div>
                `);
            });
        },
        error: function (xhr, status, error) {
            console.error("Error loading Privacy Policy:", error);
            console.error("Status:", status);
            console.error("Response:", xhr.responseText);

            privacyList.html(`
                <div class="term-item">
                    <h3>Unable to load Privacy Policy</h3>
                    <p>Please try again later.</p>
                </div>
            `);
        }
    });
}

function loadFAQs() {
    const faqSection = $("#faq");

    if (faqSection.length === 0) {
        return;
    }

    const faqGrid = faqSection.find(".faq-grid");

    if (faqGrid.length === 0) {
        return;
    }

    $.ajax({
        url: LEGAL_ENDPOINTS.FAQ,
        type: "GET",
        dataType: "json",
        success: function (result) {
            console.log("FAQ API Response:", result);
            const faqs = result.body || [];
            faqGrid.empty();

            if (faqs.length === 0) {
                faqGrid.html(`
                    <div class="faq-card">
                        <div class="faq-question">
                            <h4>No FAQs Available</h4>
                        </div>
                        <div class="faq-answer">
                            <p>FAQ information has not been added yet.</p>
                        </div>
                    </div>
                `);
                return;
            }

            $.each(faqs, function (index, faq) {
                const faqCard = $(`
                    <div class="faq-card">
                        <div class="faq-question">
                            <h4>${escapeHtml(faq.question)}</h4>
                            <span class="faq-arrow">▼</span>
                        </div>
                        <div class="faq-answer">
                            <p>${escapeHtml(faq.answer)}</p>
                        </div>
                    </div>
                `);

                faqCard.on("click", function () {
                    toggleFAQ(this);
                });

                faqGrid.append(faqCard);
            });
        },
        error: function (xhr, status, error) {
            console.error("Error loading FAQs:", error);
            console.error("Status:", status);
            console.error("Response:", xhr.responseText);

            faqGrid.html(`
                <div class="faq-card">
                    <div class="faq-question">
                        <h4>Unable to load FAQs</h4>
                    </div>
                    <div class="faq-answer">
                        <p>Please try again later.</p>
                    </div>
                </div>
            `);
        }
    });
}

function toggleFAQ(element) {
    const cards = $("#faq .faq-card");

    cards.each(function () {
        if (this !== element) {
            $(this).removeClass("active");
        }
    });

    $(element).toggleClass("active");
}

function escapeHtml(value) {
    if (value === null || value === undefined) {
        return "";
    }

    return String(value)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}

$(document).ready(function () {
    console.log("Website Legal & FAQ JS Loaded");

    if ($("#terms").length > 0) {
        loadTermsAndConditions();
    }

    if ($("#privacy-policy").length > 0) {
        loadPrivacyPolicy();
    }

    if ($("#faq").length > 0) {
        loadFAQs();
    }
});