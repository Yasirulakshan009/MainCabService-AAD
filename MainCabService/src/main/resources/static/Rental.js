const RENTAL_API_URL = "http://localhost:8080/v1/rentals";
const RENTAL_CUSTOMER_API_URL = "http://localhost:8080/v1/customers";
const VEHICLE_API_URL = "http://localhost:8080/v1/vehicles";

let rentalList = [];
let currentEditingRental = null;
let rentalCustomerList = [];
let rentalVehicleList = [];

function rentalAuthHeaders() {
    return {
        "Authorization": "Bearer " + localStorage.getItem("jwtToken")
    };
}

function loadRentalCustomers() {
    $.ajax({
        url: RENTAL_CUSTOMER_API_URL,
        type: "GET",
        headers: rentalAuthHeaders(),
        success: function (response) {
            rentalCustomerList = response.body || [];
            const select = $("#rentalCustomer");
            select.empty();
            select.append(`
                <option value="" disabled selected>
                    Select Customer
                </option>
            `);
            rentalCustomerList.forEach(function (customer) {
                select.append(`
                    <option value="${customer.customerID}">
                        ${customer.customerName}
                        (#CUST-${customer.customerID})
                    </option>
                `);
            });
        },
        error: function (xhr) {
            console.error("Failed to load rental customers:", xhr);
        }
    });
}

function loadRentalVehicles(selectedVehicleId = null) {
    $.ajax({
        url: VEHICLE_API_URL,
        type: "GET",
        headers: rentalAuthHeaders(),
        success: function (response) {
            rentalVehicleList = response.body || [];
            const select = $("#rentalVehicle");
            select.empty();
            select.append(`
                <option value="" disabled selected>
                    Select Vehicle
                </option>
            `);
            rentalVehicleList.forEach(function (vehicle) {
                const status = String(vehicle.status).toUpperCase();
                const isCurrentVehicle =
                    selectedVehicleId &&
                    Number(vehicle.vehicleID) === Number(selectedVehicleId);

                if (status === "AVAILABLE" || isCurrentVehicle) {
                    select.append(`
                        <option value="${vehicle.vehicleID}"
                                data-price="${vehicle.dailyPrice}"
                                ${isCurrentVehicle ? "selected" : ""}>
                            ${vehicle.vehicleName}
                            (#V-${vehicle.vehicleID})
                            - LKR ${Number(vehicle.dailyPrice).toLocaleString()}/day
                        </option>
                    `);
                }
            });
        },
        error: function (xhr) {
            console.error("Failed to load rental vehicles:", xhr);
        }
    });
}

$("#rentalVehicle").on("change", function () {
    calculateRentalTotal();
});

$("#rentalStartDate, #rentalEndDate, #rentalDeliveryFee").on("change input", function () {
    calculateRentalTotal();
});

function calculateRentalTotal() {
    const vehicleId = $("#rentalVehicle").val();
    const startDate = $("#rentalStartDate").val();
    const endDate = $("#rentalEndDate").val();
    const deliveryFee = Number($("#rentalDeliveryFee").val()) || 0;

    if (!vehicleId || !startDate || !endDate) {
        $("#rentalTotalAmount").val("");
        return;
    }

    const selectedVehicle = rentalVehicleList.find(function (vehicle) {
        return Number(vehicle.vehicleID) === Number(vehicleId);
    });

    if (!selectedVehicle) {
        return;
    }

    const start = new Date(startDate);
    const end = new Date(endDate);
    const difference = (end - start) / (1000 * 60 * 60 * 24);

    if (difference < 0) {
        $("#rentalTotalAmount").val("");
        return;
    }

    const days = difference <= 0 ? 1 : difference;
    const dailyRate = Number(selectedVehicle.dailyPrice) || 0;
    const total = (days * dailyRate) + deliveryFee;

    $("#rentalTotalAmount").val(total.toFixed(2));
}

function toggleRentalForm() {
    const modal = $("#rentalModal");
    modal.toggleClass("active");

    if (modal.hasClass("active")) {
        $("#addRentalForm")[0].reset();
        $("#rentalDisplayId").val("");
        currentEditingRental = null;

        const title = document.querySelector("#rentalModal .modal-header h3");
        if (title) {
            title.innerText = "Add New Rental Agreement";
        }

        loadRentalCustomers();
        loadRentalVehicles();
    } else {
        $("#addRentalForm")[0].reset();
        $("#rentalDisplayId").val("");
        currentEditingRental = null;

        const title = document.querySelector("#rentalModal .modal-header h3");
        if (title) {
            title.innerText = "Add New Rental Agreement";
        }
        loadRentalVehicles();
    }
}

