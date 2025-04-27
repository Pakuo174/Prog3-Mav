package de.daniel.bankprojekt.verarbeitung;

/**
 * Ein Girokonto, d.h. ein Konto mit einem Dispo und der Fähigkeit,
 * Überweisungen zu senden und zu empfangen.
 * Grundsätzlich sind Überweisungen und Abhebungen möglich bis
 * zu einem Kontostand von -this.dispo
 * @author Doro
 *
 */
public class Girokonto extends UeberweisungsfaehigesKonto{
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
    public boolean ueberweisungAbsenden(Geldbetrag betrag, 
    		String empfaenger, long nachKontonr, 
    		long nachBlz, String verwendungszweck) 
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


	/**
	 * Der dispo wird vorher auf die Kontowährung umgerechnet >>> in der Variable "dispoInKontoWährung"
	 * @param betrag abzuhebender Betrag
	 * @return
	 * @throws GesperrtException
	 */
	@Override
	public boolean abheben(Geldbetrag betrag) throws GesperrtException {
		if (betrag == null || betrag.isNegativ()) {
			throw new IllegalArgumentException("Betrag ungültig");
		}
		if (this.isGesperrt()) {
			throw new GesperrtException(this.getKontonummer());
		}


		// hier wird der Dispo so umgerechnet, dass er dieselbe Währung hat wie dein Kontostand + der Variable zugewiesen
		Geldbetrag dispoInKontowaehrung = this.dispo.umrechnen(this.getKontostand().getWaehrung());

		//  addieren des aktuellen Kontostand plus den Dispo (beide in der gleichen Währung)
		// saldo für dumme ~ tatsächlicher "verfügbarer Betrag", den du abheben oder überweisen darfst — inklusive Dispo (0 EUro auf Konto und 100 Euro Dispo = man darf 100 noch abheben)
		Geldbetrag saldo = this.getKontostand().plus(dispoInKontowaehrung);

		// Überprüfe, ob nach Abzug des Betrags noch über saldo ist - also im Rahmen vom Dispo
		// wenn ungleich negativ kann es abgezogen werden (300 saldo - 280 betrag ✔️ ️, 300 saldo - 350 betrag ❌)
		if (!saldo.minus(betrag).isNegativ()) {
			setKontostand(this.getKontostand().minus(betrag));
			return true;
		} else {
			return false;
		}
	}
}
