let currentStep = 1;
const totalSteps = 4;
let preselectVehicleId = null;

const API_BASE_URL = "http://localhost:8080";

(function guardBookingPage() {
    const token = localStorage.getItem("jwtToken");
    const role = localStorage.getItem("userRole");

    if (!token || role !== "CUSTOMER") {
        window.location.href = "SignIn.html?redirect=Booking.html";
    }
})();

function loadLoggedInCustomerInformation() {

    const token = localStorage.getItem("jwtToken");

    if (!token) {
        return;
    }

    $.ajax({
        url: API_BASE_URL + "/api/v1/auth/me",
        type: "GET",
        headers: {
            "Authorization": "Bearer " + token
        },

        success: function(response) {
            const customer =
                response.data ||
                response.body ||
                response;

            if (customer.userName) {
                $("#bcFullName").val(customer.userName);
            }

            if (customer.userEmail) {
                $("#bcEmail").val(customer.userEmail);
            }

            if (customer.phone) {
                $("#bcPhone").val(customer.phone);
            }
        },
        error: function(xhr) {
            console.error(
                "Unable to load logged-in customer information:",
                xhr
            );
        }
    });
}

function showStep(step) {
    document.querySelectorAll(".form-step")
        .forEach(formStep => {
            formStep.classList.remove("active");
        });

    const targetStep = document.querySelector(`.form-step[data-content="${step}"]`);

    if (targetStep) {
        targetStep.classList.add("active");
    }

    document.querySelectorAll(".step-item")
        .forEach((item, index) => {
            item.classList.remove("active", "completed");

            if (index + 1 < step) {
                item.classList.add("completed");
            } else if (index + 1 === step) {
                item.classList.add("active");
            }
        });

    const currentStepNumber = document.getElementById("current-step-num");

    if (currentStepNumber) {
        currentStepNumber.innerText = step;
    }

    window.scrollTo({
        top: 100,
        behavior: "smooth"
    });
}

function nextStep() {
    if (currentStep >= totalSteps) {
        return;
    }

    if (!validateCurrentStep()) {
        return;
    }

    currentStep++;

    if (currentStep === 3) {
        loadVehiclesByWebCategory();
    }

    showStep(currentStep);
}

function validateCurrentStep() {
    const activeStepEl = document.querySelector(`.form-step[data-content="${currentStep}"]`);

    if (!activeStepEl) {
        return true;
    }

    const requiredFields = activeStepEl.querySelectorAll("[required]");

    for (const field of requiredFields) {
        if (!field.value || !field.value.trim()) {
            Swal.fire({
                icon: "warning",
                title: "Missing Information",
                text: "Please fill in all required fields before continuing."
            });
            field.focus();
            return false;
        }
    }

    return true;
}

function prevStep() {
    if (currentStep <= 1) {
        return;
    }

    currentStep--;
    showStep(currentStep);
}

function setDeliveryMode(mode, element) {
    document.querySelectorAll(".selectable-card")
        .forEach(card => {
            card.classList.remove("active-card");
        });

    element.classList.add("active-card");

    const radio = element.querySelector('input[type="radio"]');

    if (radio) {
        radio.checked = true;
    }

    const label = document.getElementById("location-label");
    const input = document.getElementById("location-input");

    if (!label || !input) {
        return;
    }

    if (mode === "doorstep") {
        label.innerText = "Enter Doorstep Address *";
        input.value = "";
        input.disabled = false;
        input.required = true;
    } else {
        label.innerText = "Branch Location *";
        input.value = "Aura Cabs Main Branch, Bandaragama";
        input.disabled = true;
        input.required = false;
    }
}

function selectVehicleType(type, element) {
    document.querySelectorAll(".vertical-card")
        .forEach(card => {
            card.classList.remove("selected");
        });

    element.classList.add("selected");

    const radio = element.querySelector('input[type="radio"]');

    if (radio) {
        radio.checked = true;
    }

    if (currentStep === 3) {
        loadVehiclesByWebCategory();
    }
}

