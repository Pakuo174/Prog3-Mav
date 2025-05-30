package ballprojekt;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class TestApp extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        stage.setScene(new Scene(new Label("Hallo JavaFX!"), 300, 200));
        stage.show();
        System.out.println("Stage shown");
    }
}
