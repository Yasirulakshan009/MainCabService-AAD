const OVERVIEW_API_BASE = "http://localhost:8080";

function overviewAuthHeaders() {
    return {
        "Authorization": "Bearer " + localStorage.getItem("jwtToken")
    };
}

function overviewExtractValue(response) {
    return response && response.body !== undefined ? response.body : response;
}

let fleetPieChartInstance = null;
let revenueChartInstance = null;

if (typeof window.showSection === "function") {
    const _originalShowSection = window.showSection;
    window.showSection = function(sectionName, element) {
        _originalShowSection(sectionName, element);
        if (sectionName === 'overview') {
            loadOverviewStatCards();
            loadFleetStatusChart();
            loadMonthlyRevenueChart();
            loadRecentCompletedRentals();
        }
    };
}

document.addEventListener("DOMContentLoaded", function() {

    const yearLabel = document.getElementById('overviewYearLabel');
    if (yearLabel) {
        yearLabel.innerText = new Date().getFullYear();
    }

    if (document.getElementById('overview-section')) {
        loadOverviewStatCards();
        loadFleetStatusChart();
        loadMonthlyRevenueChart();
        loadRecentCompletedRentals();
    }
});

function loadOverviewStatCards() {

    $.ajax({
        url: `${OVERVIEW_API_BASE}/v1/bookings`,
        type: "GET",
        headers: overviewAuthHeaders(),
        success: function(response) {
            const bookingList = overviewExtractValue(response) || [];
            setOverviewStat('overviewTotalBookings', bookingList.length);
        },
        error: function() {
            setOverviewStat('overviewTotalBookings', 0);
        }
    });

    $.ajax({
        url: `${OVERVIEW_API_BASE}/v1/rentals/count/status/ACTIVE`,
        type: "GET",
        headers: overviewAuthHeaders(),
        success: function(response) {
            setOverviewStat('overviewActiveRentals', Number(overviewExtractValue(response)) || 0);
        },
        error: function() {
            setOverviewStat('overviewActiveRentals', 0);
        }
    });

    $.ajax({
        url: `${OVERVIEW_API_BASE}/v1/rentals/count/status/COMPLETED`,
        type: "GET",
        headers: overviewAuthHeaders(),
        success: function(response) {
            setOverviewStat('overviewCompletedRentals', Number(overviewExtractValue(response)) || 0);
        },
        error: function() {
            setOverviewStat('overviewCompletedRentals', 0);
        }
    });

    $.ajax({
        url: `${OVERVIEW_API_BASE}/v1/payments/total-revenue`,
        type: "GET",
        headers: overviewAuthHeaders(),
        success: function(response) {
            const revenue = Number(overviewExtractValue(response)) || 0;
            setOverviewStat('overviewTotalRevenue', revenue.toLocaleString());
        },
        error: function() {
            setOverviewStat('overviewTotalRevenue', '0');
        }
    });
}

function setOverviewStat(elementId, value) {
    const el = document.getElementById(elementId);
    if (el) el.innerText = value;
}


function loadFleetStatusChart() {
    const fleetPieElement = document.getElementById('fleetPieChart');
    if (!fleetPieElement) return;

    const statuses = ['AVAILABLE', 'RENTED', 'MAINTENANCE'];
    const counts = { AVAILABLE: 0, RENTED: 0, MAINTENANCE: 0 };
    let callsFinished = 0;

    statuses.forEach(function(status) {
        $.ajax({
            url: `${OVERVIEW_API_BASE}/v1/vehicles/count/status/${status}`,
            type: "GET",
            headers: overviewAuthHeaders(),
            success: function(response) {
                counts[status] = Number(overviewExtractValue(response)) || 0;
            },
            error: function() {
                counts[status] = 0;
            },
            complete: function() {
                callsFinished++;
                if (callsFinished === statuses.length) {
                    renderFleetPieChart(counts);
                }
            }
        });
    });
}

