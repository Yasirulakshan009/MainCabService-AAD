const CUSTOMER_REVIEWS_API_URL = "http://localhost:8080/v1/customer_reviews";

function customerReviewsAuthHeaders() {
    return {
        "Authorization": "Bearer " + localStorage.getItem("jwtToken")
    };
}

function loadAllReviewsFromBackend() {
    $.ajax({
        url: CUSTOMER_REVIEWS_API_URL,
        type: "GET",
        headers: customerReviewsAuthHeaders(),
        success: function (response) {
            renderReviewsGrid(response.body || []);
        },
        error: function (xhr) {
            console.error("Failed to load customer reviews:", xhr);
        }
    });
}

function renderReviewsGrid(reviews) {
    const section = document.getElementById("customer-reviews-section");
    if (!section) {
        return;
    }

    const grid = section.querySelector(".reviews-grid");
    const totalBadge = section.querySelector(".status-badge.completed");

    if (totalBadge) {
        totalBadge.innerText = `Total: ${reviews.length} Reviews`;
    }

    if (!grid) {
        return;
    }

    if (!reviews.length) {
        grid.innerHTML = `<p style="color: var(--text-muted);">No reviews submitted yet.</p>`;
        return;
    }

    grid.innerHTML = "";

    reviews.forEach(function (review) {
        const isApproved = review.status === "APPROVED";
        const initial = (review.customerName || "?").trim().charAt(0).toUpperCase();
        const stars = "⭐".repeat(review.rating || 0);

        const card = `
            <div class="content-card">
                <div class="legal-card-header">
                    <div class="review-author">
                        <div class="author-avatar">${escapeReviewHtml(initial)}</div>
                        <div>
                            <h4 style="font-size: 14px; font-weight: 700; color: var(--text-main);">${escapeReviewHtml(review.customerName)}</h4>
                            <span class="sub-title">${escapeReviewHtml(review.reviewerRole)} &bull; Rating: ${stars} (${review.rating}/5)</span>
                        </div>
                    </div>
                    <div style="display: flex; gap: 8px;">
                        <button onclick="approveReview(${review.id})" id="btn_approve_${review.id}"
                            class="status-badge ${isApproved ? 'accept' : 'pending'}" style="cursor: pointer;">
                            ${isApproved ? 'Approved' : 'Pending'}
                        </button>
                        <button onclick="deleteReview(${review.id})" class="btn-danger">Delete</button>
                    </div>
                </div>
                <p style="font-size: 13px; color: var(--text-muted); font-style: italic; margin-top: 10px;">"${escapeReviewHtml(review.message)}"</p>
            </div>
        `;

        grid.insertAdjacentHTML("beforeend", card);
    });
}

function approveReview(id) {
    const btn = document.getElementById(`btn_approve_${id}`);
    const currentlyApproved = !!btn && btn.innerText.trim() === "Approved";
    const newStatus = currentlyApproved ? "PENDING" : "APPROVED";

    $.ajax({
        url: `${CUSTOMER_REVIEWS_API_URL}/${id}/status?status=${newStatus}`,
        type: "PATCH",
        headers: customerReviewsAuthHeaders(),
        success: function (response) {
            Swal.fire({
                icon: "success",
                title: newStatus === "APPROVED" ? "Review Approved" : "Review Set to Pending",
                text: response.message || "Review status updated.",
                timer: 2000,
                showConfirmButton: false
            });

            loadAllReviewsFromBackend();
        },
        error: function (xhr) {
            Swal.fire({
                icon: "error",
                title: "Update Failed",
                text: (xhr.responseJSON && xhr.responseJSON.message) || "Unable to update review status."
            });

            console.error(xhr);
        }
    });
}

function deleteReview(id) {
    Swal.fire({
        title: "Delete Review?",
        text: "This review will be permanently deleted.",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Yes, delete it!",
        cancelButtonText: "Cancel"
    }).then(function (result) {
        if (!result.isConfirmed) {
            return;
        }

        $.ajax({
            url: `${CUSTOMER_REVIEWS_API_URL}/${id}`,
            type: "DELETE",
            headers: customerReviewsAuthHeaders(),
            success: function (response) {
                Swal.fire({
                    toast: true,
                    position: "top-end",
                    icon: "success",
                    title: response.message || "Review deleted successfully!",
                    showConfirmButton: false,
                    timer: 2500
                });

                loadAllReviewsFromBackend();
            },
            error: function (xhr) {
                Swal.fire({
                    icon: "error",
                    title: "Delete Failed",
                    text: (xhr.responseJSON && xhr.responseJSON.message) || "Unable to delete review."
                });

                console.error(xhr);
            }
        });
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
    loadAllReviewsFromBackend();
});