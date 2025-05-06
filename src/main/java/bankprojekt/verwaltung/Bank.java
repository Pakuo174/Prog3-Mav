package bankprojekt.verwaltung;

import bankprojekt.verarbeitung.*;

import java.util.HashMap;
import java.util.Map;

public class Bank {


    private long bankleitzahl;
    // Long = Typ für Schlüssel , Datentyp als Referenz auf ein konkretes Kontoobjekt, z.B. Sparbuch oder Girokonto
    public Map<Long, Konto> konten;
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

    public long getBankleitzahl() {
        return bankleitzahl;

    }

    /**
     * erstellt ein Girokonto für den angegebenen Kunden. Dabei soll die Methode eine beliebige neue, noch nicht vergebene Kontonummer erzeugen
     * * Das Konto erhält eine automatisch generierte Kontonummer - beginnend bei 1 - diese ist gleichzeitig der Schlüssel in der HashMap
     * * Erstellt ein Girokonto mit einem Standard-Dispositionskredit von 500 Euro.
     * <p>
     * *++letzteKontunummer erhöht den Wert immer +1 und speichert ihn für das nächste Konto im Attribut ab
     *
     * @param inhaber definiert eine Instanz der Klasse Kunde
     * @return ist die neue Kontonummer
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
     * @throws IllegalArgumentException wenn der Inhaber {@code null} ist.
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
     * zahlt den angegebenen Betrag auf das Konto mit der Nummer auf ein
     *
     * @param auf    dient als Parameter um auf die MAP zuzugreifen und get.... zu nutzen
     * @param betrag ist der Betrag um den das Konto erhöht werden soll
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
     * @param von
     * @param betrag
     * @return
     * @throws GesperrtException
     */
    public boolean geldAbheben(long von, Geldbetrag betrag) throws GesperrtException {
        Konto konto = konten.get(von);
        if (konto == null) {
            throw new IllegalArgumentException("Kein Konto mit der Nummer: " + von);
        }
        konto.abheben(betrag);
        return true;
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
     */
    public Geldbetrag getKontostand(long nummer) {
        Konto konto = konten.get(nummer);
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
        if (vonKonto == null || nachKonto == null || betrag == null || betrag.isNegativ()) {
            return false;
        }

        boolean erfolgreich;
        if (vonKonto instanceof Girokonto giro) {
            erfolgreich = giro.abheben(betrag);
        } else {
            erfolgreich = this.geldAbheben(vonKontonr,betrag);
        }

        if (!erfolgreich) {
            return false;
        }

        // Betrag auf das Zielkonto einzahlen
        nachKonto.einzahlen(betrag);

        return true;
    }


}