function loadVehiclesByWebCategory() {
    const selectedType = document.querySelector('input[name="vehicle_type"]:checked');
    const type = selectedType ? selectedType.value : "car";

    const webCategoryMap = {
        car: "CAR",
        van: "VAN",
        bus: "BUS"
    };

    const selectedWebCategory = webCategoryMap[type];

    const title = document.getElementById("specific-vehicle-title");

    if (title) {
        title.innerText = `Select ${type.charAt(0).toUpperCase() + type.slice(1)}`;
    }

    const container = document.getElementById("vehicle-models-container");

    if (!container) {
        return;
    }

    container.innerHTML = `
        <div class="vehicle-loading">
            <i class="ri-loader-4-line"></i>
            <p>Loading vehicles...</p>
        </div>
    `;

    $.ajax({
        url: `${API_BASE_URL}/v1/vehicles/website-fleet`,
        type: "GET",
        dataType: "json",
        success: function(response) {
            console.log("Vehicle API Response:", response);

            let vehicles = [];

            if (Array.isArray(response)) {
                vehicles = response;
            } else if (response && Array.isArray(response.body)) {
                vehicles = response.body;
            } else if (response && Array.isArray(response.data)) {
                vehicles = response.data;
            } else if (response && Array.isArray(response.content)) {
                vehicles = response.content;
            }

            console.log("All vehicles:", vehicles);

            const filteredVehicles = vehicles.filter(function(vehicle) {
                const webCategory = String(vehicle.webCategory || "").trim().toUpperCase();
                return webCategory === selectedWebCategory;
            });

            console.log("Selected Web Category:", selectedWebCategory);
            console.log("Filtered Vehicles:", filteredVehicles);

            container.innerHTML = "";

            if (filteredVehicles.length === 0) {
                container.innerHTML = `
                    <div class="no-vehicles">
                        <i class="ri-car-line"></i>
                        <h4>No Vehicles Available</h4>
                        <p>No vehicles are available for this category.</p>
                    </div>
                `;
                return;
            }

            filteredVehicles.forEach(function(vehicle, index) {
                const vehicleId = vehicle.vehicleID ?? vehicle.vehicleId ?? vehicle.id ?? "";
                const vehicleName = vehicle.vehicleName ?? vehicle.name ?? "Unknown Vehicle";
                const vehicleCategory = vehicle.vehicleCategory ?? "N/A";
                const seats = vehicle.seats ?? vehicle.seatCount ?? "N/A";
                const acStatus = vehicle.acStatus ?? vehicle.acType ?? vehicle.ac ?? "N/A";
                const price = vehicle.dailyPrice ?? vehicle.price ?? 0;

                const shouldPreselect = preselectVehicleId
                    ? String(vehicleId) === String(preselectVehicleId)
                    : index === 0;

                const card = document.createElement("label");
                card.className = "model-card";

                if (shouldPreselect) {
                    card.classList.add("selected-model");
                }

                card.innerHTML = `
                    <input type="radio" name="specific_vehicle" value="${vehicleId}" ${shouldPreselect ? "checked" : ""}>
                    <div class="vehicle-info">
                        <h4>${vehicleName}</h4>
                        <span class="vehicle-category">${vehicleCategory}</span>
                        <div class="model-details">
                            <span><i class="ri-user-line"></i> ${seats} Seats</span>
                            <span><i class="ri-snowy-line"></i> ${acStatus}</span>
                            <span><i class="ri-money-dollar-circle-line"></i> LKR ${Number(price).toLocaleString()}</span>
                        </div>
                    </div>
                `;

                card.addEventListener("click", function() {
                    document.querySelectorAll(".model-card").forEach(function(item) {
                        item.classList.remove("selected-model");
                    });

                    card.classList.add("selected-model");

                    const radio = card.querySelector('input[type="radio"]');
                    if (radio) {
                        radio.checked = true;
                    }
                });

                container.appendChild(card);
            });
            
            preselectVehicleId = null;
        },
        error: function(xhr, status, error) {
            console.error("Vehicle AJAX Error:", error);
            console.error("Response:", xhr.responseText);

            container.innerHTML = `
                <div class="no-vehicles">
                    <i class="ri-error-warning-line"></i>
                    <h4>Unable to Load Vehicles</h4>
                    <p>Please try again later.</p>
                </div>
            `;
        }
    });
}

