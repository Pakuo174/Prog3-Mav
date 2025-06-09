package ballprojekt;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Startet ein kleines Ballspiel als Übung für Threads
 *
 *
 * Jedes Mal, wenn du auf den "Ball starten"-Knopf klickst, wird ein neues Ball-Objekt erstellt.
 * Im Konstruktor jedes einzelnen Ball-Objekts wird dann sein eigener, separater Thread erzeugt und sofort gestartet.
 * Dieser Thread führt die run()-Methode der jeweiligen Ball-Instanz aus, wodurch dieser Ball anfängt zu hüpfen.
 */
public class BallSpielerei extends Application {
	private BallOberflaeche view;
	private Farbtopf[] farben = {new Farbtopf(Color.BLUE), new Farbtopf(Color.YELLOW), new Farbtopf(Color.RED)};
	private List<Ball> akiveBälle = new ArrayList<>();



	/**
	 * erzeugt die Umgebung für die BallSpielerei
	 * @param primaryStage das Hauptfenster der Anwendung
	 * @throws Exception falls beim Starten der Anwendung ein Fehler auftritt
	 *
	 * Uhrzeit wird gelichzeitig beim erstellen der Oberfläche erstellt
	 * UhrzeitThread wird erstellt und gestartet
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Hüpfende Bälle");
		view = new BallOberflaeche(this);
		Scene scene = new Scene(view, 500, 400, false);		
	    primaryStage.setScene(scene);
	    primaryStage.setOnCloseRequest(e -> {alleBeenden();});
	    primaryStage.show();

	}
	
	/**
	 * erzeugt einen neuen Ball und macht ihn in der Oberfläche sichtbar
	 */
	public void neuerBall()
	{
		Random r = new Random();
		int dauer = r.nextInt(500) + 1000; // Zufallszahl zwischen 3000 und 3500
		int farbe = r.nextInt(3);
		int dx = r.nextInt(5) + 1;			// Zufallszahl zwischen 1 bis 5
		int dy = r.nextInt(5) + 1;
		// Hier wird der Ball-Konstruktor aufgerufen!
		Ball b = new Ball(view.getVerfuegbareBreite(), view.getVerfuegbareHoehe(), dx, dy, farben[farbe]);

		b.setAnzahlHuepfer(dauer); // new
		view.ballEintragen(b);
		// Neuer Thread wird für diesen Ball gestartet
		akiveBälle.add(b);
	}
	
	/**
	 * farben
	 * @return farben
	 */
	public Farbtopf[] getFarben() {
		return farben;
	}


	/**
	 * symbolisiert durch den jeweiligen Button
	 * ruft der Klasse Farbtopf die Methode fuellstandErhoehen
	 * @param topf Diese topf-Referenz ist der spezifische Farbtopf (Blau, Gelb oder Rot), dessen Button du gerade gedrückt hast.
	 */
	public void auffuellen(Farbtopf topf)
	{
		Random r = new Random();
		int menge = r.nextInt(5000) + 1000; 
		topf.fuellstandErhoehen(menge);
	}

	/**
	 * beendet das Hüpfen aller Bälle
	 * ruft Methode beenden von Ball Klasse auf
	 */
	public void alleBeenden() {
		for (Ball b : akiveBälle){
			b.beenden();
		}
	}


}