function renderFleetPieChart(counts) {
    const fleetPieElement = document.getElementById('fleetPieChart');
    if (!fleetPieElement) return;

    const ctxPie = fleetPieElement.getContext('2d');

    if (fleetPieChartInstance) {
        fleetPieChartInstance.destroy();
    }

    fleetPieChartInstance = new Chart(ctxPie, {
        type: 'doughnut',
        data: {
            labels: ['Available', 'Rented', 'Maintenance'],
            datasets: [{
                data: [counts.AVAILABLE, counts.RENTED, counts.MAINTENANCE],
                backgroundColor: [
                    '#28a745',
                    '#007bff',
                    '#ffc107'
                ],
                borderWidth: 0,
                hoverOffset: 6
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            cutout: '70%',
            plugins: {
                legend: {
                    position: 'right',
                    labels: {
                        color: 'var(--text-main)',
                        font: {
                            size: 12,
                            family: 'Segoe UI'
                        },
                        boxWidth: 12,
                        padding: 15
                    }
                }
            }
        }
    });
}

function loadMonthlyRevenueChart() {
    const chartElement = document.getElementById('revenueChart');
    if (!chartElement) return;

    $.ajax({
        url: `${OVERVIEW_API_BASE}/v1/payments`,
        type: "GET",
        headers: overviewAuthHeaders(),
        success: function(response) {
            const payments = overviewExtractValue(response) || [];
            renderMonthlyRevenueChart(payments);
        },
        error: function() {
            renderMonthlyRevenueChart([]);
        }
    });
}

function renderMonthlyRevenueChart(payments) {
    const chartElement = document.getElementById('revenueChart');
    if (!chartElement) return;

    const now = new Date();
    const currentYear = now.getFullYear();
    const currentMonthIndex = now.getMonth();

    const monthlyTotals = new Array(12).fill(0);

    (payments || []).forEach(function(payment) {
        if (!payment.date || payment.status === 'FAILED') return;

        const paymentDate = new Date(payment.date);
        if (paymentDate.getFullYear() !== currentYear) return;

        monthlyTotals[paymentDate.getMonth()] += Number(payment.amount) || 0;
    });

    for (let i = currentMonthIndex + 1; i < 12; i++) {
        monthlyTotals[i] = 0;
    }

    const ctx = chartElement.getContext('2d');

    let gradient = ctx.createLinearGradient(0, 0, 0, 260);
    gradient.addColorStop(0, 'rgb(47 109 230)'); // #8a2be2
    gradient.addColorStop(1, 'rgba(138, 43, 226, 0.0)');

    if (revenueChartInstance) {
        revenueChartInstance.destroy();
    }

    revenueChartInstance = new Chart(ctx, {
        type: 'line',
        data: {
            labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
            datasets: [{
                label: 'Monthly Revenue (Rs.)',
                data: monthlyTotals,
                borderColor: '#8a2be2',
                borderWidth: 3,
                backgroundColor: gradient,
                fill: true,
                tension: 0.4,
                pointBackgroundColor: '#ff007f',
                pointBorderColor: '#ffffff',
                pointRadius: 4,
                pointHoverRadius: 6
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false
                }
            },
            scales: {
                x: {
                    grid: { color: 'rgba(255, 255, 255, 0.04)' },
                    ticks: { color: '#a0a0a0', font: { size: 12 } }
                },
                y: {
                    grid: { color: 'rgba(255, 255, 255, 0.04)' },
                    ticks: { color: '#a0a0a0', font: { size: 12 } }
                }
            }
        }
    });
}


function loadRecentCompletedRentals() {
    const tbody = document.getElementById('recentRentalsTableBody');
    if (!tbody) return;

    $.when(
        $.ajax({ url: `${OVERVIEW_API_BASE}/v1/rentals/status/COMPLETED`, type: "GET", headers: overviewAuthHeaders() }),
        $.ajax({ url: `${OVERVIEW_API_BASE}/v1/customers`, type: "GET", headers: overviewAuthHeaders() }),
        $.ajax({ url: `${OVERVIEW_API_BASE}/v1/vehicles`, type: "GET", headers: overviewAuthHeaders() })
    ).done(function(rentalsResult, customersResult, vehiclesResult) {
        const rentals = overviewExtractValue(rentalsResult[0]) || [];
        const customers = overviewExtractValue(customersResult[0]) || [];
        const vehicles = overviewExtractValue(vehiclesResult[0]) || [];

        renderRecentCompletedRentals(rentals, customers, vehicles);
    }).fail(function() {
        tbody.innerHTML = '<tr><td colspan="4" style="text-align:center; color:#aaa;">Unable to load recent rentals.</td></tr>';
    });
}

function renderRecentCompletedRentals(rentals, customers, vehicles) {
    const tbody = document.getElementById('recentRentalsTableBody');
    if (!tbody) return;

    if (!rentals || rentals.length === 0) {
        tbody.innerHTML = '<tr><td colspan="4" style="text-align:center; color:#aaa;">No completed rentals yet.</td></tr>';
        return;
    }

    const customerNameById = {};
    (customers || []).forEach(function(customer) {
        customerNameById[customer.customerID] = customer.customerName;
    });

    const vehicleLabelById = {};
    (vehicles || []).forEach(function(vehicle) {
        vehicleLabelById[vehicle.vehicleID] = `${vehicle.vehicleName} (${vehicle.plateNumber})`;
    });

    const recentFirst = rentals.slice().sort(function(a, b) {
        return new Date(b.startDate) - new Date(a.startDate);
    }).slice(0, 5);

    tbody.innerHTML = recentFirst.map(function(rental) {
        const customerName = customerNameById[rental.customerID] || `#CUST-${rental.customerID}`;
        const vehicleLabel = vehicleLabelById[rental.vehicleID] || `#V-${rental.vehicleID}`;

        return `
            <tr>
                <td>${customerName}</td>
                <td>${vehicleLabel}</td>
                <td>${rental.startDate || ''}</td>
                <td><span class="status-badge COMPLETE">Completed</span></td>
            </tr>
        `;
    }).join('');
}