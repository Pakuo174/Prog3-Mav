package bankprojekt.verarbeitung;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * Kunde einer Bank
 * @author Dorothea Hubrich
 * @author Daniel Kujawa
 */
public class Kunde implements Comparable<Kunde>, Serializable {

	/**
	 * Ein Musterkunde
	 */
	public static final Kunde MUSTERMANN = 
			new Kunde("Max", "Mustermann", "zuhause", LocalDate.now());
	
	/**
	 * englische oder deutsche Anrede, je nach den Systemeinstellungen
	 */
	private static final String ANREDE;

	/**
	 * liefert die systemspezifische Anrede
	 * @return systemspezifische Anrede
	 */
	public static String getAnrede() {
		return ANREDE;
	}

	/**
	 * der Vorname
	 */
	private String vorname;
	/**
	 * Der Nachname
	 */
	private String nachname;
	/**
	 * Die Adresse
	 */
	//private String adresse;
	/**
	 * Geburtstag
	 */
	private LocalDate geburtstag;


	//___________________________________________ JAVA FX _______________________________________________ ↓

	private StringProperty adresse;

	/**
	 * Die Adresse des Kunden.
	 * Diese Property ermöglicht das Binden und Überwachen von Änderungen der Kundenadresse
	 * @return Die StringProperty, die die Adresse des Kunden repräsentiert.
	 */
	public StringProperty adresseProperty(){
		return this.adresse;
	}

	/**
	 * Adresse des Kunden
	 * @return Adresse des Kunden
	 */
	public String getAdresse() {
		return adresse.get();
	}

	/**
	 * setzt die Adresse auf den angegebenen Wert
	 * @param adresse neue Adresse
	 * @throws IllegalArgumentException wenn adresse null ist
	 */
	public void setAdresse(String adresse) {
		if(adresse == null)
			throw new IllegalArgumentException("Adresse darf nicht null sein");
		this.adresse.set(adresse);
	}

	//___________________________________________ JAVA FX _______________________________________________ ↑

	/**
	 * erzeugt den Standardkunden Max Mustermann
	 */
	public Kunde() {
		this("Max", "Mustermann", "Adresse", LocalDate.now());
	}

	/**
	 * Erzeugt einen Kunden mit den übergebenen Werten
	 * 
	 * @param vorname Vorname
	 * @param nachname Nachname
	 * @param adresse Adresse
	 * @param gebdat Geburtstag
	 * @throws IllegalArgumentException wenn einer der Parameter null ist
	 */
	public Kunde(String vorname, String nachname, String adresse, LocalDate gebdat) {
		if(vorname == null || nachname == null || adresse == null || gebdat == null)
			throw new IllegalArgumentException("null als Parameter nich erlaubt");
		this.vorname = vorname;
		this.nachname = nachname;
		this.adresse = new SimpleStringProperty(adresse);
		this.geburtstag = gebdat;
		
		/*Runtime umgebung = Runtime.getRuntime();
		Runnable r = new Zerstoerer();
		Thread t = new Thread(r);
		umgebung.addShutdownHook(t);
		*/
	}
	
	/**
	 * Klasse für Aufräumarbeiten
	 * @author Doro
	 *
	 */
	private class Zerstoerer implements Runnable
	{
		@Override
		public void run() {
			System.out.println("Kunde " + Kunde.this.getName() + " zerstört");
		}
	}



	/**
	 * Erzeugt einen Kunden mit den übergebenen Werten
	 * 
	 * @param vorname Vorname
	 * @param nachname Nachname
	 * @param adresseAlsString Adresse
	 * @param gebdat Geburtstag im Format tt.mm.yy
	 * @throws DateTimeParseException wenn das Format des übergebenen Datums nicht korrekt ist
	 * @throws IllegalArgumentException wenn einer der Parameter null ist
	 */
	public Kunde(String vorname, String nachname, String adresseAlsString, String gebdat)  { // Parameter ist String, nicht StringProperty
		this(vorname,
				nachname,
				adresseAlsString, // Hier den String übergeben
				LocalDate.parse(gebdat,DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
	}

	/**
	 * gibt alle Daten des Kunden aus
	 */
	@Override
	public String toString() {
		String ausgabe;
		DateTimeFormatter df = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
		ausgabe = this.vorname + " " + this.nachname + System.getProperty("line.separator");
		ausgabe += this.getAdresse() + System.getProperty("line.separator");
		ausgabe += df.format(this.geburtstag) + System.getProperty("line.separator");
		return ausgabe;
	}



	/**
	 * vollständiger Name des Kunden in der Form "Nachname, Vorname"
	 * 
	 * @return vollständiger Name des Kunden
	 */
	public String getName() {
		return this.nachname + ", " + this.vorname;
	}


	/**
	 * Nachname des Kunden
	 * @return Nachname des Kunden
	 */
	public String getNachname() {
		return nachname;
	}

	/**
	 * setzt den Nachnamen auf den angegebenen Wert
	 * @param nachname neuer Nachname
	 * @throws IllegalArgumentException wenn nachname null ist
	 */
	public void setNachname(String nachname) {
		if(nachname == null)
			throw new IllegalArgumentException("Nachname darf nicht null sein");
		this.nachname = nachname;
	}

	/**
	 * Vorname des Kunden
	 * @return Vorname des Kunden
	 */
	public String getVorname() {
		return vorname;
	}

	/**
	 * setzt den Vornamen auf den angegebenen Wert
	 * @param vorname neuer Vorname
	 * @throws IllegalArgumentException wenn vorname null ist
	 */
	public void setVorname(String vorname) {
		if(vorname == null)
			throw new IllegalArgumentException("Vorname darf nicht null sein");
		this.vorname = vorname;
	}

	/**
	 * Geburtstag des Kunden
	 * @return Geburtstag des Kunden
	 */
	public LocalDate getGeburtstag() {
		return geburtstag;
	}

	@Override
	public int compareTo(Kunde arg0) {
		return this.getName().compareTo(arg0.getName());
	}

	static
	{
		if(Locale.getDefault().getCountry().equals("DE"))
			ANREDE = "Hallo Benutzer!";
		else
			ANREDE = "Dear Customer!";
	}
}