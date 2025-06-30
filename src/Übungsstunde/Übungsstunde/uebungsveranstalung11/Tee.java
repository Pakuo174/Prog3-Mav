package Übungsstunde.uebungsveranstalung11;

/**
 * Tee, ein mögliches Automatengetränk
 */
public class Tee extends AutomatenGetraenk{

    /**
     * Tee ziehen lassen
     */
    protected void aufgiessen()
    {
        System.out.println("Tee ziehen lassen" );
    }

    /**
     * Zitrone hinzufügen
     */
    public void zutatHinzufuegen()
    {
        System.out.println("Zitrone dazu" );
    }

	@Override
	public int getTemperatur() {
		return 8;
	}
    
    
}
