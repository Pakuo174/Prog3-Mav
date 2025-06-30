package bankprojekt.verarbeitung;

/**
 * Abstrakte Fabrik für die Erzeugung von Konto-Objekten.
 * Definiert die Schnittstelle für das Erstellen verschiedener Kontotypen.
 * Die konkreten Implementierungen dieser Fabrik sind für die Logik der Kontoerzeugung
 * basierend auf dem Typ-String verantwortlich.
 */
public abstract class Kontofabrik {

    /**
     * Erstellt ein Konto-Objekt eines bestimmten Typs.
     * Die Implementierung dieser Methode in den konkreten Fabriken
     * ist für die Zuordnung des kontoTyp-Strings zum tatsächlichen Kontotyp und dessen Erzeugung verantwortlich.
     *
     * @param kontoTyp Der Typ des zu erstellenden Kontos (z.B. "sparbuch", "girokonto", "aktienkonto").
     * @param inhaber Der Kontoinhaber.
     * @param kontonummer Die Kontonummer.
     * @param betrag Ein optionaler Betrag, z.B. für den Dispokredit beim Girokonto.
     * @return Ein neues Konto-Objekt des angeforderten Typs.
     * @throws IllegalArgumentException Wenn der angeforderte Kontotyp unbekannt ist oder Parameter fehlen/ungültig sind.
     */
    public abstract Konto erstelleKonto(String kontoTyp, Kunde inhaber, long kontonummer, Geldbetrag betrag);
}