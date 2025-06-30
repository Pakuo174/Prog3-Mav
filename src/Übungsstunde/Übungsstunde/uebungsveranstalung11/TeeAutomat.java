package Übungsstunde.uebungsveranstalung11;

/**
* ein Getränkeautomat, der ausschließlich Tee herstellt
*/
public class TeeAutomat extends Automat {

	@Override
	public AutomatenGetraenk erzeugen(int auswahl) {
		return new Tee();
	}

	
	public String textErzeugen() {
		return "Drücken Sie irgendwas, gibt immer Tee...";
	}

}
