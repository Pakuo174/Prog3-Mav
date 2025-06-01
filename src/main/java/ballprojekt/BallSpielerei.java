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
 */
public class BallSpielerei extends Application {
	private BallOberflaeche view;
	private Farbtopf[] farben = {new Farbtopf(Color.BLUE), new Farbtopf(Color.YELLOW), new Farbtopf(Color.RED)};
	private List<Ball> akiveBälle = new ArrayList<>();
	private List<Thread> ballThreads = new ArrayList<>();


	private Uhrzeit uhrzeitThread;


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

		Text uhrzeitText = view.getUhrzeitText();
		uhrzeitThread = new Uhrzeit(uhrzeitText);
		uhrzeitThread.start();
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
		Ball b = new Ball(view.getVerfuegbareBreite(), view.getVerfuegbareHoehe(), dx, dy, farben[farbe]);

		b.setAnzahlHuepfer(dauer); // new
		view.ballEintragen(b);
		// Neuer Thread wird für diesen Ball gestartet
		Thread t = new Thread(b);
		b.setThread(t);
		t.setDaemon(true); // optional, aber gut: beendet sich mit dem Programm
		t.start(); // run() im Thread des Ball wird aufgerufen

		akiveBälle.add(b);
		System.out.println("Startmethode fertig");
	}
	
	/**
	 * farben
	 * @return farben
	 */
	public Farbtopf[] getFarben() {
		return farben;
	}
	
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
