package com.example.demo.service;

import com.example.demo.dto.DistributoreDTO;
import com.example.demo.dto.EntityMapper;
import com.example.demo.dto.MonitorDistributoreDTO;
import com.example.demo.dto.MonitorStatoDTO;
import com.example.demo.dto.NuovoDistributoreMonitorDTO;
import com.example.demo.dto.ProdottoDTO;
import com.example.demo.dto.UtenteConnessoDTO;
import com.example.demo.entity.Distributore;
import com.example.demo.entity.Prodotto;
import com.example.demo.entity.Utente;
import com.example.demo.exception.AcquistoException;
import com.example.demo.repository.DistributoreRepository;
import com.example.demo.repository.ProdottoRepository;
import com.example.demo.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service per la gestione dei distributori.
 */
@Service
public class DistributoreService {

    @Autowired
    private DistributoreRepository distributoreRepository;
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private ProdottoRepository prodottoRepository;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private RestTemplate restTemplate;

    private final String MONITOR_URL = "http://localhost:8081/monitor/api";

    /**
     * Restituisce la lista dei prodotti disponibili.
     * @return Lista di ProdottoDTO.
     */
    public List<ProdottoDTO> getProdottiDisponibili() {
        return prodottoRepository.findAll().stream()
                .map(p -> new ProdottoDTO(p.getNome(), p.getPrezzo()))
                .collect(Collectors.toList());
    }

    /**
     * Gestisce l'acquisto di un prodotto da parte di un utente.
     * @param codiceVisivo Il codice della macchinetta.
     * @param username L'username dell'utente.
     * @param nomeProdotto Il nome del prodotto da acquistare.
     * @param zucchero La quantità di zucchero desiderata.
     * @return ResponseEntity con il risultato dell'operazione.
     */
    @Transactional
    public ResponseEntity<String> acquistaProdotto(String codiceVisivo, String username, String nomeProdotto, int zucchero) {
        try {
            System.out.println("--- INIZIO ACQUISTO ---");
            System.out.println("Prodotto richiesto: " + nomeProdotto);

            Prodotto prodotto = prodottoRepository.findByNome(nomeProdotto)
                    .orElseThrow(() -> new AcquistoException("Prodotto non valido."));
            
            BigDecimal prezzo = prodotto.getPrezzo();
            System.out.println("Prezzo dal DB: " + prezzo);

            Utente utente = validaUtenteEConnessione(username, codiceVisivo);
            Distributore distributore = findByCodiceVisivo(codiceVisivo);
            
            System.out.println("Credito utente PRIMA: " + utente.getCredito());

            controllaCredito(utente, prezzo);
            controllaStatoMacchina(distributore);
            controllaRisorse(distributore, prodotto, zucchero);

            decrementaRisorse(distributore, prodotto, zucchero);
            finalizzaTransazione(utente, prezzo);

            System.out.println("Credito utente DOPO: " + utente.getCredito());
            System.out.println("--- FINE ACQUISTO ---");

            String successMessage = String.format("OK: Erogato %s. Credito residuo: %.2f €", nomeProdotto, utente.getCredito());
            return ResponseEntity.ok(successMessage);

        } catch (AcquistoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problema tecnico imprevisto.");
        }
    }

    /**
     * Controlla le risorse disponibili nel distributore.
     * @param d Il distributore.
     * @param p Il prodotto richiesto.
     * @param zucchero La quantità di zucchero.
     */
    private void controllaRisorse(Distributore d, Prodotto p, int zucchero) {
        if (d.getLivelloBicchieri() <= 0) throw new AcquistoException("Bicchieri esauriti!");
        if (d.getLivelloZucchero() < zucchero) throw new AcquistoException("Zucchero insufficiente!");
        if (d.getLivelloAcqua() < p.getConsumoAcqua()) throw new AcquistoException("Acqua insufficiente!");
        if (d.getLivelloCaffe() < p.getConsumoCaffe()) throw new AcquistoException("Caffè esaurito!");
        if (d.getLivelloLatte() < p.getConsumoLatte()) throw new AcquistoException("Latte esaurito!");
        if (d.getLivelloCioccolato() < p.getConsumoCioccolato()) throw new AcquistoException("Cioccolato esaurito!");
        if (d.getLivelloTe() < p.getConsumoTe()) throw new AcquistoException("Tè esaurito!");
    }

