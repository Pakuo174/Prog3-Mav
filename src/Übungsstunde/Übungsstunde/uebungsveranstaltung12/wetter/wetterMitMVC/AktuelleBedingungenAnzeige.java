package Übungsstunde.uebungsveranstaltung12.wetter.wetterMitMVC;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * zeigt die aktuellen Wetterbedingungen an
 */
public class AktuelleBedingungenAnzeige implements PropertyChangeListener{

    @Override
	public void propertyChange(PropertyChangeEvent e)
	{
		if(e.getPropertyName().equals(Wetterdaten.TEMPERATUR))
		{
			Wetterdaten w = (Wetterdaten) e.getSource();
			System.out.println("Aktuelle Bedingungen: " + w.getTemperatur()
			+ " Grad C und " + w.getFeuchtigkeit() + "% Luftfeuchtigkeit.");
		}
		if(e.getPropertyName().equals(Wetterdaten.FEUCHTIGKEIT))
		{
			double feuchtAlt = (double) e.getOldValue();
			double feuchtNeu = (double) e.getNewValue();
			System.out.println("Die Luftfeuchtigkeit hat sich von " + feuchtAlt
					+ "% auf " + feuchtNeu + "% verändert.");
		}
	}
}