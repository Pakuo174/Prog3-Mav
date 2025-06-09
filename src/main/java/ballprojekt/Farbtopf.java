package ballprojekt;

import javafx.scene.paint.Color;

/**
 * ein Farbtopf mit einer bestimmten Menge Farbe
 */
public class Farbtopf {
	private Color farbe;
	private int fuellstand = 1000;
	/**
	 * fuellstand
	 * @return fuellstand
	 */
	public int getFuellstand() {
		return fuellstand;
	}

	/**
	 * verringert den Füllstand um die angegebene Menge.
	 * Wenn nicht genug Farbe vorhanden ist, wartet der Thread, bis der Füllstand ausreicht.
	 * @param menge entnommene Menge
	 * @throws InterruptedException wenn der wartende Thread unterbrochen wird
	 */
	public synchronized void fuellstandVerringern(int menge) throws InterruptedException {
		if (menge <= 0) { // Eine Menge von 0 oder weniger sollte nicht verringert werden
			return;
		}

		// Warten, solange nicht genug Farbe im Topf ist
		// Die Schleife ist wichtig: Man könnte geweckt werden, aber immer noch nicht genug Farbe haben (Spurious Wakeup)
		while (menge > fuellstand) {
			// System.out.println("Farbtopf " + farbe + ": Nicht genug Farbe (" + fuellstand + "l), warte auf " + menge + "l.");
			try {
				wait(); // Thread gibt den Monitor frei und geht in den Wartezustand
			} catch (InterruptedException e) {
				// Wenn der Thread während des Wartens unterbrochen wird,
				// werfen wir die Exception weiter. Der Aufrufer (Ball) muss damit umgehen.
				throw e; // Wichtig: Exception weiterwerfen
			}
		}

		// Wenn die Schleife verlassen wird, ist genug Farbe vorhanden
		fuellstand -= menge;

	}


	/**
	 * erhöht den Füllstand um die angegebene Menge
	 * @param menge dazukommende Menge
	 */
	public synchronized void fuellstandErhoehen(int menge)
	{
		if(menge > 0)
			fuellstand += menge;
		notifyAll();
	}
	
	/**
	 * farbe
	 * @return farbe
	 */
	public Color getFarbe() {
		return farbe;
	}
	
	/**
	 * erzeugt einen Farbtopf mit 1000 l der angegebenen Farbe
	 * @param farbe Farbe im Topf
	 * @throws IllegalArgumentException wenn farbe null ist
	 */
	public Farbtopf(Color farbe) {
		if(farbe == null)
			throw new IllegalArgumentException();
		this.farbe = farbe;
	}
	
}
