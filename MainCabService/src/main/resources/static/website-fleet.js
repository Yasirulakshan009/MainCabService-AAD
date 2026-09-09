function filterFleet(category, buttonElement) {
    document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
    if (buttonElement) {
        buttonElement.classList.add('active');
    }

    updateCategoryInfoHeader(category);

    fetchVehiclesFromBackend(category);
}

function updateCategoryInfoHeader(category) {
    const categoryInfoBox = document.getElementById('categoryInfo');
    if (!categoryInfoBox) return;

    let title = "Cars Services";
    let desc = "Comfortable and fuel-efficient cars for city rentals and long journeys";

    const catUpper = category.toUpperCase();
    if (catUpper === 'VANS' || catUpper === 'VAN') {
        title = "Van Services";
        desc = "Spacious vans perfect for family trips, groups, and long tours";
    } else if (catUpper === 'BUSES' || catUpper === 'BUS') {
        title = "Bus Services";
        desc = "Large and comfortable buses for corporate events and massive group travels";
    }

    categoryInfoBox.innerHTML = `
        <h3>${title}</h3>
        <p>${desc}</p>
    `;
}

function fetchVehiclesFromBackend(category) {
    let backendCategory = category.toUpperCase();
    if (backendCategory === 'CARS') backendCategory = 'CAR';
    if (backendCategory === 'VANS') backendCategory = 'VAN';
    if (backendCategory === 'BUSES') backendCategory = 'BUS';

    let endpoint = `http://localhost:8080/v1/vehicles/website/category/${backendCategory}`;

    let headers = {};
    let token = localStorage.getItem("jwtToken");
    if (token) {
        headers["Authorization"] = "Bearer " + token;
    }

    $.ajax({
        url: endpoint,
        type: "GET",
        headers: headers,
        success: function(response) {
            console.log("Backend Response for " + backendCategory + ":", response);
            let vehicleList = response.body || response.content || response || [];
            renderVehicleCards(vehicleList);
        },
        error: function(err) {
            console.error("Error fetching vehicles for category:", err);
            const grid = document.querySelector('.vehicle-grid');
            if (grid) {
                grid.innerHTML = `<p style="text-align: center; width: 100%; grid-column: 1 / -1; color: #ff6b6b; padding: 20px;">Failed to load vehicles from server.</p>`;
            }
        }
    });
}

function renderVehicleCards(response) {
    const vehicleGrid = document.querySelector('.vehicle-grid');
    if (!vehicleGrid) {
        console.error("❌ .vehicle-grid element එක හොයාගන්න බැහැ!");
        return;
    }

    vehicleGrid.innerHTML = "";

    let vehicles = [];
    if (Array.isArray(response)) {
        vehicles = response;
    } else if (response && Array.isArray(response.body)) {
        vehicles = response.body;
    } else if (response && Array.isArray(response.data)) {
        vehicles = response.data;
    }

    console.log("🚗 Render කරන්න ලැබුණු Vehicles ලැයිස්තුව:", vehicles);

    if (!vehicles || vehicles.length === 0) {
        vehicleGrid.innerHTML = `<p style="text-align: center; width: 100%; grid-column: 1 / -1; padding: 30px; color: #8da4c4; font-weight: 500;">No vehicles available in this category right now.</p>`;
        return;
    }

    vehicles.forEach(vehicle => {
        let imgData = vehicle.vehicleImage;
        let imageSrc = imgData ? (imgData.startsWith('data:') ? imgData : `data:image/jpeg;base64,${imgData}`) : 'src/main/resources/images/suzuki-wagon-r-suzuki-jimny-car-suzuki-mr-wagon-suzuki-wagon-r-removebg-preview.png';

        let formattedPrice = vehicle.dailyPrice ? Number(vehicle.dailyPrice).toLocaleString() : '0';
        let vName = vehicle.vehicleName || vehicle.vehicleModel || 'Standard Vehicle';
        let webCat = vehicle.webCategory ? vehicle.webCategory.toLowerCase() : 'car';

        const cardHTML = `
            <div class="vehicle-card show-card" data-category="${webCat}">
                <span class="premium-badge">${vehicle.tagClass || 'PREMIUM'}</span>
                <div class="car-img">
                    <img src="${imageSrc}" alt="${vName}">
                </div>
                <h3><span>${vName}</span></h3>
                <p class="availability">✅ Available for Instant Self-Drive</p>

                <div class="specs">
                    <div class="spec-box">
                        <span>👥 SEATS</span>
                        <strong>${vehicle.seats || 4}</strong>
                    </div>
                    <div class="spec-box">
                        <span>🧳 BAGS</span>
                        <strong>${vehicle.bags || 2}</strong>
                    </div>
                    <div class="spec-box">
                        <span>❄️ CLIMATE</span>
                        <strong>${vehicle.acType || 'AC'}</strong>
                    </div>
                </div>
                <div class="card-footer">
                    <div class="price">
                        <small>STARTING PRICE</small>
                        <strong>LKR ${formattedPrice} <span>/ day</span></strong>
                    </div>
                    <button class="details-btn" onclick="bookVehicle(${vehicle.vehicleID})">
                        <span class="text">Book</span>
                        <span class="arrow">→</span>
                    </button>
                </div>
            </div>
        `;

        vehicleGrid.insertAdjacentHTML('beforeend', cardHTML);
    });
}

$(document).ready(function() {
    const defaultTab = document.querySelector('.tab-btn.active') || document.querySelector('.tab-btn');
    if (defaultTab) {
        filterFleet('cars', defaultTab);
    } else {
        filterFleet('cars', null);
    }
});