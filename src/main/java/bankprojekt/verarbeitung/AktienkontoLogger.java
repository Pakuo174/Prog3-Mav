package bankprojekt.verarbeitung;// package bankprojekt.anwendungen; // Oder wo auch immer deine Anwendungslogik liegt

import bankprojekt.verarbeitung.Aktie;
import bankprojekt.verarbeitung.Aktienkonto;
import bankprojekt.verarbeitung.AktienkontoBeobachter;

/**
 * Ein Beispiel-Beobachter, der Änderungen im Aktiendepot auf der Konsole ausgibt.
 */
public class AktienkontoLogger implements AktienkontoBeobachter {
    private final String name;

    public AktienkontoLogger(String name) {
        this.name = name;
    }



    @Override
    public void aktualisieren(Aktienkonto konto, Aktie aktie, int anzahl, int neueStückzahl) {

        System.out.println(name + ": Aktiendepot von Konto " + konto.getKontonummer() + " hat sich geändert.");
        System.out.println("  Aktie: " + aktie.getWkn() + ", Menge " + Math.abs(anzahl));
        System.out.println("  Neuer Bestand von " + aktie.getWkn() + ": " + neueStückzahl + " Stück.");
        System.out.println("  Aktueller Kontostand: " + konto.getKontostand());
        System.out.println("---");
    }
}