function saveRental(event) {
    event.preventDefault();

    const rentalData = {
        rentalID: currentEditingRental ? currentEditingRental.rentalID : 0,
        startDate: $("#rentalStartDate").val(),
        endDate: $("#rentalEndDate").val(),
        pickupAddress: $("#rentalPickupAddress").val(),
        deliveryFee: Number($("#rentalDeliveryFee").val()) || 0,
        totalAmount: Number($("#rentalTotalAmount").val()) || 0,
        paymentMethod: $("#rentalPaymentMethod").val(),
        rentalStatus: $("#rentalStatus").val(),
        customerID: Number($("#rentalCustomer").val()),
        vehicleID: Number($("#rentalVehicle").val())
    };

    const isUpdate = currentEditingRental && currentEditingRental.rentalID;

    $.ajax({
        url: RENTAL_API_URL,
        type: isUpdate ? "PUT" : "POST",
        contentType: "application/json",
        data: JSON.stringify(rentalData),
        headers: rentalAuthHeaders(),
        success: function (response) {
            Swal.fire({
                toast: true,
                position: "top-end",
                icon: "success",
                title: response.message || (isUpdate ? "Rental updated successfully!" : "Rental saved successfully!"),
                showConfirmButton: false,
                timer: 2500
            });

            toggleRentalForm();
            loadAllRentalsFromBackend();
            loadRentalVehicles();

            if (typeof loadAllVehiclesFromBackend === "function") {
                loadAllVehiclesFromBackend();
            }

            if (typeof loadVehicleCountsFromBackend === "function") {
                loadVehicleCountsFromBackend();
            }
        },
        error: function (xhr) {
            console.error("Rental save error:", xhr);
            let message = "Unable to save rental.";
            if (xhr.responseJSON) {
                message = xhr.responseJSON.message || message;
            }
            Swal.fire({
                icon: "error",
                title: "Rental Failed",
                text: message
            });
        }
    });
}

function loadAllRentalsFromBackend() {
    $.ajax({
        url: RENTAL_API_URL,
        type: "GET",
        headers: rentalAuthHeaders(),
        success: function (response) {
            rentalList = response.body || [];
            renderRentalTable(rentalList);
            updateRentalCounts();
        },
        error: function (xhr) {
            console.error("Failed to load rentals:", xhr);
        }
    });
}

function renderRentalTable(list) {
    const tbody = $("#rentalTableBody");
    tbody.empty();

    if (!list || list.length === 0) {
        tbody.html(`
            <tr>
                <td colspan="10" style="text-align:center;">
                    No rental records found.
                </td>
            </tr>
        `);
        return;
    }

    list.forEach(function (rental) {
        const status = String(rental.rentalStatus || "").toUpperCase();

        const cssStatusClass = (status === "COMPLETED") ? "COMPLETE" : status;

        tbody.append(`
        <tr>
            <td>#RNT-${rental.rentalID}</td>
            <td>#CUST-${rental.customerID}</td>
            <td>#V-${rental.vehicleID}</td>
            <td>${rental.startDate || ""}</td>
            <td>${rental.endDate || ""}</td>
            <td>${rental.pickupAddress || "-"}</td>
            <td>LKR ${Number(rental.deliveryFee || 0).toLocaleString()}</td>
            <td>LKR ${Number(rental.totalAmount || 0).toLocaleString()}</td>
            <td>
                <span class="status-badge ${cssStatusClass}">
                    ${status}
                </span>
            </td>
            <td>
                <button class="btn-action accept" onclick="editRental(${rental.rentalID})" title="Edit">
                    <i class="ri-edit-line"></i>
                </button>
                <button class="btn-action reject" onclick="deleteRental(${rental.rentalID})" title="Delete">
                    <i class="ri-delete-bin-line"></i>
                </button>
            </td>
        </tr>
    `);
    });
}

