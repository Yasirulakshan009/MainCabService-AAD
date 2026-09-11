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

const LEGAL_API_BASE = "http://localhost:8080";

const LEGAL_ENDPOINTS = {
    TERMS: `${LEGAL_API_BASE}/v1/terms-condition`,
    PRIVACY: `${LEGAL_API_BASE}/v1/privacy-policy`,
    FAQ: `${LEGAL_API_BASE}/v1/faqs`
};

function getLegalInputs(slot, type) {
    if (type === 'TERMS') {
        return {
            headingInput: document.getElementById(`heading_${slot}`),
            contentTextarea: document.getElementById(`content_${slot}`),
            editBtn: document.getElementById(`btn_${slot}`)
        };
    } else if (type === 'PRIVACY') {
        return {
            headingInput: document.getElementById(`heading_privacy_${slot}`),
            contentTextarea: document.getElementById(`content_privacy_${slot}`),
            editBtn: document.getElementById(`btn_privacy_${slot}`)
        };
    } else {
        return {
            headingInput: document.getElementById(`heading_faq_${slot}`),
            contentTextarea: document.getElementById(`content_faq_${slot}`),
            editBtn: document.getElementById(`btn_faq_${slot}`)
        };
    }
}

function loadLegalData() {
    const token = localStorage.getItem("jwtToken");
    const headers = {
        "Authorization": "Bearer " + token
    };

    $.ajax({
        url: LEGAL_ENDPOINTS.TERMS,
        type: "GET",
        headers: headers,
        success: function (res) {
            console.log("Terms Response:", res);
            renderLegalItems('TERMS', res.body || []);
        },
        error: function (err) {
            console.error("Failed to load terms and conditions:", err);
            Swal.fire({
                icon: 'error',
                title: 'Failed to Load',
                text: 'Unable to load Terms & Conditions.',
                confirmButtonText: 'OK'
            });
        }
    });

    $.ajax({
        url: LEGAL_ENDPOINTS.PRIVACY,
        type: "GET",
        headers: headers,
        success: function (res) {
            console.log("Privacy Response:", res);
            renderLegalItems('PRIVACY', res.body || []);
        },
        error: function (err) {
            console.error("Failed to load privacy policies:", err);
            Swal.fire({
                icon: 'error',
                title: 'Failed to Load',
                text: 'Unable to load Privacy Policy.',
                confirmButtonText: 'OK'
            });
        }
    });

    $.ajax({
        url: LEGAL_ENDPOINTS.FAQ,
        type: "GET",
        headers: headers,
        success: function (res) {
            console.log("FAQ Response:", res);
            renderLegalItems('FAQ', res.body || []);
        },
        error: function (err) {
            console.error("Failed to load FAQs:", err);
            Swal.fire({
                icon: 'error',
                title: 'Failed to Load',
                text: 'Unable to load FAQs.',
                confirmButtonText: 'OK'
            });
        }
    });
}

function renderLegalItems(type, list) {
    for (let slot = 1; slot <= 7; slot++) {
        const { headingInput, contentTextarea } = getLegalInputs(slot, type);

        if (!headingInput || !contentTextarea) {
            continue;
        }

        const item = list[slot - 1];

        if (item) {
            headingInput.value = (type === 'FAQ') ? item.question : item.heading;
            contentTextarea.value = (type === 'FAQ') ? item.answer : item.content;
            headingInput.dataset.id = item.id;
        } else {
            headingInput.value = '';
            contentTextarea.value = '';
            headingInput.dataset.id = '';
        }
    }
}

function toggleEdit(slot, type) {
    const { headingInput, contentTextarea, editBtn } = getLegalInputs(slot, type);

    if (headingInput.disabled) {
        headingInput.disabled = false;
        contentTextarea.disabled = false;
        headingInput.focus();

        editBtn.innerText = 'Save';
        editBtn.style.background = '#10b981';
        return;
    }

    const headingVal = headingInput.value.trim();
    const contentVal = contentTextarea.value.trim();

    if (!headingVal || !contentVal) {
        Swal.fire({
            icon: 'warning',
            title: 'Required Fields',
            text: 'Both fields are required!',
            confirmButtonText: 'OK'
        });
        return;
    }

    const existingId = headingInput.dataset.id;

    const payload = (type === 'FAQ')
        ? { question: headingVal, answer: contentVal }
        : { heading: headingVal, content: contentVal };

    if (existingId) {
        payload.id = Number(existingId);
    }

    const endpoint = LEGAL_ENDPOINTS[type];
    const ajaxType = existingId ? "PUT" : "POST";
    const ajaxUrl = existingId ? `${endpoint}/${existingId}` : endpoint;

    $.ajax({
        url: ajaxUrl,
        type: ajaxType,
        contentType: "application/json",
        data: JSON.stringify(payload),
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("jwtToken")
        },
        success: function (res) {
            console.log(`${type} saved:`, res);

            if (res.body && res.body.id) {
                headingInput.dataset.id = res.body.id;
            }

            headingInput.disabled = true;
            contentTextarea.disabled = true;

            editBtn.innerText = 'Edit';
            editBtn.style.background = '#3b82f6';

            Swal.fire({
                icon: 'success',
                title: 'Saved Successfully!',
                text: `${type} item saved successfully.`,
                confirmButtonText: 'OK',
                timer: 1800,
                timerProgressBar: true
            });
        },
        error: function (err) {
            console.error(`Error saving ${type} item:`, err);

            let errorMessage = 'Failed to save. Please check the fields and try again.';

            if (err.responseJSON && err.responseJSON.message) {
                errorMessage = err.responseJSON.message;
            }

            Swal.fire({
                icon: 'error',
                title: 'Save Failed',
                text: errorMessage,
                confirmButtonText: 'Try Again'
            });
        }
    });
}