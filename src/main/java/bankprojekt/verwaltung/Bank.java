package bankprojekt.verwaltung;

import bankprojekt.verarbeitung.*;

import java.util.HashMap;
import java.util.Map;

public class Bank  {


    private long bankleitzahl;
    // Long = Typ für Schlüssel , Datentyp als Referenz auf ein konkretes Kontoobjekt, z.B. Sparbuch oder Girokonto
    public Map<Long,Konto> konten;
    private long letzteKontonummer = 0;


    /**
     * Erstellt eine Bankinstanz mit fester Bankleitzahl und einer leeren Kontenliste.
     * @param bankleitzahl Variable zur Zuordnung einer Bank
     */
    public Bank(long bankleitzahl){
        this.bankleitzahl = bankleitzahl;
        this.konten = new HashMap<>();  // Initialisierung der Map
    }

    public long getBankleitzahl(){
        return bankleitzahl;

    }

    /**
     * erstellt ein Girokonto für den angegebenen Kunden. Dabei soll die Methode eine beliebige neue, noch nicht vergebene Kontonummer erzeugen
     * * Das Konto erhält eine automatisch generierte Kontonummer - beginnend bei 1 - diese ist gleichzeitig der Schlüssel in der HashMap
     * * Erstellt ein Girokonto mit einem Standard-Dispositionskredit von 500 Euro.
     *
     * *++letzteKontunummer erhöht den Wert immer +1 und speichert ihn für das nächste Konto im Attribut ab
     * @param inhaber definiert eine Instanz der Klasse Kunde
     * @return ist die neue Kontonummer
     */
    public long girokontoErstellen(Kunde inhaber){
        if (inhaber == null) {
            throw new IllegalArgumentException("Inhaber darf nicht null sein");
        }
        long neueNummer = ++letzteKontonummer;
        Konto konto = new Girokonto(inhaber, neueNummer,this.getBankleitzahl(),new Geldbetrag(500) );
        konten.put(neueNummer,konto);
        return neueNummer;
    }

    /**
     * Erstellt ein Sparbuch für den angegebenen Kunden.
     * Die Kontonummer wird automatisch generiert, beginnend bei 1. Diese dient gleichzeitig als Schlüssel in der Konten-HashMap.
     *
     * Im Unterschied zum Girokonto hat ein Sparbuch Einschränkungen bei der Abhebung
     *
     * @param inhaber Der Kontoinhaber des neuen Sparbuchs.
     * @return Die neu vergebene Kontonummer.
     * @throws IllegalArgumentException wenn der Inhaber {@code null} ist.
     */
    public long sparbuchErstellen(Kunde inhaber){
        if (inhaber == null) {
            throw new IllegalArgumentException("Inhaber darf nicht null sein");
        }
        long neueNummer = ++letzteKontonummer;
        Konto konto = new Sparbuch(inhaber, neueNummer,this.getBankleitzahl(),new Kalender());
        konten.put(neueNummer,konto);
        return neueNummer;
    }


}
