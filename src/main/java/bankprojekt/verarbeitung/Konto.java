package bankprojekt.verarbeitung;

import bankprojekt.geld.Waehrung;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.Serializable;

/**
 * stellt ein allgemeines Bank-Konto dar
 */
public abstract class Konto implements Comparable<Konto>, Serializable
{
    protected long bankleitzahl;

    public void ausgeben() {
        System.out.println(this.toString());
    }

    /**
     * der Kontoinhaber
     */
    private Kunde inhaber;

    /**
     * die Kontonummer
     */
    private final long nummer;

    /**
     * der aktuelle Kontostand
     */
   // private Geldbetrag kontostand;
    private ReadOnlyObjectWrapper<Geldbetrag> kontostand;
    /**
     * setzt den aktuellen Kontostand
     * @param kontostand neuer Kontostand, darf nicht null sein
     */
    protected void setKontostand(Geldbetrag kontostand) {
        if(kontostand != null)
            this.kontostand.set(kontostand);
    }

    /**
     * Wenn das Konto gesperrt ist (gesperrt = true), können keine Aktionen daran mehr vorgenommen werden,
     * die zum Schaden des Kontoinhabers wären (abheben, Inhaberwechsel)
     */
    //private boolean gesperrt;
    private BooleanProperty gesperrt;

    /**
     * Property für negativen und postiven Zustand des Konto
     */
    private BooleanProperty imPlus;

    /**
     * Setzt die beiden Eigenschaften kontoinhaber und kontonummer auf die angegebenen Werte,
     * der anfängliche Kontostand wird auf 0 gesetzt.
     *
     * @param inhaber der Inhaber
     * @param kontonummer die gewünschte Kontonummer
     * @throws IllegalArgumentException wenn der inhaber null ist
     */
    public Konto(Kunde inhaber, long kontonummer) {
        if(inhaber == null)
            throw new IllegalArgumentException("Inhaber darf nicht null sein!");
        this.inhaber = inhaber;
        this.nummer = kontonummer;
        this.kontostand = new ReadOnlyObjectWrapper<>(Geldbetrag.NULL_EURO);
        this.gesperrt = new SimpleBooleanProperty(false);
        this.imPlus = new SimpleBooleanProperty(true); // Initial im Plus, da Kontostand 0
        this.kontostand.addListener((obs, oldVal, newVal) -> updateImPlusProperty(newVal));
    }

    /**
     * setzt alle Eigenschaften des Kontos auf Standardwerte
     */
    public Konto() {
        this(Kunde.MUSTERMANN, 1234567);
    }

    /**
     * liefert den Kontoinhaber zurück
     * @return   der Inhaber
     */
    public Kunde getInhaber() {
        return this.inhaber;
    }

    /**
     * setzt den Kontoinhaber
     * @param kinh   neuer Kontoinhaber
     * @throws GesperrtException wenn das Konto gesperrt ist
     * @throws IllegalArgumentException wenn kinh null ist
     */
    public void setInhaber(Kunde kinh) throws GesperrtException{
        if (kinh == null)
            throw new IllegalArgumentException("Der Inhaber darf nicht null sein!");
        if(this.gesperrt.get()) {
            throw new GesperrtException(this.nummer);
        }
        this.inhaber = kinh;

    }

    private void updateImPlusProperty(Geldbetrag neuerKontostand) {
        if (neuerKontostand != null) {
            this.imPlus.set(!neuerKontostand.isNegativ());
        }
    }
    public BooleanProperty imPlusProperty() {
        return this.imPlus;
    }

    /**
     * Getter für die Property hinzufügen
     * @return kontostand
     */
    public ReadOnlyObjectProperty<Geldbetrag> kontostandProperty() {
        return this.kontostand.getReadOnlyProperty();
    }
    public BooleanProperty gesperrtProperty() {
        return this.gesperrt;
    }

    /**
     * liefert den aktuellen Kontostand
     * @return   Kontostand
     */
    public Geldbetrag getKontostand() {
        return kontostand.get();
    }

    /**
     * liefert die Kontonummer zurück
     * @return   Kontonummer
     */
    public long getKontonummer() {
        return nummer;
    }

    /**
     * liefert zurück, ob das Konto gesperrt ist oder nicht
     * @return true, wenn das Konto gesperrt ist
     */
    public boolean isGesperrt() {
        return gesperrt.get();
    }

