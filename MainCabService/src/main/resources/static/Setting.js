$(document).ready(function() {
    loadSystemUsers();
    applySidebarPermissions();
});

function applySidebarPermissions() {
    const userRole = localStorage.getItem("userRole");
    const storedPermissions = JSON.parse(localStorage.getItem("userPermissions")) || [];

    if (userRole === "ADMIN") {
        return;
    }

    $('[data-permission]').each(function() {
        const permission = $(this).attr('data-permission');

        if (storedPermissions.includes(permission)) {
            $(this).show();
        } else {
            $(this).hide();
        }
    });
}

function togglePermissions() {
    const roleSelect = document.getElementById('regRole').value;
    const permissionsBox = document.getElementById('staffPermissionsBox');

    if (roleSelect === "2") {
        permissionsBox.style.display = "block";
    } else {
        permissionsBox.style.display = "none";
        document.querySelectorAll('input[name="regSection"]').forEach(cb => cb.checked = false);
    }
}

function handleUserRegister(event) {
    event.preventDefault();

    const name = document.getElementById('regName').value.trim();
    const phone = document.getElementById('regPhone').value.trim();
    const email = document.getElementById('regEmail').value.trim();
    const roleId = document.getElementById('regRole').value;
    const statusStr = document.getElementById('regStatus').value;
    const password = document.getElementById('regPassword').value;
    const confirmPassword = document.getElementById('regConfirmPassword').value;

    if (password !== confirmPassword) {
        Swal.fire({
            toast: true,
            position: 'top-end',
            icon: 'error',
            title: 'Passwords do not match!',
            showConfirmButton: false,
            timer: 3000
        });
        return;
    }

    let selectedPermissions = [];
    if (roleId === "2") {
        document.querySelectorAll('input[name="regSection"]:checked').forEach((checkbox) => {
            selectedPermissions.push(checkbox.value);
        });
    }

    const registerData = {
        userName: name,
        userEmail: email,
        phone: phone,
        userPassword: password,
        confirmPassword: confirmPassword,
        status: statusStr,
        userRole: {
            userRoleID: parseInt(roleId),
            role: (roleId === "1") ? "ADMIN" : "STAFF"
        },
        permissions: selectedPermissions
    };

    $.ajax({
        url: "http://localhost:8080/api/v1/auth/register",
        type: "POST",
        contentType: "application/json",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("jwtToken")
        },
        data: JSON.stringify(registerData),
        success: function(response) {
            const successMsg = response.message || "User registered successfully!";

            Swal.fire({
                toast: true,
                position: 'top-end',
                icon: 'success',
                title: successMsg,
                showConfirmButton: false,
                timer: 3000,
                timerProgressBar: true
            });

            resetRegisterForm();
            loadSystemUsers();
        },
        error: function(xhr, status, error) {
            let errorMsg = "Registration failed!";
            let errObj = xhr.responseJSON;

            if (errObj && errObj.body && typeof errObj.body === "object" && !Array.isArray(errObj.body)) {
                errorMsg = Object.values(errObj.body).join("\n");
            } else if (errObj && errObj.message) {
                errorMsg = errObj.message;
            }

            Swal.fire({
                toast: true,
                position: 'top-end',
                icon: 'error',
                title: errorMsg,
                showConfirmButton: false,
                timer: 3000,
                timerProgressBar: true
            });
            console.error("Register error details: ", xhr);
        }
    });
}

function resetRegisterForm() {
    document.getElementById('registerForm').reset();
    togglePermissions();
}

function loadSystemUsers() {
    $.ajax({
        url: "http://localhost:8080/api/v1/auth/all",
        type: "GET",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("jwtToken")
        },
        success: function(response) {
            console.log("Users List Received:", response);

            let users = [];
            if (Array.isArray(response)) {
                users = response;
            } else if (response && Array.isArray(response.body)) {
                users = response.body;
            } else if (response && Array.isArray(response.data)) {
                users = response.data;
            }

            const tableBody = document.getElementById('staffTableBody');
            if (!tableBody) return;
            tableBody.innerHTML = "";

            if (!users || users.length === 0) {
                tableBody.innerHTML = `<tr><td colspan="5" style="text-align: center;">No users found</td></tr>`;
                return;
            }

            users.forEach(user => {
                const roleName = user.userRole ? user.userRole.role : "N/A";
                const statusBadge = user.status === "ACTIVE"
                    ? `<span style="color: green; font-weight: bold;">Active</span>`
                    : `<span style="color: red; font-weight: bold;">Inactive</span>`;

                const actionBtnText = user.status === "ACTIVE" ? "Make Inactive" : "Make Active";
                const newStatus = user.status === "ACTIVE" ? "INACTIVE" : "ACTIVE";
                const btnColor = user.status === "ACTIVE" ? "#ff4d4d" : "#28a745";
                const userId = user.userID || user.id;

                const row = `
                    <tr>
        <td>${user.userName || 'N/A'}</td>
        <td>${user.userEmail || 'N/A'}</td>
        <td>${user.phone || 'N/A'}</td>
        <td><span class="badge">${roleName}</span></td>
        <td>${statusBadge}</td>
        <td>
            <button onclick='openEditModal(${userId}, "${user.userName}", "${user.phone}", "${roleName}", ${JSON.stringify(user.permissions || [])})' 
                    style="padding: 5px 10px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; margin-right: 5px;">
                Edit
            </button>
            <button onclick="updateUserStatus(${userId}, '${newStatus}')" 
                    style="padding: 5px 10px; background-color: ${btnColor}; color: white; border: none; border-radius: 4px; cursor: pointer;">
                ${actionBtnText}
            </button>
        </td>
    </tr>
                `;
                tableBody.innerHTML += row;
            });
        },
        error: function(xhr, status, error) {
            console.error("Failed to load users:", xhr);
        }
    });
}

