
package Übungsstunde.uebungsveranstaltung12.wetter.wetterPropertyChange;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * enthält die Beschreibung einer Wettersituation aus Temperatur,
 * Luftfeuchtigkeit und Luftdruck
 */
public class Wetterdaten {
	/**
	 * Name der Temperaturveränderung
	 */
	public static final String TEMPERATUR = "Temperatur";
	/**
	 * Name der Luftdruckveränderung
	 */
	public static final String LUFTDRUCK = "Luftdruck";
	/**
	 * Name der Luftfeuchtigkeitsveränderung
	 */
	public static final String FEUCHTIGKEIT = "Feuchtigkeit";
	
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
	 * Beobachterverwaltung
	 */
	private PropertyChangeSupport prop = 
			new PropertyChangeSupport(this);
	
	/**
	* meldet b am Subjekt an
	* @param b neuer Beobachter
	*/
        public void anmelden(PropertyChangeListener b)
        {
            prop.addPropertyChangeListener(b);
        }
    /**
	* meldet b am Subjekt an
	* @param eigenschaft Name der Veränderung, über die der Beobachter informiert werden möchte
	* @param b neuer Beobachter
	*/
    public void anmelden(String eigenschaft, 
    							PropertyChangeListener b)
    {
        prop.addPropertyChangeListener(eigenschaft, b);
    }

	/**
	* meldet b am Subjekt wieder ab
	* @param b abzumeldender Beobachter
	*/
    public void abmelden(PropertyChangeListener b)
    {
    	prop.removePropertyChangeListener(b);
    }

	/**
	* Benachrichtigung an Beobachter senden
	* @param name Name des veränderten Wertes
	* @param alt bisheriger Wert
	* @param neu aktueller Wert
	*/
	protected void firePropertyChange(String name, Object alt, Object neu)
	{
		prop.firePropertyChange(name, alt, neu);
	}


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
		double alt = this.temperatur;
		this.temperatur = temp + sonne;
		firePropertyChange(TEMPERATUR, alt, this.temperatur);
		alt = this.feuchtigkeit;
		this.feuchtigkeit = feucht + regen;
		firePropertyChange(FEUCHTIGKEIT, alt, this.feuchtigkeit);
		alt = this.luftdruck;
		this.luftdruck = druck;
		firePropertyChange(LUFTDRUCK, alt, this.luftdruck);
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