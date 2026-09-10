const BOOKING_CUSTOMER_API_URL = "http://localhost:8080/v1/bookingCustomers";

let globalBookingCustomerList = [];

function bookingCustomerAuthHeaders() {
    return { "Authorization": "Bearer " + localStorage.getItem("jwtToken") };
}

function loadAllBookingCustomersFromBackend() {
    $.ajax({
        url: BOOKING_CUSTOMER_API_URL,
        type: "GET",
        headers: bookingCustomerAuthHeaders(),
        success: function (response) {
            let customerList = response.body || [];
            renderBookingCustomerTable(customerList);
        },
        error: function (err) {
            console.error("Failed to load booking customers from backend", err);
        }
    });
}

function renderBookingCustomerTable(customerList) {
    globalBookingCustomerList = customerList || [];

    const tableBody = document.getElementById('bookingCustTableBody');
    if (!tableBody) return;

    tableBody.innerHTML = "";

    if (!customerList || customerList.length === 0) {
        tableBody.innerHTML = `<tr><td colspan="7" style="text-align:center; color:#aaa;">No booking customers found.</td></tr>`;
        return;
    }

    customerList.forEach((customer) => {
        const rowHTML = `
            <tr class="booking-cust-row" data-id="${customer.bookingCustomerID}">
                <td><span class="cust-ref">#BC-${customer.bookingCustomerID}</span></td>
                <td class="date-cell">${customer.bookingCustomerRegisterDate || ''}</td>
                <td><span class="c-name">${customer.bookingCustomerName}</span></td>
                <td>${customer.bookingCustomerEmail}</td>
                <td>${customer.bookingCustomerNumber}</td>
                <td>${customer.bookingCustomerLicenseNumber}</td>
                <td>
                    <button class="btn-action reject" onclick="deleteBookingCustomerData(${customer.bookingCustomerID})" title="Delete Customer">
                        <i class="ri-delete-bin-line"></i>
                    </button>
                </td>
            </tr>
        `;
        tableBody.insertAdjacentHTML('beforeend', rowHTML);
    });
}

function deleteBookingCustomerData(id) {
    Swal.fire({
        title: 'Are you sure?',
        text: "This booking customer record will be permanently deleted!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#ff4d4d',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: `${BOOKING_CUSTOMER_API_URL}/${id}`,
                type: "DELETE",
                headers: bookingCustomerAuthHeaders(),
                success: function (response) {
                    Swal.fire({
                        toast: true,
                        position: 'top-end',
                        icon: 'success',
                        title: response.message || "Booking customer deleted successfully!",
                        showConfirmButton: false,
                        timer: 3000,
                        timerProgressBar: true
                    });
                    loadAllBookingCustomersFromBackend();
                },
                error: function (xhr) {

                    let errorMessage = "Unable to delete booking customer.";

                    if (xhr.status === 403) {
                        errorMessage = "You don't have permission to delete this booking customer.";
                    } else if (xhr.status === 500) {
                        errorMessage = "Cannot delete this booking customer because booking records are linked to this customer.";
                    }

                    Swal.fire({
                        icon: "error",
                        title: "Delete Failed",
                        text: errorMessage
                    });

                    console.error("Delete booking customer failed:", xhr);
                }
            });
        }
    });
}

function searchBookingCustomers() {
    let keyword = $('#bookingCustSearch').val().trim();

    if (keyword.length === 0) {
        loadAllBookingCustomersFromBackend();
        return;
    }

    $.ajax({
        url: `${BOOKING_CUSTOMER_API_URL}/search?keyword=${encodeURIComponent(keyword)}`,
        type: "GET",
        headers: bookingCustomerAuthHeaders(),
        success: function (response) {
            renderBookingCustomerTable(response.body || []);
        },
        error: function (err) {
            console.error("Search failed", err);
        }
    });
}

$(document).ready(function () {
    loadAllBookingCustomersFromBackend();
});