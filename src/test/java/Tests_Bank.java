import bankprojekt.verarbeitung.Kunde;
import bankprojekt.verwaltung.Bank;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class Tests_Bank {



    @Test
    public void bankErstellenUndGirokonto(){
        // Bank erstellen
        Bank b1 = new Bank(1234567);
        System.out.println("Bankleitzahl: " + b1.getBankleitzahl());  // Bankleitzahl ausgeben

        // Kunde erstellen
        Kunde k1 = new Kunde("Daniel", "Kujawa", "Bärlin", LocalDate.of(2000, 7, 12));
        System.out.println("\n"+ k1);

        // Girokonto erstellen
        long kontonummer = b1.girokontoErstellen(k1);
        System.out.println("Neue Kontonummer: " + kontonummer);  // Kontonummer des erstellten Girokontos ausgeben

        // Anzahl der Konten in der Map ausgeben
        System.out.println("Anzahl der Konten in der Bank: " + b1.konten.size());

    }

    /**
     * testen ob 2 Konten erstellt werden können zu einer Bank
     * + ob die HashMap sich mit erhöht
     */
    @Test
    public void mehrereGirokontoErstellen(){
        // Bank erstellen
        Bank b1 = new Bank(1234567);
        System.out.println("Bankleitzahl: " + b1.getBankleitzahl());  // Bankleitzahl ausgeben

        // Kunden erstellen
        Kunde k1 = new Kunde("Daniel", "Kujawa", "Bärlin", LocalDate.of(2000, 7, 12));
        System.out.println("\n"+ k1);

        Kunde k2 = new Kunde("Nico", "Froelich", "Rathenow", LocalDate.of(2000, 12, 13));
        System.out.println("\n"+ k2);

        // Girokontos erstellen und ausgeben
        long kontonummer = b1.girokontoErstellen(k1);
        System.out.println("Neue Kontonummer: " + kontonummer);  // Kontonummer des erstellten Girokontos ausgeben

        long kontonummer2 = b1.girokontoErstellen(k2);
        System.out.println("Neue Kontonummer: " + kontonummer2);

        // Anzahl der Konten in der Map ausgeben
        System.out.println("Anzahl der Konten in der Bank: " + b1.konten.size());
    }


    @Test
    public void mehrereVerschKontenErstellen(){
        Bank b1 = new Bank(1234567);
        System.out.println("Bankleitzahl: " + b1.getBankleitzahl());  // Bankleitzahl ausgeben

        // Kunden erstellen
        Kunde k1 = new Kunde("Daniel", "Kujawa", "Bärlin", LocalDate.of(2000, 7, 12));
        System.out.println("\n"+ k1);

        Kunde k2 = new Kunde("Nico", "Froelich", "Rathenow", LocalDate.of(2000, 12, 13));
        System.out.println("\n"+ k2);

        // Girokontos erstellen und ausgeben
        long kontonummer = b1.girokontoErstellen(k1);
        System.out.println("Neue Kontonummer: " + kontonummer);

        //Sparbuch erstellen und ausgeben
        long kontonummer2 = b1.sparbuchErstellen(k2);
        System.out.println("Neue Kontonummer: " + kontonummer2);

        // Anzahl der Konten in der Map ausgeben
        System.out.println("Anzahl der Konten in der Bank: " + b1.konten.size());

    }



}
