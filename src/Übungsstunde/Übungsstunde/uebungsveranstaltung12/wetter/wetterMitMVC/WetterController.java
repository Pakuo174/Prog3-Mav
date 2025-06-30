package Übungsstunde.uebungsveranstaltung12.wetter.wetterMitMVC;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Ein Controller für eine Wetteroberfläche
 * @author Doro
 */
public class WetterController implements ActionListener{
	private Wetterdaten model;
	private Wetteroberflaeche view;
	
	/**
	 * erstellt den Controller für die beiden angegebenen Wetterdaten und bringt die zugehörige 
	 * Wetteroberflaeche auf den Bildschirm
	 * @param model die Wetterdaten
	 */
	public WetterController(Wetterdaten model)
	{
		this.model = model;
		view = new Wetteroberflaeche(this);
		this.model.anmelden(view);
	}
	
	/**
	 * reagiert auf den Sonnentanz-Button
	 */
	public void sonne()
	{
		model.sonnentanz();
		view.aktionSetzen(true);
	}

	/**
	 * regiert auf den Regentanz-Button
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		model.regentanz();	
		view.aktionSetzen(false);
	}
}
