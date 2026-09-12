function goToBooking() {
    const token = localStorage.getItem("jwtToken");
    const role = localStorage.getItem("userRole");

    if (token && role === "CUSTOMER") {
        window.location.href = "Booking.html";
    } else {
        window.location.href = "SignIn.html?redirect=Booking.html";
    }
}