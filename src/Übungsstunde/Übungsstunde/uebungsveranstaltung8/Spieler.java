package Übungsstunde.uebungsveranstaltung8;

import java.util.Random;

/**
 * ein Mitspieler
 * @author Doro
 *
 */
public class Spieler implements Runnable {

	private Auswahl wahl;
	private boolean darfStarten = false;

	/**
	 * erstellt einen Spieler
	 */
	public Spieler () {

	}

	public synchronized void setDarfStarten(boolean darfStarten){
		this.darfStarten = darfStarten;
		notify();
	}

	public Auswahl getWahl(){
		return wahl;
	}
	
	/**
	 * trifft eine Zufallsauswahl
	 * @return liefert das vom Spieler gewählt Symbol 
	 * (Schere, Stein oder Papier) zurück
	 */
	private Auswahl waehlen()
	{
		Random r = new Random();
		return Auswahl.values()[r.nextInt(5)];
	}


	@Override
	public void run() {
		wahl = waehlen();
		synchronized (this) {
			while (!darfStarten) {
				try {
					wait();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}
		System.out.println("Spieler wählt:" +wahl);
	}
}