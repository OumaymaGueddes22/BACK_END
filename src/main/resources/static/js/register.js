const registerForm = document.getElementById("register-form");
const successMessage = document.getElementById("success-message");

registerForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const firstname = registerForm.querySelector("input[name=firstname]").value;
    const lastname = registerForm.querySelector("input[name=lastname]").value;
    const phoneNumber = registerForm.querySelector("input[name=phoneNumber]").value;
    const email = registerForm.querySelector("input[name=email]").value;
    const password = registerForm.querySelector("input[name=password]").value;
    const role = registerForm.querySelector("select[name=role]").value;

    try {
        const response = await fetch("/api/v1/auth/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                firstname,
                lastname,
                phoneNumber,
                email,
                password,
                role,
            }),
        });

        if (response.ok) {
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
