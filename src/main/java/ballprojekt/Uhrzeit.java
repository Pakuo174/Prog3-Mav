package ballprojekt;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Diese Klasse aktualisiert die angezeigte Uhrzeit in der Benutzeroberfläche.
 *
 * Die Uhrzeit wird beim Start gesetzt und anschließend alle 60 Sekunden aktualisiert.
 * Die Aktualisierung erfolgt in einem separaten Daemon-Thread.
 */
public class Uhrzeit implements Runnable {

    private  Text textfeld;
    private  Thread meinUhrThread;
    private volatile boolean weiterLaufen = true;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");


    /**
     * Konstruktor: Initialisiert das Uhrzeit-Objekt mit dem übergebenen Textfeld.
     * Der zugehörige Thread wird erstellt, aber noch nicht gestartet.
     *
     * @param textfeld Das Text-UI-Element, in dem die Uhrzeit angezeigt werden soll.
     */
    public Uhrzeit(Text textfeld) {

        this.textfeld = textfeld;
        meinUhrThread = new Thread(this);
        meinUhrThread.setDaemon(true); // beendet sich automatisch mit dem Programm
    }

    public void start() {
        meinUhrThread.start();
    }

    public void stop() {
        weiterLaufen = false;
        meinUhrThread.interrupt();
    }

    /**
     * run Methode für die Threads der Uhrzeit Objekte
     *
     * Sie aktualisiert die Uhrzeitanzeige sofort und dann alle 60 Sekunden,
     * solange das Steuerflag {@code weiterLaufen} aktiv ist.
     */
    @Override
    public void run() {
        while (weiterLaufen) {
            Platform.runLater(() -> {
                String aktuelleZeit = LocalTime.now().format(FORMATTER);
                textfeld.setText(aktuelleZeit);
            });

            try {
                Thread.sleep(60_000); // alle 60 Sekunden
            } catch (InterruptedException e) {
                break; // sauber beenden
            }
        }
    }
}
