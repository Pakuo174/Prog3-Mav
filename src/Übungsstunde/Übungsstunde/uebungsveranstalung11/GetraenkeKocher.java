package Übungsstunde.uebungsveranstalung11;

/**
 * startet einen Getränkeautomaten
 * @author Doro
 *
 */
public class GetraenkeKocher {
	/**
	 * startet den Getränkeautomaten
	 * @param args wird nicht benutzt
	 */
	public static void main(String[] args) {
		Automat gk = new LuxusAutomat();
		gk.getraenkKochen();
	}
}
