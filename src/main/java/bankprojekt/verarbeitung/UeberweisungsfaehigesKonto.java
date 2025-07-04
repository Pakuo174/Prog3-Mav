package bankprojekt.verarbeitung;

import java.io.Serializable;

/**
 * ein Bank-Konto, das Absender und Ziel von Überweisungen
 * sein kann.
 */
public abstract class UeberweisungsfaehigesKonto extends Konto implements Serializable {


	private long bankleitzahl;
	/**
	 * setzt alle Eigenschaften des Kontos auf Standardwerte
	 */
	public UeberweisungsfaehigesKonto()
	{
		super();
	}
	
	/**
	 * Setzt die beiden Eigenschaften kontoinhaber und kontonummer auf die angegebenen Werte,
	 * der anfängliche Kontostand wird auf 0 gesetzt.
	 *
	 * @param inhaber der Inhaber
	 * @param kontonummer die gewünschte Kontonummer
	 * @throws IllegalArgumentException wenn der inhaber null ist
	 */
	public UeberweisungsfaehigesKonto(Kunde inhaber, long kontonummer) {
		super(inhaber, kontonummer);
	}

	/**
	 * Konstruktor für ein Überweisungskonto
	 * @param inhaber der Kontoinhaber
	 * @param kontonummer die gewünschte Kontonummer
	 * @param bankleitzahl die Bankleitzahl der zugehörigen Bank
	 * @throws IllegalArgumentException wenn der Inhaber null ist
	 */
	public UeberweisungsfaehigesKonto(Kunde inhaber, long kontonummer, long bankleitzahl) {
		super(inhaber, kontonummer);  // Konstruktor der Oberklasse aufrufen
		this.bankleitzahl = bankleitzahl;
	}

	/**
     * bucht den angegebenen Betrag von this als Überweisung ab, 
     * falls es nicht gesperrt ist und alle kontospezifischen 
     * Regeln für die Überweisung eingehalten werden.
     * Am Empfängerkonto wird keine Änderung vorgenommen, da davon ausgegangen wird, dass dieses sich
     * bei einer anderen Bank befindet.
     * @param betrag double
     * @param empfaenger String
     * @param nachKontonr int
     * @param nachBlz int
     * @param verwendungszweck String
     * @return boolean true, wenn die Überweisungsabbuchung ausgeführt wurde,
     *                 false, wenn nicht (weil kontospezifische Regeln verletzt wurden, 
     *                 	     z.B. Kontostand reicht nicht aus)
     * @throws GesperrtException wenn das Konto gesperrt ist
     * @throws IllegalArgumentException wenn der Betrag negativ bzw. NaN bzw. unendlich ist oder
     * 					empfaenger oder verwendungszweck null ist
     */
    public abstract boolean ueberweisungAbsenden(Geldbetrag betrag, String empfaenger, long nachKontonr, long nachBlz, String verwendungszweck)  throws GesperrtException;
    
    /**
     * this empfängt den angegebenen betrag per Überweisung
     * @param betrag double
     * @param vonName String
     * @param vonKontonr int
     * @param vonBlz int
     * @param verwendungszweck String
     * @throws IllegalArgumentException wenn der Betrag negativ bzw. NaN bzw. unendlich ist oder
     * 									vonName oder verwendungszweck null ist
     */
    public abstract void ueberweisungEmpfangen(Geldbetrag betrag, String vonName, long vonKontonr, long vonBlz, String verwendungszweck);
}
