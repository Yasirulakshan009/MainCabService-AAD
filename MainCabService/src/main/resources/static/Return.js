const RETURN_API_URL = "http://localhost:8080/v1/returns";
const RETURN_RENTAL_API_URL = "http://localhost:8080/v1/rentals";

let returnList = [];
let currentEditingReturn = null;
let returnRentalList = [];

function returnAuthHeaders() {
    return {
        "Authorization": "Bearer " + localStorage.getItem("jwtToken")
    };
}

function loadReturnRentals(selectedRentalId = null) {
    $.ajax({
        url: RETURN_RENTAL_API_URL + "/status/ACTIVE",
        type: "GET",
        headers: returnAuthHeaders(),
        success: function (response) {
            returnRentalList = response.body || [];
            const select = $("#returnRentalId");
            select.empty();
            select.append(`
                <option value="" disabled selected>
                    Select Rental Agreement
                </option>
            `);
            returnRentalList.forEach(function (rental) {
                const isCurrentRental =
                    selectedRentalId &&
                    Number(rental.rentalID) === Number(selectedRentalId);

                if (isCurrentRental || String(rental.rentalStatus).toUpperCase() === "ACTIVE") {
                    select.append(`
                        <option value="${rental.rentalID}"
                                ${isCurrentRental ? "selected" : ""}>
                            #RNT-${rental.rentalID}
                            - Customer #CUST-${rental.customerID}
                            - Vehicle #V-${rental.vehicleID}
                            - LKR ${Number(rental.totalAmount || 0).toLocaleString()}
                        </option>
                    `);
                }
            });
        },
        error: function (xhr) {
            console.error("Failed to load active rentals:", xhr);
            Swal.fire({
                icon: "error",
                title: "Unable to load rentals",
                text: "Active rental records could not be loaded."
            });
        }
    });
}

$("#returnRentalId").on("change", function () {
    const rentalId = $(this).val();
    if (!rentalId) {
        return;
    }
    loadRentalForReturn(rentalId);
});

function loadRentalForReturn(rentalId) {
    $.ajax({
        url: `${RETURN_RENTAL_API_URL}/${rentalId}`,
        type: "GET",
        headers: returnAuthHeaders(),
        success: function (response) {
            const rental = response.body;
            if (!rental) {
                return;
            }
            $("#returnInitialDate").val(rental.endDate || "");
            const today = new Date().toISOString().split("T")[0];
            $("#returnDate").val(today);
            if (!$("#returnExtraCharges").val()) {
                $("#returnExtraCharges").val(0);
            }
            calculateReturnFinalAmount();
        },
        error: function (xhr) {
            console.error("Failed to load rental:", xhr);
            Swal.fire({
                icon: "error",
                title: "Rental Loading Failed",
                text: "Could not load selected rental."
            });
        }
    });
}

$("#returnExtraCharges").on("input change", function () {
    calculateReturnFinalAmount();
});

function calculateReturnFinalAmount() {
    const rentalId = $("#returnRentalId").val();
    if (!rentalId) {
        $("#returnFinalAmount").val("");
        return;
    }

    $.ajax({
        url: `${RETURN_RENTAL_API_URL}/${rentalId}`,
        type: "GET",
        headers: returnAuthHeaders(),
        success: function (response) {
            const rental = response.body;
            if (!rental) {
                return;
            }
            const rentalAmount = Number(rental.totalAmount || 0);
            const extraCharges = Number($("#returnExtraCharges").val()) || 0;
            const finalAmount = rentalAmount + extraCharges;
            $("#returnFinalAmount").val(finalAmount.toFixed(2));
        },
        error: function (xhr) {
            console.error("Failed to calculate return amount:", xhr);
        }
    });
}

function toggleReturnForm() {
    const modal = $("#returnModal");
    if (modal.hasClass("active")) {
        closeReturnForm();
    } else {
        openReturnForm();
    }
}

function openReturnForm() {
    const form = document.getElementById("addReturnForm");
    if (!form) {
        return;
    }

    form.reset();
    currentEditingReturn = null;
    $("#returnDisplayId").val("");
    $("#returnDate").val(new Date().toISOString().split("T")[0]);
    $("#returnInitialDate").val("");
    $("#returnExtraCharges").val("0");
    $("#returnFinalAmount").val("");
    $("#returnNotes").val("");
    $("#returnRentalId").val("");
    $("#returnPaymentMethod").val("CASH");
    $("#returnStatus").val("COMPLETED");
    $("#returnModalTitle").text("Process Vehicle Return");
    $("#returnModal").addClass("active");

    loadReturnRentals();
}