function updateUserStatus(userId, newStatus) {
    Swal.fire({
        title: 'Are you sure?',
        text: `Do you want to change this user status to ${newStatus}?`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#28a745',
        cancelButtonColor: '#ff4d4d',
        confirmButtonText: 'Yes, change it!'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: `http://localhost:8080/api/v1/auth/user-status/${userId}?status=${newStatus}`,
                type: "PUT",
                headers: {
                    "Authorization": "Bearer " + localStorage.getItem("jwtToken")
                },
                success: function(response) {
                    const successMsg = response.message || "User status updated successfully!";

                    Swal.fire({
                        toast: true,
                        position: 'top-end',
                        icon: 'success',
                        title: successMsg,
                        showConfirmButton: false,
                        timer: 3000,
                        timerProgressBar: true
                    });

                    loadSystemUsers();
                },
                error: function(xhr, status, error) {
                    let errorMsg = "Failed to update status!";

                    if (xhr.responseJSON && xhr.responseJSON.message) {
                        errorMsg = xhr.responseJSON.message;
                    } else if (xhr.responseText) {
                        errorMsg = xhr.responseText;
                    }

                    Swal.fire({
                        icon: 'error',
                        title: 'Action Denied',
                        text: errorMsg,
                        confirmButtonColor: '#ff4d4d'
                    });
                }
            });
        }
    });
}

function openEditModal(userId, currentName, currentPhone, currentRole, userPermissions) {
    console.log("User Permissions for Edit:", userPermissions);
    let permissionsHtml = '';

    if (currentRole === "STAFF") {
        permissionsHtml = `
            <div style="text-align: left; margin-top: 15px; max-height: 150px; overflow-y: auto; padding: 10px; border: 1px solid #ddd; border-radius: 5px;">
                <label style="font-weight: bold; display: block; margin-bottom: 5px;">Permissions:</label>`;

        const allSections = [
            "OVERVIEW",
            "FLEET",
            "BOOKINGS",
            "BOOKING_CUSTOMER",
            "CUSTOMER",
            "MAINTENANCE",
            "RENTAL",
            "RETURNS",
            "PAYMENT",
            "SETTINGS",
            "REGISTER_CUSTOMER",
            "WEBSITE_SETTING",
            "LEGAL_AND_FAQ",
            "CUSTOMER_REVIEWS"
        ];
        allSections.forEach(section => {
            let isChecked = false;
            if (userPermissions && Array.isArray(userPermissions)) {
                isChecked = userPermissions.includes(section);
            }

            permissionsHtml += `
                <div style="margin-bottom: 5px;">
                    <input type="checkbox" name="editSection" value="${section}" ${isChecked ? 'checked' : ''} style="margin-right: 8px;">
                    <label>${section}</label>
                </div>`;
        });
        permissionsHtml += `</div>`;
    }

    Swal.fire({
        title: 'Update User Profile',
        html:
            `<input id="swal-input-name" class="swal2-input" placeholder="Full Name" value="${currentName}">` +
            `<input id="swal-input-phone" class="swal2-input" placeholder="Phone Number" value="${currentPhone}">` +
            permissionsHtml,
        focusConfirm: false,
        showCancelButton: true,
        confirmButtonText: 'Update',
        confirmButtonColor: '#007bff',
        preConfirm: () => {
            const name = document.getElementById('swal-input-name').value;
            const phone = document.getElementById('swal-input-phone').value;

            let selectedPermissions = [];
            if (currentRole === "STAFF") {
                document.querySelectorAll('input[name="editSection"]:checked').forEach((checkbox) => {
                    selectedPermissions.push(checkbox.value);
                });
            }

            if (!name || !phone) {
                Swal.showValidationMessage('Please enter both name and phone number!');
            }
            return { userName: name, phone: phone, permissions: selectedPermissions };
        }
    }).then((result) => {
        if (result.isConfirmed) {
            sendUpdateProfileRequest(userId, result.value);
        }
    });
}

