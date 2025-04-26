package de.daniel.bank.bankprojekt.verarbeitung;

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
		// this(betrag, Waehrung.EUR);
	}

	/**
	 * erstellt einen Geldbetrag
	 * @param betrag Betrag in Euro
	 * @param w ist die genutze Währung
	 */
	public Geldbetrag (double betrag, Waehrung w){
		if(!Double.isFinite(betrag))
			throw new IllegalArgumentException();
		this.betrag = betrag;
		this.waehrung = w;
	}

	/**
	 * rechnet die Währungs des Geldbetrages in einen anderen um
	 * @param zielwaehrung Währung in die umgerechnet werden soll
	 * @return neues Geldbetrag Objekt mit umgerechnter Währung
	 */
	public Geldbetrag umrechnen(Waehrung zielwaehrung){

		if (zielwaehrung == null)
			throw new IllegalArgumentException();

		double newBetrag = 0;

			// Wenn die Ausgangswährung EUR ist, werden die Umrechnungslogiken für alle Zielwährungen implementiert
			if (this.waehrung == Waehrung.EUR) {
				switch (zielwaehrung) {
					case ESCUDO:
						newBetrag = this.betrag * 109.8269;
						break;
					case DOBRA:
						newBetrag = this.betrag * 24304.7429;
						break;
					case FRANC:
						newBetrag = this.betrag * 490.92;
						break;
					default:
						throw new IllegalArgumentException("Unbekannte Zielwährung");
				}
			}
			// Wenn die Ausgangswährung ESCUDO ist
			else if (this.waehrung == Waehrung.ESCUDO) {
				switch (zielwaehrung) {
					case EUR:
						newBetrag = this.betrag / 109.8269;
						break;
					case DOBRA:
						newBetrag = (this.betrag / 109.8269) * 24304.7429;
						break;
					case FRANC:
						newBetrag = (this.betrag / 109.8269) * 490.92;
						break;
					default:
						throw new IllegalArgumentException("Unbekannte Zielwährung");
				}
			}
			// Wenn die Ausgangswährung DOBRA ist
			else if (this.waehrung == Waehrung.DOBRA) {
				switch (zielwaehrung){
					case EUR:
					newBetrag = this.betrag / 24304.7429;
					break;
					case FRANC:
						newBetrag = (this.betrag / 24304.7429) * 490.92;
						break;
					case ESCUDO:
						newBetrag = (this.betrag / 24304.7429) * 109.8269;
						break;
					default:
						throw new IllegalArgumentException("Unbekannte Zielwährung");
				}
			}
			// Wenn die Ausgangswährung FRANC ist
			else if (this.waehrung == Waehrung.FRANC) {
				switch (zielwaehrung){
					case EUR:
						newBetrag = this.betrag / 490.92;
						break;
					case ESCUDO:
						newBetrag = (this.betrag / 490.92) * 109.8269;
						break;
					case DOBRA:
						newBetrag = (this.betrag / 490.92) * 24304.7429;
						break;
					default:
						throw new IllegalArgumentException("Unbekannte Zielwährung");
				}

			}


		// Setze den neuen Betrag und die Zielwährung
			return new Geldbetrag(newBetrag, zielwaehrung);
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
	 * rechnet this + summand
	 * @param summand zu addierender Betrag
	 * @return this + summand in der Währung von this
	 * @throws IllegalArgumentException wenn summand null ist
	 */
	public Geldbetrag plus(Geldbetrag summand)
	{
		if(summand == null)
			throw new IllegalArgumentException();
		return new Geldbetrag(this.betrag + summand.betrag);
	}
	
	/**
	 * rechnet this - siúbtrahend
	 * @param subtrahend abzuziehender Betrag
	 * @return this - subtrahend in der Währung von this
	 * @throws IllegalArgumentException wenn subtrahend null ist
	 */
	public Geldbetrag minus(Geldbetrag subtrahend)
	{
		if(subtrahend == null)
			throw new IllegalArgumentException();
		return new Geldbetrag(this.betrag - subtrahend.betrag);
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
		return new Geldbetrag(this.betrag * faktor);
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