function closeReturnForm() {
    $("#returnModal").removeClass("active");

    const form = document.getElementById("addReturnForm");
    if (form) {
        form.reset();
    }

    currentEditingReturn = null;
    $("#returnDisplayId").val("");
    $("#returnFinalAmount").val("");
    $("#returnInitialDate").val("");
    $("#returnExtraCharges").val("");
    $("#returnNotes").val("");
    $("#returnRentalId").val("");
    $("#returnModalTitle").text("Process Vehicle Return");
}

function saveReturn(event) {
    event.preventDefault();

    const returnData = {
        returnID: currentEditingReturn ? currentEditingReturn.returnID : 0,
        rentalID: Number($("#returnRentalId").val()),
        returnDate: $("#returnDate").val(),
        initialReturnDate: $("#returnInitialDate").val(),
        extraCharges: Number($("#returnExtraCharges").val()) || 0,
        finalAmount: Number($("#returnFinalAmount").val()) || 0,
        paymentMethod: $("#returnPaymentMethod").val(),
        returnStatus: $("#returnStatus").val(),
        notes: $("#returnNotes").val()
    };

    const isUpdate = currentEditingReturn && currentEditingReturn.returnID;

    $.ajax({
        url: RETURN_API_URL,
        type: isUpdate ? "PUT" : "POST",
        contentType: "application/json",
        data: JSON.stringify(returnData),
        headers: returnAuthHeaders(),
        success: function (response) {
            Swal.fire({
                toast: true,
                position: "top-end",
                icon: "success",
                title: response.message || "Return saved successfully!",
                showConfirmButton: false,
                timer: 3000
            });

            toggleReturnForm();
            loadAllReturnsFromBackend();

            if (typeof loadAllRentalsFromBackend === "function") {
                loadAllRentalsFromBackend();
            }
            if (typeof loadRentalVehicles === "function") {
                loadRentalVehicles();
            }
            if (typeof loadAllVehiclesFromBackend === "function") {
                loadAllVehiclesFromBackend();
            }
            if (typeof loadVehicleCountsFromBackend === "function") {
                loadVehicleCountsFromBackend();
            }
            if (typeof loadAllPaymentsFromBackend === "function") {
                loadAllPaymentsFromBackend();
            }
            if (typeof loadRevenueStatsFromBackend === "function") {
                loadRevenueStatsFromBackend();
            }
            if (typeof loadCompletedPaymentCountFromBackend === "function") {
                loadCompletedPaymentCountFromBackend();
            }
        },
        error: function (xhr) {
            const errObj = xhr.responseJSON;
            let errorText = "Something went wrong";

            if (errObj) {
                if (errObj.body && typeof errObj.body === "object" && !Array.isArray(errObj.body)) {
                    errorText = Object.values(errObj.body).join("\n");
                } else {
                    errorText = errObj.message || errorText;
                }
            }

            Swal.fire({
                icon: "error",
                title: "Save Failed",
                text: errorText,
                confirmButtonColor: "#ff4d4d"
            });

            console.error("Return save error:", xhr);
        }
    });
}

function loadAllReturnsFromBackend() {
    $.ajax({
        url: RETURN_API_URL,
        type: "GET",
        headers: returnAuthHeaders(),
        success: function (response) {
            returnList = response.body || [];
            renderReturnTable(returnList);
            updateReturnCounts();
        },
        error: function (xhr) {
            console.error("Failed to load returns:", xhr);
        }
    });
}

function renderReturnTable(list) {
    const tbody = $("#returnTableBody");
    if (!tbody.length) {
        return;
    }
    tbody.empty();

    if (!list || list.length === 0) {
        tbody.html(`
            <tr>
                <td colspan="9" style="text-align:center;">
                    No return records found.
                </td>
            </tr>
        `);
        return;
    }

    list.forEach(function (item) {
        const status = String(item.returnStatus || "").toUpperCase();
        tbody.append(`
            <tr>
                <td>#RET-${item.returnID}</td>
                <td>#RNT-${item.rentalID}</td>
                <td>${item.returnDate || "-"}</td>
                <td>${item.initialReturnDate || "-"}</td>
                <td>LKR ${Number(item.extraCharges || 0).toLocaleString()}</td>
                <td>LKR ${Number(item.finalAmount || 0).toLocaleString()}</td>
                <td>${item.paymentMethod || "-"}</td>
                <td>
                    <span class="status-badge ${status}">
                        ${status.replace("_", " ")}
                    </span>
                </td>
                <td>
                    <button class="btn-action accept" onclick="editReturn(${item.returnID})" title="Edit">
                        <i class="ri-edit-line"></i>
                    </button>
                    <button class="btn-action reject" onclick="deleteReturn(${item.returnID})" title="Delete">
                        <i class="ri-delete-bin-line"></i>
                    </button>
                </td>
            </tr>
        `);
    });
}

