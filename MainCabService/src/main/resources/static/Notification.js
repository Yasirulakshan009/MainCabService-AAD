const BASE_URL = "http://localhost:8080/v1/notifications";

function authHeaders() {
    return { "Authorization": "Bearer " + localStorage.getItem("jwtToken") };
}

function fetchUnreadCount() {
    fetch(BASE_URL + "/unread-count", { headers: authHeaders() })
        .then(res => res.json())
        .then(data => {
            const count = data.body.count;
            const badge = document.getElementById("notificationBadge");
            if (count > 0) {
                badge.textContent = count > 9 ? "9+" : count;
                badge.style.display = "inline-block";
            } else {
                badge.style.display = "none";
            }
        })
        .catch(err => console.error("Failed to fetch unread count:", err));
}

function fetchNotificationList() {
    fetch(BASE_URL, { headers: authHeaders() })
        .then(res => res.json())
        .then(data => {
            const list = data.body || [];
            const container = document.getElementById("notificationList");
            container.innerHTML = "";

            if (list.length === 0) {
                container.innerHTML = "<div style='padding:16px; text-align:center; color:#999; font-size:13px;'>No notifications</div>";
                return;
            }

            list.forEach(n => {
                const item = document.createElement("div");
                item.style.padding = "10px 16px";
                item.style.borderBottom = "1px solid #f2f2f2";
                item.style.cursor = "pointer";
                item.style.background = n.read ? "#fff" : "#eef5ff";

                const time = new Date(n.createdDate).toLocaleString();
                item.innerHTML = `
                    <div style="font-size:13px; color:#222;">${n.message}</div>
                    <div style="font-size:11px; color:#999; margin-top:2px;">${time}</div>
                `;

                item.addEventListener("click", () => handleNotificationClick(n));
                container.appendChild(item);
            });
        })
        .catch(err => console.error("Failed to fetch notifications:", err));
}

function handleNotificationClick(notification) {
    fetch(BASE_URL + "/" + notification.id + "/read", {
        method: "PATCH",
        headers: authHeaders()
    }).then(() => {
        fetchUnreadCount();
        fetchNotificationList();
    });

    if (notification.type === "BOOKING") {
        showSection('bookings', document.getElementById('menu-bookings'));
    } else if (notification.type === "REVIEW") {
        showSection('customer-reviews', document.getElementById('menu-update-reviews'));
    } else if (notification.type === "RENTAL_OVERDUE") {
        showSection('rentals', document.getElementById('menu-rentals'));
    }

    document.getElementById("notificationDropdown").style.display = "none";
}

document.addEventListener("DOMContentLoaded", () => {
    const bellBtn = document.getElementById("notificationBellBtn");
    const dropdown = document.getElementById("notificationDropdown");
    const markAllBtn = document.getElementById("markAllReadBtn");

    fetchUnreadCount();

    bellBtn.addEventListener("click", () => {
        const isOpen = dropdown.style.display === "block";
        dropdown.style.display = isOpen ? "none" : "block";
        if (!isOpen) fetchNotificationList();
    });

    document.addEventListener("click", (e) => {
        if (!e.target.closest(".notification-wrapper")) {
            dropdown.style.display = "none";
        }
    });

    markAllBtn.addEventListener("click", (e) => {
        e.preventDefault();
        fetch(BASE_URL + "/mark-all-read", { method: "PATCH", headers: authHeaders() })
            .then(() => {
                fetchUnreadCount();
                fetchNotificationList();
            });
    });

    setInterval(fetchUnreadCount, 25000);
});