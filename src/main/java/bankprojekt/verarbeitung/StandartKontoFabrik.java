package bankprojekt.verarbeitung;

public class StandartKontoFabrik extends Kontofabrik{

    @Override
    public Sparbuch erstelleSparbuch(Kunde inhaber, long kontonummer) {
        return new Sparbuch(inhaber, kontonummer);
    }

    @Override
    public Girokonto erstelleGirokonto(Kunde inhaber, long kontonummer, Geldbetrag dispo) {
        return new Girokonto(inhaber, kontonummer, dispo);
    }

    @Override
    public Aktienkonto erstelleAktienkonto(Kunde inhaber, long kontonummer) {
        return new Aktienkonto(inhaber, kontonummer);
    }
}
