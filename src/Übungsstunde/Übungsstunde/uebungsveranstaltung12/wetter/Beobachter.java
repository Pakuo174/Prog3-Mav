package Übungsstunde.uebungsveranstaltung12.wetter;

/**
 * Beobachter des Wetters
 */
public interface Beobachter {
	/**
	 * wird aufgerufen, immer wenn sich das Wetter ändert
	 * @param wetter aktuelle Daten
	 */
	void aktualisieren(Wetterdaten wetter);

}