function editReturn(id) {
    $.ajax({
        url: `${RETURN_API_URL}/${id}`,
        type: "GET",
        headers: returnAuthHeaders(),
        success: function (response) {
            const returnData = response.body;
            if (!returnData) {
                return;
            }

            currentEditingReturn = returnData;
            $("#returnModal").addClass("active");
            $("#returnModalTitle").text("Edit Vehicle Return");
            $("#returnDisplayId").val("RET-" + returnData.returnID);
            loadReturnRentals(returnData.rentalID);
            $("#returnDate").val(returnData.returnDate || "");
            $("#returnInitialDate").val(returnData.initialReturnDate || "");
            $("#returnExtraCharges").val(returnData.extraCharges ?? 0);
            $("#returnFinalAmount").val(returnData.finalAmount ?? "");
            $("#returnPaymentMethod").val(returnData.paymentMethod || "");
            $("#returnStatus").val(returnData.returnStatus || "");
            $("#returnNotes").val(returnData.notes || "");
        },
        error: function (xhr) {
            console.error("Failed to load return:", xhr);
            Swal.fire({
                icon: "error",
                title: "Error",
                text: "Unable to load return record."
            });
        }
    });
}

function deleteReturn(id) {
    Swal.fire({
        title: "Delete Return?",
        text: "This return record will be permanently deleted.",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Yes, delete it!",
        cancelButtonText: "Cancel"
    }).then(function (result) {
        if (!result.isConfirmed) {
            return;
        }

        $.ajax({
            url: `${RETURN_API_URL}/${id}`,
            type: "DELETE",
            headers: returnAuthHeaders(),
            success: function (response) {
                Swal.fire({
                    toast: true,
                    position: "top-end",
                    icon: "success",
                    title: response.message || "Return deleted successfully!",
                    showConfirmButton: false,
                    timer: 2500
                });

                loadAllReturnsFromBackend();

                if (typeof loadAllVehiclesFromBackend === "function") {
                    loadAllVehiclesFromBackend();
                }
                if (typeof loadAllRentalsFromBackend === "function") {
                    loadAllRentalsFromBackend();
                }
                if (typeof loadRentalVehicles === "function") {
                    loadRentalVehicles();
                }
                if (typeof loadAllPaymentsFromBackend === "function") {
                    loadAllPaymentsFromBackend();
                }
                if (typeof loadRevenueStatsFromBackend === "function") {
                    loadRevenueStatsFromBackend();
                }
                if (typeof loadCompletedPaymentCountFromBackend === "function") {
                    loadCompletedPaymentCountFromBackend();
                }
            },
            error: function (xhr) {
                console.error("Delete return error:", xhr);
                Swal.fire({
                    icon: "error",
                    title: "Delete Failed",
                    text: xhr.responseJSON?.message || "Unable to delete return."
                });
            }
        });
    });
}

function updateReturnCounts() {
    $.ajax({
        url: `${RETURN_API_URL}/count/status/COMPLETED`,
        type: "GET",
        headers: returnAuthHeaders(),
        success: function (response) {
            $("#completedReturnCount").text(response.body || 0);
        }
    });

    $.ajax({
        url: `${RETURN_API_URL}/count/status/OVERDUE_RETURNED`,
        type: "GET",
        headers: returnAuthHeaders(),
        success: function (response) {
            $("#overdueReturnCount").text(response.body || 0);
        }
    });

    $.ajax({
        url: `${RETURN_API_URL}/count`,
        type: "GET",
        headers: returnAuthHeaders(),
        success: function (response) {
            $("#totalReturnCount").text(response.body || 0);
        }
    });
}

function searchReturns() {
    const keyword = $("#returnSearch").val().trim();

    if (!keyword) {
        loadAllReturnsFromBackend();
        return;
    }

    $.ajax({
        url: `${RETURN_API_URL}/search?keyword=${encodeURIComponent(keyword)}`,
        type: "GET",
        headers: returnAuthHeaders(),
        success: function (response) {
            returnList = response.body || [];
            renderReturnTable(returnList);
        },
        error: function (xhr) {
            console.error("Return search error:", xhr);
        }
    });
}

function filterReturns(status, button) {
    $("#returns-section .filter-btn").removeClass("active");

    if (button) {
        $(button).addClass("active");
    }

    if (!status || status === "all") {
        loadAllReturnsFromBackend();
        return;
    }

    const statusEnum = status.toUpperCase().replace(/-/g, "_");

    $.ajax({
        url: `${RETURN_API_URL}/status/${statusEnum}`,
        type: "GET",
        headers: returnAuthHeaders(),
        success: function (response) {
            const list = response.body || [];
            renderReturnTable(list);
        },
        error: function (xhr) {
            console.error("Return filter error:", xhr);
        }
    });
}

$(document).ready(function () {
    loadAllReturnsFromBackend();
});