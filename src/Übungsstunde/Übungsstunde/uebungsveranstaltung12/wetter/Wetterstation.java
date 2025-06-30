package Übungsstunde.uebungsveranstaltung12.wetter;

import java.util.Random;

/**
 * stellt eine Wetterstation 
 * @author Doro
 *
 */
public class Wetterstation {
	
	/**
	 * simuliert den Empfang von Messdaten aus einer Wetterstation mit verschiedenen Anzeigen
	 * der empfangenen Daten
	 * @param args wird nicht verwendet
	 */
	public static void main(String[] args) {
			Wetterdaten wetterDaten = new Wetterdaten();

			Beobachter aktuelleAnzeige
				= new AktuelleBedingungenAnzeige();
			Beobachter statistikAnzeige
				= new VorhersageAnzeige();
			
			wetterDaten.anmelden(statistikAnzeige);
			wetterDaten.anmelden(aktuelleAnzeige);
			wetterDaten.anmelden(new StatistikAnzeige());
			wetterDaten.anmelden(
					wetter -> System.out.println("Anderes Wetter halt"));

			// Das Messgerät liefert neue Wetterdaten:
			wetterDaten.setMesswerte(30, 65, 1013);
			wetterDaten.setMesswerte(30, 70, 1020);
			wetterDaten.abmelden(statistikAnzeige);
			wetterDaten.setMesswerte(28, 90, 980);	

		}



}
