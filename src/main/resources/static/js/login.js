const loginForm = document.querySelector("form");
const errorMessage = document.getElementById("error-message");

loginForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const phoneNumber = event.target.querySelector("input[name=phoneNumber]").value;
    const password = event.target.querySelector("input[name=password]").value;

    const response = await fetch("/api/v1/auth/authenticate", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            phoneNumber,
            password,
        }),
    });

    if (response.ok) {
        const authResponse = await response.json();

        localStorage.setItem("authResponse", JSON.stringify(authResponse));

        window.location.href = "/";
    } else {
        // Affiche un message d'erreur en tant qu'alerte Bootstrap
        errorMessage.textContent = "Authentification échouée. Veuillez vérifier votre email et votre mot de passe.";
        errorMessage.style.display = "block";

        // Efface les champs du formulaire
        event.target.reset();
    }
});
