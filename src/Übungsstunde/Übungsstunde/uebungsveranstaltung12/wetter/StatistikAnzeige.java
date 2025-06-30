package Übungsstunde.uebungsveranstaltung12.wetter;

/**
 * zeigt eine statistische Auswertung der bisherigen Wetterentwicklung an
 */
public class StatistikAnzeige implements Beobachter{
    private double maxTemp;
    private double minTemp;
    private double tempSum;
    private int anzMesswerte;
    
    /**
     * erstellt eine neue Statistik
     */
	 public StatistikAnzeige()
	 {
		 this.maxTemp = 0;
		 this.minTemp = 200;
		 this.tempSum = 0;
		 this.anzMesswerte = 0;
	 }

        /**
         * fügt die übergebenen Daten ur bisherigen Statistik hinzu und zeigt die 
         * aktualisierten statistische Auswertung an
         * @param wetter die aktuellen Wetterbedingungen, die zur Statistik hinzugefügt werden sollen
         */
	public void aktualisieren(Wetterdaten wetter)
	{
		tempSum += wetter.getTemperatur();
		anzMesswerte++;

		if (wetter.getTemperatur() > maxTemp) {
			maxTemp = wetter.getTemperatur();
		}

		if (wetter.getTemperatur() < minTemp) {
			minTemp = wetter.getTemperatur();
		}

		aufDenBildschirm();
	}

        /**
         * zeigt die statistische Auswertung auf dem Bildschirm an
         */
	public void aufDenBildschirm()
	{
		System.out.println("Mit/Max/Min Temperatur = " 
					+(tempSum / anzMesswerte)
		+ "/" + maxTemp + "/" + minTemp);
	}
};
