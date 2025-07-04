package bankprojekt.verarbeitung;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * ein Sparbuch, d.h. ein Konto, das nur recht eingeschränkt genutzt
 * werden kann. Insbesondere darf man monatlich nur höchstens 2000€
 * abheben, wobei der Kontostand nie unter 0,50€ fallen darf. Zur Abfrage
 * des heutigen Datums (was die Zuordnung zweier Abhebungen zu einem gemeinsamen
 * Monat bestimmt) kann ein Kalender-Objekt angegeben werden. 
 * @author Doro
 *
 */public class Sparbuch extends Konto implements Serializable {
	/**
	 * Zinssatz, mit dem das Sparbuch verzinst wird. 0,03 entspricht 3%
	 */
	private double zinssatz;

	/**
	 * Monatlich erlaubter Gesamtbetrag für Abhebungen
	 */
	public static final Geldbetrag ABHEBESUMME = new Geldbetrag(2000);

	/**
	 * Betrag, der nach einer Abhebung mindestens auf dem Konto bleiben muss
	 */
	public static final Geldbetrag MINIMUM = new Geldbetrag(0.5);

	/**
	 * Betrag, der im aktuellen Monat bereits abgehoben wurde
	 */
	private Geldbetrag bereitsAbgehoben = Geldbetrag.NULL_EURO;

	/**
	 * Kalender zur Abfrage des heutigen Datums
	 */
	private Kalender kalender;

	/**
	 * *auch für das Datum des Erstellens des Kontos
	 * Monat und Jahr der letzten Abhebung
	 */
	private LocalDate zeitpunkt;

	/**
	 * ein Standard-Sparbuch
	 */
	public Sparbuch() {
		this.zinssatz = 0.03;
		this.kalender = new Kalender();
		zeitpunkt = kalender.getHeutigesDatum();
	}

	/**
	 * ein Standard-Sparbuch, das inhaber gehört und die angegebene Kontonummer hat
	 * Das heutige Datum wird an allen Stellen, wo es benötigt wird, vom Betriebssystem
	 * abgefragt.
	 *
	 * @param inhaber     der Kontoinhaber
	 * @param kontonummer die Wunsch-Kontonummer
	 * @throws IllegalArgumentException wenn inhaber null ist
	 */
	public Sparbuch(Kunde inhaber, long kontonummer) {
		this(inhaber, kontonummer, new Kalender());
	}

	/**
	 * ein Standard-Sparbuch, das inhaber gehört und die angegebene Kontonummer hat
	 *
	 * @param inhaber     der Kontoinhaber
	 * @param kontonummer die Wunsch-Kontonummer
	 * @param kalender    der Kalender, der für die Abfrage des heutigen Datums benutzt wird
	 * @throws IllegalArgumentException wenn inhaber oder kalender null ist
	 */
	public Sparbuch(Kunde inhaber, long kontonummer, Kalender kalender) {
		super(inhaber, kontonummer);
		this.zinssatz = 0.03;
		if (kalender == null)
			throw new IllegalArgumentException();
		this.kalender = kalender;
		zeitpunkt = kalender.getHeutigesDatum();
	}

	public Sparbuch(Kunde inhaber, long kontonummer, long bankleitzahl, Kalender kalender) {
		super(inhaber, kontonummer);  // Konstruktor der Oberklasse aufrufen
		this.zinssatz = 0.03;
		if (kalender == null)
			throw new IllegalArgumentException();
		this.kalender = kalender;
		zeitpunkt = kalender.getHeutigesDatum();
		this.bankleitzahl = bankleitzahl;
	}

	@Override
	public String toString() {
		String ausgabe = "-- SPARBUCH --" + System.lineSeparator() +
				super.toString()
				+ "Zinssatz: " + this.zinssatz * 100 + "%" + System.lineSeparator();
		return ausgabe;
	}

	/**
	 * kontrolliere ob mehr Geld auf Konto ist als Minimum -> Limit nicht überschreiten
	 * kontrolliere ob genug Geld auf dem Konto drauf ist -> ausreichender Kontostand
	 *
	 * @param betrag der abzuhebende Betrag
	 * @return
	 */
	@Override
	public boolean pruefeAbhebungSpezifisch(Geldbetrag betrag) {
		LocalDate heute = kalender.getHeutigesDatum();

		if (heute.getMonth() != zeitpunkt.getMonth() || heute.getYear() != zeitpunkt.getYear()) {
			this.bereitsAbgehoben = Geldbetrag.NULL_EURO; // setze Betrag zurück, wenn Monat oder Jahr sich geändert hat

		}
		Geldbetrag neuKontostandNachAbhebung = getKontostand().minus(betrag);
		Geldbetrag gesamtAbgehobenImMonat = bereitsAbgehoben.plus(betrag);

		boolean kontostandAusreichend = neuKontostandNachAbhebung.compareTo(MINIMUM) >= 0;
		boolean kontostandNichtÜberLimit = gesamtAbgehobenImMonat.compareTo(ABHEBESUMME) <= 0;

		if (kontostandAusreichend && kontostandNichtÜberLimit) {

			// erst hier tatsächlich erhöhen, wenn Bedingungen stimmen

			this.bereitsAbgehoben = gesamtAbgehobenImMonat;
			this.zeitpunkt = heute;
			return true;

		}else {
			return false;
		}
    }
}
