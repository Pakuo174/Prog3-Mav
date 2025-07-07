package bankprojekt.verarbeitung;

import javafx.stage.Stage;
import javafx.application.Application; // Wichtig: importieren

import static javafx.application.Application.launch; // Das ist der statische Import für launch()

public class KontoStarter extends Application {

    @Override // Diese Methode ist nur nötig, wenn du Application erweiterst
    public void start(Stage primarystage){
        new KontoController(primarystage);
    }
    public static void main(String[] args) {
        launch(args); // launch() erfordert, dass die aufrufende Klasse Application erweitert
    }
}