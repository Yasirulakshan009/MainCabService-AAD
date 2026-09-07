function toggleForm() {
    const modal = document.getElementById('vehicleModal');
    if (!modal) return;

    modal.classList.toggle('active');

    if (!modal.classList.contains('active')) {
        const form = document.getElementById('addVehicleForm');
        if (form) form.reset();

        const modalTitle = document.getElementById('modalTitle');
        if (modalTitle) modalTitle.innerText = "Add New Vehicle";

        resetImagePreview();

        if (typeof editingCard !== 'undefined') {
            editingCard = null;
        }
    }
}

function resetImagePreview() {
    const preview = document.getElementById('imagePreview');
    const uploadIcon = document.getElementById('uploadIcon');
    const uploadText = document.getElementById('uploadText');
    const imageInput = document.getElementById('vehicleImage');

    if (preview) {
        preview.src = "";
        preview.style.display = 'none';
    }
    if (uploadIcon) uploadIcon.style.display = 'block';
    if (uploadText) uploadText.style.display = 'block';
    if (imageInput) imageInput.value = "";
}

function previewImage(input) {
    const preview = document.getElementById('imagePreview');
    const uploadIcon = document.getElementById('uploadIcon');
    const uploadText = document.getElementById('uploadText');

    if (input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = function(e) {
            if (preview) {
                preview.src = e.target.result;
                preview.style.display = 'block';
            }
            if (uploadIcon) uploadIcon.style.display = 'none';
            if (uploadText) uploadText.style.display = 'none';
        }
        reader.readAsDataURL(input.files[0]);
    }
}

function loadAllVehiclesFromBackend() {
    $.ajax({
        url: "http://localhost:8080/v1/vehicles",
        type: "GET",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("jwtToken")
        },
        success: function(response) {
            let vehicleList = response.body || [];
            loadFleetToGrid(vehicleList);
        },
        error: function(err) {
            console.error("Failed to load vehicles from backend", err);
        }
    });
}

function loadFleetToGrid(vehicleList) {
    const vehicleGrid = document.getElementById('vehicleGrid');
    if (!vehicleGrid) return;

    vehicleGrid.innerHTML = "";

    if (!vehicleList || vehicleList.length === 0) {
        vehicleGrid.innerHTML = `<p style="color: #aaa; text-align: center; width: 100%;">No vehicles found.</p>`;

        return;
    }

    vehicleList.forEach(vehicle => {
        let statusClass = vehicle.status ? vehicle.status.toUpperCase() : 'AVAILABLE';
        let statusText = statusClass.charAt(0).toUpperCase() + statusClass.slice(1);

        let imageSrc = vehicle.vehicleImage ? `data:image/jpeg;base64,${vehicle.vehicleImage}` : 'assets/default-car.png';

        let categoryVal = vehicle.vehicleCategory || '';
        let categoryName = categoryVal ? categoryVal.replace('_', ' ') : 'CAR';

        const cardHTML = `
            <div class="vehicle-admin-card" data-status="${statusClass}" data-id="${vehicle.vehicleID}" 
                 data-name="${vehicle.vehicleName || ''}" 
                 data-category="${categoryVal}" 
                 data-plate="${vehicle.plateNumber || ''}" 
                 data-price="${vehicle.dailyPrice || ''}" 
                 data-insurance="${vehicle.insuranceNo || ''}" 
                 data-license="${vehicle.licenseNo || ''}" 
                 data-seats="${vehicle.seats || ''}" 
                 data-bags="${vehicle.bags || ''}" 
                 data-tag="${vehicle.tagClass || ''}" 
                 data-actype="${vehicle.acType || ''}" 
                 data-webcategory="${vehicle.webCategory || ''}">
                
                <span class="badge-tag">${vehicle.tagClass || 'PREMIUM'}</span>

                <div class="card-top-section">
                    <div class="car-img" style="width: 120px; height: 90px; min-width: 120px; overflow: hidden; display: flex; align-items: center; justify-content: center; position: relative;">
                        <img src="${imageSrc}" alt="${vehicle.vehicleName}" style="max-width: 100%; max-height: 100%; width: auto; height: auto; object-fit: contain; display: block;">
                    </div>
                    <div class="card-top-info">
                        <h3 class="v-title">${vehicle.vehicleName}</h3>
                        <span class="v-sub">${categoryName}</span>
                        <span class="status-badge ${statusClass}">${statusText}</span>
                    </div>
                </div>

                <div class="v-details-box">
                    <div class="spec-box">
                        <span>👥 SEATS</span>
                        <strong>${vehicle.seats}</strong>
                    </div>
                    <div class="spec-box">
                        <span>🧳 BAGS</span>
                        <strong>${vehicle.bags || 0}</strong>
                    </div>
                    <div class="spec-box">
                        <span>❄️ CLIMATE</span>
                        <strong>${vehicle.acType || 'AC'}</strong>
                    </div>
                </div>

                <div class="admin-extra-info">
                    <div class="detail-row"><span>Plate</span> <strong>${vehicle.plateNumber}</strong></div>
                    <div class="detail-row"><span>Insurance</span> <strong>${vehicle.insuranceNo || 'N/A'}</strong></div>
                    <div class="detail-row"><span>License</span> <strong>${vehicle.licenseNo || 'N/A'}</strong></div>
                </div>

                <div class="card-footer">
                    <div class="price">
                        <small>DAILY RATE</small>
                        <strong>LKR ${Number(vehicle.dailyPrice).toLocaleString()} <span>/ day</span></strong>
                    </div>
                </div>

                <div class="v-footer-actions">
                    <button class="btn-edit" onclick="editVehicle(this)">Edit</button>
                    <button class="btn-delete" onclick="deleteVehicleData(${vehicle.vehicleID})">Delete</button>
                </div>

            </div>
        `;

        vehicleGrid.insertAdjacentHTML('beforeend', cardHTML);
    });

    loadVehicleCountsFromBackend()
}

