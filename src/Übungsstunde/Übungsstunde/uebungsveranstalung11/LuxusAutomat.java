package Übungsstunde.uebungsveranstalung11;

/**
 * stellt einen ganz tollen Getränkeautomaten mit Edelgetränken dar
 */
public class LuxusAutomat extends Automat {

	@Override
	public AutomatenGetraenk erzeugen(int auswahl) {
	    AutomatenGetraenk getraenk = null;

	    if (auswahl == 1)
	    {
	        getraenk = new LuxusKaffee();
	    }
	    if (auswahl == 2)
	    {
	        getraenk = new LuxusTee();
	    }
	    if(auswahl == 3)
	    {
	        getraenk = new LuxusKakao();
	    }
	    if(auswahl == 4)
	    {
	        getraenk = new WeisserKakao();
	    }
	    return getraenk;
	}

}


/*"Wählen Sie aus unserem großen Angebot:" + System.lineSeparator();
+ "1 - Edler Kaffee aus Arabica-Bohnen" + System.lineSeparator();
+ "2 - Darjeeling-Tee" + System.lineSeparator();
+ "3 - Kakao aus echter Schokolade" + System.lineSeparator();
+ "4 - ganz neu, weißer Kakao für Genießer"
*/
