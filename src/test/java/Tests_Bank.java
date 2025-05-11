import bankprojekt.geld.Waehrung;
import bankprojekt.verarbeitung.Geldbetrag;
import bankprojekt.verarbeitung.GesperrtException;
import bankprojekt.verarbeitung.Kunde;
import bankprojekt.verarbeitung.Sparbuch;
import bankprojekt.verwaltung.Bank;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class Tests_Bank {


    @Test
    public void bankErstellenUndGirokonto() {
        // Bank erstellen
        Bank b1 = new Bank(1234567);
        System.out.println("Bankleitzahl: " + b1.getBankleitzahl());  // Bankleitzahl ausgeben

        // Kunde erstellen
        Kunde k1 = new Kunde("Daniel", "Kujawa", "Bärlin", LocalDate.of(2000, 7, 12));
        System.out.println("\n" + k1);

        // Girokonto erstellen
        long kontonummer = b1.girokontoErstellen(k1);
        System.out.println("Neue Kontonummer: " + kontonummer);  // Kontonummer des erstellten Girokontos ausgeben

        // Anzahl der Konten in der Map ausgeben
        System.out.println("Anzahl der Konten in der Bank: " + b1.getAlleKonten());

    }

    /**
     * testen ob 2 Konten erstellt werden können zu einer Bank
     * + ob die HashMap sich mit erhöht
     */
    @Test
    public void mehrereGirokontoErstellen() {
        // Bank erstellen
        Bank b1 = new Bank(1234567);
        System.out.println("Bankleitzahl: " + b1.getBankleitzahl());  // Bankleitzahl ausgeben

        // Kunden erstellen
        Kunde k1 = new Kunde("Daniel", "Kujawa", "Bärlin", LocalDate.of(2000, 7, 12));
        System.out.println("\n" + k1);

        Kunde k2 = new Kunde("Nico", "Froelich", "Rathenow", LocalDate.of(2000, 12, 13));
        System.out.println("\n" + k2);

        // Girokontos erstellen und ausgeben
        long kontonummer = b1.girokontoErstellen(k1);
        System.out.println("Neue Kontonummer: " + kontonummer);  // Kontonummer des erstellten Girokontos ausgeben

        long kontonummer2 = b1.girokontoErstellen(k2);
        System.out.println("Neue Kontonummer: " + kontonummer2);

        // Anzahl der Konten in der Map ausgeben
        System.out.println("Anzahl der Konten in der Bank: " + b1.getAlleKonten());
    }


    /**
     * Konten werden erstellt, aber auf das konktrete Sparbuch Instanz kann nicht zugreiffen werden (auf Attribute)
     */
    @Test
    public void mehrereVerschKontenErstellen() {
        Bank b1 = new Bank(1234567);
        System.out.println("Bankleitzahl: " + b1.getBankleitzahl());  // Bankleitzahl ausgeben

        // Kunden erstellen
        Kunde k1 = new Kunde("Daniel", "Kujawa", "Bärlin", LocalDate.of(2000, 7, 12));
        System.out.println("\n" + k1);

        Kunde k2 = new Kunde("Nico", "Froelich", "Rathenow", LocalDate.of(2000, 12, 13));
        System.out.println("\n" + k2);

        // Girokontos erstellen und ausgeben
        long kontonummer = b1.girokontoErstellen(k1);
        System.out.println("Neue Kontonummer: " + kontonummer);

        //Sparbuch erstellen und ausgeben
        long kontonummer2 = b1.sparbuchErstellen(k2);
        System.out.println("Neue Kontonummer: " + kontonummer2);

        // Anzahl der Konten in der Map ausgeben
        System.out.println("Anzahl der Konten in der Bank: " + b1.getAlleKonten());;
    }

    @Test
    public void KontenAusgeben(){

        Bank b1 = new Bank(1234567);

        // Kunden erstellen
        Kunde k1 = new Kunde("Daniel", "Kujawa", "Bärlin", LocalDate.of(2000, 7, 12));
        Kunde k2 = new Kunde("Nico", "Froelich", "Rathenow", LocalDate.of(2000, 12, 13));

        // Girokontos erstellen und ausgeben
        long kontonummer = b1.girokontoErstellen(k1);
        //Sparbuch erstellen und ausgeben
        long kontonummer2 = b1.sparbuchErstellen(k2);


        System.out.println(b1.getAlleKonten());
    }


    @Test
    public void kontoNummernAusgeben(){
        Bank b1 = new Bank(1234567);

        // Kunden erstellen
        Kunde k1 = new Kunde("Daniel", "Kujawa", "Bärlin", LocalDate.of(2000, 7, 12));
        Kunde k2 = new Kunde("Nico", "Froelich", "Rathenow", LocalDate.of(2000, 12, 13));

        // Girokontos erstellen
        long kontonummer1 = b1.girokontoErstellen(k1);
        long kontonummer2 = b1.girokontoErstellen(k2);

        System.out.println(b1.getAlleKontonummern());

    } 

    @Test
    public void KontoEinzahlen(){
        Bank b1 = new Bank(1234567);

        // Kunden erstellen
        Kunde k1 = new Kunde("Daniel", "Kujawa", "Bärlin", LocalDate.of(2000, 7, 12));
        Kunde k2 = new Kunde("Nico", "Froelich", "Rathenow", LocalDate.of(2000, 12, 13));

        // Girokontos erstellen und ausgeben
        long kontonummer = b1.girokontoErstellen(k1);
        //Sparbuch erstellen und ausgeben
        long kontonummer2 = b1.sparbuchErstellen(k2);


        b1.geldEinzahlen(1,new Geldbetrag(5, Waehrung.EUR));
        System.out.println(b1.getAlleKonten());
    }

    @Test
    public void KontoAbheben() throws GesperrtException {
        Bank b1 = new Bank(1234567);

        // Kunden erstellen
        Kunde k1 = new Kunde("Daniel", "Kujawa", "Bärlin", LocalDate.of(2000, 7, 12));
        Kunde k2 = new Kunde("Nico", "Froelich", "Rathenow", LocalDate.of(2000, 12, 13));

        long kontonummer = b1.girokontoErstellen(k1);
        long kontonummer2 = b1.sparbuchErstellen(k2);

        b1.geldEinzahlen(1,new Geldbetrag(5000, Waehrung.EUR));
        System.out.println("alle aktuellen Konten nach Einzahlung\n____________________\n"
                + b1.getAlleKonten());

        b1.geldAbheben(1,new Geldbetrag(109,Waehrung.ESCUDO));
        System.out.println("alle aktuellen Konten nach Abheben\n____________________\n"
                + b1.getAlleKonten());
    }

    @Test
    public void KontoLöschung(){
        Bank b1 = new Bank(1234567);

        // Kunden erstellen
        Kunde k1 = new Kunde("Daniel", "Kujawa", "Bärlin", LocalDate.of(2000, 7, 12));
        Kunde k2 = new Kunde("Nico", "Froelich", "Rathenow", LocalDate.of(2000, 12, 13));

        long kontonummer = b1.girokontoErstellen(k1);
        long kontonummer2 = b1.sparbuchErstellen(k2);

        b1.kontoLoeschen(1);
        System.out.println(b1.getAlleKonten());
    }

    @Test
    public void testGetKontostand() {
        Bank b1 = new Bank(1234567);

        // Kunden erstellen
        Kunde k1 = new Kunde("Daniel", "Kujawa", "Bärlin", LocalDate.of(2000, 7, 12));
        Kunde k2 = new Kunde("Nico", "Froelich", "Rathenow", LocalDate.of(2000, 12, 13));

        // Girokontos erstellen
        long kontonummer = b1.girokontoErstellen(k1);
        // Sparbuch erstellen
        long kontonummer2 = b1.sparbuchErstellen(k2);

        // Einzahlen auf das erste Konto
        b1.geldEinzahlen(kontonummer, new Geldbetrag(100, Waehrung.EUR));

        // Teste, ob der Kontostand des ersten Kontos korrekt zurückgegeben wird
        Geldbetrag kontostand = b1.getKontostand(kontonummer);
        assertNotNull(kontostand, "Kontostand sollte nicht null sein");
        assertEquals(100, kontostand.getBetrag(), "Kontostand sollte 100 Euro betragen");

        // Teste ein Konto, das nicht existiert
        Geldbetrag kontostandNichtExistierend = b1.getKontostand(99999);
        assertNull(kontostandNichtExistierend, "Kontostand für nicht existierendes Konto sollte null sein");
    }

    @Test
    public void testGeldUeberweisen() throws GesperrtException {
        Bank b1 = new Bank(1234567);

        // Kunden erstellen
        Kunde k1 = new Kunde("Daniel", "Kujawa", "Bärlin", LocalDate.of(2000, 7, 12));
        Kunde k2 = new Kunde("Nico", "Froelich", "Rathenow", LocalDate.of(2000, 12, 13));

        // Girokontos erstellen
        long kontonummer1 = b1.girokontoErstellen(k1);
        long kontonummer2 = b1.girokontoErstellen(k2);

        // Einzahlen auf das erste Konto
        b1.geldEinzahlen(kontonummer1, new Geldbetrag(1000, Waehrung.EUR));

        // Überweisung durchführen
        boolean erfolg = b1.geldUeberweisen(kontonummer1, kontonummer2, new Geldbetrag(500, Waehrung.EUR), "Miete");

        // Teste, ob die Überweisung erfolgreich war
        assertTrue(erfolg, "Überweisung sollte erfolgreich sein");

        // Teste, ob die Kontostände korrekt aktualisiert wurden
        assertEquals(500, b1.getKontostand(kontonummer1).getBetrag(), "Kontostand von k1 sollte 500 Euro betragen");
        assertEquals(500, b1.getKontostand(kontonummer2).getBetrag(), "Kontostand von k2 sollte 500 Euro betragen");

        System.out.println(b1.getAlleKonten());
    }

    /**
     * sollte nicht klappen, da zu wenig Geld auf den Konto wäre
     * 500 ist disp , 500 ist auf den bankkonto 1 ---> das - 1001 würde zu -501 führen was über den dispo ist
     * @throws GesperrtException
     */
    @Test
    public void testUeberweisenOhneGeldAufKonto() throws GesperrtException {
        Bank b1 = new Bank(1234567);

        // Kunden erstellen
        Kunde k1 = new Kunde("Daniel", "Kujawa", "Bärlin", LocalDate.of(2000, 7, 12));
        Kunde k2 = new Kunde("Nico", "Froelich", "Rathenow", LocalDate.of(2000, 12, 13));

        // Girokontos erstellen
        long kontonummer1 = b1.girokontoErstellen(k1);
        long kontonummer2 = b1.girokontoErstellen(k2);

        // Einzahlen auf das erste Konto
        //b1.geldEinzahlen(kontonummer1, new Geldbetrag(1000, Waehrung.EUR));


        b1.geldEinzahlen(kontonummer1, new Geldbetrag(500, Waehrung.EUR));

        // Überweisung durchführen
        boolean erfolg = b1.geldUeberweisen(kontonummer1, kontonummer2, new Geldbetrag(1001, Waehrung.EUR), "AutoVersicherung");

        assertFalse(erfolg,"Überweisung klappt nur bei ausreichenden Geldbetrag auf Konto");

        System.out.println(b1.getAlleKonten());


        }





}