function sendUpdateProfileRequest(userId, updateData) {
    $.ajax({
        url: `http://localhost:8080/api/v1/auth/update-profile/${userId}`,
        type: "PUT",
        contentType: "application/json",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("jwtToken")
        },
        data: JSON.stringify(updateData),
        success: function(response) {
            Swal.fire({
                toast: true,
                position: 'top-end',
                icon: 'success',
                title: response.message || "Profile updated successfully!",
                showConfirmButton: false,
                timer: 3000,
                timerProgressBar: true
            });
            loadSystemUsers();
        },
        error: function(xhr) {
            let errorMsg = "Failed to update profile!";
            if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMsg = xhr.responseJSON.message;
            }
            Swal.fire({
                icon: 'error',
                title: 'Update Failed',
                text: errorMsg,
                confirmButtonColor: '#ff4d4d'
            });
        }
    });
}


function updateAdminEmail(event) {
    event.preventDefault();

    const currentEmail = document.getElementById('currentEmail').value.trim();
    const newEmail = document.getElementById('newEmail').value.trim();
    const confirmNewEmail = document.getElementById('confirmEmail').value.trim();

    if (newEmail !== confirmNewEmail) {
        Swal.fire({
            toast: true,
            position: 'top-end',
            icon: 'error',
            title: 'New emails do not match!',
            showConfirmButton: false,
            timer: 3000,
            timerProgressBar: true
        });
        return;
    }

    const emailData = {
        currentEmail: currentEmail,
        newEmail: newEmail,
        confirmNewEmail: confirmNewEmail
    };

    $.ajax({
        url: "http://localhost:8080/api/v1/auth/change-email",
        type: "PUT",
        contentType: "application/json",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("jwtToken")
        },
        data: JSON.stringify(emailData),
        success: function(response) {
            Swal.fire({
                icon: 'success',
                title: 'Email Updated Successfully!',
                text: 'Please log in again with your new email.',
                confirmButtonColor: '#007bff',
                allowOutsideClick: false
            }).then((result) => {
                if (result.isConfirmed) {
                    localStorage.removeItem("jwtToken");
                    localStorage.removeItem("user");

                    window.location.href = "login.html";
                }
            });
        },
        error: function(xhr) {
            let errorMsg = "Failed to update email!";
            let errObj = xhr.responseJSON;

            if (errObj && errObj.body && typeof errObj.body === "object" && !Array.isArray(errObj.body)) {
                errorMsg = Object.values(errObj.body).join("\n");
            } else if (errObj && errObj.message) {
                errorMsg = errObj.message;
            } else if (xhr.responseText) {
                errorMsg = xhr.responseText;
            }
            Swal.fire({
                icon: 'error',
                title: 'Update Failed',
                text: errorMsg,
                confirmButtonColor: '#ff4d4d'
            });
        }
    });
}


function updateAdminPassword(event) {
    event.preventDefault();

    const emailField = document.getElementById('passVerifyEmail');
    const currentPassField = document.getElementById('currentPassword');
    const newPassField = document.getElementById('newPassword');
    const confirmPassField = document.getElementById('confirmPassword');

    if (!emailField || !currentPassField || !newPassField || !confirmPassField) {
        console.error("One or more password form elements not found!");
        return;
    }

    const email = emailField.value.trim();
    const currentPassword = currentPassField.value.trim();
    const newPassword = newPassField.value.trim();
    const confirmNewPassword = confirmPassField.value.trim();

    if (newPassword !== confirmNewPassword) {
        Swal.fire({
            toast: true,
            position: 'top-end',
            icon: 'error',
            title: 'New passwords do not match!',
            showConfirmButton: false,
            timer: 3000,
            timerProgressBar: true
        });
        return;
    }

    const passwordData = {
        email: email,
        currentPassword: currentPassword,
        newPassword: newPassword,
        confirmNewPassword: confirmNewPassword
    };

    $.ajax({
        url: "http://localhost:8080/api/v1/auth/change-password",
        type: "PUT",
        contentType: "application/json",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem("jwtToken")
        },
        data: JSON.stringify(passwordData),
        success: function(response) {
            Swal.fire({
                icon: 'success',
                title: 'Password Updated Successfully!',
                text: 'Please log in again with your new password.',
                confirmButtonColor: '#007bff',
                allowOutsideClick: false
            }).then((result) => {
                if (result.isConfirmed) {
                    localStorage.removeItem("jwtToken");
                    localStorage.removeItem("user");
                    window.location.href = "login.html";
                }
            });
        },
        error: function(xhr) {
            let errorMsg = "Failed to update password!";
            let errObj = xhr.responseJSON;

            if (errObj && errObj.body && typeof errObj.body === "object" && !Array.isArray(errObj.body)) {
                errorMsg = Object.values(errObj.body).join("\n");
            } else if (errObj && errObj.message) {
                errorMsg = errObj.message;
            } else if (xhr.responseText) {
                errorMsg = xhr.responseText;
            }
            Swal.fire({
                icon: 'error',
                title: 'Update Failed',
                text: errorMsg,
                confirmButtonColor: '#ff4d4d'
            });
        }
    });
}