let editingCard = null;

function editVehicle(button) {
    editingCard = button.closest('.vehicle-admin-card');
    if (!editingCard) return;

    toggleForm();

    const modalTitle = document.getElementById('modalTitle');
    if (modalTitle) modalTitle.innerText = "Update Vehicle";

    $('#vehicleDisplayId').val(editingCard.getAttribute('data-id'));
    $('#vName').val(editingCard.getAttribute('data-name'));
    $('#vCategory').val(editingCard.getAttribute('data-category'));
    $('#vNumberPlate').val(editingCard.getAttribute('data-plate'));
    $('#vPrice').val(editingCard.getAttribute('data-price'));
    $('#vInsurance').val(editingCard.getAttribute('data-insurance'));
    $('#vLicense').val(editingCard.getAttribute('data-license'));
    $('#vSeats').val(editingCard.getAttribute('data-seats'));
    $('#vBags').val(editingCard.getAttribute('data-bags'));
    $('#vTag').val(editingCard.getAttribute('data-tag'));
    $('#vStatus').val(editingCard.getAttribute('data-status'));
    $('#vAcType').val(editingCard.getAttribute('data-actype'));
    $('#webCategory').val(editingCard.getAttribute('data-webcategory'));

    const imgElement = editingCard.querySelector('.car-img img');
    const preview = document.getElementById('imagePreview');
    const uploadIcon = document.getElementById('uploadIcon');
    const uploadText = document.getElementById('uploadText');

    if (imgElement && preview && imgElement.src) {
        preview.src = imgElement.src;
        preview.style.display = 'block';
        if (uploadIcon) uploadIcon.style.display = 'none';
        if (uploadText) uploadText.style.display = 'none';
    }
}

function deleteVehicleData(id) {
    if (confirm("Are you sure you want to delete this vehicle?")) {
        $.ajax({
            url: `http://localhost:8080/v1/vehicles/${id}`,
            type: "DELETE",
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("jwtToken")
            },
            success: function(response) {
                alert(response.message || "Vehicle deleted successfully!");
                loadAllVehiclesFromBackend();
                loadVehicleCountsFromBackend()
            },
            error: function(xhr) {
                alert("Failed to delete vehicle: " + (xhr.responseJSON?.message || "Error occurred"));
                console.error(xhr);
            }
        });
    }
}

