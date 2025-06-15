package bankprojekt.verwaltung;

import bankprojekt.verarbeitung.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Bank implements Serializable{


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
    public long AktienKontoErstellen(Kunde inhaber){
        if (inhaber == null) {
            throw new IllegalArgumentException("Inhaber darf nicht null sein");
        }
        long neueNummer = ++letzteKontonummer;
        Aktienkonto konto = new Aktienkonto(inhaber,neueNummer);
        konten.put(neueNummer,konto);
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

    /**
     * Methode dient für Mocking-OBjekte
     * @param k ist ein Konto
     * @return Die neu vergebene Kontonummer.
     */
    public long mockEinfuegen(Konto k){
        long neueNummer = ++letzteKontonummer;
        konten.put(neueNummer, k);
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
        if (verwendungszweck == null){
            throw new IllegalArgumentException("Verwendungszweck darf nicht null sein");
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

    /**
     * die Methode zahlt auf alle Konten von Kunden, die in diesem Jahr 18 werden, den betrag ein
     * @param betrag welcher auf die Konten mit einem Alter über 18 überwiesen werden soll
     */
    public void schenkungAnNeuerwachsene(Geldbetrag betrag){

        if ( betrag == null || betrag.isNegativ()) {
            throw new IllegalArgumentException("Betrag darf nicht null oder negativ sein");
        }

        int aktuellesJahr = LocalDate.now().getYear();

        konten.values().stream()
                .filter(konto -> konto.getInhaber().getGeburtstag().getYear() < aktuellesJahr - 18)
                .forEach(konto -> konto.einzahlen(betrag));
    }

    /**
     * Die Methode liefert eine Liste aller Kunden, die ein Konto mit negativem Kontostand haben
     * @return
     */
    public List<Kunde> getKundenMitLeeremKonto(){

       return konten.values().stream()
                .filter(konto -> konto.getKontostand().isNegativ())
                .map(konto -> new Kunde(konto.getInhaber().getVorname(),konto.getInhaber().getNachname(),konto.getInhaber().getAdresse(),konto.getInhaber().getGeburtstag()))
                .distinct() // Optional: falls ein Kunde mehrere Konten hat
                .toList(); // erstellt direkt die finale Liste.

    }

    /**
     * liefert die Namen und Geburtstage aller Kunden der Bank
     * Doppelte Kunden sollen dabei aussortiert werden
     * Liste nach Monat und Tag des Geburtstages (nicht nach dem Geburtsjahr!)
     * @return
     *  Format jedes Eintrags: "Vorname Nachname – yyyy-MM-dd"
     */
    public String getKundengeburtstage() {
        return konten.values().stream()
                .map(Konto::getInhaber)
                .distinct()
                .sorted((k1, k2) -> {
                    int monthCompare = Integer.compare(k1.getGeburtstag().getMonthValue(), k2.getGeburtstag().getMonthValue());
                    if (monthCompare != 0) return monthCompare;
                    return Integer.compare(k1.getGeburtstag().getDayOfMonth(), k2.getGeburtstag().getDayOfMonth());
                })
                .map(k -> k.getVorname() + " " + k.getNachname() + " – " + k.getGeburtstag())
                .collect(Collectors.joining("\n"));
    }

    /**
     * liefert die Anzahl der Kunden, die jetzt mindestens 67 sind
     * @return
     */
    public long getAnzahlSenioren(){


        return konten.values().stream()
                .map(Konto::getInhaber)
                //.map(konto -> new Kunde(konto.getInhaber().getVorname(),konto.getInhaber().getNachname(),konto.getInhaber().getAdresse(),konto.getInhaber().getGeburtstag()))
                .distinct()
                .filter(kunde -> kunde.getGeburtstag().isBefore(LocalDate.now().minusYears(67)) ||
                        kunde.getGeburtstag().isEqual(LocalDate.now().minusYears(67)))
                .count();
                // .map(kunde ->1)             // aus jeden Kudne wird eine 1
                // .reduce (0,(a,b)) -> a+b;  // addere dieses einsen miteindaner ---->  macht das selbe wie .count()
    }

    // Sie speichert this in den angegebenen ziel-Strom mit allen in der Bank gespeicherten
    //Informationen. Geht dabei etwas schief, wird eine IOException geworfen.
    public void speichern(OutputStream ziel) throws  IOException{

        try (ObjectOutputStream oos = new ObjectOutputStream(ziel)) {
            oos.writeObject(this);
        }

    }
    //Sie liest aus der angegebenen Quelle ein Bank-Objekt ein. geht dabei etwas schief,
    //soll eine leere Bank zurückgegeben werden.
    public static Bank einlesen(InputStream quelle){
        try (ObjectInputStream ois = new ObjectInputStream(quelle)) {
            // Versuche, das Objekt zu lesen und zu casten
            Object obj = ois.readObject();
            if (obj instanceof Bank) {
                return (Bank) obj;
            } else {
                // Falls das gelesene Objekt keine Bank ist (sollte nicht passieren, wenn nur Bank-Objekte gespeichert werden)
                System.err.println("Das gelesene Objekt ist keine Bank-Instanz.");
                return new Bank(0); // Oder eine andere Standard-Bankleitzahl für eine leere Bank
            }
        } catch (IOException | ClassNotFoundException e) {
            // Fange IO-Fehler (z.B. Datei nicht gefunden, Lesefehler)
            // und ClassNotFoundException (wenn die Klasse des Objekts nicht gefunden wird)
            System.err.println("Fehler beim Einlesen der Bankdaten: " + e.getMessage());
            // Stacktrace nur für Debugging ausgeben, im Produktivsystem oft unerwünscht
            // e.printStackTrace();
            return new Bank(0); // Gib eine neue, leere Bank zurück, wie in der Anforderung
        }
    }







}
