const REVIEWS_API_BASE = "http://localhost:8080";

const REVIEWS_ENDPOINTS = {
    APPROVED: REVIEWS_API_BASE + "/v1/customer_reviews/approved",
    SUBMIT: REVIEWS_API_BASE + "/v1/customer_reviews"
};

function loadApprovedReviews() {
    const feedbackGrid = $("#reviews .feedback-grid");

    if (feedbackGrid.length === 0) {
        return;
    }

    $.ajax({
        url: REVIEWS_ENDPOINTS.APPROVED,
        type: "GET",
        dataType: "json",
        success: function (result) {
            console.log("Approved Reviews API Response:", result);
            const reviews = result.body || [];
            feedbackGrid.empty();

            if (reviews.length === 0) {
                feedbackGrid.html(`
                    <div class="feedback-card">
                        <p class="review-text">No reviews yet. Be the first to share your experience!</p>
                    </div>
                `);
                return;
            }

            $.each(reviews, function (index, review) {
                const stars = "⭐".repeat(review.rating || 0);
                const initial = (review.customerName || "?").trim().charAt(0).toUpperCase();

                feedbackGrid.append(`
                    <div class="feedback-card">
                        <div class="stars">${stars}</div>
                        <p class="review-text">"${escapeReviewHtml(review.message)}"</p>
                        <div class="customer-info">
                            <div class="customer-avatar">${escapeReviewHtml(initial)}</div>
                            <div>
                                <h4>${escapeReviewHtml(review.customerName)}</h4>
                                <span>${escapeReviewHtml(review.reviewerRole)}</span>
                            </div>
                        </div>
                    </div>
                `);
            });
        },
        error: function (xhr, status, error) {
            console.error("Error loading approved reviews:", error);
            console.error("Response:", xhr.responseText);

            feedbackGrid.html(`
                <div class="feedback-card">
                    <p class="review-text">Unable to load reviews right now. Please try again later.</p>
                </div>
            `);
        }
    });
}

function decodeJwtPayload(token) {
    try {
        const payloadPart = token.split(".")[1];
        const normalized = payloadPart.replace(/-/g, "+").replace(/_/g, "/");
        const json = decodeURIComponent(
            atob(normalized)
                .split("")
                .map(function (c) { return "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2); })
                .join("")
        );
        return JSON.parse(json);
    } catch (e) {
        return null;
    }
}

function getValidCustomerToken() {
    const token = localStorage.getItem("jwtToken");
    const role = localStorage.getItem("userRole");

    if (!token || role !== "CUSTOMER") {
        return null;
    }

    const payload = decodeJwtPayload(token);
    const isExpired = payload && payload.exp && (Date.now() >= payload.exp * 1000);

    if (!payload || isExpired) {
        // Stale/expired token - clear it so the user isn't stuck in a broken "logged in" state.
        localStorage.removeItem("jwtToken");
        localStorage.removeItem("userRole");
        localStorage.removeItem("userEmail");
        localStorage.removeItem("userPermissions");
        return null;
    }

    return token;
}

function redirectToReviewLogin() {
    const returnTo = encodeURIComponent("Home.html?openReview=1");
    window.location.href = "SignIn.html?redirect=" + returnTo;
}

function promptLoginForReview() {
    if (typeof Swal !== "undefined") {
        Swal.fire({
            icon: "info",
            title: "Login Required",
            text: "Please log in to your customer account to write a review.",
            confirmButtonText: "Go to Login",
            confirmButtonColor: "#0071e3"
        }).then(function () {
            redirectToReviewLogin();
        });
    } else {
        alert("Please log in to your customer account to write a review.");
        redirectToReviewLogin();
    }
}

function openReviewModal() {
    const token = getValidCustomerToken();

    if (!token) {
        promptLoginForReview();
        return;
    }

    const modal = document.getElementById('reviewModal');
    if (modal) {
        modal.style.display = 'flex';
    }
}

function closeReviewModal() {
    const modal = document.getElementById('reviewModal');
    if (modal) {
        modal.style.display = 'none';
    }
}

function submitReviewForm(event) {
    event.preventDefault();

    const name = document.getElementById('reviewerName').value.trim();
    const role = document.getElementById('reviewerRole').value.trim();
    const rating = parseInt(document.getElementById('reviewerRating').value, 10);
    const message = document.getElementById('reviewerMessage').value.trim();

    const token = getValidCustomerToken();

    if (!token) {
        // Safety net in case the token expired while the modal was open.
        closeReviewModal();
        promptLoginForReview();
        return;
    }

    const reviewData = {
        customerName: name,
        reviewerRole: role,
        rating: rating,
        message: message
    };

    $.ajax({
        url: REVIEWS_ENDPOINTS.SUBMIT,
        type: "POST",
        contentType: "application/json",
        headers: {
            "Authorization": "Bearer " + token
        },
        data: JSON.stringify(reviewData),
        success: function (result) {
            console.log("Review submitted:", result);
            alert("Thank you! Your review has been submitted and is pending approval.");

            document.getElementById('reviewerName').value = '';
            document.getElementById('reviewerRole').value = '';
            document.getElementById('reviewerMessage').value = '';
            closeReviewModal();
        },
        error: function (xhr) {
            console.error("Error submitting review:", xhr);

            if (xhr.status === 401 || xhr.status === 403) {
                closeReviewModal();
                promptLoginForReview();
                return;
            }

            const errObj = xhr.responseJSON;
            alert((errObj && errObj.message) || "Unable to submit your review. Please try again.");
        }
    });
}

function escapeReviewHtml(value) {
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
    console.log("Website Reviews JS Loaded");

    if ($("#reviews").length > 0) {
        loadApprovedReviews();
    }

    const params = new URLSearchParams(window.location.search);
    if (params.get("openReview") === "1") {
        openReviewModal();

        const cleanUrl = window.location.pathname;
        window.history.replaceState({}, document.title, cleanUrl);
    }
});