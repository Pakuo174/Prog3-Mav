package Übungsstunde.uebungsveranstaltung12.wetter.wetterPropertyChange;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Anzeige, mit der aus den bisherigen Wetterdaten eine Vorhersage abgeleitet wird
 */
public class VorhersageAnzeige implements PropertyChangeListener
{
    @Override
	public void propertyChange(PropertyChangeEvent e)
	{
		double aktuellerLuftdruck = (double)e.getNewValue();
		double letzterLuftdruck = (double)e.getOldValue();

		System.out.println("Vorhersage: ");
		if (aktuellerLuftdruck > letzterLuftdruck) {
			System.out.println("Wetter-Besserung in Sicht!");
		} else if (aktuellerLuftdruck == letzterLuftdruck) {
			 System.out.println("Wetter geht weiter so." );
		} else if (aktuellerLuftdruck < letzterLuftdruck) {
			System.out.println( "Gehen Sie von kälterem, regnerischem Wetter aus.");
		}
	}
}
