package Übungsstunde.uebungsveranstaltung12.wetter.wetterPropertyChange;

/**
 * stellt eine Wetterstation 
 * @author Doro
 */
public class Wetterstation {
	
	/**
	 * simuliert den Empfang von Messdaten aus einer Wetterstation mit einer Anzeige
	 * der empfangenen Daten
	 * @param args wird nicht verwendet
	 */
	public static void main(String[] args) {
			Wetterdaten wetterDaten = new Wetterdaten();

		AktuelleBedingungenAnzeige aktuelleAnzeige
				= new AktuelleBedingungenAnzeige();
			VorhersageAnzeige vorhersage
				= new VorhersageAnzeige();
			wetterDaten.anmelden(aktuelleAnzeige);
			wetterDaten.anmelden(Wetterdaten.LUFTDRUCK, vorhersage);

			// Das Messgerät liefert neue Wetterdaten:
			wetterDaten.setMesswerte(30, 65, 1013);
			wetterDaten.setMesswerte(30, 70, 1020);
			wetterDaten.setMesswerte(28, 90, 980);	
		}



}
