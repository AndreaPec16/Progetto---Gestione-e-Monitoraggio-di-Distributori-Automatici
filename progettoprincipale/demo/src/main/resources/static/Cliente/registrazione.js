document.getElementById("registerForm").addEventListener("submit", function (e) {
    e.preventDefault();

    const nuovoUtente = {
        nome: document.getElementById("nome").value,
        username: document.getElementById("username").value,
        password: document.getElementById("password").value,
        email: document.getElementById("email").value
    };

    fetch("/api/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(nuovoUtente)
    })
    .then(response => {
        if (response.ok) {
            return response.text();
        } else {

            return response.text().then(text => { throw new Error(text) });
        }
    })
    .then(messaggio => {
        alert("Registrazione completata con successo!");
        window.location.href = "/login"; // Vai alla pagina di login
    })
    .catch(err => {
        console.error("Errore registrazione:", err);
        alert("Errore durante la registrazione: " + err.message);
    });
});
