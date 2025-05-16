package Übungsstunde;

import java.util.*;

/**
 * nimmt eine Bestellung vom Benutzer entgegen und erzeugt daraus - wenn
 * möglich - die gewünschte Bowl
 *
 */
public class BowlBestellung {
	/**
	 * An den Benutzer zu stellende Frage
	 */
	public static final String FRAGE = "Willkommen beim Bowl-Bot! Was möchtest du gerne bestellen?";
	/**
	 * Meldung, wenn eine Bestellung nicht verstanden wurde
	 */
	public static final String SORRY = "Entschuldigung, ich habe dich nicht verstanden.";
	/**
	 * Text für die Ausgabe des Preises
	 */
	public static final String PREISTEXT = "Deine Bowl kostet ";
    private final Kommunikation input;
    private final BowlBuilder builder;
    private final EingabeParser parser;

    /**
     * erzeugt eine BowlBestellung
     * @param kom das Kommunikationsojekt, das die Interaktion mit
     * 			  der Benutzer erlaubt
     * @param builder  der Builder, der die gewünschte Bowl erzeugt
     * @param ip  der Parser, der die Eingaben des Benutzers zerlegt und daraus
     * 	          die gewünschten Zutaten extrahiert
     */
    public BowlBestellung(Kommunikation kom, BowlBuilder builder, EingabeParser ip) {
        this.input = kom;
        this.builder = builder;
        this.parser = ip;
    }

    /**
     * führt die Bestellung aus, d.h. <ul>
     * <li> FRAGE wird an das Kommunikationsobjekt übergeben, 
     * um den Benutzer nach seinen Wünschen zu fragen.</li>
     * <li>Ist die Eingabe des Benutzers nicht mit Hilfe des Parsers parsebar, 
     * wird SORRY vom Kommunikationsobjekt angezeigt.</li>
     * <li>Ansonsten baut der Builder eine Bowl mit der geparsten Zutatenliste
     * zusammen. Der Preis dieser Bowl wird mit Hilfe des Kommunikationsobjektes angezeigt.<li></ul>
     * @return die erzeugte Bowl mit allen gewünschten Zutaten bzw. null, wenn die
     * 	       eingegebene Bestellung nicht parsebar ist.
     */
    public Bowl bestellungDurchfuehren() {
        String bestellung = input.fragen(FRAGE);
        Bowl bowl = null;
        String reaktion = null;
        List<String> ingredients = parser.bestellungParsen(bestellung);
        if(ingredients.isEmpty()) {
        	reaktion = SORRY;
        } else {
            for (String ing : ingredients){ 
            	builder.add(ing);
            	bowl = builder.build();
            }
            reaktion = PREISTEXT + bowl.getPreis();
            input.ausgeben(reaktion);
        }
        return bowl;
    }
}
