package de.daniel.bankprojekt.verarbeitung.test.Geldbetrag;

public class NichtGenugGuthabenException extends Exception {
    public NichtGenugGuthabenException(String message) {
        super(message);
    }
}