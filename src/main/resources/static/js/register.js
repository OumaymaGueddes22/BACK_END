const registerForm = document.querySelector("form");
const successMessage = document.getElementById("success-message");

registerForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const firstname = event.target.querySelector("input[name=firstname]").value;
    const lastname = event.target.querySelector("input[name=lastname]").value;
    const email = event.target.querySelector("input[name=email]").value;
    const password = event.target.querySelector("input[name=password]").value;
    const role = event.target.querySelector("select[name=role]").value;

    try {
        const response = await fetch("/api/v1/auth/register", {
            method: "POST", // Ensure that the server supports POST method for registration
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                firstname,
                lastname,
                email,
                password,
                role,
            }),
        });

        if (response.ok) {
            // Display the success message
            successMessage.style.display = "block";

            // Clear the form fields
            event.target.reset();
        } else {
            // Display an error message
            const errorMessage = await response.text();
            alert(errorMessage);
        }
    } catch (error) {
        console.error("Error:", error);
        // Handle any unexpected errors, e.g., network issues
    }
});
