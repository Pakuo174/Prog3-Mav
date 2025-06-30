package Übungsstunde.uebungsveranstalung11;

public class Kakao extends AutomatenGetraenk {

	@Override
	protected void aufgiessen() {
		System.out.println("Pulver reinrühren");
	}

	@Override
	public int getTemperatur() {
		return 7;
	}

}
