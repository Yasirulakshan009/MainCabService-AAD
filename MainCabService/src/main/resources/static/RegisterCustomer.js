const CUSTOMER_API = "http://localhost:8080/api/v1/auth";

function loadRegisteredCustomers() {
    const token = localStorage.getItem("jwtToken");

    if (!token) {
        console.error("Admin JWT token not found.");
        return;
    }

    $.ajax({
        url: CUSTOMER_API + "/customers",
        type: "GET",
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function (response) {
            console.log("Registered Customers:", response);
            let customers = [];

            if (Array.isArray(response)) {
                customers = response;
            } else if (response && Array.isArray(response.data)) {
                customers = response.data;
            } else if (response && Array.isArray(response.body)) {
                customers = response.body;
            }

            displayRegisteredCustomers(customers);
        },
        error: function (xhr) {
            console.error("Failed to load registered customers:", xhr);

            if (xhr.status === 401) {
                Swal.fire({
                    icon: "error",
                    title: "Session Expired",
                    text: "Please login again."
                });
                return;
            }

            if (xhr.status === 403) {
                Swal.fire({
                    icon: "error",
                    title: "Access Denied",
                    text: "Only administrators can view registered customers."
                });
                return;
            }

            Swal.fire({
                icon: "error",
                title: "Loading Failed",
                text: "Unable to load registered customers."
            });
        }
    });
}

function displayRegisteredCustomers(customers) {
    const tableBody = document.querySelector("#customerRegisterTable tbody");

    if (!tableBody) {
        console.error("Customer table tbody not found.");
        return;
    }

    tableBody.innerHTML = "";

    if (!customers || customers.length === 0) {
        tableBody.innerHTML = `
            <tr>
                <td colspan="7" style="text-align:center;">
                    No registered customers found.
                </td>
            </tr>
        `;
        return;
    }

    customers.forEach(function (customer) {
        const userId = customer.userID ?? customer.id ?? "-";
        const name = customer.userName ?? "-";
        const phone = customer.phone ?? "-";
        const email = customer.userEmail ?? "-";
        const status = customer.status ?? "ACTIVE";

        let statusBadge = "";

        if (status === "ACTIVE") {
            statusBadge = `
                <span style="color: green; font-weight: bold;">Active</span>
            `;
        } else {
            statusBadge = `
                <span style="color: red; font-weight: bold;">Inactive</span>
            `;
        }

        let actionButton = "";

        if (status === "ACTIVE") {
            actionButton = `
                            <button
                                style="padding: 5px 10px; background-color: #ff4d4d; color: white; border: none; border-radius: 4px; cursor: pointer;"
                                onclick="changeCustomerStatus(${userId}, 'INACTIVE')">
                                Make Inactive
                            </button>
                        `;
        } else {
            actionButton = `
                            <button
                                style="padding: 5px 10px; background-color: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer;"
                                onclick="changeCustomerStatus(${userId}, 'ACTIVE')">
                                Make Active
                            </button>
                        `;
        }

        const row = `
            <tr>
                <td>${userId}</td>
                <td>${name}</td>
                <td>${phone}</td>
                <td>${email}</td>
                <td>
                    <span class="badge bg-primary">
                        CUSTOMER
                    </span>
                </td>
                <td>${statusBadge}</td>
                <td>${actionButton}</td>
            </tr>
        `;

        tableBody.insertAdjacentHTML("beforeend", row);
    });
}

function changeCustomerStatus(userId, newStatus) {
    let message = "";

    if (newStatus === "INACTIVE") {
        message = "This customer will no longer be able to login.";
    } else {
        message = "This customer will be able to login again.";
    }

    Swal.fire({
        title: "Change Customer Status?",
        text: message,
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Yes, change it!",
        cancelButtonText: "Cancel"
    }).then(function (result) {
        if (!result.isConfirmed) {
            return;
        }

        updateCustomerStatus(userId, newStatus);
    });
}

function updateCustomerStatus(userId, newStatus) {
    const token = localStorage.getItem("jwtToken");

    if (!token) {
        Swal.fire({
            icon: "error",
            title: "Not Logged In",
            text: "Please login as administrator."
        });
        return;
    }

    $.ajax({
        url: CUSTOMER_API + "/user-status/" + userId + "?status=" + newStatus,
        type: "PUT",
        headers: {
            "Authorization": "Bearer " + token
        },
        success: function (response) {
            console.log("Customer status updated:", response);

            Swal.fire({
                icon: "success",
                title: "Status Updated",
                text: "Customer is now " + newStatus.toLowerCase() + ".",
                timer: 1500,
                showConfirmButton: false
            });

            loadRegisteredCustomers();
        },
        error: function (xhr) {
            console.error("Status update failed:", xhr);

            let errorMessage = "Unable to update customer status.";

            if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMessage = xhr.responseJSON.message;
            }

            Swal.fire({
                icon: "error",
                title: "Update Failed",
                text: errorMessage
            });
        }
    });
}

$(document).ready(function () {
    loadRegisteredCustomers();
});