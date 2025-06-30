package Übungsstunde.uebungsveranstalung11;

/**
 * weißer Kakao mit echter Vollmilch
 * @author Doro
 *
 */
public class WeisserKakao extends AutomatenGetraenk {
    @Override
	public void aufgiessen()
	{
		System.out.println("Weißes Edel-Schokoladenpulver langsam rein in Vollmilch auflösen");
	}
	
    /**
     * Pistazie hinzufügen
     */
    @Override
    public void zutatHinzufuegen()
    {
    	System.out.println("gemahlene Pistazien drüberstreuen");
    }
    
    @Override
	public int getTemperatur()
	{
		return 6;
	}
}
