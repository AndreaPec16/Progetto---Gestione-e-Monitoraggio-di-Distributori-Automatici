const machineTable = document.querySelector('#machineTable tbody');
const staffTable = document.querySelector('#staffTable tbody');
const modalMachine = document.getElementById('modalMachine');
const modalStaff = document.getElementById('modalStaff');

let map = null;
let marker = null;

function getTagVal(parent, tagName) {
    const elements = parent.getElementsByTagName(tagName);
    return elements.length > 0 && elements[0].textContent ? elements[0].textContent : "N/D";
}
// carica tabella distributori
async function loadMachines() {
    try {
        const response = await fetch("/api/distributori/xml");
        if (!response.ok) throw new Error("Errore Server");
        const text = await response.text();
        const parser = new DOMParser();
        const xml = parser.parseFromString(text, "application/xml");
        const macchinette = xml.getElementsByTagName("distributore");
        machineTable.innerHTML = '';
        if (macchinette.length === 0) {
            machineTable.innerHTML = "<tr><td colspan='5'>Nessuna macchinetta trovata.</td></tr>";
            return;
        }
        Array.from(macchinette).forEach(m => {
            const code = getTagVal(m, "codice");
            const location = getTagVal(m, "posizione");
            const status = getTagVal(m, "stato");
            const model = getTagVal(m, "modello");
            const row = document.createElement('tr');
            const statusClass = (status && status !== "N/D") ? status.toLowerCase() : "sconosciuto";
            row.innerHTML = `
                <td><strong>${code}</strong></td>
                <td>${location}</td>
                <td>${model}</td>
                <td><span class="status ${statusClass}">${status}</span></td>
                <td><button onclick="eliminaMacchinetta('${code}')" style="background-color:red;">🗑️ Elimina</button></td>
            `;
            machineTable.appendChild(row);
        });
    } catch (err) {
        console.error("Errore durante il caricamento delle macchinette:", err);
        machineTable.innerHTML = "<tr><td colspan='5'>Errore caricamento dati dal server.</td></tr>";
    }
}

document.getElementById('addMachineBtn').addEventListener('click', () => {
    modalMachine.style.display = 'flex';
    setTimeout(() => initMap(), 200);
});
document.getElementById('cancelAddMachine').addEventListener('click', () => modalMachine.style.display = 'none');
// inizializza mappa
function initMap() {
    if (map) { map.invalidateSize(); return; }
    map = L.map('mapSelect').setView([41.9028, 12.4964], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);
    map.on('click', e => {
        const lat = e.latlng.lat.toFixed(6);
        const lon = e.latlng.lng.toFixed(6);
        document.getElementById('newMachineLat').value = lat;
        document.getElementById('newMachineLon').value = lon;
        if (marker) marker.setLatLng(e.latlng);
        else marker = L.marker(e.latlng).addTo(map);
    });
}

document.getElementById('confirmAddMachine').addEventListener('click', () => {
    const code = document.getElementById('newMachineCode').value.trim();
    const location = document.getElementById('newMachineLocation').value.trim();
    const model = document.getElementById('newMachineModel').value.trim();
    const lat = document.getElementById('newMachineLat').value.trim();
    const lon = document.getElementById('newMachineLon').value.trim();
    if (!code || !location || !model || !lat || !lon) { alert("Compila tutti i campi!"); return; }
    fetch(`/api/distributori/aggiungi?codice=${code}&indirizzo=${location}&modello=${model}&lat=${lat}&lon=${lon}`, { method: 'POST' })
        .then(res => res.text()).then(msg => {
            alert(msg);
            if (msg.startsWith("OK")) {
                modalMachine.style.display = 'none';
                loadMachines();
                document.getElementById('newMachineCode').value = "";
                document.getElementById('newMachineLocation').value = "";
                document.getElementById('newMachineModel').value = "";
                document.getElementById('newMachineLat').value = "";
                document.getElementById('newMachineLon').value = "";
                if (marker) { map.removeLayer(marker); marker = null; }
            }
        }).catch(err => alert("Errore: " + err));
});

