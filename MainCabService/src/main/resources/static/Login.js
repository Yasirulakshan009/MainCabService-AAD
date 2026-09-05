function handleLogin(event) {
    event.preventDefault();

    const email = document.getElementById('adminEmail').value.trim();
    const password = document.getElementById('adminPassword').value.trim();
    const errorMsg = document.getElementById('errorMsg');

    if (!email || !password) {
        errorMsg.style.display = "block";
        errorMsg.innerText = "Please fill in all fields!";
        return;
    }

    const loginData = {
        email: email,
        password: password
    };

    $.ajax({
        url: "http://localhost:8080/api/v1/auth/login",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(loginData),
        success: function(response) {
            console.log("Server Response:", response);

            const token = response.body || response.data || response.token;

            if (token) {
                localStorage.setItem("jwtToken", token);

                window.location.href = "pos.html";
            } else {
                errorMsg.style.display = "block";
                errorMsg.innerText = "Token not found in server response!";
            }
        },
        error: function(xhr, status, error) {
            errorMsg.style.display = "block";

            if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMsg.innerText = xhr.responseJSON.message;
            } else {
                errorMsg.innerText = "Invalid Email or Password!";
            }
            console.error("Login error details: ", xhr);
        }
    });
}

function togglePasswordVisibility() {
    const passwordInput = document.getElementById('adminPassword');
    const toggleIcon = document.getElementById('togglePassword');

    if (passwordInput.type === "password") {
        passwordInput.type = "text";
        toggleIcon.classList.replace("ri-eye-off-line", "ri-eye-line");
    } else {
        passwordInput.type = "password";
        toggleIcon.classList.replace("ri-eye-line", "ri-eye-off-line");
    }
}

function forgotPassword(event) {
    event.preventDefault();
    alert("Password reset instructions will be sent to your registered email.");
}