package Übungsstunde.uebungsveranstaltung12.wetter;

import java.util.LinkedList;
import java.util.List;

/**
 * enthält die Beschreibung einer Wettersituation aus Temperatur,
 * Luftfeuchtigkeit und Luftdruck
 */
public class Wetterdaten {
	
	private List<Beobachter> anzeigenliste 
					= new LinkedList<>();
	
	/**
	* meldet b am Subjekt an
    * @param b der neue Beobachter
	*/
        public void anmelden(Beobachter b)
        {
        	if(b != null)
            		anzeigenliste.add(b);
        }

	/**
	* meldet b am Subjekt wieder ab
    * @param b der zu entfernende Beobachter
	*/
        public void abmelden(Beobachter b)
        {
            anzeigenliste.remove(b);
        }
        
    	/** 
    	* benachrichtigt alle angemeldeten Beobachter
    	*/
            protected void benachrichtigen()
            {
                    anzeigenliste.stream().forEach(b->b.aktualisieren(this));
            }



	/**
	 * aktuelle Temperatur
	 */
	private double temperatur;
	/**
	 * aktuelle Luftfeuchtigkeit
	 */
	private double feuchtigkeit;
	/**
	 * aktueller Luftdruck
	 */
	private double luftdruck;
	/**
	 * wie viel Sonnentanz es gab
	 */
	private double sonne = 0;
	/**
	 * wie viel Regentanz es gab
	 */
	private double regen = 0;

	/**
	 * erstellt eine Wettersituation, in der Temperatur, Luftfeuchtigkeit und
	 * Luftdruck 0 sind
	 */
	public Wetterdaten() {
		this.temperatur = 0;
		this.feuchtigkeit = 0;
		this.luftdruck = 0;
	}

	/**
	 * Setzen der aktuellen Wettersituation
	 * 
	 * @param temp
	 *            aktueller Temperaturwert
	 * @param feucht
	 *            aktuelle Luftfeuchtigkeit
	 * @param druck
	 *            aktueller Luftdruck
	 */
	public void setMesswerte(double temp, double feucht, double druck) {
		this.temperatur = temp + sonne;
		this.feuchtigkeit = feucht + regen;
		this.luftdruck = druck;
		
		benachrichtigen();
	}

	/**
	 * liefert die aktuelle gespeicherte Temperatur zurück
	 * 
	 * @return aktuelle Temperatur
	 */
	public double getTemperatur() {
		return temperatur;
	}

	/**
	 * liefert die aktuelle gespeicherte Luftfeuchtigkeit zurück
	 * 
	 * @return aktuelle Luftfeuchtigkeit
	 */
	public double getFeuchtigkeit() {
		return feuchtigkeit;
	}

	/**
	 * liefert den aktuell gespeicherten Luftdruck zurück
	 * 
	 * @return aktueller Luftdruck
	 */
	public double getLuftdruck() {
		return luftdruck;
	}
	
	/**
	 * führt einen Sonnentanz durch und erhöht dadurch grundsätzlich die Temperatur
	 */
	public void sonnentanz()
	{
		sonne++;
	}
	
	/**
	 * führt einen Regentanz durch und erhöht dadurch grundsätzlich die Feuchtigkeit
	 */
	public void regentanz()
	{
		regen++;
	}
}