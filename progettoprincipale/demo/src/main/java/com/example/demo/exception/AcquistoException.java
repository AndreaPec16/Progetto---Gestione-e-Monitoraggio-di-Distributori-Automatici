package com.example.demo.exception;

/**
 * Eccezione custom per gestire errori durante il processo di acquisto.
 */
public class AcquistoException extends RuntimeException {
    /**
     * Costruttore per AcquistoException.
     * @param message Il messaggio di errore.
     */
    public AcquistoException(String message) {
        super(message);
    }
}