    /**
     * Decrementa le risorse del distributore dopo un acquisto.
     * @param d Il distributore.
     * @param p Il prodotto acquistato.
     * @param zucchero La quantità di zucchero utilizzata.
     */
    private void decrementaRisorse(Distributore d, Prodotto p, int zucchero) {
        d.setLivelloBicchieri(d.getLivelloBicchieri() - 1);
        d.setLivelloZucchero(d.getLivelloZucchero() - zucchero);
        d.setLivelloAcqua(d.getLivelloAcqua() - p.getConsumoAcqua());
        d.setLivelloCaffe(d.getLivelloCaffe() - p.getConsumoCaffe());
        d.setLivelloLatte(d.getLivelloLatte() - p.getConsumoLatte());
        d.setLivelloCioccolato(d.getLivelloCioccolato() - p.getConsumoCioccolato());
        d.setLivelloTe(d.getLivelloTe() - p.getConsumoTe());
    }

    /**
     * Valida l'utente e la sua connessione alla macchinetta.
     * @param username L'username dell'utente.
     * @param codiceVisivo Il codice della macchinetta.
     * @return L'utente validato.
     */
    private Utente validaUtenteEConnessione(String username, String codiceVisivo) {
        Utente utente = utenteRepository.findByUsername(username);
        if (utente == null) throw new AcquistoException("Utente non valido.");
        if (!codiceVisivo.equals(utente.getConnectedMachineId())) throw new AcquistoException("Non sei connesso a questa macchinetta.");
        return utente;
    }

    /**
     * Controlla se l'utente ha credito sufficiente.
     * @param utente L'utente.
     * @param prezzo Il prezzo del prodotto.
     */
    private void controllaCredito(Utente utente, BigDecimal prezzo) {
        BigDecimal creditoUtente = BigDecimal.valueOf(utente.getCredito());
        if (creditoUtente.compareTo(prezzo) < 0) {
            throw new AcquistoException("Credito insufficiente!");
        }
    }

    /**
     * Controlla lo stato operativo della macchinetta tramite il servizio di monitoraggio.
     * @param distributore Il distributore.
     */
    private void controllaStatoMacchina(Distributore distributore) {
        String statoCorrente = getStatiFromMonitor().getOrDefault(distributore.getCodiceVisivo(), "SCONOSCIUTO");
        if (!"ATTIVO".equals(statoCorrente)) {
            throw new AcquistoException("Macchinetta non operativa (" + statoCorrente + ")");
        }
    }

    /**
     * Finalizza la transazione, addebitando il costo all'utente.
     * @param utente L'utente.
     * @param prezzo Il prezzo del prodotto.
     */
    private void finalizzaTransazione(Utente utente, BigDecimal prezzo) {
        BigDecimal creditoIniziale = BigDecimal.valueOf(utente.getCredito());
        BigDecimal nuovoCredito = creditoIniziale.subtract(prezzo);
        utente.setCredito(nuovoCredito.doubleValue());
        utenteRepository.save(utente);
    }
    
    /**
     * Aggiunge un nuovo distributore.
     * @param codice Il codice visivo del distributore.
     * @param indirizzo L'indirizzo del distributore.
     * @param modello Il modello del distributore.
     * @param lat La latitudine.
     * @param lon La longitudine.
     * @return Messaggio di successo o errore.
     */
    @Transactional
    public String aggiungiDistributore(String codice, String indirizzo, String modello, Double lat, Double lon) {
        if (distributoreRepository.findByCodiceVisivo(codice).isPresent()) {
            return "ERRORE: Codice macchinetta già esistente!";
        }
        Distributore d = new Distributore();
        d.setCodiceVisivo(codice);
        d.setIndirizzo(indirizzo);
        d.setModello(modello);
        d.setLatitudine(lat);
        d.setLongitudine(lon);
        d.setDataUltimaManutenzione(LocalDateTime.now());
        d.setLivelloAcqua(100);
        d.setLivelloCaffe(100);
        d.setLivelloZucchero(100);
        d.setLivelloBicchieri(100);
        d.setLivelloPalette(100);
        d.setLivelloLatte(100);
        d.setLivelloTe(100);
        d.setLivelloCioccolato(100);
        distributoreRepository.save(d);

        try {
            String url = MONITOR_URL + "/distributori";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            NuovoDistributoreMonitorDTO newDto = new NuovoDistributoreMonitorDTO(d.getCodiceVisivo(), d.getLatitudine(), d.getLongitudine());
            HttpEntity<NuovoDistributoreMonitorDTO> request = new HttpEntity<>(newDto, headers);
            restTemplate.postForEntity(url, request, String.class);
        } catch (Exception e) {
            System.err.println("ATTENZIONE: Impossibile notificare il monitor della nuova macchina. " + e.getMessage());
        }
        
        return "OK: Macchinetta aggiunta con successo";
    }

