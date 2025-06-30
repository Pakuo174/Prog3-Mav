package Übungsstunde.uebungsveranstalung11;

/**
 * stellt einen einfachen Getränkeautomaten dar
 */
public class HtwAutomat extends Automat{
	@Override
	public AutomatenGetraenk erzeugen(int auswahl) {
		AutomatenGetraenk get = null;
		
		switch (auswahl)
		{
		case 1 -> get = new Kaffee();			
		case 2 -> get = new Tee();
		case 3 -> get = new Kakao();
		}
		return get;
	}

}