$(document).ready(function() {
    const addVehicleForm = document.getElementById('addVehicleForm');

    if (addVehicleForm) {
        addVehicleForm.addEventListener('submit', function(e) {
            e.preventDefault();

            var formData = new FormData();

            if (typeof editingCard !== 'undefined' && editingCard !== null) {
                let vehicleId = editingCard.getAttribute('data-id');
                if (vehicleId) {
                    formData.append('vehicleID', vehicleId);
                }
            }

            formData.append('vehicleName', $('#vName').val());
            formData.append('vehicleCategory', $('#vCategory').val()); // කෙලින්ම Dropdown එකේ value එක (Enum එක) යැවීම
            formData.append('plateNumber', $('#vNumberPlate').val());
            formData.append('dailyPrice', $('#vPrice').val());
            formData.append('insuranceNo', $('#vInsurance').val());
            formData.append('licenseNo', $('#vLicense').val());
            formData.append('seats', $('#vSeats').val());
            formData.append('bags', $('#vBags').val());
            formData.append('tagClass', $('#vTag').val());
            formData.append('status', $('#vStatus').val());
            formData.append('acType', $('#vAcType').val());

            formData.append('showOnWebsite', true);
            formData.append('webCategory', $('#webCategory').val() || 'CAR');

            var imageInput = document.getElementById('vehicleImage');
            if (imageInput && imageInput.files[0]) {
                formData.append('vehicleImage', imageInput.files[0]);
            }

            let ajaxUrl = "http://localhost:8080/v1/vehicles";
            let ajaxType = "POST";

            if (typeof editingCard !== 'undefined' && editingCard !== null && editingCard.getAttribute('data-id')) {
                ajaxType = "PUT";
            }

            $.ajax({
                url: ajaxUrl,
                type: ajaxType,
                data: formData,
                processData: false,
                contentType: false,
                headers: {
                    "Authorization": "Bearer " + localStorage.getItem("jwtToken")
                },
                success: function(response) {
                    alert(response.message || "Vehicle saved successfully!");
                    toggleForm();
                    loadAllVehiclesFromBackend();
                    loadVehicleCountsFromBackend()
                },
                error: function(xhr, status, error) {
                    var errObj = xhr.responseJSON;
                    alert("Error: " + (errObj ? errObj.message : error));
                    console.error("Save error: ", xhr);
                }
            });
        });
    }

    loadAllVehiclesFromBackend();
    loadVehicleCountsFromBackend()
});

function loadVehicleCountsFromBackend() {
    let headers = { "Authorization": "Bearer " + localStorage.getItem("jwtToken") };

    $.ajax({
        url: "http://localhost:8080/v1/vehicles/count",
        type: "GET",
        headers: headers,
        success: function(res) {
            let totalCount = res.body !== undefined ? res.body : res;
            if(document.getElementById('totalCount')) {
                document.getElementById('totalCount').innerText = totalCount;
            }
        }
    });

    $.ajax({
        url: "http://localhost:8080/v1/vehicles/count/status/AVAILABLE",
        type: "GET",
        headers: headers,
        success: function(res) {
            let count = res.body !== undefined ? res.body : res;
            if(document.getElementById('availableCount')) {
                document.getElementById('availableCount').innerText = count;
            }
        }
    });

    $.ajax({
        url: "http://localhost:8080/v1/vehicles/count/status/RENTED",
        type: "GET",
        headers: headers,
        success: function(res) {
            let count = res.body !== undefined ? res.body : res;
            if(document.getElementById('rentedCount')) {
                document.getElementById('rentedCount').innerText = count;
            }
        }
    });

    $.ajax({
        url: "http://localhost:8080/v1/vehicles/count/status/MAINTENANCE",
        type: "GET",
        headers: headers,
        success: function(res) {
            let count = res.body !== undefined ? res.body : res;
            if(document.getElementById('maintCount')) {
                document.getElementById('maintCount').innerText = count;
            }
        }
    });
}

function searchVehicles() {
    let keyword = $('#vehicleSearch').val().trim();
    let headers = { "Authorization": "Bearer " + localStorage.getItem("jwtToken") };

    if (keyword.length === 0) {
        loadAllVehiclesFromBackend();
        return;
    }

    $.ajax({
        url: `http://localhost:8080/v1/vehicles/search?keyword=${encodeURIComponent(keyword)}`,
        type: "GET",
        headers: headers,
        success: function(response) {
            let vehicleList = response.body || [];
            loadFleetToGrid(vehicleList);
        },
        error: function(err) {
            console.error("Search failed", err);
        }
    });
}

function filterVehicles(status, buttonElement) {
    $('.filter-btn').removeClass('active');
    $(buttonElement).addClass('active');

    let headers = { "Authorization": "Bearer " + localStorage.getItem("jwtToken") };

    if (!status || status.toLowerCase() === 'all') {
        loadAllVehiclesFromBackend();
    } else {
        let upperStatus = status.toUpperCase();

        $.ajax({
            url: `http://localhost:8080/v1/vehicles/status/${upperStatus}`,
            type: "GET",
            headers: headers,
            success: function(response) {
                let vehicleList = response.body || [];
                loadFleetToGrid(vehicleList);
            },
            error: function(err) {
                console.error("Failed to filter by status", err);
            }
        });
    }
}