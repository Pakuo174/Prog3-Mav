package bankprojekt.verarbeitung;// package bankprojekt.verarbeitung;

import bankprojekt.geld.Waehrung; // Importieren, falls Geldbetrag eine Waehrung erwartet
import bankprojekt.verarbeitung.*;

/**
 * Konkrete Fabrik zur Erstellung von Mock-Konto-Objekten für Testzwecke.
 */
public class MockKontofabrik extends Kontofabrik {

    @Override
    public Sparbuch erstelleSparbuch(Kunde inhaber, long kontonummer) {
        Sparbuch sparbuch = new Sparbuch(inhaber, kontonummer);
        sparbuch.einzahlen(new Geldbetrag(1000.00, Waehrung.EUR)); // Beispiel: Startguthaben für Mock
        // Weitere Mock-Spezifikationen hier
        return sparbuch;
    }

    @Override
    public Girokonto erstelleGirokonto(Kunde inhaber, long kontonummer, Geldbetrag dispo) {
        Girokonto giro = new Girokonto(inhaber, kontonummer, dispo);
        giro.einzahlen(new Geldbetrag(500.00, Waehrung.EUR)); // Beispiel: Startguthaben für Mock
        return giro;
    }

    @Override
    public Aktienkonto erstelleAktienkonto(Kunde inhaber, long kontonummer) {
        Aktienkonto aktienkonto = new Aktienkonto(inhaber, kontonummer);
        aktienkonto.einzahlen(new Geldbetrag(20000.00, Waehrung.EUR)); // Beispiel: Startkapital für Mock
        return aktienkonto;
    }
}