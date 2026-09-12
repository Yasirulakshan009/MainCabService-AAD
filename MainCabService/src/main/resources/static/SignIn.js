const API_BASE_URL = "http://localhost:8080";

function getRedirectTarget() {
    const params = new URLSearchParams(window.location.search);
    const redirect = params.get("redirect");
    if (redirect && !redirect.startsWith("http")) {
        return redirect;
    }
    return "Home.html";
}

function switchAuthTab(tab, event) {
    const loginForm = document.getElementById("loginForm");
    const registerForm = document.getElementById("registerForm");
    const authCaption = document.getElementById("auth-caption");

    document.querySelectorAll(".auth-tab-btn")
        .forEach(btn => btn.classList.remove("active"));

    event.currentTarget.classList.add("active");

    if (tab === "login") {
        loginForm.style.display = "block";
        registerForm.style.display = "none";

        if (authCaption) {
            authCaption.innerText = "If you already have an account, enter your Gmail and password to sign in.";
        }
    } else {
        loginForm.style.display = "none";
        registerForm.style.display = "block";

        if (authCaption) {
            authCaption.innerText = "If you don't have an Aura Cabs account yet, please register first.";
        }
    }
}

function handleRegister(event) {
    event.preventDefault();

    const name = document.getElementById("regName").value.trim();
    const email = document.getElementById("regEmail").value.trim();
    const phone = document.getElementById("regPhone").value.trim();
    const password = document.getElementById("regPassword").value;

    if (!name || !email || !phone || !password) {
        Swal.fire({
            icon: "warning",
            title: "Required Fields",
            text: "Please fill all registration fields."
        });
        return;
    }

    const registerData = {
        userName: name,
        userEmail: email,
        phone: phone,
        userPassword: password,
        confirmPassword: password
    };

    $.ajax({
        url: API_BASE_URL + "/api/v1/auth/customer-register",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(registerData),

        success: function () {
            Swal.fire({
                icon: "success",
                title: "Registration Successful!",
                text: "Your customer account has been created. Please login to continue.",
                confirmButtonColor: "#0d6efd"
            }).then(() => {
                document.querySelectorAll(".auth-tab-btn")
                    .forEach(btn => btn.classList.remove("active"));

                const loginButton = document.querySelector(".auth-tab-btn:first-child");
                if (loginButton) {
                    loginButton.classList.add("active");
                }

                document.getElementById("loginForm").style.display = "block";
                document.getElementById("registerForm").style.display = "none";

                document.getElementById("loginEmail").value = email;
                document.getElementById("loginPassword").value = "";

                const caption = document.getElementById("auth-caption");
                if (caption) {
                    caption.innerText = "Account created successfully. Please login to continue.";
                }
            });
        },

        error: function (xhr) {
            let message = "Registration failed.";

            if (xhr.responseJSON) {
                if (xhr.responseJSON.message) {
                    message = xhr.responseJSON.message;
                }
                if (xhr.responseJSON.body && typeof xhr.responseJSON.body === "string") {
                    message = xhr.responseJSON.body;
                }
            }

            Swal.fire({
                icon: "error",
                title: "Registration Failed",
                text: message
            });
            console.error("Customer registration error:", xhr);
        }
    });
}

function handleLogin(event) {
    event.preventDefault();

    const email = document.getElementById("loginEmail").value.trim();
    const password = document.getElementById("loginPassword").value;

    if (!email || !password) {
        Swal.fire({
            icon: "warning",
            title: "Required Fields",
            text: "Please enter your email and password."
        });
        return;
    }

    const loginData = {
        identifier: email,
        password: password
    };

    $.ajax({
        url: API_BASE_URL + "/api/v1/auth/login",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(loginData),

        success: function (response) {
            const authData = response.data || response.body || response;
            const token = authData.token;
            const role = authData.role;

            if (!token) {
                Swal.fire({
                    icon: "error",
                    title: "Login Failed",
                    text: "Authentication token was not received."
                });
                return;
            }

            if (role !== "CUSTOMER") {
                Swal.fire({
                    icon: "error",
                    title: "Access Denied",
                    text: "This sign-in page is only for customer accounts."
                });
                return;
            }

            localStorage.setItem("jwtToken", token);
            localStorage.setItem("userRole", role);
            localStorage.setItem("userEmail", email);
            localStorage.setItem("userPermissions", JSON.stringify(authData.permissions || []));

            Swal.fire({
                icon: "success",
                title: "Login Successful!",
                timer: 1200,
                showConfirmButton: false
            }).then(() => {
                window.location.href = getRedirectTarget();
            });
        },

        error: function (xhr) {
            let message = "Invalid email or password.";

            if (xhr.responseJSON) {
                if (xhr.responseJSON.message) {
                    message = xhr.responseJSON.message;
                }
                if (xhr.responseJSON.body && typeof xhr.responseJSON.body === "string") {
                    message = xhr.responseJSON.body;
                }
            }

            Swal.fire({
                icon: "error",
                title: "Login Failed",
                text: message
            });
            console.error("Customer login error:", xhr);
        }
    });
}

$(document).ready(function () {
    const token = localStorage.getItem("jwtToken");
    const role = localStorage.getItem("userRole");

    if (token && role === "CUSTOMER") {
        window.location.href = getRedirectTarget();
    }
});