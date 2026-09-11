const PAYMENT_API_URL = "http://localhost:8080/v1/payments";

let paymentList = [];

function paymentAuthHeaders() {
    return {
        "Authorization": "Bearer " + localStorage.getItem("jwtToken")
    };
}

function loadAllPaymentsFromBackend() {
    $.ajax({
        url: PAYMENT_API_URL,
        type: "GET",
        headers: paymentAuthHeaders(),
        success: function (response) {
            paymentList = response.body || [];
            renderPaymentTable(paymentList);
        },
        error: function (xhr) {
            console.error("Failed to load payments:", xhr);
        }
    });
}

function loadRevenueStatsFromBackend() {
    $.ajax({
        url: `${PAYMENT_API_URL}/total-revenue`,
        type: "GET",
        headers: paymentAuthHeaders(),
        success: function (response) {
            $("#totalRevenueCount").text("LKR " + Number(response.body || 0).toLocaleString());
        }
    });

    $.ajax({
        url: `${PAYMENT_API_URL}/monthly-revenue`,
        type: "GET",
        headers: paymentAuthHeaders(),
        success: function (response) {
            $("#thisMonthRevenueCount").text("LKR " + Number(response.body || 0).toLocaleString());
        }
    });
}

function loadCompletedPaymentCountFromBackend() {
    let combinedCount = 0;
    let callsFinished = 0;

    function handleResult(count) {
        combinedCount += Number(count) || 0;
        callsFinished++;
        if (callsFinished === 2) {
            $("#completedPaymentCount").text(combinedCount);
        }
    }

    $.ajax({
        url: `${PAYMENT_API_URL}/count/status/ALL_COMPLETED`,
        type: "GET",
        headers: paymentAuthHeaders(),
        success: function (response) {
            handleResult(response.body);
        },
        error: function () {
            handleResult(0);
        }
    });

    $.ajax({
        url: `${PAYMENT_API_URL}/count/status/RENT_PAID_DONE`,
        type: "GET",
        headers: paymentAuthHeaders(),
        success: function (response) {
            handleResult(response.body);
        },
        error: function () {
            handleResult(0);
        }
    });
}

function renderPaymentTable(list) {
    const tbody = $("#paymentTableBody");
    tbody.empty();

    if (!list || list.length === 0) {
        tbody.html(`
            <tr id="noPaymentsRow">
                <td colspan="6" style="text-align: center; padding: 40px; color: var(--text-muted);">
                    <div style="font-size: 16px; font-weight: 600; margin-bottom: 4px;">No payments found</div>
                    <div style="font-size: 12px;">No payments recorded yet.</div>
                </td>
            </tr>
        `);
        return;
    }

    list.forEach(function (payment) {
        const status = String(payment.status || "").toUpperCase();

        let statusColor = "#ff4d4d";
        let statusText = "Failed";

        if (status === "ALL_COMPLETED") {
            statusColor = "#28a745";
            statusText = "Completed";
        } else if (status === "RENT_PAID_DONE") {
            statusColor = "#0071e3";
            statusText = "Rent Paid Done";
        }

        const statusBadge = `<span style="background-color: ${statusColor}22; color: ${statusColor}; border: 1px solid ${statusColor}55; padding: 5px 12px; border-radius: 20px; font-size: 11px; font-weight: 700; text-transform: uppercase;">${statusText}</span>`;

        tbody.append(`
            <tr>
                <td>#PAY-${payment.paymentID}</td>
                <td>#RNT-${payment.rentalID}</td>
                <td>LKR ${Number(payment.amount || 0).toLocaleString()}</td>
                <td>${payment.paymentMethod || "-"}</td>
                <td>${payment.date || ""}</td>
                <td>${statusBadge}</td>
            </tr>
        `);
    });
}

function searchPayments() {
    const keyword = $("#paymentSearch").val().trim();

    if (!keyword) {
        loadAllPaymentsFromBackend();
        return;
    }

    $.ajax({
        url: `${PAYMENT_API_URL}/${encodeURIComponent(keyword)}`,
        type: "GET",
        headers: paymentAuthHeaders(),
        success: function (response) {
            const list = response.body || [];
            renderPaymentTable(list);
        },
        error: function (xhr) {
            console.error("Payment search error:", xhr);
        }
    });
}

function filterPayments(status, button) {
    $("#payments-section .filter-btn").removeClass("active");

    if (button) {
        $(button).addClass("active");
    }

    if (!status || status === "all") {
        loadAllPaymentsFromBackend();
        return;
    }

    if (status === "failed") {
        $.ajax({
            url: `${PAYMENT_API_URL}/status/FAILED`,
            type: "GET",
            headers: paymentAuthHeaders(),
            success: function (response) {
                renderPaymentTable(response.body || []);
            },
            error: function (xhr) {
                console.error("Payment filter error:", xhr);
            }
        });
        return;
    }

    if (status === "completed") {
        let combined = [];
        let callsFinished = 0;

        function handleResult(list) {
            combined = combined.concat(list || []);
            callsFinished++;
            if (callsFinished === 2) {
                renderPaymentTable(combined);
            }
        }

        $.ajax({
            url: `${PAYMENT_API_URL}/status/ALL_COMPLETED`,
            type: "GET",
            headers: paymentAuthHeaders(),
            success: function (response) {
                handleResult(response.body);
            },
            error: function () {
                handleResult([]);
            }
        });

        $.ajax({
            url: `${PAYMENT_API_URL}/status/RENT_PAID_DONE`,
            type: "GET",
            headers: paymentAuthHeaders(),
            success: function (response) {
                handleResult(response.body);
            },
            error: function () {
                handleResult([]);
            }
        });
    }
}

$(document).ready(function () {
    loadAllPaymentsFromBackend();
    loadRevenueStatsFromBackend();
    loadCompletedPaymentCountFromBackend();
});