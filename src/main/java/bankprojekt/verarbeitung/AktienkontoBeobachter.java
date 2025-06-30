package bankprojekt.verarbeitung;
/**
 * Interface für Beobachter von Aktienkonto-Änderungen.
 * Beobachter werden benachrichtigt, wenn sich das Aktiendepot eines Aktienkontos ändert.
 */
public interface AktienkontoBeobachter  {
    /**
     * Wird aufgerufen, wenn sich das Aktiendepot des beobachteten Aktienkontos geändert hat.
     * @param konto Das Aktienkonto, dessen Depot sich geändert hat.
     * @param aktie Die Aktie, die hinzugefügt oder entfernt wurde (oder null, wenn eine allgemeine Änderung).
     * @param anzahl Die Anzahl der Stücke, die hinzugefügt (positiv) oder entfernt (negativ) wurden.
     * @param neueStückzahl Der neue Gesamtbestand dieser Aktie im Depot.
     */
    void aktualisieren(Aktienkonto konto, Aktie aktie, int anzahl, int neueStückzahl);
}
