const loginForm = document.querySelector("form");
const errorMessage = document.getElementById("error-message");

loginForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const phoneNumber = event.target.querySelector("input[name=phoneNumber]").value;
    const password = event.target.querySelector("input[name=password]").value;

    // Envoie des informations de connexion au serveur
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
        // Connexion réussie, stocke le token JWT dans le stockage local
        const authResponse = await response.json();
        const accessToken = authResponse.accessToken;
        localStorage.setItem("accessToken",  JSON.stringify(accessToken));

        // Redirige l'utilisateur vers la plateforme
        window.location.href = "../chat.html";
    } else {
        // Affiche un message d'erreur as a Bootstrap alert
        errorMessage.textContent = "Authentification échouée. Veuillez vérifier votre email et votre mot de passe.";
        errorMessage.style.display = "block";

        // Clear the form fields
        event.target.reset();
    }
});