document.getElementById("booking-form")?.addEventListener("submit", function(event) {
    event.preventDefault();

    const selectedVehicle = document.querySelector(
        'input[name="specific_vehicle"]:checked'
    );

    if (!selectedVehicle) {
        Swal.fire({
            icon: "warning",
            title: "Select Vehicle",
            text: "Please select a vehicle first."
        });
        return;
    }

    const vehicleCard = selectedVehicle.closest(".model-card");
    const vehicleModel =
        vehicleCard?.querySelector("h4")?.innerText?.trim();

    const bookingCustomerData = {
        bookingCustomerName: $('#bcFullName').val(),
        bookingCustomerEmail: $('#bcEmail').val(),
        bookingCustomerNumber: $('#bcPhone').val(),
        bookingCustomerLicenseNumber: $('#bcLicense').val(),
        bookingCustomerRegisterDate: new Date().toISOString().split('T')[0]
    };

    const bookingData = {
        vehicleModel: vehicleModel,
        startDate: $('#pickupDate').val(),
        endDate: $('#returnDate').val(),
        pickupAddress: $('#location-input').val(),
        bookingStatus: "PENDING",
        bookingCustomerID: null,
        bookingCustomerName: $('#bcFullName').val()
    };

    const submitBtn = document.querySelector('#booking-form .btn-submit');

    if (submitBtn) {
        submitBtn.disabled = true;
        submitBtn.innerText = "Processing...";
    }

    $.ajax({
        url: API_BASE_URL + "/v1/bookingCustomers",
        type: "POST",
        contentType: "application/json",

        headers: {
            "Authorization":
                "Bearer " + localStorage.getItem("jwtToken")
        },

        data: JSON.stringify(bookingCustomerData),

        success: function(customerResponse) {

            console.log("Booking Customer Response:", customerResponse);

            const bookingCustomerId = customerResponse.body;

            if (!bookingCustomerId) {
                Swal.fire({
                    icon: "error",
                    title: "Booking Failed",
                    text: "Booking customer ID was not received."
                });
                return;
            }

            bookingData.bookingCustomerID = bookingCustomerId;

            console.log("Booking Data:", bookingData);

            $.ajax({
                url: API_BASE_URL + "/v1/bookings",
                type: "POST",
                contentType: "application/json",

                headers: {
                    "Authorization":
                        "Bearer " + localStorage.getItem("jwtToken")
                },

                data: JSON.stringify(bookingData),

                success: function(bookingResponse) {

                    console.log("Booking Response:", bookingResponse);

                    Swal.fire({
                        icon: "success",
                        title: "Booking Confirmed!",
                        text: "Thank you for choosing Aura Cabs. We will contact you shortly.",
                        confirmButtonColor: "#0d6efd"
                    });

                    document.getElementById("booking-form").reset();

                    currentStep = 1;
                    showStep(currentStep);

                    window.scrollTo({
                        top: 0,
                        behavior: "smooth"
                    });
                },

                error: function(xhr) {

                    let errorText =
                        xhr.responseJSON?.message ||
                        "Unable to create booking.";

                    Swal.fire({
                        icon: "error",
                        title: "Booking Failed",
                        text: errorText,
                        confirmButtonColor: "#dc3545"
                    });

                    console.error("Booking save error:", xhr);
                },

                complete: function() {
                    if (submitBtn) {
                        submitBtn.disabled = false;
                        submitBtn.innerText = "Confirm Booking";
                    }
                }
            });
        },

        error: function(xhr) {

            let errorText =
                xhr.responseJSON?.message ||
                "Unable to save customer information.";

            Swal.fire({
                icon: "error",
                title: "Booking Failed",
                text: errorText,
                confirmButtonColor: "#dc3545"
            });

            console.error("Booking customer save error:", xhr);

            if (submitBtn) {
                submitBtn.disabled = false;
                submitBtn.innerText = "Confirm Booking";
            }
        }
    });
});

function preselectVehicleFromUrl() {
    const params = new URLSearchParams(window.location.search);
    const vehicleId = params.get("vehicleId");

    if (!vehicleId) {
        return;
    }

    $.ajax({
        url: `${API_BASE_URL}/v1/vehicles/${vehicleId}`,
        type: "GET",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("jwtToken")
        },
        success: function (response) {
            const vehicle = response.body || response;

            if (!vehicle || !vehicle.webCategory) {
                return;
            }

            const categoryMap = { CAR: "car", VAN: "van", BUS: "bus" };
            const type = categoryMap[String(vehicle.webCategory).toUpperCase()] || "car";

            const typeRadio = document.querySelector(`input[name="vehicle_type"][value="${type}"]`);
            if (typeRadio) {
                typeRadio.checked = true;

                const typeCard = typeRadio.closest(".vertical-card");
                if (typeCard) {
                    document.querySelectorAll(".vertical-card").forEach(c => c.classList.remove("selected"));
                    typeCard.classList.add("selected");
                }
            }

            preselectVehicleId = vehicleId;
        },
        error: function (xhr) {
            console.error("Could not preselect vehicle from URL:", xhr);
        }
    });
}

document.addEventListener("DOMContentLoaded", function() {
    currentStep = 1;
    showStep(currentStep);
    loadLoggedInCustomerInformation();
    preselectVehicleFromUrl();
});