package Übungsstunde;

import java.util.List;

/**
 * Der InputParser soll aus einer Eingabe vom Benutzer die
 * genannten Zutaten extrahieren
 */
public interface EingabeParser {
	/**
	 * extrahiert aus bestellung alle darin genannten Zutaten
	 * @param bestellung eingegebener Text, der geparst werden soll
	 * @return eine Liste aller Zutaten, deren Namen in bestellung vorkommen
	 */
    List<String> bestellungParsen(String bestellung);
}
