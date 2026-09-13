const CONTACT_API_BASE_URL = "http://localhost:8080";

document.getElementById("contactForm")?.addEventListener("submit", function (event) {
    event.preventDefault();

    const subjectSelect = document.getElementById("contactSubject");
    const subjectText = subjectSelect.options[subjectSelect.selectedIndex]?.text || subjectSelect.value;

    const contactMessageData = {
        name: $('#contactName').val(),
        email: $('#contactEmail').val(),
        phone: $('#contactPhone').val(),
        subject: subjectText,
        message: $('#contactMessage').val()
    };

    const submitBtn = document.getElementById("contactSubmitBtn");

    if (submitBtn) {
        submitBtn.disabled = true;
        submitBtn.innerText = "Sending...";
    }

    $.ajax({
        url: CONTACT_API_BASE_URL + "/v1/contact",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(contactMessageData),

        success: function () {
            Swal.fire({
                icon: "success",
                title: "Message Sent!",
                text: "Thank you for contacting Aura Cabs. We will get back to you shortly.",
                confirmButtonColor: "#0d6efd"
            });

            document.getElementById("contactForm").reset();
        },

        error: function (xhr) {
            let errorText =
                xhr.responseJSON?.message ||
                "Unable to send your message. Please try again later.";

            Swal.fire({
                icon: "error",
                title: "Message Failed",
                text: errorText,
                confirmButtonColor: "#dc3545"
            });

            console.error("Contact message send error:", xhr);
        },

        complete: function () {
            if (submitBtn) {
                submitBtn.disabled = false;
                submitBtn.innerHTML = '<i class="ri-send-plane-line"></i> Send Message';
            }
        }
    });
});