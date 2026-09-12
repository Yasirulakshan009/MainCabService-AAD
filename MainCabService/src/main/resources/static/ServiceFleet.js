const SERVICE_API = "http://localhost:8080/v1/vehicles/website/category";

function getServiceCategory() {
    const page = window.location.pathname.toLowerCase();

    if (page.includes("carservice")) {
        return ["CAR"];
    }
    if (page.includes("vanservice")) {
        return ["VAN"];
    }
    if (page.includes("busservice")) {
        return ["BUS"];
    }
    if (page.includes("weddingservice")) {
        return ["CAR", "VAN"];
    }

    return [];
}

function loadServiceFleet() {
    const categories = getServiceCategory();
    const grid = document.querySelector(".vehicle-grid-service");

    if (!grid) {
        console.error("❌ .vehicle-grid-service not found");
        return;
    }

    if (categories.length === 0) {
        console.error("❌ Service category not found");
        return;
    }

    grid.innerHTML = `
        <div class="fleet-loading">
            Loading vehicles...
        </div>
    `;

    const token = localStorage.getItem("jwtToken");
    const headers = {};

    if (token) {
        headers["Authorization"] = "Bearer " + token;
    }

    const requests = categories.map(category => {
        return $.ajax({
            url: `${SERVICE_API}/${category}`,
            type: "GET",
            headers: headers
        });
    });

    Promise.all(requests)
        .then(function(responses) {
            let vehicles = [];

            responses.forEach(function(response) {
                const list = response.body || response.content || response.data || [];

                if (Array.isArray(list)) {
                    vehicles = vehicles.concat(list);
                }
            });

            console.log("🚗 Service vehicles:", vehicles);
            renderServiceVehicleCards(vehicles);
        })
        .catch(function(error) {
            console.error("❌ Failed to load service vehicles:", error);

            grid.innerHTML = `
                <p class="fleet-error">
                    Failed to load vehicles from server.
                </p>
            `;
        });
}

function renderServiceVehicleCards(vehicles) {
    const grid = document.querySelector(".vehicle-grid-service");

    if (!grid) return;

    grid.innerHTML = "";

    if (!vehicles || vehicles.length === 0) {
        grid.innerHTML = `
            <p class="fleet-empty">
                No vehicles available for this service right now.
            </p>
        `;
        return;
    }

    vehicles.forEach(function(vehicle) {
        let imageSrc;

        if (vehicle.vehicleImage) {
            imageSrc = vehicle.vehicleImage.startsWith("data:")
                ? vehicle.vehicleImage
                : `data:image/jpeg;base64,${vehicle.vehicleImage}`;
        } else {
            imageSrc = "images/default-car.png";
        }

        const vehicleName = vehicle.vehicleName || vehicle.vehicleModel || "Standard Vehicle";
        const price = vehicle.dailyPrice ? Number(vehicle.dailyPrice).toLocaleString() : "0";
        const badge = vehicle.tagClass || "PREMIUM";
        const seats = vehicle.seats || 4;
        const bags = vehicle.bags || 2;
        const ac = vehicle.acType || "AC";

        const card = document.createElement("div");
        card.className = "vehicle-card-service";

        card.innerHTML = `
            <span class="premium-badge">
                ${badge}
            </span>
            <div class="car-img">
                <img src="${imageSrc}" alt="${vehicleName}">
            </div>
            <h3>
                ${vehicleName}
            </h3>
            <p class="availability">
                ✅ Available for Booking
            </p>
            <div class="specs">
                <div class="spec-box">
                    <span>👥 SEATS</span>
                    <strong>${seats}</strong>
                </div>
                <div class="spec-box">
                    <span>🧳 BAGS</span>
                    <strong>${bags}</strong>
                </div>
                <div class="spec-box">
                    <span>❄️ CLIMATE</span>
                    <strong>${ac}</strong>
                </div>
            </div>
            <div class="card-footer">
                <div class="price">
                    <small>STARTING PRICE</small>
                    <strong>
                        LKR ${price}
                        <span>/ day</span>
                    </strong>
                </div>
                <button class="details-btn" onclick="bookServiceVehicle(${vehicle.vehicleID})">
                    <span class="text">Book</span>
                    <span class="arrow">→</span>
                </button>
            </div>
        `;

        grid.appendChild(card);
    });
}

function bookServiceVehicle(vehicleId) {
    const token = localStorage.getItem("jwtToken");
    const role = localStorage.getItem("userRole");
    const bookingTarget = `Booking.html?vehicleId=${vehicleId}`;

    if (token && role === "CUSTOMER") {
        window.location.href = bookingTarget;
    } else {
        window.location.href = "SignIn.html?redirect=" + encodeURIComponent(bookingTarget);
    }
}

$(document).ready(function() {
    loadServiceFleet();
});