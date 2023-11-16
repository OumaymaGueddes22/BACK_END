const registerForm = document.getElementById("register-form");
const successMessage = document.getElementById("success-message");
const fileInput = document.querySelector('input[type="file"]');

registerForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const formData = new FormData(registerForm);
    const fileInput = document.querySelector('input[type="file"]');

    // Check if a file is selected before adding it to the formData
    if (fileInput.files.length > 0) {
        formData.append('file', fileInput.files[0]);
    }

    try {
        const response = await fetch("/api/v1/auth/register", {
            method: "POST",
            body: formData,
        });

        const plainFormData = {};
        formData.forEach((value, key) => {
            plainFormData[key] = value;
        });

        console.log(plainFormData);
        if (response.ok) {
            const uploadedFileName = fileInput.files[0]?.name || 'No file selected';
            successMessage.innerHTML = `Success`;
            successMessage.style.display = "block";
            registerForm.reset();
        } else {
            const errorMessage = await response.text();
            alert(errorMessage);
        }
    } catch (error) {
        console.error("Error:", error);
    }
});

fileInput.addEventListener('change', () => {
    const fileName = fileInput.files[0]?.name || 'No file selected';
    document.querySelector('.file-upload-info').value = fileName;
});

fileInput.addEventListener('change', () => {
    const fileName = fileInput.files[0]?.name || 'No file selected';
    document.querySelector('.file-upload-info').value = fileName;
});
