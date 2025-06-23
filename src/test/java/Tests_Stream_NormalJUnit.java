import bankprojekt.geld.Waehrung;
import bankprojekt.verarbeitung.*;
import bankprojekt.verwaltung.Bank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class Tests_Stream_NormalJUnit {

    Bank bank;
    Kunde kunde1, kunde2, kunde3;
    long kontoNummerSenior;
    long kontoNummerJung;

    @BeforeEach
    void setup() {
        bank = new Bank(12345678L);

        /*
        // Kunde mit Geb. am 5. März
        kunde1 = new Kunde("Anna", "Müller", "Adresse 1", LocalDate.of(1990, 3, 5));
        bank.girokontoErstellen(kunde1);

        // Kunde mit Geb. am 10. Januar
        kunde2 = new Kunde("Max", "Mustermann", "Adresse 2", LocalDate.of(1985, 1, 10));
        bank.girokontoErstellen(kunde2);

        // Derselbe Kunde wie kunde1 (soll ignoriert werden)
        bank.girokontoErstellen(kunde1);

        // Kunde mit Geb. am 8. März
        kunde3 = new Kunde("Erika", "Musterfrau", "Adresse 3", LocalDate.of(1992, 3, 8));
        bank.girokontoErstellen(kunde3);

        // Senior Kunde, mind. 67 Jahre alt
        Kunde senior = new Kunde("Anna", "Alt", "Adresse", LocalDate.now().minusYears(67).minusDays(1));
        kontoNummerSenior = bank.girokontoErstellen(senior);


        // Junger Kunde, z.B. 30 Jahre alt
        Kunde jung = new Kunde("Ben", "Jung", "Adresse", LocalDate.of(1925, 1, 10));
        kontoNummerJung = bank.girokontoErstellen(jung);


         */
    }

/*
    @Test
    void testGetKundengeburtstage() {

        String result = bank.getKundengeburtstage();

        // Ausgabe zum Debuggen
        System.out.println(result);

        // Inhalt prüfen
        assertTrue(result.contains("Max Mustermann – 1985-01-10"));
        assertTrue(result.contains("Anna Müller – 1990-03-05"));
        assertTrue(result.contains("Erika Musterfrau – 1992-03-08"));


    }
*/

    /*
    @Test
    void testGetAnzahlSenioren() {
        long seniorenCount = bank.getAnzahlSenioren();
        // Nur der eine Senior sollte gezählt werden
        assertEquals(2, seniorenCount);
    }
*/
}
