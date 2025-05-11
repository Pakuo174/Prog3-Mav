package bankprojekt.verwaltung;

import bankprojekt.verarbeitung.*;

import java.util.*;

public class Bank {


    private long bankleitzahl;
    // Long = Typ für Schlüssel , Datentyp als Referenz auf ein konkretes Kontoobjekt, z.B. Sparbuch oder Girokonto
    private Map<Long, Konto> konten;
    private long letzteKontonummer = 0;


    /**
     * Erstellt eine Bankinstanz mit fester Bankleitzahl und einer leeren Kontenliste.
     *
     * @param bankleitzahl Variable zur Zuordnung einer Bank
     */
    public Bank(long bankleitzahl) {
        this.bankleitzahl = bankleitzahl;
        this.konten = new HashMap<>();  // Initialisierung der Map
    }

    /**
     * gibt die bankleitzahl einer Bank Instanz zurück
     * @return bankleitzahl Wert von einer Instanz von Bank
     */
    public long getBankleitzahl() {
        return bankleitzahl;

    }

    /**
     * erstellt ein Girokonto für den angegebenen Kunden. Dabei soll die Methode eine beliebige neue, noch nicht vergebene Kontonummer erzeugen
     * * Das Konto erhält eine automatisch generierte Kontonummer - beginnend bei 1
     * * Erstellt ein Girokonto mit einem Standard-Dispositionskredit von 500 Euro.
     * <p>
     * @param inhaber definiert eine Instanz der Klasse Kunde
     * @return ist die neue Kontonummer
     * @exception IllegalArgumentException wird geworfen wenn Ein Kunde nur mit null Werten übergeben wird
     */
    public long girokontoErstellen(Kunde inhaber) {
        if (inhaber == null) {
            throw new IllegalArgumentException("Inhaber darf nicht null sein");
        }
        long neueNummer = ++letzteKontonummer;
        Girokonto konto = new Girokonto(inhaber, neueNummer, this.getBankleitzahl(), new Geldbetrag(500));
        konten.put(neueNummer, konto);
        return neueNummer;
    }

    /**
     * Erstellt ein Sparbuch für den angegebenen Kunden.
     * Die Kontonummer wird automatisch generiert, beginnend bei 1. Diese dient gleichzeitig als Schlüssel in der Konten-HashMap.
     * <p>
     * Im Unterschied zum Girokonto hat ein Sparbuch Einschränkungen bei der Abhebung
     *
     * @param inhaber Der Kontoinhaber des neuen Sparbuchs.
     * @return Die neu vergebene Kontonummer.
     * @throws IllegalArgumentException wenn der Inhaber ist.
     */
    public long sparbuchErstellen(Kunde inhaber) {
        if (inhaber == null) {
            throw new IllegalArgumentException("Inhaber darf nicht null sein");
        }
        long neueNummer = ++letzteKontonummer;
        Sparbuch konto = new Sparbuch(inhaber, neueNummer, this.getBankleitzahl(), new Kalender());
        konten.put(neueNummer, konto);
        return neueNummer;
    }


    public String getAlleKonten() {
        StringBuilder sb = new StringBuilder();

        for (Konto ko : konten.values()) {
            sb.append("Kontonummer: ").append(ko.getKontonummer())
                    .append(", Kontostand: ").append(ko.getKontostand())
                    .append(System.lineSeparator());
        }

        //geht auch - mit den Schlüssel for each Schleife nutzen :
       /* for (Long kontonummer : konten.keySet()) {
            Konto konto = konten.get(kontonummer); // Zugriff über den Schlüssel
            sb.append("Kontonummer: ").append(kontonummer)
                    .append(", Kontostand: ").append(konto.getKontostand())
                    .append(System.lineSeparator());
        }
        */

        return sb.toString();
    }

    /**
     * um eine Liste zu ersellten die alle Kontonumemrn erhält
     * @return Liste mit Kontonummern
     */
    /**
     * Liefert eine Liste aller gültigen Kontonummern in der Bank.
     * @return Liste mit Kontonummern
     */
    public List<Long> getAlleKontonummern() {
        return new ArrayList<>(konten.keySet());
    }

