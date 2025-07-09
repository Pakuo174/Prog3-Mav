package bankprojekt.verarbeitung;

import bankprojekt.geld.Waehrung;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.time.LocalDate;

// Imports für Alert-Boxen sind immer noch nicht nötig

public class KontoController {

    private Girokonto model;
    private KontoOberflaeche view;

    public KontoController(Stage primaryStage){
        // 1. Model erzeugen
        Kunde inhaber = new Kunde("Maria", "Musterfrau", "Musterweg 1, 12345 Musterstadt", LocalDate.of(2000, 7, 12));
        this.model = new Girokonto(inhaber, 1234L, new Geldbetrag(1000.0, Waehrung.EUR));


        // 2. View erzeugen
        this.view = new KontoOberflaeche();

        // 3. Bindungen einrichten
        // a. Kontonummer anzeigen
        this.view.nummer.setText(String.valueOf(this.model.getKontonummer()));

        // b. Aktuellen Kontostand anzeigen und formatieren
        this.view.stand.textProperty().bind(
                Bindings.createStringBinding(() -> {
                    Geldbetrag currentBetrag = this.model.kontostandProperty().get();
                    return String.format("%,.2f %s", currentBetrag.getBetrag(), currentBetrag.getWaehrung().toString());
                }, this.model.kontostandProperty())
        );


        // Listener für imPlus/imMinus Property, um Kontostand-Farbe zu ändern
        this.model.imPlusProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                this.view.stand.setFill(Color.GREEN);
            } else {
                this.view.stand.setFill(Color.RED);
            }
        });
        // Initialen Zustand der Kontostandsfarbe setzen
        if (this.model.imPlusProperty().get()) {
            this.view.stand.setFill(Color.GREEN);
        } else {
            this.view.stand.setFill(Color.RED);
        }

        /**
         * _________________________________ Andere Bindungen _________________________________
         */


        // Bidirektionales Binding für den 'gesperrt'-Status (hier bleibt es)
        this.view.gesperrt.selectedProperty().bindBidirectional(this.model.gesperrtProperty());

        // Unidirektionale Bindung für die ANZEIGE der aktuellen Adresse
        this.view.aktuelleAdresseAnzeige.textProperty().bind(this.model.getInhaber().adresseProperty());

        // Initialisiere das Eingabefeld mit der aktuellen Adresse
        this.view.adresseEingabe.setText(this.model.getInhaber().getAdresse());


        // 4. Ereignisbehandlung für Buttons
        this.view.einzahlen.setOnAction(actionEvent -> handleEinzahlen());
        this.view.abheben.setOnAction(actionEvent -> handleAbheben());

        // Ereignisbehandlung für den Adresse-Speichern-Button
        this.view.adresseSpeichern.setOnAction(actionEvent -> handleAdresseSpeichern());


        // 5. Szene und Stage einrichten und anzeigen
        Scene scene = new Scene(view,400,500); // Fensterhöhe eventuell anpassen
        primaryStage.setScene(scene);
        primaryStage.setTitle("Kontoübersicht");
        primaryStage.show();
    }


    /**
     * Behandelt den Klick auf den 'Einzahlen'-Button.
     */
    private void handleEinzahlen(){
        try {
            double betragValue = Double.parseDouble(view.betrag.getText());
            if (betragValue <= 0) {
                view.meldung.setText("Fehler: Der einzuzahlende Betrag muss positiv sein.");
                view.meldung.setFill(Color.RED);
                return;
            }
            Waehrung selectedWaehrung = view.waehrung.getSelectionModel().getSelectedItem();

            if (selectedWaehrung == null) {
                view.meldung.setText("Fehler: Bitte wählen Sie eine Währung aus.");
                view.meldung.setFill(Color.RED);
                return;
            }
            Geldbetrag einzuzahlenderBetrag = new Geldbetrag(betragValue, selectedWaehrung);

            model.einzahlen(einzuzahlenderBetrag);
            view.meldung.setText("Einzahlung erfolgreich: " + einzuzahlenderBetrag.toString());
            view.meldung.setFill(Color.GREEN);
            view.betrag.clear();
        } catch (NumberFormatException e) {
            view.meldung.setText("Fehler: Ungültiger Betrag. Bitte geben Sie eine Zahl ein.");
            view.meldung.setFill(Color.RED);
        } catch (IllegalArgumentException e) {
            view.meldung.setText("Fehler bei Einzahlung: " + e.getMessage());
            view.meldung.setFill(Color.RED);
        }
    }

    /**
     * Behandelt den Klick auf den 'Abheben'-Button.
     */
    private void handleAbheben(){
        try {
            double betragValue = Double.parseDouble(view.betrag.getText());
            if (betragValue <= 0) {
                view.meldung.setText("Fehler: Der abzuhebende Betrag muss positiv sein.");
                view.meldung.setFill(Color.RED);
                return;
            }
            Waehrung selectedWaehrung = view.waehrung.getSelectionModel().getSelectedItem();

            if (selectedWaehrung == null) {
                view.meldung.setText("Fehler: Bitte wählen Sie eine Währung aus.");
                view.meldung.setFill(Color.RED);
                return;
            }
            Geldbetrag abzuhebenderBetrag = new Geldbetrag(betragValue, selectedWaehrung);

            if (model.abheben(abzuhebenderBetrag)) {
                view.meldung.setText("Abhebung erfolgreich: " + abzuhebenderBetrag.toString());
                view.meldung.setFill(Color.GREEN);
                view.betrag.clear();
            } else {
                view.meldung.setText("Abhebung nicht möglich: Kontostand unzureichend oder Dispo überschritten.");
                view.meldung.setFill(Color.ORANGE);
            }
        } catch (NumberFormatException e) {
            view.meldung.setText("Fehler: Ungültiger Betrag. Bitte geben Sie eine Zahl ein.");
            view.meldung.setFill(Color.RED);
        } catch (GesperrtException e) {
            view.meldung.setText("Fehler: Konto ist gesperrt! " + e.getMessage());
            view.meldung.setFill(Color.RED);
        } catch (IllegalArgumentException e) {
            view.meldung.setText("Fehler bei Abhebung: " + e.getMessage());
            view.meldung.setFill(Color.RED);
        }
    }

    /**
     * NEU: Behandelt den Klick auf den 'Adresse speichern'-Button.
     * Aktualisiert die Adresse im Model.
     */
    private void handleAdresseSpeichern() {
        String neueAdresse = view.adresseEingabe.getText();
        if (neueAdresse == null || neueAdresse.trim().isEmpty()) {
            view.meldung.setText("Fehler: Adresse darf nicht leer sein.");
            view.meldung.setFill(Color.RED);
            return;
        }

        try {
            // Adresse im Model aktualisieren
            this.model.getInhaber().setAdresse(neueAdresse);
            view.meldung.setText("Adresse erfolgreich geändert.");
            view.meldung.setFill(Color.GREEN);
        } catch (Exception e) {
            view.meldung.setText("Fehler beim Speichern der Adresse: " + e.getMessage());
            view.meldung.setFill(Color.RED);
        }
    }
}