window.eliminaMacchinetta = function(codice) {
    if (!confirm(`Eliminare la macchinetta ${codice}?`)) return;
    fetch(`/api/distributori/${codice}/elimina`, { method: 'DELETE' })
        .then(res => res.text()).then(msg => { alert(msg); loadMachines(); });
};
// carica tabella addetti
async function loadStaff() {
    try {
        const response = await fetch("/api/manutentori/xml");
        if (!response.ok) throw new Error("Errore Server");
        const text = await response.text();
        const parser = new DOMParser();
        const xml = parser.parseFromString(text, "application/xml");
        const manutentori = xml.getElementsByTagName("manutentore");
        staffTable.innerHTML = '';
        Array.from(manutentori).forEach(m => {
            const id = m.getElementsByTagName("id")[0].textContent;
            const nome = m.getElementsByTagName("nome")[0].textContent;
            const email = m.getElementsByTagName("email")[0].textContent;
            const row = document.createElement('tr');
            row.innerHTML = `<td>${nome}</td><td>${email}</td><td><button onclick="eliminaStaff(${id})" style="background-color:red;">❌ Elimina</button></td>`;
            staffTable.appendChild(row);
        });
    } catch (err) {
        console.error(err);
        staffTable.innerHTML = "<tr><td colspan='3'>Nessun addetto o errore server.</td></tr>";
    }
}

document.getElementById('addStaffBtn').addEventListener('click', () => modalStaff.style.display = 'flex');
document.getElementById('cancelAddStaff').addEventListener('click', () => modalStaff.style.display = 'none');

document.getElementById('confirmAddStaff').addEventListener('click', () => {
    const nome = document.getElementById('newStaffName').value.trim();
    const email = document.getElementById('newStaffEmail').value.trim();
    const username = document.getElementById('newStaffUsername').value.trim();
    const password = document.getElementById('newStaffPassword').value.trim();

    if (!nome || !email || !username || !password) {
        alert("Compila tutti i campi!");
        return;
    }

    const formData = new URLSearchParams();
    formData.append('nome', nome);
    formData.append('email', email);
    formData.append('username', username);
    formData.append('password', password);

    fetch('/api/manutentori/aggiungi', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: formData
    })
    .then(res => {
        if (!res.ok) {
            return res.text().then(text => { throw new Error(text || 'Errore dal server') });
        }
        return res.text();
    })
    .then(msg => {
        alert(msg);
        if (msg.startsWith("OK")) {
            modalStaff.style.display = 'none';
            loadStaff();
            document.getElementById('newStaffName').value = "";
            document.getElementById('newStaffEmail').value = "";
            document.getElementById('newStaffUsername').value = "";
            document.getElementById('newStaffPassword').value = "";
        }
    }).catch(err => {
        alert("Errore: " + err.message);
    });
});
// elimina addetto
window.eliminaStaff = function(id) {
    if (!confirm("Vuoi davvero eliminare questo addetto?")) return;
    fetch(`/api/manutentori/${id}/elimina`, { method: 'DELETE' })
        .then(res => res.text()).then(msg => { alert(msg); loadStaff(); });
};
// ricerca macchinette e addetti
document.getElementById('searchMachineBtn').addEventListener('click', () => {
    const filter = document.getElementById('machineSearch').value.toUpperCase();
    const rows = machineTable.getElementsByTagName('tr');
    for (let i = 0; i < rows.length; i++) {
        const tdCode = rows[i].getElementsByTagName("td")[0];
        const tdLoc = rows[i].getElementsByTagName("td")[1];
        if (tdCode || tdLoc) {
            const txt = (tdCode.textContent + " " + tdLoc.textContent).toUpperCase();
            rows[i].style.display = txt.indexOf(filter) > -1 ? "" : "none";
        }
    }
});

document.getElementById('searchStaffBtn').addEventListener('click', () => {
    const filter = document.getElementById('staffSearch').value.toUpperCase();
    const rows = staffTable.getElementsByTagName('tr');
    for (let i = 0; i < rows.length; i++) {
        const tdNome = rows[i].getElementsByTagName("td")[0];
        const tdEmail = rows[i].getElementsByTagName("td")[1];
        if (tdNome || tdEmail) {
            const txt = (tdNome.textContent + " " + tdEmail.textContent).toUpperCase();
            rows[i].style.display = txt.indexOf(filter) > -1 ? "" : "none";
        }
    }
});

loadMachines();
loadStaff();