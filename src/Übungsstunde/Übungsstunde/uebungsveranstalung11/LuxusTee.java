package Übungsstunde.uebungsveranstalung11;

/**
 * Ganz besonders guter Tee
 * @author Doro
 *
 */
public class LuxusTee extends AutomatenGetraenk {

    @Override
    public void aufgiessen()
    {
       System.out.println("Earl-Grey-Tee ziehen lassen");
    }

    @Override
    public void zutatHinzufuegen()
    {
    	System.out.println("Echte Zitrone dazu");
    }
    
    @Override
	public int getTemperatur()
	{
		return 8;
	}
}
