document.addEventListener("DOMContentLoaded", function () {
    const requestCodeForm = document.getElementById("request-code-form");
    const resetPasswordForm = document.getElementById("reset-password-form");
    const message = document.getElementById("message");

    requestCodeForm.addEventListener("submit", function (e) {
        e.preventDefault();
        const phoneNumber = document.getElementById("phoneNumber").value;

        const data = "code de réinitialisation a été envoyé";

        message.textContent = data;
        // Make an AJAX request to request the reset code
        fetch("/api/v1/users/request-code?phoneNumber=" + phoneNumber, {
            method: "POST"
        })
            .then(response => response.text())
            .then(data => {
                message.textContent = data;

                // Check the message content
                if (message.textContent.trim() !== "") {
                    resetPasswordForm.style.display = "block";
                    requestCodeForm.style.display = "none"; // Hide the request-code-form
                }

            });
    });

    resetPasswordForm.addEventListener("submit", function (e) {
        e.preventDefault();
        const phoneNumber = document.getElementById("phoneNumber").value;
        const resetCode = document.getElementById("resetCode").value;
        const newPassword = document.getElementById("newPassword").value;

        // Make an AJAX request to reset the password
        fetch("/api/v1/users/reset?phoneNumber=" + phoneNumber + "&resetCode=" + resetCode + "&newPassword=" + newPassword, {
            method: "POST"
        })
            .then(response => response.text())
            .then(data => {
                message.textContent = data;
            });
    });
});