function editRental(id) {
    $.ajax({
        url: `${RENTAL_API_URL}/${id}`,
        type: "GET",
        headers: rentalAuthHeaders(),
        success: function (response) {
            const rental = response.body;
            if (!rental) return;

            currentEditingRental = rental;
            const modal = $("#rentalModal");
            modal.addClass("active");

            const title = document.querySelector("#rentalModal .modal-header h3");
            if (title) {
                title.innerText = "Edit Rental Agreement";
            }

            $("#rentalDisplayId").val("RNT-" + rental.rentalID);
            loadRentalCustomers();
            loadRentalVehicles(rental.vehicleID);

            setTimeout(function () {
                $("#rentalCustomer").val(rental.customerID);
                $("#rentalVehicle").val(rental.vehicleID);
            }, 300);

            $("#rentalStartDate").val(rental.startDate);
            $("#rentalEndDate").val(rental.endDate);
            $("#rentalDeliveryFee").val(rental.deliveryFee);
            $("#rentalTotalAmount").val(rental.totalAmount);
            $("#rentalPaymentMethod").val(rental.paymentMethod);
            $("#rentalStatus").val(rental.rentalStatus);
            $("#rentalPickupAddress").val(rental.pickupAddress);
        },
        error: function (xhr) {
            console.error("Failed to get rental:", xhr);
        }
    });
}

function deleteRental(id) {
    Swal.fire({
        title: "Delete Rental?",
        text: "This rental will be permanently deleted.",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Yes, delete it!",
        cancelButtonText: "Cancel"
    }).then(function (result) {
        if (!result.isConfirmed) return;

        $.ajax({
            url: `${RENTAL_API_URL}/${id}`,
            type: "DELETE",
            headers: rentalAuthHeaders(),
            success: function (response) {
                Swal.fire({
                    toast: true,
                    position: "top-end",
                    icon: "success",
                    title: response.message || "Rental deleted successfully!",
                    showConfirmButton: false,
                    timer: 2500
                });

                loadAllRentalsFromBackend();
                loadRentalVehicles();

                if (typeof loadAllVehiclesFromBackend === "function") {
                    loadAllVehiclesFromBackend();
                }

                if (typeof loadVehicleCountsFromBackend === "function") {
                    loadVehicleCountsFromBackend();
                }
            },
            error: function (xhr) {
                console.error("Delete rental error:", xhr);
                Swal.fire({
                    icon: "error",
                    title: "Delete Failed",
                    text: "Unable to delete rental."
                });
            }
        });
    });
}

function searchRentals() {
    const keyword = $("#rentalSearch").val().trim();

    if (!keyword) {
        loadAllRentalsFromBackend();
        return;
    }

    $.ajax({
        url: `${RENTAL_API_URL}/search?keyword=${encodeURIComponent(keyword)}`,
        type: "GET",
        headers: rentalAuthHeaders(),
        success: function (response) {
            rentalList = response.body || [];
            renderRentalTable(rentalList);
        },
        error: function (xhr) {
            console.error("Rental search error:", xhr);
        }
    });
}

function filterRentals(status, button) {
    $(".filter-btn").removeClass("active");

    if (button) {
        $(button).addClass("active");
    }

    if (status === "all") {
        loadAllRentalsFromBackend();
        return;
    }

    $.ajax({
        url: `${RENTAL_API_URL}/status/${status.toUpperCase()}`,
        type: "GET",
        headers: rentalAuthHeaders(),
        success: function (response) {
            const list = response.body || [];
            renderRentalTable(list);
        },
        error: function (xhr) {
            console.error("Rental filter error:", xhr);
        }
    });
}

function updateRentalCounts() {
    $.ajax({
        url: `${RENTAL_API_URL}/count/status/ACTIVE`,
        type: "GET",
        headers: rentalAuthHeaders(),
        success: function (response) {
            $("#activeRentalCount").text(response.body || 0);
        }
    });

    $.ajax({
        url: `${RENTAL_API_URL}/count/status/COMPLETED`,
        type: "GET",
        headers: rentalAuthHeaders(),
        success: function (response) {
            $("#completedRentalCount").text(response.body || 0);
        }
    });

    $.ajax({
        url: `${RENTAL_API_URL}/count/status/CANCELLED`,
        type: "GET",
        headers: rentalAuthHeaders(),
        success: function (response) {
            $("#cancelledRentalCount").text(response.body || 0);
        }
    });

    $.ajax({
        url: `${RENTAL_API_URL}/count`,
        type: "GET",
        headers: rentalAuthHeaders(),
        success: function (response) {
            $("#totalRentalCount").text(response.body || 0);
        }
    });
}

$(document).ready(function () {
    loadAllRentalsFromBackend();
    loadRentalCustomers();
    loadRentalVehicles();
});