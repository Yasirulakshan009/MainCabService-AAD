function goToBooking() {
    const token = localStorage.getItem("jwtToken");
    const role = localStorage.getItem("userRole");

    if (token && role === "CUSTOMER") {
        window.location.href = "Booking.html";
    } else {
        window.location.href = "SignIn.html?redirect=Booking.html";
    }
}

function bookVehicle(vehicleId) {
    const token = localStorage.getItem("jwtToken");
    const role = localStorage.getItem("userRole");
    const bookingTarget = "Booking.html?vehicleId=" + vehicleId;

    if (token && role === "CUSTOMER") {
        window.location.href = bookingTarget;
    } else {
        window.location.href = "SignIn.html?redirect=" + encodeURIComponent(bookingTarget);
    }
}

function initNavAuthStatus() {
    const signInLink = document.getElementById("signInLink");
    const avatarWrapper = document.getElementById("customerAvatarWrapper");

    if (!signInLink || !avatarWrapper) {
        return;
    }

    const token = localStorage.getItem("jwtToken");
    const role = localStorage.getItem("userRole");

    if (!token || role !== "CUSTOMER") {
        signInLink.style.display = "inline-block";
        avatarWrapper.style.display = "none";
        return;
    }

    fetch("http://localhost:8080/api/v1/auth/me", {
        headers: {
            "Authorization": "Bearer " + token
        }
    })
        .then(res => res.json())
        .then(data => {
            const user = data.body;
            if (!user || !user.userName) {
                return;
            }

            const firstLetter = user.userName.trim().charAt(0).toUpperCase();

            const avatarBtn = document.getElementById("customerAvatarBtn");
            const nameEl = document.getElementById("customerAvatarName");
            const emailEl = document.getElementById("customerAvatarEmail");

            if (avatarBtn) avatarBtn.textContent = firstLetter;
            if (nameEl) nameEl.textContent = user.userName;
            if (emailEl) emailEl.textContent = user.userEmail;

            signInLink.style.display = "none";
            avatarWrapper.style.display = "inline-block";
        })
        .catch(err => {
            console.error("Failed to load current user:", err);
        });
}

function logoutCustomer() {
    localStorage.removeItem("jwtToken");
    localStorage.removeItem("userRole");
    localStorage.removeItem("userEmail");
    localStorage.removeItem("userPermissions");
    window.location.href = "Home.html";
}

document.addEventListener("click", function (event) {
    const dropdown = document.getElementById("customerAvatarDropdown");
    const avatarBtn = document.getElementById("customerAvatarBtn");

    if (!dropdown || !avatarBtn) {
        return;
    }

    if (avatarBtn.contains(event.target)) {
        dropdown.style.display = (dropdown.style.display === "block") ? "none" : "block";
    } else if (!dropdown.contains(event.target)) {
        dropdown.style.display = "none";
    }
});

document.addEventListener("DOMContentLoaded", initNavAuthStatus);