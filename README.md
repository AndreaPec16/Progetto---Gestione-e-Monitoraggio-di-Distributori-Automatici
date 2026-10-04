# Gestione e Monitoraggio di Distributori Automatici

Progettazione e sviluppo full-stack di una soluzione web per l'amministrazione centralizzata e il telecontrollo di una rete di distributori fisici, con gestione dell'intero flusso dati dalle macchine all'utente finale.

## Architettura e Funzionalità Core

* **Architettura e Sviluppo Backend:** Realizzazione del backend in Java strutturato sul pattern MVC. Il sistema è progettato per orchestrare in modo sicuro le comunicazioni tra server, interfacce web e hardware remoto.
* **Gestione Dati Multi-Database:** Progettazione di un'architettura dati distribuita su due database MariaDB indipendenti: uno dedicato alla gestione anagrafica e transazionale (utenti, crediti, inventario prodotti), l'altro isolato e ottimizzato per la storicizzazione e l'ingestione ad alta frequenza dei log operativi.
* **Telemetria e Controllo Remoto:** Implementazione di endpoint REST per l'acquisizione continua dei segnali di stato (*heartbeat*) e la geolocalizzazione dei distributori. Integrazione di task in background per il monitoraggio in tempo reale delle scorte e il rilevamento automatico di guasti o disconnessioni.
* **Gestione Multi-Ruolo (RBAC):** Sviluppo del frontend con interfacce dinamiche e chiamate asincrone per tre profili utente protetti:
  * **Clienti:** Gestione portafoglio, ricarica credito e acquisto prodotti.
  * **Manutentori:** Gestione degli interventi tecnici, rifornimento scorte e risoluzione guasti.
  * **Gestori:** Pannello di amministrazione e supervisione globale dell'intera flotta.

## Stack Tecnologico
* **Backend:** Java, MVC, REST API
* **Frontend:** JSP, HTML5, CSS3, JavaScript
* **Database:** MariaDB, SQL
* **Strumenti:** Maven