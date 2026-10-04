document.addEventListener("DOMContentLoaded", () => {
    const machineId = document.body.dataset.id;
    const username = document.body.dataset.username;
    const lat = document.body.dataset.lat;
    const lon = document.body.dataset.lon;

    const productGrid = document.getElementById('productGrid');
    const productsSection = document.querySelector('.products');
    const sugarSection = document.getElementById('sugarSection');
    const progressSection = document.getElementById('progressSection');
    const resultSection = document.getElementById('result');
    const progressText = document.getElementById('progressText');
    const progressFill = document.getElementById('progressFill');

    let selectedProduct = null;

    async function loadProducts() {
        if (!productGrid) return;
        try {
            const response = await fetch('/api/prodotti');
            if (!response.ok) throw new Error('Errore nel caricamento dei prodotti.');
            
            const prodotti = await response.json();
            productGrid.innerHTML = ''; 

            prodotti.forEach(p => {
                const card = document.createElement('div');
                card.className = 'product-card';
                card.dataset.product = p.nome;
                
                card.innerHTML = `
                    <img src="/immagini/img_tazza.png" alt="${p.nome}">
                    <h3>${p.nome}</h3>
                    <p>${Number(p.prezzo).toFixed(2)} €</p>
                `;
                card.addEventListener('click', () => handleProductSelection(p.nome));
                productGrid.appendChild(card);
            });
        } catch (error) {
            productGrid.innerHTML = `<p style="color:red;">${error.message}</p>`;
        }
    }

    function handleProductSelection(productName) {
        selectedProduct = productName;
        if (productsSection) productsSection.style.display = 'none';
        if (sugarSection) sugarSection.style.display = 'block';
    }

    async function sendHeartbeat() {
        const details = { 'codice': machineId, 'lat': lat, 'lon': lon };
        let formBody = [];
        for (const property in details) {
            formBody.push(encodeURIComponent(property) + "=" + encodeURIComponent(details[property]));
        }
        formBody = formBody.join("&");
        try {
            const monitorUrl = 'http://localhost:8081/monitor/api/heartbeat';
            await fetch(monitorUrl, {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' },
                body: formBody
            });
        } catch (error) {
            console.error(`Errore di rete durante l'heartbeat: ${error}`);
        }
    }

    async function startPurchase(product, sugar) {
        if (progressText) progressText.textContent = "Controllo disponibilità...";
        if (progressFill) progressFill.style.width = "20%";

        try {
            const url = `/api/distributori/${machineId}/acquista?username=${username}&prodotto=${product}&zucchero=${sugar}`;
            const response = await fetch(url, { method: 'POST' });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || `Errore HTTP ${response.status}`);
            }
            
            if (progressText) progressText.textContent = "Erogazione in corso...";
            if (progressFill) progressFill.style.width = "70%";
            await new Promise(resolve => setTimeout(resolve, 1500));

            if (progressFill) progressFill.style.width = "100%";
            if (progressText) progressText.textContent = "Completato!";
            await new Promise(resolve => setTimeout(resolve, 1000));
            
            if (progressSection) progressSection.style.display = 'none';
            if (resultSection) {
                resultSection.style.display = 'block';
                resultSection.textContent = `✅ ${product} pronto!`;
            }
            setTimeout(() => window.location.reload(), 4000);

        } catch (err) {
            if (progressSection) progressSection.style.display = 'none';
            if (resultSection) {
                resultSection.style.display = 'block';
                resultSection.textContent = `❌ Errore: ${err.message.replace("ERRORE: ", "")}`;
            }
            setTimeout(() => window.location.reload(), 4000);
        }
    }

    async function pollUserStatus() {
        console.log("Polling...");
        
        const cacheBustingUrl = `/api/distributori/${machineId}/check-utente?_=${new Date().getTime()}`;

        try {
            const response = await fetch(cacheBustingUrl, {
                cache: 'no-store'
            });

            console.log("Stato risposta:", response.status);
            console.log("Username attuale sulla pagina:", username || "Nessuno");

            if (response.status === 200) {
                const utenteConnesso = await response.json();
                console.log("Utente connesso ricevuto:", utenteConnesso);

                if (!username) {
                    console.log("Pagina in attesa, utente rilevato -> Ricarico.");
                    window.location.reload();
                    return;
                }
                
                const creditElement = document.getElementById('userCredit');
                if (creditElement && username === utenteConnesso.username) {
                    const currentCreditText = creditElement.textContent;
                    const newCreditText = utenteConnesso.credito.toFixed(2) + ' €';
                    if (currentCreditText !== newCreditText) {
                        console.log(`Aggiorno credito da ${currentCreditText} a ${newCreditText}`);
                        creditElement.textContent = newCreditText;
                    }
                }
            } else if (response.status === 204) {
                if (username) {
                    console.log("Pagina con utente, ma nessuno più connesso -> Ricarico.");
                    window.location.reload();
                }
            }
        } catch (error) {
            console.error("Errore durante il polling:", error);
        }
    }

    document.querySelectorAll('.sugar-btn').forEach(button => {
        button.addEventListener('click', () => {
            const sugar = parseInt(button.dataset.sugar);
            if (sugarSection) sugarSection.style.display = 'none';
            if (progressSection) progressSection.style.display = 'block';
            startPurchase(selectedProduct, sugar);
        });
    });

    sendHeartbeat();
    setInterval(sendHeartbeat, 60000);
    setInterval(pollUserStatus, 3000);

    if (username) {
        loadProducts();
    }
});
