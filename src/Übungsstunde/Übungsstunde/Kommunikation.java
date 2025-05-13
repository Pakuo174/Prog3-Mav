package Übungsstunde;

/**
 * kommuniziert mit einem Benutzer
 *
 */
public interface Kommunikation {

	/**
	 * Die Methode stellt frage für den menschlichen Benutzer sichtbar dar und
	 * erwartet dann seine Antwort
	 * @param frage an den Benutzer gestellte Frage
	 * @return Die Antwort des Benutzers
	 */
    String fragen(String frage);
    
    /**
     * Die Methode gibt nachricht für den Benutzer sichtbar aus
     * @param nachricht der auszugebende Text
     */
    void ausgeben(String nachricht);
}
