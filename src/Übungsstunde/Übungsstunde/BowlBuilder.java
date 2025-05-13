package Übungsstunde;

/**
 * erstellt aus einer Zutatenliste eine Bowl
 *
 */
public interface BowlBuilder {
	
	/**
	 * fügt eine Zutat zur Bowl hinzu
	 * @param zutat hinzuzufügende Zutat
	 */
    void add(String zutat);

    /**
     * erzeugt die fertige Bowl aus allen vorher hinzugefügten Zutaten.
     * Die bisher hinzugefügten Zutaten werden gelöscht, der BowlBuilder
     * ist nach diesem Methodenaufruf also wieder "leer" für eine neue Bowl.
     * @return fertige Bowl, die alle seit dem letzten Aufruf von build() hinzugefügten Zutaten enthält
     */
    Bowl build();
}
