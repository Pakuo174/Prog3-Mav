package bankprojekt.verarbeitung;

import bankprojekt.geld.Waehrung;

/**
 * Ein Geldbetrag mit Währung
 */
public class Geldbetrag implements Comparable<Geldbetrag>{
	/**
	 * Betrag in der in waehrung angegebenen Währung
	 */
	private double betrag;
	/**
	 * Die Währung
	 */
	private Waehrung waehrung;
	
	/**
	 * 0 €
	 */
	public static final Geldbetrag NULL_EURO = new Geldbetrag(0);

	
	/**
	 * erstellt einen Geldbetrag in der Währung Euro
	 * @param betrag Betrag in €
	 * @throws IllegalArgumentException wenn betrag unendlich oder NaN ist
	 */
	public Geldbetrag(double betrag)
	{
		if(!Double.isFinite(betrag))
			throw new IllegalArgumentException();
		this.betrag = betrag;
		this.waehrung = Waehrung.EUR;
	}

	/**
	 * erstellt einen Geldbetrag
	 * @param betrag Betrag in Euro
	 * @param w ist die genutze Währung
	 */
	public Geldbetrag (double betrag, Waehrung w){
		if(!Double.isFinite(betrag))
			throw new IllegalArgumentException();
		if (w == null)
			throw new NullPointerException();
		this.betrag = betrag;
		this.waehrung = w;
	}

	/**
	 * Rechnet die Währung des aufrufenden Objektes in Parameter "zielwährung" um
	 * @param zielwaehrung Währung in die umgerechnet werden soll
	 * @return neues Geldbetrag-Objekt mit umgerechneter Währung
	 */
	public Geldbetrag umrechnen(Waehrung zielwaehrung){

		if (zielwaehrung == null)
			throw new IllegalArgumentException("Zielwährung darf nicht null sein");

		double faktor = this.waehrung.getNewWährung(zielwaehrung); 	// Umrechnungsfaktor von der aktuellen Währung zu der Zielwährung
		double neuerBetrag = this.betrag * faktor;					 // Berechnung des neuen Betrags in der Zielwährung

		return new Geldbetrag(neuerBetrag, zielwaehrung);
	}

	/**
	 * Betrag von this
	 * @return Betrag in der Währung von this
	 */
	public double getBetrag() {
		return betrag;
	}

	/**
	 * Währung von this
	 * @return währung von this
	 */
	public Waehrung getWaehrung(){
		return this.waehrung;
	}


	/**
	 * rechnet this + summand in der Währung von this
	 * @param summand zu addierender Betrag
	 * @return neues Objekt in der Währung von this
	 * @throws IllegalArgumentException wenn summand null ist
	 */
	public Geldbetrag plus(Geldbetrag summand)
	{
		if(summand == null)
			throw new IllegalArgumentException();

		// wenn this und Parameter dieselbe Währung haben
		if (summand.waehrung == this.waehrung) {
			return new Geldbetrag(this.betrag + summand.betrag, this.waehrung);
		}

		// Umrechnung in die Währung von this, falls sie nicht mit den Parameter übereinstimmt
		double andererBetragUmgerechnet = summand.umrechnen(this.waehrung).betrag;
		double ergebnis = this.betrag + andererBetragUmgerechnet;

		return new Geldbetrag(ergebnis,this.waehrung);
	}
	
	/**
	 * rechnet this - subtrahend
	 * @param subtrahend abzuziehender Betrag
	 * @return this - subtrahend in der Währung von this
	 * @throws IllegalArgumentException wenn subtrahend null ist
	 */
	public Geldbetrag minus(Geldbetrag subtrahend)
	{
		if(subtrahend == null)
			throw new IllegalArgumentException();

		// wenn this und Parameter dieselbe Währung haben
		if (subtrahend.waehrung == this.waehrung) {
			return new Geldbetrag(this.betrag - subtrahend.betrag, this.waehrung);
		}

		// Umrechnung in die Währung von this, falls sie nicht mit den Parameter übereinstimmt
		double andererBetragUmgerechnet = subtrahend.umrechnen(this.waehrung).betrag;
		double ergebnis = this.betrag - andererBetragUmgerechnet;

		return new Geldbetrag(ergebnis - subtrahend.betrag);
	}

	/**
	 * multipliziert this mit faktor
	 * @param faktor Faktor der Multiplikation
	 * @return das faktor-Fache von this
	 * @throws IllegalArgumentException wenn faktor nicht finit ist
	 */
	public Geldbetrag mal(double faktor)
	{
		if(!Double.isFinite(faktor))
			throw new IllegalArgumentException();


		return new Geldbetrag(this.betrag * faktor,this.waehrung);
	}

	@Override
	public int compareTo(Geldbetrag o) {
		return Double.compare(this.betrag, o.betrag);
	}

	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof Geldbetrag)) return false;
		if(o == this) return true;
		return this.compareTo((Geldbetrag) o) == 0;
	}
	
	/**
	 * prüft, ob this einen negativen Betrag darstellt
	 * @return true, wenn this negativ ist
	 */
	public boolean isNegativ()
	{
		return this.betrag < 0;
	}

	@Override
	public String toString() {
		return String.format("%,.2f %s", this.betrag, this.waehrung.name());
	}

}
