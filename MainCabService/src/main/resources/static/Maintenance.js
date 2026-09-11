let currentEditingMaintenance = null;
let globalMaintenanceList = [];
let globalVehicleListForMaintenance = [];

function maintenanceAuthHeaders() {
    return { "Authorization": "Bearer " + localStorage.getItem("jwtToken") };
}

function loadVehiclesForMaintenanceDropdown() {
    $.ajax({
        url: "http://localhost:8080/v1/vehicles",
        type: "GET",
        headers: maintenanceAuthHeaders(),
        success: function (response) {
            globalVehicleListForMaintenance = response.body || [];

            const vehicleSelect = document.getElementById('maintVehicle');
            if (!vehicleSelect) return;

            vehicleSelect.innerHTML = '<option value="">Select vehicle</option>';

            globalVehicleListForMaintenance.forEach(vehicle => {
                vehicleSelect.innerHTML += `<option value="${vehicle.vehicleID}">${vehicle.vehicleName} (#${vehicle.vehicleID})</option>`;
            });
        },
        error: function (err) {
            console.error("Failed to load vehicles for maintenance dropdown", err);
        }
    });
}

function getVehicleNameById(vehicleID) {
    const vehicle = globalVehicleListForMaintenance.find(v => v.vehicleID === vehicleID);
    return vehicle ? vehicle.vehicleName : `#${vehicleID}`;
}

function loadAllMaintenanceFromBackend() {
    $.ajax({
        url: "http://localhost:8080/v1/maintenance",
        type: "GET",
        headers: maintenanceAuthHeaders(),
        success: function (response) {
            let maintenanceList = response.body || [];
            renderMaintenanceTable(maintenanceList);
        },
        error: function (err) {
            console.error("Failed to load maintenance records from backend", err);
        }
    });
}

function renderMaintenanceTable(maintenanceList) {
    globalMaintenanceList = maintenanceList || [];

    const tableBody = document.getElementById('maintenanceTableBody');
    if (!tableBody) return;

    tableBody.innerHTML = "";

    if (!maintenanceList || maintenanceList.length === 0) {
        tableBody.innerHTML = `<tr><td colspan="7" style="text-align:center; color:#aaa;">No maintenance jobs found.</td></tr>`;
        return;
    }

    maintenanceList.forEach((job, index) => {
        const vehicleName = getVehicleNameById(job.vehicleID);

        let statusColor = "#ff9f0a";
        let statusText = "Pending";

        if (job.maintenanceStatus === "IN_PROGRESS") {
            statusColor = "#0071e3";
            statusText = "In Progress";
        } else if (job.maintenanceStatus === "COMPLETED") {
            statusColor = "#28a745";
            statusText = "Completed";
        }

        const statusBadge = `<span style="background-color: ${statusColor}22; color: ${statusColor}; border: 1px solid ${statusColor}55; padding: 5px 12px; border-radius: 20px; font-size: 11px; font-weight: 700; text-transform: uppercase;">${statusText}</span>`;

        const rowHTML = `
            <tr data-id="${job.maintenanceID}">
                <td>${vehicleName}</td>
                <td>${job.title}</td>
                <td>${job.scheduledDate || ''}</td>
                <td>Rs. ${Number(job.cost).toLocaleString()}</td>
                <td>${job.vendor || 'N/A'}</td>
                <td>${statusBadge}</td>
                <td>
                    <button class="btn-action accept" onclick="editMaintenanceByIndex(${index})" title="Edit Job"><i class="ri-pencil-line"></i></button>
                    <button class="btn-action reject" onclick="deleteMaintenanceData(${job.maintenanceID})" title="Delete Job"><i class="ri-delete-bin-line"></i></button>
                </td>
            </tr>
        `;
        tableBody.insertAdjacentHTML('beforeend', rowHTML);
    });
}

