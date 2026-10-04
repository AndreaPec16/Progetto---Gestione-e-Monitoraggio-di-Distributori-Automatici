document.addEventListener("DOMContentLoaded", () => {
    console.log("Script Cliente caricato.");

    const connectBtn = document.getElementById("connectBtn");
    if (connectBtn) {
        connectBtn.addEventListener("click", async () => {
            const machineId = document.getElementById("machineCode").value.trim();
            if (!machineId) {
                alert("Inserisci un codice macchinetta!");
                return;
            }

            try {
                const response = await fetch(`/api/connect?machineId=${machineId}`, { method: "POST" });

                if (!response.ok) {
                    const errorText = await response.text();
                    throw new Error(errorText || "Errore sconosciuto dal server.");
                }

                alert("Connesso alla macchinetta: " + machineId);
                window.location.reload();

            } catch (error) {
                alert("Errore: " + error.message);
            }
        });
    }

    const disconnectBtn = document.getElementById("disconnectBtn");
    if (disconnectBtn) {
        disconnectBtn.addEventListener("click", () => {
            fetch(`/api/disconnect`, { method: "POST" })
                .then(response => {
                    if (response.ok) {
                        alert("Disconnesso.");
                        window.location.reload();
                    } else {
                        alert("Errore durante la disconnessione.");
                    }
                });
        });
    }

    const rechargeBtn = document.getElementById("rechargeBtn");
    if (rechargeBtn) {
        rechargeBtn.addEventListener("click", () => {
            const amount = prompt("Quanto vuoi ricaricare? (Es. 5.00)");
            if (amount && !isNaN(amount)) {
                fetch(`/api/utenti/ricarica?importo=${amount}`, { method: "POST" })
                    .then(response => response.text())
                    .then(msg => {
                        alert(msg);
                        if (msg.startsWith("OK")) {
                            window.location.reload();
                        }
                    });
            } else if (amount !== null) {
                alert("Inserisci un numero valido.");
            }
        });
    }
});
