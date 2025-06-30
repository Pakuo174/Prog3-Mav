package Übungsstunde.uebungsveranstalung11;

/**
 * Kaffee, ein mögliches Getränk aus einem Getränkeautomaten
 */
public class Kaffee extends AutomatenGetraenk {

    /**
     * Kaffe durch den Filter laufen lassen
     */
	@Override
	protected void aufgiessen()
    {
        System.out.println("Wasser durch Kaffeefilter laufen lassen");
    }

    /**
     * es werden Milch und Zucker zum Kaffee hinzugefügt
     */
	@Override
    public void zutatHinzufuegen()
    {
        System.out.println("Milch und Zucker dazu");
    }
	
	@Override
	public int getTemperatur() {
		return 10;
	}
}