function saveMaintenanceJob(event) {
    event.preventDefault();

    const maintenanceData = {
        vehicleID: $('#maintVehicle').val(),
        title: $('#maintTitle').val(),
        description: $('#maintDescription').val(),
        priority: $('#maintPriority').val(),
        maintenanceStatus: $('#maintStatus').val(),
        scheduledDate: $('#maintDate').val(),
        cost: $('#maintCost').val(),
        vendor: $('#maintVendor').val()
    };

    let ajaxType = "POST";
    if (currentEditingMaintenance && currentEditingMaintenance.maintenanceID) {
        maintenanceData.maintenanceID = currentEditingMaintenance.maintenanceID;
        ajaxType = "PUT";
    }

    $.ajax({
        url: "http://localhost:8080/v1/maintenance",
        type: ajaxType,
        contentType: "application/json",
        data: JSON.stringify(maintenanceData),
        headers: maintenanceAuthHeaders(),
        success: function (response) {
            Swal.fire({
                toast: true,
                position: 'top-end',
                icon: 'success',
                title: response.message || "Maintenance job saved successfully!",
                showConfirmButton: false,
                timer: 3000,
                timerProgressBar: true
            });

            document.getElementById('maintenanceForm').reset();
            currentEditingMaintenance = null;
            loadAllMaintenanceFromBackend();

            if (typeof loadAllVehiclesFromBackend === 'function') {
                loadAllVehiclesFromBackend();
            }
            if (typeof loadVehicleCountsFromBackend === 'function') {
                loadVehicleCountsFromBackend();
            }
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

function editMaintenanceByIndex(index) {
    const job = globalMaintenanceList[index];
    if (!job) return;

    currentEditingMaintenance = job;

    $('#maintVehicle').val(job.vehicleID);
    $('#maintTitle').val(job.title);
    $('#maintDescription').val(job.description);
    $('#maintPriority').val(job.priority);
    $('#maintStatus').val(job.maintenanceStatus);
    $('#maintDate').val(job.scheduledDate);
    $('#maintCost').val(job.cost);
    $('#maintVendor').val(job.vendor);

    document.getElementById('maintenanceForm').scrollIntoView({ behavior: 'smooth' });
}

function deleteMaintenanceData(id) {
    Swal.fire({
        title: 'Are you sure?',
        text: "This maintenance job will be permanently deleted!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#ff4d4d',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: `http://localhost:8080/v1/maintenance/${id}`,
                type: "DELETE",
                headers: maintenanceAuthHeaders(),
                success: function (response) {
                    Swal.fire({
                        toast: true,
                        position: 'top-end',
                        icon: 'success',
                        title: response.message || "Maintenance job deleted successfully!",
                        showConfirmButton: false,
                        timer: 3000,
                        timerProgressBar: true
                    });
                    loadAllMaintenanceFromBackend();

                    if (typeof loadAllVehiclesFromBackend === 'function') {
                        loadAllVehiclesFromBackend();
                    }
                    if (typeof loadVehicleCountsFromBackend === 'function') {
                        loadVehicleCountsFromBackend();
                    }
                },
                error: function (xhr) {
                    Swal.fire({
                        icon: 'error',
                        title: 'Delete Failed',
                        text: xhr.responseJSON?.message || "Unable to delete maintenance job.",
                        confirmButtonColor: '#ff4d4d'
                    });
                    console.error(xhr);
                }
            });
        }
    });
}

function filterMaintJobs(status, buttonElement) {
    $('.filter-btn').removeClass('active');
    if (buttonElement) $(buttonElement).addClass('active');

    if (!status || status.toLowerCase() === 'all') {
        loadAllMaintenanceFromBackend();
        return;
    }

    $.ajax({
        url: `http://localhost:8080/v1/maintenance/status/${status}`,
        type: "GET",
        headers: maintenanceAuthHeaders(),
        success: function (response) {
            let maintenanceList = response.body || [];
            renderMaintenanceTable(maintenanceList);
        },
        error: function (err) {
            console.error("Failed to filter maintenance jobs by status", err);
        }
    });
}

function clearMaintenanceForm() {
    document.getElementById('maintenanceForm').reset();
    currentEditingMaintenance = null;
}

$(document).ready(function () {
    loadVehiclesForMaintenanceDropdown();
    loadAllMaintenanceFromBackend();
});