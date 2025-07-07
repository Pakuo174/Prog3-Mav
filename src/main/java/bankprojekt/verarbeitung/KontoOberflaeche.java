package bankprojekt.verarbeitung;

import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import bankprojekt.geld.Waehrung;

/**
 * Eine Oberfläche für ein einzelnes Konto. Man kann einzahlen
 * und abheben und sperren und die Adresse des Kontoinhabers
 * ändern
 * @author Doro
 *
 */
public class KontoOberflaeche extends BorderPane {
	private Text ueberschrift;
	private GridPane anzeige;
	private Text txtNummer;
	/**
	 * Anzeige der Kontonummer
	 */
	public Text nummer;
	private Text txtStand;
	/**
	 * Anzeige des Kontostandes
	 */
	public Text stand;
	private Text txtGesperrt;
	/**
	 * Anzeige und Änderung des Gesperrt-Zustandes
	 */
	public CheckBox gesperrt;

	// NEU: Textfeld für die Anzeige der AKTUELLEN Adresse
	private Text txtAktuelleAdresse;
	/**
	 * Anzeige der aktuellen Adresse des Kontoinhabers
	 */
	public Text aktuelleAdresseAnzeige; // Zum Anzeigen der Adresse

	private Text txtAdresseAendern; // Neues Label für das Eingabefeld
	/**
	 * Textfeld für die Eingabe und Änderung der Adresse des Kontoinhabers
	 */
	public TextArea adresseEingabe; // Zum Ändern/Eingeben der Adresse (umbenannt)

	/**
	 * Button zum Bestätigen der Adressänderung
	 */
	public Button adresseSpeichern; // Neuer Button

	/**
	 * Anzeige von Meldungen über Kontoaktionen
	 */
	public Text meldung;
	private HBox aktionen;
	/**
	 * Auswahl des Betrags für eine Kontoaktion
	 */
	public TextField betrag;
	/**
	 * Auswahl für die Währung des Betrages für eine Kontoaktion
	 */
	public ChoiceBox<Waehrung> waehrung;
	/**
	 * löst eine Einzahlung aus
	 */
	public Button einzahlen;
	/**
	 * löst eine Abhebung aus
	 */
	public Button abheben;

	/**
	 * erstellt die Oberfläche
	 */
	public KontoOberflaeche()
	{
		ueberschrift = new Text("Ein Konto verändern");
		ueberschrift.setFont(new Font("Sans Serif", 25));
		BorderPane.setAlignment(ueberschrift, Pos.CENTER);
		this.setTop(ueberschrift);

		anzeige = new GridPane();
		anzeige.setPadding(new Insets(20));
		anzeige.setVgap(10);
		anzeige.setAlignment(Pos.CENTER);

		txtNummer = new Text("Kontonummer:");
		txtNummer.setFont(new Font("Sans Serif", 15));
		anzeige.add(txtNummer, 0, 0);
		nummer = new Text();
		nummer.setFont(new Font("Sans Serif", 15));
		GridPane.setHalignment(nummer, HPos.RIGHT);
		anzeige.add(nummer, 1, 0);

		txtStand = new Text("Kontostand:");
		txtStand.setFont(new Font("Sans Serif", 15));
		anzeige.add(txtStand, 0, 1);
		stand = new Text();
		stand.setFont(new Font("Sans Serif", 15));
		GridPane.setHalignment(stand, HPos.RIGHT);
		anzeige.add(stand, 1, 1);

		txtGesperrt = new Text("Gesperrt: ");
		txtGesperrt.setFont(new Font("Sans Serif", 15));
		anzeige.add(txtGesperrt, 0, 2);
		gesperrt = new CheckBox();
		GridPane.setHalignment(gesperrt, HPos.RIGHT);
		anzeige.add(gesperrt, 1, 2);

		// NEU: Anzeige der aktuellen Adresse
		txtAktuelleAdresse = new Text("Aktuelle Adresse:");
		txtAktuelleAdresse.setFont(new Font("Sans Serif", 15));
		anzeige.add(txtAktuelleAdresse, 0, 3);
		aktuelleAdresseAnzeige = new Text(); // Dies ist das Anzeigefeld
		aktuelleAdresseAnzeige.setFont(new Font("Sans Serif", 15));
		GridPane.setHalignment(aktuelleAdresseAnzeige, HPos.RIGHT);
		anzeige.add(aktuelleAdresseAnzeige, 1, 3);

		// NEU: Eingabefeld für Adressänderung
		txtAdresseAendern = new Text("Adresse ändern:");
		txtAdresseAendern.setFont(new Font("Sans Serif", 15));
		anzeige.add(txtAdresseAendern, 0, 4); // Neue Zeile für Eingabefeld
		adresseEingabe = new TextArea(); // Umbenannt von 'adresse' zu 'adresseEingabe'
		adresseEingabe.setPrefColumnCount(25);
		adresseEingabe.setPrefRowCount(2);
		adresseEingabe.setPromptText("Neue Adresse hier eingeben..."); // Hilfetext
		GridPane.setHalignment(adresseEingabe, HPos.RIGHT);
		anzeige.add(adresseEingabe, 1, 4);

		// NEU: Button zum Speichern der Adresse
		adresseSpeichern = new Button("Adresse speichern");
		// Wir fügen den Button in einer neuen Zeile oder neben dem Textfeld ein
		// Für den Anfang: eine neue Zeile unter dem Textfeld
		GridPane.setHalignment(adresseSpeichern, HPos.RIGHT);
		anzeige.add(adresseSpeichern, 1, 5); // Eine Zeile nach der Adresseingabe

		meldung = new Text("Willkommen lieber Benutzer");
		meldung.setFont(new Font("Sans Serif", 15));
		meldung.setFill(Color.BLACK);
		// Die Zeile für Meldungen muss angepasst werden, da wir neue Zeilen hinzugefügt haben
		anzeige.add(meldung,  0, 6, 2, 1); // Passt die Zeile an (war 4, jetzt 6)

		this.setCenter(anzeige);

		aktionen = new HBox();
		aktionen.setSpacing(10);
		aktionen.setAlignment(Pos.CENTER);
		aktionen.setPadding(new Insets(10));

		betrag = new TextField("100.00");
		betrag.setPromptText("Betrag");
		waehrung = new ChoiceBox<>();
		waehrung.setItems(FXCollections.observableArrayList(Waehrung.values()));
		waehrung.getSelectionModel().selectFirst();
		aktionen.getChildren().add(betrag);
		aktionen.getChildren().add(waehrung);
		einzahlen = new Button("Einzahlen");
		aktionen.getChildren().add(einzahlen);
		abheben = new Button("Abheben");
		aktionen.getChildren().add(abheben);

		this.setBottom(aktionen);
	}
}