package bankprojekt.verarbeitung;

public class StandartKontoFabrik extends Kontofabrik{


    /**
     * Geldbetrag als letzter Parameter macht nur Sinn für Girokonto
     * @param kontoTyp Der Typ des zu erstellenden Kontos (z.B. "sparbuch", "girokonto", "aktienkonto").
     * @param inhaber Der Kontoinhaber.
     * @param kontonummer Die Kontonummer.
     * @param betrag Ein optionaler Betrag, z.B. für den Dispokredit beim Girokonto.
     * @return
     */
    @Override
    public Konto erstelleKonto(String kontoTyp, Kunde inhaber, long kontonummer, Geldbetrag betrag) {

        switch (kontoTyp.toLowerCase()){
            case "sparbuch":
                return new Sparbuch(inhaber,kontonummer);

            case "girokonto":
                if (betrag == null){
                    throw new IllegalArgumentException("Für Girokonto muss ein Dispo-Betrag angegeben werden.");
                }
                return new Girokonto(inhaber,kontonummer,betrag);
            case "aktienkonto":
                return new Aktienkonto(inhaber,kontonummer);
            default:
                throw new IllegalArgumentException("Unbekannter KOntotyp für StandardKontofabrik: " + kontoTyp);
        }
    }
}
