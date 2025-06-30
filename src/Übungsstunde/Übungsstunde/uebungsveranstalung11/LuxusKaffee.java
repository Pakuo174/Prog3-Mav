package Übungsstunde.uebungsveranstalung11;

/**
 * Kaffee, ein mögliches Getränk aus einem Getränkeautomaten
 */
public class LuxusKaffee extends AutomatenGetraenk
{
	@Override
    public void aufgiessen()
    {
        System.out.println("kalkfreies Wasser durch Kaffeefilter laufen lassen");
    }

	@Override
    public void zutatHinzufuegen()
    {
        System.out.println("Echte Kuh-Milch und Rohrzucker dazu");
    }
	
	@Override
	public int getTemperatur()
	{
		return 11;
	}
}