    /**
     * Elimina un distributore.
     * @param codiceVisivo Il codice del distributore da eliminare.
     * @return Messaggio di successo o errore.
     */
    @Transactional
    public String eliminaDistributore(String codiceVisivo) {
        Distributore d = findByCodiceVisivo(codiceVisivo);
        distributoreRepository.delete(d);

        try {
            String url = MONITOR_URL + "/distributori/" + codiceVisivo;
            restTemplate.delete(url);
        } catch (Exception e) {
            System.err.println("ATTENZIONE: Impossibile notificare il monitor dell'eliminazione. " + e.getMessage());
        }

        return "OK: Macchinetta eliminata";
    }

    /**
     * Trova tutti i distributori e unisce i loro dati con lo stato proveniente dal monitor.
     * @return Lista di DistributoreDTO con stato aggiornato.
     */
    public List<DistributoreDTO> findAllAndMergeWithMonitor() {
        List<Distributore> distributoriLocali = distributoreRepository.findAll();
        Map<String, String> statiMonitor = getStatiFromMonitor();
        return distributoriLocali.stream().map(d -> {
            DistributoreDTO dto = EntityMapper.toDistributoreDTO(d);
            String statoAggiornato = statiMonitor.get(d.getCodiceVisivo());
            dto.setStato(statoAggiornato != null ? statoAggiornato : "SCONOSCIUTO");
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * Recupera gli stati di tutti i distributori dal servizio di monitoraggio.
     * @return Mappa [codiceVisivo -> stato].
     */
    private Map<String, String> getStatiFromMonitor() {
        try {
            ResponseEntity<MonitorDistributoreDTO[]> response = restTemplate.getForEntity(MONITOR_URL + "/distributori", MonitorDistributoreDTO[].class);
            if (response.getBody() != null) {
                return Arrays.stream(response.getBody())
                        .collect(Collectors.toMap(MonitorDistributoreDTO::getCodice, MonitorDistributoreDTO::getStato));
            }
        } catch (Exception e) {
            System.err.println("ERRORE: Impossibile contattare il servizio di monitoraggio. " + e.getMessage());
        }
        return Map.of();
    }

    /**
     * Genera un file XML con la lista dei distributori.
     * @return Stringa contenente l'XML.
     */
    public String getDistributoriXml() {
        Context ctx = new Context();
        ctx.setVariable("distributori", findAllAndMergeWithMonitor());
        return templateEngine.process("xml/distributori.xml", ctx).trim();
    }

    /**
     * Invia una richiesta di cambio stato al servizio di monitoraggio.
     * @param codiceVisivo Il codice del distributore.
     * @param nuovoStato Il nuovo stato da impostare.
     * @return Messaggio di risultato.
     */
    public String cambiaStato(String codiceVisivo, String nuovoStato) {
        String url = MONITOR_URL + "/stato/" + codiceVisivo;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MonitorStatoDTO> request = new HttpEntity<>(new MonitorStatoDTO(nuovoStato), headers);
        try {
            restTemplate.postForEntity(url, request, String.class);
            return "OK: Richiesta di cambio stato inviata al monitor.";
        } catch (Exception e) {
            return "ERRORE: Impossibile contattare il servizio di monitoraggio.";
        }
    }

    /**
     * Trova un distributore tramite il suo codice visivo.
     * @param codiceVisivo Il codice da cercare.
     * @return Il distributore trovato.
     * @throws ResponseStatusException se non trovato.
     */
    public Distributore findByCodiceVisivo(String codiceVisivo) {
        return distributoreRepository.findByCodiceVisivo(codiceVisivo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Macchinetta non trovata"));
    }

    /**
     * Ripristina le scorte di un distributore e lo imposta come ATTIVO.
     * @param codiceVisivo Il codice del distributore.
     * @return Messaggio di risultato.
     */
    public String ripristinaScorte(String codiceVisivo) {
        Distributore d = findByCodiceVisivo(codiceVisivo);
        d.setLivelloAcqua(100);
        d.setLivelloCaffe(100);
        d.setLivelloZucchero(100);
        d.setLivelloBicchieri(100);
        d.setLivelloPalette(100);
        d.setLivelloLatte(100);
        d.setLivelloTe(100);
        d.setLivelloCioccolato(100);
        d.setDataUltimaManutenzione(LocalDateTime.now());
        distributoreRepository.save(d);
        return cambiaStato(codiceVisivo, "ATTIVO");
    }

    /**
     * Recupera l'utente attualmente connesso a una specifica macchinetta.
     * @param codiceVisivo Il codice della macchinetta.
     * @return UtenteConnessoDTO se un utente è connesso, altrimenti null.
     */
    public UtenteConnessoDTO getUtenteConnesso(String codiceVisivo) {
        List<Utente> utenti = utenteRepository.findByConnectedMachineId(codiceVisivo);
        if (!utenti.isEmpty()) {
            Utente utente = utenti.get(0);
            return new UtenteConnessoDTO(utente.getUsername(), utente.getNome(), utente.getCredito(), utente.getConnectedMachineId());
        }
        return null;
    }
}
