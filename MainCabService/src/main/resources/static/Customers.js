const CUSTOMER_API_URL = "http://localhost:8080/v1/customers";

let currentEditingCustomer = null;
let globalCustomerList = [];

function customerAuthHeaders() {
    return { "Authorization": "Bearer " + localStorage.getItem("jwtToken") };
}

function toggleCustomerForm() {
    const modal = document.getElementById('addCustomerModal');
    if (!modal) return;

    modal.classList.toggle('active');

    if (!modal.classList.contains('active')) {
        const form = document.getElementById('addCustomerForm');
        if (form) form.reset();

        const titleEl = modal.querySelector('h3');
        if (titleEl) titleEl.innerText = "Add New Customer";

        currentEditingCustomer = null;
    }
}

function loadAllCustomersFromBackend() {
    $.ajax({
        url: CUSTOMER_API_URL,
        type: "GET",
        headers: customerAuthHeaders(),
        success: function (response) {
            let customerList = response.body || [];
            renderCustomerTable(customerList);
        },
        error: function (err) {
            console.error("Failed to load customers from backend", err);
        }
    });
}

function renderCustomerTable(customerList) {
    globalCustomerList = customerList || [];

    const tableBody = document.getElementById('normalCustTableBody');
    if (!tableBody) return;

    tableBody.innerHTML = "";

    if (!customerList || customerList.length === 0) {
        tableBody.innerHTML = `<tr><td colspan="8" style="text-align:center; color:#aaa;">No customers found.</td></tr>`;
        return;
    }

    customerList.forEach((customer, index) => {
        const rowHTML = `
            <tr class="normal-cust-row" data-id="${customer.customerID}">
                <td><span class="cust-ref">#CUST-${customer.customerID}</span></td>
                <td class="date-cell">${customer.customerRegisterDate || ''}</td>
                <td><span class="c-name">${customer.customerName}</span></td>
                <td>${customer.customerNIC}</td>
                <td>${customer.customerLicenseNumber}</td>
                <td>${customer.customerNumber}</td>
                <td>${customer.customerAddress}</td>
                <td>
                    <button class="btn-action accept" onclick="editCustomerByIndex(${index})" title="Edit Customer"><i class="ri-edit-line"></i></button>
                    <button class="btn-action reject" onclick="deleteCustomerData(${customer.customerID})" title="Delete Customer"><i class="ri-delete-bin-line"></i></button>
                </td>
            </tr>
        `;
        tableBody.insertAdjacentHTML('beforeend', rowHTML);
    });
}

function saveCustomer(event) {
    event.preventDefault();

    const customerData = {
        customerName: $('#newCustName').val(),
        customerNIC: $('#newCustNic').val(),
        customerLicenseNumber: $('#newCustLicense').val(),
        customerNumber: $('#newCustPhone').val(),
        customerAddress: $('#newCustAddress').val(),
        customerRegisterDate: currentEditingCustomer
            ? currentEditingCustomer.customerRegisterDate
            : new Date().toISOString().split('T')[0]
    };

    let ajaxType = "POST";
    if (currentEditingCustomer && currentEditingCustomer.customerID) {
        customerData.customerID = currentEditingCustomer.customerID;
        ajaxType = "PUT";
    }

    $.ajax({
        url: CUSTOMER_API_URL,
        type: ajaxType,
        contentType: "application/json",
        data: JSON.stringify(customerData),
        headers: customerAuthHeaders(),
        success: function (response) {
            Swal.fire({
                toast: true,
                position: 'top-end',
                icon: 'success',
                title: response.message || "Customer saved successfully!",
                showConfirmButton: false,
                timer: 3000,
                timerProgressBar: true
            });
            toggleCustomerForm();
            loadAllCustomersFromBackend();
        },
        error: function (xhr) {
            var errObj = xhr.responseJSON;
            var errorText = "Something went wrong";

            if (errObj) {
                if (errObj.body && typeof errObj.body === "object" && !Array.isArray(errObj.body)) {
                    errorText = Object.values(errObj.body).join("\n");
                } else {
                    errorText = errObj.message;
                }
            }

            Swal.fire({
                icon: 'error',
                title: 'Save Failed',
                text: errorText,
                confirmButtonColor: '#ff4d4d'
            });
            console.error("Save error: ", xhr);
        }
    });
}

function editCustomerByIndex(index) {
    const customer = globalCustomerList[index];
    if (!customer) return;

    currentEditingCustomer = customer;
    toggleCustomerForm();

    const modal = document.getElementById('addCustomerModal');
    if (modal) {
        const titleEl = modal.querySelector('h3');
        if (titleEl) titleEl.innerText = "Edit Customer";
    }

    $('#customerDisplayId').val("CUST-" + customer.customerID);
    $('#newCustName').val(customer.customerName);
    $('#newCustNic').val(customer.customerNIC);
    $('#newCustLicense').val(customer.customerLicenseNumber);
    $('#newCustPhone').val(customer.customerNumber);
    $('#newCustAddress').val(customer.customerAddress);
}

function deleteCustomerData(id) {
    Swal.fire({
        title: 'Are you sure?',
        text: "This customer will be permanently deleted!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#ff4d4d',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: `${CUSTOMER_API_URL}/${id}`,
                type: "DELETE",
                headers: customerAuthHeaders(),
                success: function (response) {
                    Swal.fire({
                        toast: true,
                        position: 'top-end',
                        icon: 'success',
                        title: response.message || "Customer deleted successfully!",
                        showConfirmButton: false,
                        timer: 3000,
                        timerProgressBar: true
                    });
                    loadAllCustomersFromBackend();
                },
                error: function (xhr) {

                    let errorMessage = "Unable to delete customer.";

                    if (xhr.status === 403) {
                        errorMessage = "You don't have permission to delete this customer.";
                    } else if (xhr.status === 500) {
                        errorMessage = "Cannot delete this customer because related records are linked to this customer.";
                    }

                    Swal.fire({
                        icon: 'error',
                        title: 'Delete Failed',
                        text: errorMessage,
                        confirmButtonColor: '#ff4d4d'
                    });

                    console.error(xhr);
                }
            });
        }
    });
}

function searchNormalCustomers() {
    let keyword = $('#normalCustSearch').val().trim();

    if (keyword.length === 0) {
        loadAllCustomersFromBackend();
        return;
    }

    $.ajax({
        url: `${CUSTOMER_API_URL}/search?keyword=${encodeURIComponent(keyword)}`,
        type: "GET",
        headers: customerAuthHeaders(),
        success: function (response) {
            renderCustomerTable(response.body || []);
        },
        error: function (err) {
            console.error("Search failed", err);
        }
    });
}

$(document).ready(function () {
    loadAllCustomersFromBackend();
});