package Übungsstunde.uebungsveranstaltung12.wetter.wetterMitMVC;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * stellt eine Wetterstation 
 * @author Doro
 *
 */
public class Wetterstation {
	
	/**
	 * Ändert das übergebene Wetterdaten-Objekt alle zwei Sekunden zufällig
	 * @param w das zu verändernde Wetter-Objekt
	 */
	public static void wetterAendern(Wetterdaten w)
	{
		Random r = new Random();
		w.setMesswerte(r.nextInt(35), r.nextInt(100), (Math.random()*145 + 925));
	}
	
	/**
	 * simuliert den Empfang von Messdaten aus einer Wetterstation mit verschiedenen Anzeigen
	 * der empfangenen Daten
	 * @param args wird nicht verwendet
	 */
	public static void main(String[] args) {
			Wetterdaten wetterDaten = new Wetterdaten();
			AktuelleBedingungenAnzeige aktuelleAnzeige
							=new AktuelleBedingungenAnzeige();
			wetterDaten.anmelden(aktuelleAnzeige);
			
			WetterController wc = 
						new WetterController(wetterDaten);
			
			// Das Messgerät liefert neue Wetterdaten:
			wetterDaten.setMesswerte(30, 65, 1013);
			wetterDaten.setMesswerte(30, 70, 1020);
			wetterDaten.setMesswerte(28, 90, 980);
			
			Executors.newScheduledThreadPool(1)
			.scheduleAtFixedRate(()-> wetterAendern(wetterDaten),
								0, 2, TimeUnit.SECONDS);
			

		}



}