    /**
     * zahlt den angegebenen Betrag auf das Konto mit der Nummer auf ein
     *
     * @param auf    dient als Parameter um auf die MAP zuzugreifen und get.... zu nutzen
     * @param betrag ist der Betrag um den das Konto erhöht werden soll
     * @exception IllegalArgumentException wird geworfen wenn das konto null ist
     */
    public void geldEinzahlen(long auf, Geldbetrag betrag) {
        Konto konto = konten.get(auf);
        if (konto == null) {
            throw new IllegalArgumentException("Kein Konto mit der Nummer: " + auf);
        }
        konto.einzahlen(betrag);

    }

    /**
     * hebt den Betrag vom Konto mit der Nummer von ab und gibt zurück, ob die Abhebung geklappt hat
     *
     * @param von Konto von welches Geld abgehoben werden soll
     * @param betrag Betrag welcher abgezogen wird
     * @return true wenn abgehen funktioniert hat
     * @throws GesperrtException
     * @exception IllegalArgumentException wenn das Konto null ist
     */
    public boolean geldAbheben(long von, Geldbetrag betrag) throws GesperrtException {
        Konto konto = konten.get(von);
        if (konto == null) {
            throw new IllegalArgumentException("Kein Konto mit der Nummer: " + von);
        }
        return konto.abheben(betrag);
    }

    /**
     * löscht das Konto mit der angegebenen nummer und gibt true zurück, wenn tatsächlich ein Konto gelöscht wurde, bzw. false, wenn die Kontonummer gar nicht erst existierte.
     *
     *  @param nummer Kontonummer, die gelöscht werden soll
     * @return true, wenn ein Konto gelöscht wurde, false, wenn kein Konto mit dieser Nummer existierte
     */
    public boolean kontoLoeschen(long nummer) {

        if (konten.containsKey(nummer)) {
            konten.remove(nummer);
            return true;
        }
        return false;
    }
    /**
     * Gibt den Kontostand des Kontos mit der angegebenen Nummer zurück.
     * @param nummer die Kontonummer
     * @return der Kontostand, oder null wenn kein Konto mit dieser Nummer existiert
     * @exception NoSuchElementException wenn das Konto als Element in der Map nicht vorhanden ist
     */
    public Geldbetrag getKontostand(long nummer) {
        Konto konto = konten.get(nummer);
        if (konto == null) {
            throw new NoSuchElementException("Kein Konto mit der Nummer: " + nummer);
        }
        if (konto != null) {
            return konto.getKontostand();
        }
        return null;
    }

    /**
     * überweist den genannten Betrag vom überweisungsfähigen Konto mit der Nummer vonKontonr zum überweisungsfähigen Konto mit der Nummer nachKontonr
     * ** es wird auch drauf geachtet, dass wenn es ein Dispo fähiges Konto ist der dispo mit einbezogen wird
     * @param vonKontonr Kontonummer von der Geld abgehoben wird
     * @param nachKontonr Kontonummer die die abgehobene Geldmenge von vonKontonr erhält
     * @param betrag Summe die Überwiesen werden soll
     * @param verwendungszweck Hinweis wozo das Geld überwiesen wird
     * @return gibt zurück true, wenn die Überweisung geklappt hat
     */
    public boolean geldUeberweisen(long vonKontonr, long nachKontonr, Geldbetrag betrag, String verwendungszweck) throws GesperrtException {

        Konto vonKonto = konten.get(vonKontonr);
        Konto nachKonto = konten.get(nachKontonr);

        // Überprüfe, ob beide Konten existieren
        if (vonKonto == null || nachKonto == null) {
            throw new IllegalArgumentException("Konto´s dürfen nicht null sein");
        }
        if ( betrag == null || betrag.isNegativ()){
            throw new IllegalArgumentException("Betrag darf nicht null oder negativ sein");
        }
        if (!(vonKonto instanceof Girokonto giroSenden)) {
            return false; // Nur Girokonto kann Überweisungen senden
        }

        if (!(nachKonto instanceof Girokonto giroEmpfangen)) {
            return false; // Nur Girokonto kann Überweisungen senden
        }

        boolean erfolgreich = giroSenden.ueberweisungAbsenden(
                betrag,
                nachKonto.getInhaber().getName(),
                nachKonto.getKontonummer(),
                this.getBankleitzahl(),  // falls Konto diese Methode hat, sonst this.getBankleitzahl()
                verwendungszweck
        );

        if (!erfolgreich) {
            return false;
        }

        // Betrag auf das Zielkonto einzahlen

        giroEmpfangen.ueberweisungEmpfangen(betrag, vonKonto.getInhaber().getName(), vonKonto.getKontonummer(),this.getBankleitzahl(),verwendungszweck);
        return true;
    }


}
