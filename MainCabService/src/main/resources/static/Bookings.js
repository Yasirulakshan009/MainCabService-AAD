const BOOKING_API_URL = "http://localhost:8080/v1/bookings";

function bookingAuthHeaders() {
    return {
        "Authorization": "Bearer " + localStorage.getItem("jwtToken")
    };
}

function loadAllBookingsFromBackend() {
    $.ajax({
        url: BOOKING_API_URL,
        type: "GET",
        headers: bookingAuthHeaders(),

        success: function (response) {
            const bookings = response.body || [];

            updateBookingStats(bookings);
            renderBookingTable(response.body || []);
        },

        error: function (xhr) {
            console.error("Failed to load bookings:", xhr);
        }
    });
}

function renderBookingTable(bookings) {

    const tableBody = document.getElementById("bookingTableBody");
    if (!tableBody) return;

    tableBody.innerHTML = "";

    if (!bookings.length) {
        tableBody.innerHTML = `
            <tr>
                <td colspan="8" style="text-align:center;">
                    No booking requests found.
                </td>
            </tr>
        `;
        return;
    }

    bookings.forEach(function (booking) {

        const status = String(booking.bookingStatus || "PENDING").toUpperCase();

        const row = `
            <tr class="booking-row" data-status="${status}">
                <td><strong>#BKG-${String(booking.bookingID).padStart(3, "0")}</strong></td>

                <td class="cust-column">
                    <div class="cust-info">
                        <span class="cust-ref">
                            #BC-${booking.bookingCustomerID}
                        </span>
                        <span class="c-name">
                            ${booking.bookingCustomerName || ""}
                        </span>
                    </div>
                </td>

                <td>${booking.vehicleModel || ""}</td>

                <td class="date-cell">
                    ${booking.startDate || ""}
                </td>

                <td class="date-cell">
                    ${booking.endDate || ""}
                </td>

                <td>${booking.pickupAddress || ""}</td>

                <td>
                    <span class="status-badge ${status}">
                        ${status.charAt(0).toUpperCase() + status.slice(1)}
                    </span>
                </td>

                <td>
                    <button class="btn-action accept"
                        onclick="updateBookingStatus(${booking.bookingID}, 'CONFIRMED')">
                        <i class="ri-check-line"></i>
                    </button>
                
                    <button class="btn-action reject"
                        onclick="updateBookingStatus(${booking.bookingID}, 'CANCELLED')">
                        <i class="ri-close-line"></i>
                    </button>
                </td>
            </tr>
        `;

        tableBody.insertAdjacentHTML("beforeend", row);
    });
}

function updateBookingStatus(id, status) {

    $.ajax({
        url: `${BOOKING_API_URL}/${id}/status?status=${status}`,
        type: "PATCH",
        headers: bookingAuthHeaders(),

        success: function (response) {

            Swal.fire({
                icon: "success",
                title: "Booking Updated",
                text: response.message || "Booking status updated successfully.",
                timer: 2000,
                showConfirmButton: false
            });

            loadAllBookingsFromBackend();
        },

        error: function (xhr) {

            Swal.fire({
                icon: "error",
                title: "Update Failed",
                text: xhr.responseJSON?.message || "Unable to update booking status."
            });

            console.error(xhr);
        }
    });
}

function searchBookings() {

    const keyword = $("#bookingSearch").val().trim();

    if (!keyword) {
        loadAllBookingsFromBackend();
        return;
    }

    $.ajax({
        url: `${BOOKING_API_URL}/search?keyword=${encodeURIComponent(keyword)}`,
        type: "GET",
        headers: bookingAuthHeaders(),

        success: function (response) {
            renderBookingTable(response.body || []);
        },

        error: function (xhr) {
            console.error("Booking search failed:", xhr);
        }
    });
}

function updateBookingStats(bookings) {

    const pending = bookings.filter(b =>
        String(b.bookingStatus).toUpperCase() === "PENDING"
    ).length;

    const confirmed = bookings.filter(b =>
        String(b.bookingStatus).toUpperCase() === "CONFIRMED"
    ).length;

    const cancelled = bookings.filter(b =>
        String(b.bookingStatus).toUpperCase() === "CANCELLED"
    ).length;

    document.getElementById("pendingCount").innerText = pending;
    document.getElementById("confirmedCount").innerText = confirmed;
    document.getElementById("cancelledCount").innerText = cancelled;
    document.getElementById("totalBookingCount").innerText = bookings.length;
}

function filterBookings(status, button) {

    document.querySelectorAll(".filter-btn").forEach(btn => {
        btn.classList.remove("active");
    });

    button.classList.add("active");

    document.querySelectorAll(".booking-row").forEach(row => {

        const rowStatus = String(row.dataset.status || "").toUpperCase();

        if (status.toUpperCase() === "ALL" ||
            rowStatus === status.toUpperCase()) {

            row.style.display = "";
        } else {
            row.style.display = "none";
        }
    });
}

$(document).ready(function () {
    loadAllBookingsFromBackend();
});