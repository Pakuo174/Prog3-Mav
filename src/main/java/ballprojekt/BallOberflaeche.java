package ballprojekt;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.application.Platform;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Eine Oberfläche, in der viele bunte Bälle hüpfen
 *
 */
public class BallOberflaeche extends VBox {
	private Text uhrzeitTextfeld;
	private Pane spielflaeche;

	// Attribute für die Uhrzeit
	private Thread uhrzeitUpdateThread;
	private volatile boolean weiterLaufen = true;
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
	/**
	 * erstellt die Oberfläche
	 * @param controller Objekt, in dem die Ereignisse verarbeitet werden
	 */
	public BallOberflaeche(BallSpielerei controller)
	{
		this.setSpacing(10);
		HBox oben = new HBox(5);
		oben.setAlignment(Pos.CENTER);
		Label beschriftung = new Label("Uhrzeit: ");
		uhrzeitTextfeld = new Text("00:00:00");
		oben.getChildren().add(beschriftung);
		oben.getChildren().add(uhrzeitTextfeld);
		this.getChildren().add(oben);
		
		spielflaeche = new Pane();
		spielflaeche.setPrefHeight(300);
		spielflaeche.setPrefWidth(500);
		spielflaeche.setBackground(new Background(new BackgroundFill(Color.ANTIQUEWHITE, 
				CornerRadii.EMPTY, Insets.EMPTY)));
		this.getChildren().add(spielflaeche);
		
		HBox unten = new HBox(5);
		unten.setAlignment(Pos.CENTER);
		Button starten = new Button("Ball starten");
		starten.setOnAction(e -> controller.neuerBall());
		unten.getChildren().add(starten);
		Farbtopf[] farben = controller.getFarben();
		Button[] farbbuttons = new Button[farben.length];
		for(int i = 0; i < farbbuttons.length; i++)
		{
			Farbtopf topf = farben[i];
			farbbuttons[i] = new Button(topf.getFarbe().toString());
			farbbuttons[i].setTextFill(topf.getFarbe());
			farbbuttons[i].setOnAction(e -> controller.auffuellen(topf));
			unten.getChildren().add(farbbuttons[i]);
		}
		Button leeren = new Button("alle entfernen");
		leeren.setOnAction(e -> controller.alleBeenden());
		unten.getChildren().add(leeren);
		this.getChildren().add(unten);

		startUhrzeitAnzeige();
	}
	// mit Lambda den Uhrzeit Thread staren
	/*private void startUhrzeitAnzeige() {
		// Hier wird der Thread erzeugt und seine run-Methode als Lambda definiert
		uhrzeitUpdateThread = new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				String aktuelleZeit = LocalTime.now().format(FORMATTER); // Uhrzeit direkt hier abrufen
				Platform.runLater(() -> {
					uhrzeitTextfeld.setText(aktuelleZeit);
				});
				try {
					Thread.sleep(1000); // Jede Sekunde aktualisieren
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}
			System.out.println("Uhrzeit-Thread beendet.");
		});
		uhrzeitUpdateThread.setDaemon(true);
		uhrzeitUpdateThread.start(); // Hier wird der Thread gestartet
	}
	*/
	// ohne Lambda den UhrzeitThread starten
	private void startUhrzeitAnzeige()
	{
		uhrzeitUpdateThread = new Thread(new Runnable() { // Anonyme innere Klasse wird erzeugt
			@Override
			public void run() {
				while (!Thread.currentThread().isInterrupted()){
					String aktuelleZeit = LocalTime.now().format(FORMATTER);
					Platform.runLater(() -> uhrzeitTextfeld.setText(aktuelleZeit));

                try {
				Thread.sleep(60_000);
			} catch (InterruptedException e) {
                Thread.currentThread().interrupt();
				break;
            }
            }
				System.out.println("Uhrzeit-Thread beendet"); // Ausgabe nachdem die while Schleife nicht mehr läuft
		}
	});
		uhrzeitUpdateThread.setDaemon(true);
		uhrzeitUpdateThread.start();
	}


	/**
	 * Breite der Hüpffläche
	 * @return Breite der Hüpffläche
	 */
	public double getVerfuegbareBreite() {
		return spielflaeche.getWidth();
	}

	/**
	 * Höhe der Hüpffläche
	 * @return Höhe der Hüpffläche
	 */

	public double getVerfuegbareHoehe()
	{
		return spielflaeche.getHeight();
	}
	
	/**
	 * fügt einn Ball in die Hüpffläche ein
	 * @param ball einzufügender Ball
	 */
	public void ballEintragen(Ball ball) {
		spielflaeche.getChildren().add(ball);
	}
	public Text getUhrzeitText() {
		return uhrzeitTextfeld;
	}

}