    /**
     * Erhöht den Kontostand um den eingezahlten Betrag.
     *
     * @param betrag double
     * @throws IllegalArgumentException wenn der betrag negativ ist
     */
    public void einzahlen(Geldbetrag betrag) {
        if (betrag == null || betrag.isNegativ()) {
            throw new IllegalArgumentException("Falscher Betrag");
        }
        setKontostand(getKontostand().plus(betrag));
    }

    @Override
    public String toString() {
        String ausgabe;
        ausgabe = "Kontonummer: " + this.getKontonummerFormatiert()
                + System.getProperty("line.separator");
        ausgabe += "Inhaber: " + this.inhaber;
        ausgabe += "Aktueller Kontostand: " + getKontostand() + " ";
        ausgabe += this.getGesperrtText() + System.getProperty("line.separator");
        return ausgabe;
    }

    /**
     * Template-Methode: Versucht, den geforderten Betrag vom Konto abzuheben.
     * Definiert den allgemeinen Abhebeprozess, bei dem spezifische Prüfungen
     * den Unterklassen überlassen werden.
     * @param betrag abzuhebender Betrag
     * @return true, wenn die Abhebung erfolgreich war, false sonst
     * @throws GesperrtException wenn das Konto gesperrt ist
     * @throws IllegalArgumentException wenn der Betrag ungültig ist
     */
    public final  boolean abheben(Geldbetrag betrag)
            throws GesperrtException{
        if (betrag == null || betrag.isNegativ()) {
            throw new IllegalArgumentException("Betrag ungültig: " + betrag);
        }
        if (this.isGesperrt()) {
            throw new GesperrtException(this.getKontonummer());
        }

        // Hook-Methode für kontospezifische Prüfungen
        if (!pruefeAbhebungSpezifisch(betrag)) {
            return false; // Spezifische Regeln erlauben keine Abhebung
        }

        setKontostand(getKontostand().minus(betrag));
        return true;
    }

    /**
     * Überprüft die kontospezifischen Regeln für eine Abhebung.
     * Muss von den konkreten Unterklassen implementiert werden.
     * @param betrag der abzuhebende Betrag
     * @return true, wenn die kontospezifischen Regeln die Abhebung erlauben, false sonst.
     */
    protected abstract boolean pruefeAbhebungSpezifisch(Geldbetrag betrag);

    /**
     * sperrt das Konto, Aktionen zum Schaden des Benutzers sind nicht mehr möglich.
     */
    public void sperren() {
        this.gesperrt.set(true);
    }

    /**
     * entsperrt das Konto, alle Kontoaktionen sind wieder möglich.
     */
    public void entsperren() {
        this.gesperrt.set(false);
    }


    /**
     * liefert eine String-Ausgabe, wenn das Konto gesperrt ist
     * @return "GESPERRT", wenn das Konto gesperrt ist, ansonsten ""
     */
    public String getGesperrtText()
    {
        if (this.gesperrt.get())
        {
            return "GESPERRT";
        }
        else
        {
            return "";
        }
    }

    /**
     * liefert die ordentlich formatierte Kontonummer
     * @return auf 10 Stellen formatierte Kontonummer
     */
    public String getKontonummerFormatiert()
    {
        return String.format("%10d", this.nummer);
    }

    /**
     * Vergleich von this mit other; Zwei Konten gelten als gleich,
     * wen sie die gleiche Kontonummer haben
     * @param other das Vergleichskonto
     * @return true, wenn beide Konten die gleiche Nummer haben
     */
    @Override
    public boolean equals(Object other)
    {
        if(this == other)
            return true;
        if(other == null)
            return false;
        if(this.getClass() != other.getClass())
            return false;
        if(this.nummer == ((Konto)other).nummer)
            return true;
        else
            return false;
    }

    @Override
    public int hashCode()
    {
        return 31 + (int) (this.nummer ^ (this.nummer >>> 32));
    }

    @Override
    public int compareTo(Konto other)
    {
        if(other.getKontonummer() > this.getKontonummer())
            return -1;
        if(other.getKontonummer() < this.getKontonummer())
            return 1;
        return 0;
    }

    /**
     * wechseln der Währung die das Konto aktuell führt
     * @param neu wechseln zu anderer WÄhrung
     */
    public void waehrungswechsel(Waehrung neu){
       if (neu == null)
           throw new IllegalArgumentException();

        Geldbetrag neuerBetrag = this.kontostand.get().umrechnen(neu);
        this.setKontostand(neuerBetrag); // Ruft set(neuerBetrag) auf der Property auf
    }
}
