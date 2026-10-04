const machineTable = document.querySelector('#machineTable tbody');
const machineInfoDiv = document.getElementById('machineInfo');
const mapContainer = document.getElementById('mapDetail');

let detailMap = null;
let detailMarker = null;

function getTagVal(parent, tagName) {
    const elements = parent.getElementsByTagName(tagName);
    return elements.length > 0 ? elements[0].textContent : "---";
}
// Carica il file XML dei distributori dal server
async function loadXML() {
    try {
        const response = await fetch("/api/distributori/xml");
        if (!response.ok) throw new Error("Errore comunicazione server");
        return await response.text();
    } catch (err) {
        console.error(err);
        alert("Impossibile caricare i dati.");
        return null;
    }
}
// riempie tabella con lista distributori
async function renderMachines() {
    const xmlText = await loadXML();
    if (!xmlText) return;

    const parser = new DOMParser();
    const xml = parser.parseFromString(xmlText, "application/xml");

    const errorNode = xml.querySelector('parsererror');
    if (errorNode) {
        console.error("ERRORE DI PARSING XML:", errorNode.textContent);
        machineTable.innerHTML = `<tr><td colspan="3" style="color: red; font-weight: bold;">Errore nel formato dei dati ricevuti.</td></tr>`;
        return;
    }
    
    const macchinette = xml.getElementsByTagName("distributore");
    machineTable.innerHTML = "";

    if (macchinette.length === 0) {
        machineTable.innerHTML = "<tr><td colspan='3'>Nessuna macchinetta trovata.</td></tr>";
        return;
    }
    
    Array.from(macchinette).forEach(mac => {
        const id = getTagVal(mac, "codice");
        const posizione = getTagVal(mac, "posizione");
        const stato = getTagVal(mac, "stato");
        const statusClass = (stato && stato !== "---") ? stato.toLowerCase() : "sconosciuto";

        const row = document.createElement("tr");
        row.classList.add("machine-row");
        row.dataset.id = id;
        row.style.cursor = "pointer";

        if (stato === "GUASTO" || stato === "MANUTENZIONE") {
            row.style.backgroundColor = "#ffe6e6";
        }
        
        row.innerHTML = `
            <td><strong>${id}</strong></td>
            <td>${posizione}</td>
            <td><span class="status ${statusClass}">${stato}</span></td>
        `;
        machineTable.appendChild(row);
    });
}
// mostra dettagli macchinetta
async function showMachineInfoById(idCercato) {
    const xmlText = await loadXML();
    if (!xmlText) return;

    const parser = new DOMParser();
    const xml = parser.parseFromString(xmlText, "application/xml");
    
    let match = null;
    const macchinette = xml.getElementsByTagName("distributore");
    for (let m of macchinette) {
        if (getTagVal(m, "codice") === idCercato) {
            match = m;
            break;
        }
    }

    if (!match) {
        machineInfoDiv.innerHTML = `<p style="color:red;">ID <strong>${idCercato}</strong> non trovato.</p>`;
        mapContainer.style.display = 'none';
        return;
    }

    const modello = getTagVal(match, "modello");
    const posizione = getTagVal(match, "posizione");
    const stato = getTagVal(match, "stato");
    const statusClass = (stato && stato !== "---") ? stato.toLowerCase() : "sconosciuto";
    
    const livelli = match.getElementsByTagName("livelli")[0];
    const locazione = match.getElementsByTagName("locazione")[0];
    
    const lat = locazione ? getTagVal(locazione, "latitudine") : "---";
    const lon = locazione ? getTagVal(locazione, "longitudine") : "---";

    const lvCaffe = livelli ? getTagVal(livelli, "caffe") : "---";
    const lvAcqua = livelli ? getTagVal(livelli, "acqua") : "---";
    const lvZucchero = livelli ? getTagVal(livelli, "zucchero") : "---";
    const lvBicchieri = livelli ? getTagVal(livelli, "bicchieri") : "---";

    const htmlRisorse = `
        <h4>Livelli Risorse</h4>
        <table class="inner-table">
            <tr><td>Caffè</td><td><b>${lvCaffe}%</b></td></tr>
            <tr><td>Acqua</td><td><b>${lvAcqua}%</b></td></tr>
            <tr><td>Zucchero</td><td><b>${lvZucchero}%</b></td></tr>
            <tr><td>Bicchieri</td><td><b>${lvBicchieri}%</b></td></tr>
        </table>
    `;

    machineInfoDiv.innerHTML = `
        <div style="border: 1px solid #ccc; padding: 15px; border-radius: 8px; background: #f9f9f9;">
            <h3 style="margin-top:0;">🔧 Pannello Controllo: ${idCercato}</h3>
            <div style="display: flex; gap: 10px; margin-bottom: 20px; flex-wrap: wrap;">
                <button onclick="ripristinaScorte('${idCercato}')" style="background: green; color: white; border: none; padding: 8px 12px; border-radius: 4px; cursor: pointer;">🔄 Ripristina Scorte</button>
                <button onclick="cambiaStato('${idCercato}', '${stato}')" style="background: orange; color: white; border: none; padding: 8px 12px; border-radius: 4px; cursor: pointer;">⚠️ Cambia Stato</button>
            </div>
            <p><strong>Modello:</strong> ${modello}</p>
            <p><strong>Posizione:</strong> ${posizione}</p>
            <p><strong>Stato:</strong> <span class="status ${statusClass}">${stato}</span></p>
            ${htmlRisorse}
        </div>
    `;

    if (lat && lon && lat !== '---' && lon !== '---') {
        mapContainer.style.display = 'block';
        initDetailMap([lat, lon]);
    } else {
        mapContainer.style.display = 'none';
    }
}
// aggiorna mappa
function initDetailMap(coords) {
    if (!detailMap) {
        detailMap = L.map('mapDetail');
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { attribution: '&copy; OpenStreetMap contributors' }).addTo(detailMap);
    }
    detailMap.setView(coords, 16);
    if (detailMarker) {
        detailMarker.setLatLng(coords);
    } else {
        detailMarker = L.marker(coords).addTo(detailMap);
    }
    setTimeout(() => detailMap.invalidateSize(), 200);
}

window.ripristinaScorte = function(id) {
    if (!confirm(`Vuoi riempire i serbatoi e attivare la macchina ${id}?`)) return;
    fetch(`/api/distributori/${id}/ripristina`, { method: "POST" }).then(res => res.text()).then(res => {
        alert(res);
        showMachineInfoById(id);
        renderMachines();
    });
}

window.cambiaStato = function(id, statoAttuale) {
    const nuovoStato = (statoAttuale === "ATTIVO") ? "MANUTENZIONE" : "ATTIVO";
    if (!confirm(`Vuoi cambiare lo stato da ${statoAttuale} a ${nuovoStato}?`)) return;
    fetch(`/api/distributori/${id}/stato?nuovoStato=${nuovoStato}`, { method: "POST" }).then(res => res.text()).then(res => {
        alert(res);
        renderMachines();
        showMachineInfoById(id);
    });
}

document.addEventListener("click", e => {
    const row = e.target.closest(".machine-row");
    if (row) {
        document.querySelectorAll(".machine-row").forEach(r => r.style.backgroundColor = "");
        row.style.backgroundColor = "#e0f7fa";
        showMachineInfoById(row.dataset.id);
    }
});

document.getElementById("searchBtn")?.addEventListener("click", () => {
    const id = document.getElementById("searchInput").value.trim();
    if (id) showMachineInfoById(id);
});

document.addEventListener("DOMContentLoaded", renderMachines);