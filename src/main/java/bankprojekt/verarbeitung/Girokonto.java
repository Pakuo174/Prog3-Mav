package bankprojekt.verarbeitung;

import bankprojekt.verwaltung.Bank;

import java.io.Serializable;

/**
 * Ein Girokonto, d.h. ein Konto mit einem Dispo und der Fähigkeit,
 * Überweisungen zu senden und zu empfangen.
 * Grundsätzlich sind Überweisungen und Abhebungen möglich bis
 * zu einem Kontostand von -this.dispo
 * @author Doro
 *
 */
public class Girokonto extends UeberweisungsfaehigesKonto implements Serializable {
	/**
	 * Wert, bis zu dem das Konto überzogen werden darf
	 */
	private Geldbetrag dispo;


	/**
	 * erzeugt ein leeres, nicht gesperrtes Standard-Girokonto
	 * von Max MUSTERMANN
	 */
	public Girokonto()
	{
		super(Kunde.MUSTERMANN, 99887766);
		this.dispo = new Geldbetrag(500);
	}
	
	/**
	 * erzeugt ein Girokonto mit den angegebenen Werten
	 * @param inhaber Kontoinhaber
	 * @param nummer Kontonummer
	 * @param dispo Dispo
	 * @throws IllegalArgumentException wenn der inhaber null ist oder der angegebene dispo negativ bzw. NaN ist
	 */
	public Girokonto(Kunde inhaber, long nummer, Geldbetrag dispo)
	{
		super(inhaber, nummer);
		if(dispo == null || dispo.isNegativ())
			throw new IllegalArgumentException("Der Dispo ist nicht gültig!");
		this.dispo = dispo;
	}

	/**
	 * erstellt eine Instanz für ein Überweisungskonto zusammen mit einer zugehörigen Bank - bankleitzahl
	 * @param inhaber der Inhaber
	 * @param kontonummer die gewünschte Kontonummer
	 * @param bankleitzahl ist die bankleitzahl der zugehörigen Bank
	 * @throws IllegalArgumentException wenn der inhaber null ist
	 */
	public Girokonto(Kunde inhaber, long kontonummer, long bankleitzahl, Geldbetrag dispo) {
		super(inhaber, kontonummer, bankleitzahl); // wenn der Superkonstruktor das akzeptiert
		this.dispo = dispo;
	}
	
	/**
	 * liefert den Dispo
	 * @return Dispo von this
	 */
	public Geldbetrag getDispo() {
		return dispo;
	}

	/**
	 * setzt den Dispo neu
	 * @param dispo muss größer sein als 0
	 * @throws IllegalArgumentException wenn dispo negativ bzw. NaN ist
	 */
	public void setDispo(Geldbetrag dispo) {
		if(dispo == null || dispo.isNegativ())
			throw new IllegalArgumentException("Der Dispo ist nicht gültig!");
		this.dispo = dispo;
	}
	
	@Override
	public boolean ueberweisungAbsenden(Geldbetrag betrag, String empfaenger, long nachKontonr, long nachBlz, String verwendungszweck)
    				throws GesperrtException 
    {
      if (this.isGesperrt())
            throw new GesperrtException(this.getKontonummer());
        if (betrag == null || betrag.isNegativ()|| empfaenger == null || verwendungszweck == null)
            throw new IllegalArgumentException("Parameter fehlerhaft");

		//_____________________________________________________________
		Geldbetrag dispoInKontowaehrung = this.dispo.umrechnen(this.getKontostand().getWaehrung());
		if (!getKontostand().plus(dispoInKontowaehrung).minus(betrag).isNegativ())
		{
			setKontostand(getKontostand().minus(betrag));
			return true;
		}
		else
			return false;
    }

    @Override
    public void ueberweisungEmpfangen(Geldbetrag betrag, String vonName, long vonKontonr, long vonBlz, String verwendungszweck)
    {
        if (betrag == null || betrag.isNegativ()|| vonName == null || verwendungszweck == null)
            throw new IllegalArgumentException("Parameter fehlerhaft");
        setKontostand(getKontostand().plus(betrag));
    }
    
    @Override
    public String toString()
    {
    	String ausgabe = "-- GIROKONTO --" + System.lineSeparator() +
    	super.toString()
    	+ "Dispo: " + this.dispo + System.lineSeparator();
    	return ausgabe;
    }

	@Override
	public boolean pruefeAbhebungSpezifisch(Geldbetrag betrag){

		Geldbetrag dispoInKontowaehrung = this.dispo.umrechnen(this.getKontostand().getWaehrung());
		Geldbetrag saldoNachAbhebung = this.getKontostand().minus(betrag).plus(dispoInKontowaehrung);
		// Prüfen, ob der Saldo nach Abhebung unter dem negativen Dispo liegt (d.h. zu weit überzogen wäre)
		return saldoNachAbhebung.compareTo(Geldbetrag.NULL_EURO) >= 0;
    }



}
