package de.daniel.bankprojekt.geld;

import org.decimal4j.util.DoubleRounder;

/**
 * Konstanten sind verschiedene Währungen - ausgehend von Euro werden die anderen Währungen betrachtet
 */
public enum Waehrung {

    EUR(1.0), // 1 EUR -> 1 EUR
    ESCUDO(109.8269), // 1 EUR = 109.8269 Escudos
    DOBRA(24304.7429), // 1 EUR = 24304.7429 Dobras
    FRANC(490.92); // 1 EUR = 490.92 Francs

    private double umrKursEuro; // Konstruktor, der den Wert speichert

    /**
     * Beispiel für Ausgabe der Umrechnungskurse
     */
    public static void main(String[] args) {

        for (Waehrung waehrung : Waehrung.values()) {
            System.out.println(waehrung.name() + ": 1 EUR = " + waehrung.getUmrKursEuro() + " " + waehrung.name());
        }
    }

    /**
     * Konstruktor für Umrechnungskurs
     * @param umrKursEuro
     */
    Waehrung(double umrKursEuro) {
        this.umrKursEuro = umrKursEuro;
    }

    /**
     *  simpler Getter für den Umrechnungskurs zum Euro
      * @return einfach nur den Wert umgerechnet zum 1 Euro
     */
    public double getUmrKursEuro() {

        return this.umrKursEuro;
    }

    /**
     *
     * @param zielw soll die Währung sein, in die umgerechet wird
     ** DoubleRounder rundet die Währung auf 2 Nachkommastellen
     * @return gewünschte Währung, indem zielw durch momentane Währung geteilt wird
     */
    public double getNewWährung(Waehrung zielw){
        if (zielw == null)
            throw new NullPointerException("Zielwährung darf nicht null sein");
        return DoubleRounder.round((zielw.umrKursEuro / this.umrKursEuro),2);
    }

}




