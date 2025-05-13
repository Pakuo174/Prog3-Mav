package Übungsstunde;

import java.util.List;

/**
 * stellt eine kleine Mittagsmahlzeit dar
 */
public interface Bowl {
	/**
	 * Preis der Bowl
	 * @return Gesamtpreis der Bowl
	 */
    double getPreis();
    /**
     * Kalorienzahl der Bowl
     * @return Gesamtkalorien der Bowl
     */
    int getKalorien();
    /**
     * in der Bowl enthaltene Zutaten
     * @return Liste der Namen der in der Bowl enthaltenen Zutaten
     */
    List<String> getZutatennamen();
}
