package bankprojekt.verarbeitung;

/**
 * Abstrakte Fabrik für die Erzeugung von Konto-Objekten.
 * Definiert die Schnittstelle für das Erstellen verschiedener Kontotypen.
 */
public abstract class Kontofabrik {


    //public abstract  Konto erstellen(Kunde inhaber,long kontonummer);

    // oder hier sollte die Switch Case eingebracht werden -- >
    // auch mittels Object ...args für 0 oder mehre Paraemter damit auch Geldbetrag pbegeben werden könnte für Girokonto


    /**
     * Erstellt ein Sparbuch-Objekt.
     * @param inhaber Der Kontoinhaber.
     * @param kontonummer Die Kontonummer.
     * @return Ein neues Sparbuch-Objekt.
     */
    public abstract Sparbuch erstelleSparbuch(Kunde inhaber, long kontonummer);

    /**
     * Erstellt ein Girokonto-Objekt.
     * @param inhaber Der Kontoinhaber.
     * @param kontonummer Die Kontonummer.
     * @param dispo Der Dispositionskredit.
     * @return Ein neues Girokonto-Objekt.
     */
    public abstract Girokonto erstelleGirokonto(Kunde inhaber, long kontonummer, Geldbetrag dispo);

    /**
     * Erstellt ein Aktienkonto-Objekt.
     * @param inhaber Der Kontoinhaber.
     * @param kontonummer Die Kontonummer.
     * @return Ein neues Aktienkonto-Objekt.
     */
    public abstract Aktienkonto erstelleAktienkonto(Kunde inhaber, long kontonummer);
}
