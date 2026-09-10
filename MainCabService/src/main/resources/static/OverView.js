document.addEventListener("DOMContentLoaded", function() {

    const fleetPieElement = document.getElementById('fleetPieChart');

    if (fleetPieElement) {
        const ctxPie = fleetPieElement.getContext('2d');

        new Chart(ctxPie, {
            type: 'doughnut',
            data: {
                labels: ['Available', 'Rented', 'Maintenance'],
                datasets: [{
                    data: [65, 25, 10],
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

    const chartElement = document.getElementById('revenueChart');

    if (chartElement) {
        const ctx = chartElement.getContext('2d');

        let gradient = ctx.createLinearGradient(0, 0, 0, 260);
        gradient.addColorStop(0, 'rgb(47 109 230)'); // #8a2be2
        gradient.addColorStop(1, 'rgba(138, 43, 226, 0.0)');

        new Chart(ctx, {
            type: 'line',
            data: {
                labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
                datasets: [{
                    label: 'Monthly Revenue (Rs.)',
                    data: [120000, 190000, 150000, 220000, 310000, 450000, 380000, 0, 0, 0, 0, 0],
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
});