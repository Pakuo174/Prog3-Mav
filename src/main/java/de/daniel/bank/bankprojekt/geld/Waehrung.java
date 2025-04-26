package de.daniel.bank.bankprojekt.geld;

public enum Waehrung {

    EUR(1.0), // 1 EUR -> 1 EUR
    ESCUDO(109.8269), // 1 EUR = 109.8269 Escudos
    DOBRA(24304.7429), // 1 EUR = 24304.7429 Dobras
    FRANC(490.92); // 1 EUR = 490.92 Francs

    double umrKursEUro; // Attribut für Umrechnungskurs in Euro

    public static void main(String[] args) {
        // Beispiel für Ausgabe der Umrechnungskurse
        for (Waehrung waehrung : Waehrung.values()) {
            System.out.println(waehrung.name() + ": 1 EUR = " + waehrung.getUmrKursEUro() + " " + waehrung.name());
        }
    }

    // Konstruktor für Umrechnungskurs
    Waehrung(double umrKursEUro) {
        this.umrKursEUro = umrKursEUro;
    }

    // Getter für den Umrechnungskurs zum Euro
    public double getUmrKursEUro() {
        return this.umrKursEUro;
    }


}




