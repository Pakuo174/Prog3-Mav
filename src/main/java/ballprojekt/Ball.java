package ballprojekt;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * ein hüpfender Ball für eine JavaFx-Oberfläche
 *
 */
public class Ball extends Circle implements Runnable {
	private static final int RADIUS = 8;
	private static final int BENOETIGTE_MENGE = 2;
	private final double breite;
	private final double hoehe;
	private int dx = 2;
	private int dy = 2;
	private double x;
	private double y;
	private Farbtopf topf;

	private Thread meinThread; // Referenz auf eigenen Thread

	private int anzahlHuepfer; // Wird extern gesetzt, steuert die "Lebenszeit" des Balls

	/**
	 * erstellt einen Ball und lässt ihn loshüpfen.
	 * Der Ball erzeugt und startet seinen eigenen Thread.
	 * @param breite Breite des zur Verfügung stehenden Platzes
	 * @param hoehe Höhe des zur Verfügung stehenden Platzes
	 * @param dx Sprungweite des Balles in x-Richtung (zwischen 1 und 5)
	 * @param dy Sprungweite des Balles in y-Richtung (zwischen 1 und 5)
	 * @param topf Farbtopf, mit dessen Farbe dieser Ball gezeichnet wird
	 */
	public Ball(double breite, double hoehe, int dx, int dy, Farbtopf topf) {
		this.breite = breite;
		this.hoehe = hoehe;
		this.setFill(topf.getFarbe()); // HIER bekommt der Ball seine Farbe zugewiesen! --> Methode aus der Circle-Klasse
		this.setVisible(true);
		this.setRadius(RADIUS);
		x = RADIUS;
		y = RADIUS;
		zeichnen(true); // Ball initial grau zeichnen, bis der erste Hüpfer erfolgreich war
		this.topf = topf; // weist dem Ball seinen Farbtopf zu.
		this.dx = Math.max(Math.min(dx, 5), 1);
		this.dy = Math.max(Math.min(dy, 5), 1);

		// Ball erzeugt und startet seinen eigenen Thread
		meinThread = new Thread(this); // 'this' ist der Ball selbst, der Runnable implementiert
		meinThread.setDaemon(true);     // Optional, aber gut: beendet sich mit dem Programm
		meinThread.start();             // Startet den Thread sofort nach Erstellung des Balls
	}

	/**
	 * unterbricht den eigenen Thread des Balls.
	 * Dadurch wird die run()-Methode beendet und der Ball verschwindet.
	 */
	public void beenden() {
		if (meinThread != null) {
			meinThread.interrupt();
		}
	}

	/**
	 * setzt den Ball an die angegebene Position
	 * @param grau true, wenn der Ball in grau gezeichnet werden soll,
	 * false, wenn er in der richtigen Farbe gezeichnet werden soll
	 */
	private void zeichnen(boolean grau) {
		Platform.runLater(() -> {
			if (grau)
				this.setFill(Color.GREY);
			else
				this.setFill(topf.getFarbe());
			this.setLayoutX(x);
			this.setLayoutY(y);
		});
	}

	/**
	 * macht den Ball unsichtbar
	 */
	private void unsichtbarMachen() {
		Platform.runLater(() -> {
			this.setVisible(false);
		});
	}

	/**
	 * bewegt den Ball einen Schritt weiter (macht einen kleinen Sprung mit dx und dy).
	 * Versucht, Farbe aus dem Topf zu entnehmen. Wartet, wenn nicht genug Farbe da ist.
	 * @throws InterruptedException wenn der wartende Thread unterbrochen wird
	 */
	private void einHuepfer() throws InterruptedException {
		try {
			topf.fuellstandVerringern(BENOETIGTE_MENGE);
			// Wenn fuellstandVerringern erfolgreich war (oder gewartet hat), fährt der Ball fort
			x += dx;
			y += dy;
			if (x - RADIUS <= 0) {
				x = RADIUS;
				dx = -dx;
			}
			if (x + RADIUS >= breite) {
				x = breite - RADIUS;
				dx = -dx;
			}
			if (y - RADIUS <= 0) {
				y = RADIUS;
				dy = -dy;
			}
			if (y + RADIUS >= hoehe) {
				y = hoehe - RADIUS;
				dy = -dy;
			}
			zeichnen(false); // Ball in Farbe zeichnen, da Farbe entnommen wurde
		} catch (InterruptedException e) {
			// Der Ball-Thread wurde unterbrochen, während er auf Farbe gewartet hat (in fuellstandVerringern)
			zeichnen(true); // Optional: Ball grau zeichnen, wenn er gestoppt wird/wurde
			throw e; // InterruptedException weiterwerfen, damit run() sie abfangen kann
		}
	}

	// Die Methode huepfen() wird entfernt, da ihre Logik direkt in run() integriert wird.
	// public void huepfen (int anzahlHuepfer){
	//    for (int i = 1; i <= anzahlHuepfer; i++) {
	//         this.einHuepfer(); // Diese Zeile würde jetzt auch InterruptedException werfen
	//         try {
	//          Thread.sleep(5); // Pause nach jedem Hüpfer
	//       } catch (InterruptedException e) {
	//          Thread.currentThread().interrupt();
	//          break; // Schleife verlassen, wenn Thread unterbrochen wird
	//       }
	//    }
	// }

	public void setAnzahlHuepfer(int anzahlHuepfer) {
		this.anzahlHuepfer = anzahlHuepfer;
	}

	/**
	 * Die Hauptlogik des Ball-Threads.
	 * Der Ball hüpft die festgelegte Anzahl an Hüpfern oder bis er unterbrochen wird.
	 * Wenn der Ball unterbrochen wird (z.B. durch Farbmangel und Stoppen des Programms),
	 * verschwindet er am Ende.
	 */
	@Override
	public void run() {
		try {
			for (int i = 0; i < anzahlHuepfer; i++) {
				// Prüfen, ob der Thread unterbrochen wurde, bevor der nächste Hüpfer gemacht wird
				if (Thread.currentThread().isInterrupted()) {
					break; // Schleife verlassen, wenn unterbrochen
				}

				einHuepfer(); // Versucht einen Hüpfer zu machen und Farbe zu entnehmen. Kann InterruptedException werfen.

				// Wenn einHuepfer() erfolgreich war, mache eine kurze Pause.
				// Auch diese kann eine InterruptedException werfen.
				Thread.sleep(5);
			}
		} catch (InterruptedException e) {
			// Dieser Block fängt InterruptedException ab,
			// die entweder von einHuepfer() (beim Warten auf Farbe) oder von Thread.sleep() kommen.
			System.out.println("Ball-Thread wurde unterbrochen.");
			// Das Thread.currentThread().interrupt(); wird hier nicht benötigt,
			// da die Exception das Flag schon gesetzt hat und wir die Schleife sowieso verlassen.
		} finally {
			// Dieser Block wird immer ausgeführt, egal ob der Thread normal zu Ende läuft
			// oder durch eine Exception (InterruptedException) beendet wird.
			unsichtbarMachen(); // Ball verschwindet von der Oberfläche
		}
	}
}