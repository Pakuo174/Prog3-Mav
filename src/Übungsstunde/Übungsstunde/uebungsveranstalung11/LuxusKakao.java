package Übungsstunde.uebungsveranstalung11;

/**
 * Edelkakao, hmm. lecker
 * @author Doro
 *
 */
public class LuxusKakao extends AutomatenGetraenk {

    @Override
	public void aufgiessen()
	{
		System.out.println("Edles Kakaopulver rein und mit echter Milch umrühren");
	}
    
    @Override
	public int getTemperatur()
	{
		return 7;
	}
}
