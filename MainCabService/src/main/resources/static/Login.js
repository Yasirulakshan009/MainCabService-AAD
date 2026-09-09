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
        identifier: email,
        password: password
    };

    $.ajax({
        url: "http://localhost:8080/api/v1/auth/login",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(loginData),
        success: function(response) {
            console.log("Full Login Response:", response);
            const resData = response.data || response.body || response;

            const token = resData.token || response.token || "";
            localStorage.setItem("jwtToken", token);

            const role = resData.role || response.role;
            localStorage.setItem("userRole", role);

            const email = resData.email || resData.userEmail || loginData.identifier ;
            localStorage.setItem("userEmail", email);

            const permissions = resData.permissions || response.permissions || [];
            localStorage.setItem("userPermissions", JSON.stringify(permissions));

            window.location.href = "pos.html";
        },
        error: function(xhr, status, error) {
            errorMsg.style.display = "block";

            var errObj = xhr.responseJSON;

            if (errObj && errObj.body && typeof errObj.body === "object" && !Array.isArray(errObj.body)) {
                errorMsg.innerText = Object.values(errObj.body).join("\n");
            } else if (errObj && errObj.message) {
                errorMsg.innerText = errObj.message;
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