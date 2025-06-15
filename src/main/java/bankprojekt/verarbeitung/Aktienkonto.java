package bankprojekt.verarbeitung;

import bankprojekt.geld.Waehrung;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Aktienkonto extends Konto implements Serializable {

    /**
     * Speichert das Aktiendepot: für jede Aktie die zugehörige Stückzahl.
     * Der Schlüssel ist das Aktie-Objekt, der Wert ist die Stückzahl (Integer).
     */
    public Map <Aktie,Integer> aktiendepot;


    //einen ExecutorService, um die Kaufaufträge asynchron auszuführen.
    // Ein CachedThreadPool ist gut für viele kurzlebige, unabhängige Aufgaben.
    private static final ExecutorService KaufauftragExecutor = Executors.newCachedThreadPool();


    /**
     * Konstruktor für ein Aktienkonto.
     * Ruft den Konstruktor der Superklasse Konto auf und initialisiert das Aktiendepot.
     *
     * @param inhaber Inhaber des Aktienkontos
     * @param kontonummer
     */
    public Aktienkonto(Kunde inhaber, long kontonummer) {
        super(inhaber, kontonummer);
        this.aktiendepot = new HashMap<>(); // Depot initialisieren
    }
    public Aktienkonto(Kunde inhaber) {
        this.aktiendepot = new HashMap<>(); // Depot initialisieren
    }

    /**
     * Fügt eine Anzahl von Aktien zum Depot hinzu.
     * synchronied stellt sicher das nur der aktuell ausgeführte Tread während der Ausführung der Methode auf das aktiendepot zugreifen kann
     * @param aktie Die Aktie.
     * @param anzahl Die Anzahl der hinzuzufügenden Stücke.
     */
    public void addAktienToDepot(Aktie aktie, int anzahl) throws GesperrtException {
        if (aktie == null || anzahl <= 0) {
            throw new IllegalArgumentException("Aktie darf nicht null sein und Anzahl muss positiv sein.");
        }

        synchronized (this) {
            Geldbetrag aktuellerKursDerAktie = aktie.getKurs(); // Kurs als Geldbetrag holen
            // Den Gesamtpreis berechnen: aktuellerKurs * anzahl
            Geldbetrag gesamtpreis = aktuellerKursDerAktie.mal(anzahl);

            // Umrechnung des Gesamtpreises in die Kontowährung, falls unterschiedlich
            Geldbetrag preisInKontowaehrung = gesamtpreis.umrechnen(getKontostand().getWaehrung());

            // Überprüfen, ob genügend Geld auf dem Konto ist
            if (this.getKontostand().getBetrag() < preisInKontowaehrung.getBetrag()) {
                throw new IllegalArgumentException("Geld auf Konto reicht nicht aus!");
            }

            // Geld vom Konto abziehen
            // Die Methode "abheben" ist bereits in der Superklasse Konto synchronisiert und kümmert sich um die Abhebung.
            this.abheben(preisInKontowaehrung); // Das Geld wird tatsächlich abgezogen

            // Aktien zum Depot hinzufügen (thread-safe durch äußeren synchronized-Block)
            aktiendepot.merge(aktie, anzahl, Integer::sum);
        }}

    /**
     * Entfernt eine Anzahl von Aktien aus dem Depot.
     * Wenn die Anzahl größer oder gleich dem Bestand ist, wird der Eintrag gelöscht.
     * @param aktie Die Aktie.
     * @param anzahl Die Anzahl der zu entfernenden Stücke.
     * @return Die tatsächlich entfernte Stückzahl.
     */
    private int removeAktienFromDepot(Aktie aktie, int anzahl) {
        synchronized (this){
        if (!aktiendepot.containsKey(aktie)) {
            return 0; // Aktie nicht im Depot
        }
        int aktuellerBestand = aktiendepot.get(aktie);
        if (anzahl >= aktuellerBestand) {
            aktiendepot.remove(aktie); // Komplett entfernen
            return aktuellerBestand;
        } else {
            aktiendepot.put(aktie, aktuellerBestand - anzahl); // Bestand reduzieren
            return anzahl;
        }
    }}



    public Future<Geldbetrag> kaufauftrag(String wkn, int anzahl, Geldbetrag hoechstpreis){
        Aktie aktie = Aktie.getAktie(wkn);

        if (anzahl <= 0 || hoechstpreis == null || hoechstpreis.isNegativ() || aktie == null) {
            // Ein Future zurückgeben, das sofort eine IllegalArgumentException wirft, wenn get() aufgerufen wird.
            return KaufauftragExecutor.submit(() -> { throw new IllegalArgumentException("Ungültige Parameter für Kaufauftrag oder Aktie nicht gefunden."); });
        }

        // Die eigentliche Kauflogik als Callable, die im ExecutorService ausgeführt wird
        Callable<Geldbetrag> kaufTask = () -> {
            Lock aktienLock = aktie.getAktienlock();
            Condition kursRunter = aktie.getKursRunter();
            Geldbetrag bezahlterPreis = null; // Auf null initialisieren

            aktienLock.lock(); // Lock für die Aktie erwerben
            try {
                // Schleife, um auf den passenden Kurs zu warten.
                // Vergleich erfolgt jetzt mit compareTo für Geldbetrag-Objekte.
                while (aktie.getKurs().compareTo(hoechstpreis) > 0) { // Bedingung: Kurs ist ZU HOCH
                    // Keine System.out.println-Aufrufe hier, um die Trennung der Belange zu wahren.
                    try {
                        kursRunter.await(); // Warte unbegrenzt, bis ein Signal kommt oder unterbrochen wird
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Interrupt-Flag wieder setzen
                        return null; // Auftrag abgebrochen (wird vom aufrufenden Code gehandhabt)
                    }
                }
                // An diesem Punkt ist der Kurs passend, oder der Thread wurde unterbrochen.
                if (Thread.currentThread().isInterrupted()) {
                    return null; // Auftrag abgebrochen
                }

                // Kurs ist unter oder gleich dem Höchstpreis. Jetzt kaufen!
                Geldbetrag aktuellerKurs = aktie.getKurs();
                Geldbetrag gesamtpreisBerechnet = aktuellerKurs.mal(anzahl);
                // Der Kaufbetrag muss für den Vergleich und die Abhebung in der Währung des Kontos vorliegen
                Geldbetrag kaufbetragInKontowaehrung = gesamtpreisBerechnet.umrechnen(getKontostand().getWaehrung());

                // Lock für das KONTO erwerben, um den Kontostand zu ändern und das Depot zu aktualisieren
                synchronized (this) { // Synchronisiere auf dem Aktienkonto-Objekt
                    // Prüfen, ob das Konto gesperrt ist
                    if (this.isGesperrt()) {
                        // Werfe eine Ausnahme statt zu drucken; sie wird vom Future aufgefangen
                        throw new GesperrtException(this.getKontonummer());
                    }
                    if (getKontostand().compareTo(kaufbetragInKontowaehrung) >= 0) {
                        // Geld abziehen
                        this.abheben(kaufbetragInKontowaehrung);
                        // Aktien zum Depot hinzufügen (Methode ist intern synchronisiert)
                        this.aktiendepot.merge(aktie, anzahl, Integer::sum); // Merge direkt verwenden
                        bezahlterPreis = gesamtpreisBerechnet; // Den Preis in der Aktienwährung oder Kontowährung zurückgeben
                    } else {
                        // Unzureichende Mittel, null zurückgeben, um Misserfolg anzuzeigen
                        return null;
                    }
                }
            } catch (GesperrtException e) {
                // Die geprüfte GesperrtException in eine ungeprüfte CompletionException für Callable verpacken
                throw new CompletionException(e);
            } finally {
                aktienLock.unlock(); // Das Lock der Aktie MUSS freigegeben werden, egal was passiert
            }
            return bezahlterPreis;
        };
        // Den Kauf-Task an den ExecutorService übergeben und das Future zurückgeben
        return KaufauftragExecutor.submit(kaufTask);
    }



    /**
     * Leitet einen asynchronen Verkaufsauftrag für eine bestimmte Aktie ein.
     * Der Auftrag wartet, bis der Kurs der Aktie den Mindestpreis erreicht oder überschreitet.
     *
     * @param wkn Die Wertpapierkennnummer der Aktie.
     * @param minimalpreis Der Mindestpreis, zu dem die Aktie verkauft werden soll.
     * @return Ein {@link Future}-Objekt, das das Ergebnis des Verkaufs darstellt.
     * Es enthält den gesamten erzielten {@link Geldbetrag} bei Erfolg, oder einen {@link Geldbetrag} von 0/wirft eine Ausnahme.
     * @throws IllegalArgumentException Wenn die Parameter ungültig sind.
     */
    public Future<Geldbetrag> verkaufauftrag(String wkn, Geldbetrag minimalpreis){
        Aktie aktie = Aktie.getAktie(wkn);
        // Wrapper für Integer, da Lambda-Ausdrücke nur final/effectively final Variablen nutzen
        final int[] stueckzahlWrapper = {0};

        synchronized (this) {
            // Finde das Aktie-Objekt im Depot über die WKN
            Aktie aktieImDepot = null;
            for (Map.Entry<Aktie, Integer> entry : aktiendepot.entrySet()) {
                if (entry.getKey().getWkn().equals(wkn)) {
                    aktieImDepot = entry.getKey();
                    stueckzahlWrapper[0] = entry.getValue();
                    break;
                }
            }

            if (aktieImDepot == null || stueckzahlWrapper[0] == 0) {
                // Wenn Aktie nicht im Depot oder Bestand 0, sofort ein Future mit 0 zurückgeben
                return KaufauftragExecutor.submit(() -> new Geldbetrag(0, Waehrung.EUR)); // Annahme EUR für Null-Rückgabe
            }
            aktie = aktieImDepot; // Sicherstellen, dass wir die Depot-Aktien-Instanz verwenden
        }

        // Erneute Prüfung nach potenzieller Null von Aktie.getAktie(wkn), falls nicht bereits oben behandelt
        if (aktie == null) {
            return KaufauftragExecutor.submit(() -> new Geldbetrag(0, Waehrung.EUR));
        }
        if (minimalpreis == null || minimalpreis.isNegativ()) {
            return KaufauftragExecutor.submit(() -> { throw new IllegalArgumentException("Ungültiger Minimalpreis für Verkaufsauftrag."); });
        }


        // Die eigentliche Verkaufslogik als Callable
        final Aktie finalAktie = aktie; // Aktie-Objekt ist nun final
        final int anzahlZuVerkaufen = stueckzahlWrapper[0]; // Die vollständige Stückzahl aus dem Depot

        Callable<Geldbetrag> verkaufTask = () -> {
            Lock aktienLock = finalAktie.getAktienlock();
            Condition kursHoch = finalAktie.getKursHoch(); // Brauchen Condition für Kursanstieg
            Geldbetrag gesamterloes = new Geldbetrag(0, getKontostand().getWaehrung()); // Auf Null initialisieren

            aktienLock.lock(); // Lock für die Aktie erwerben
            try {
                // Schleife, um auf den passenden Kurs zu warten
                while (finalAktie.getKurs().compareTo(minimalpreis) < 0) { // Bedingung: Kurs ist ZU NIEDRIG
                    // Keine Konsolenausgabe hier
                    try {
                        kursHoch.await(); // Warte unbegrenzt
                    } catch (InterruptedException e) {
                        return new Geldbetrag(0, getKontostand().getWaehrung()); // Erlös 0 bei Abbruch
                    }
                }
                // An diesem Punkt ist der Kurs passend, oder der Thread wurde unterbrochen.
                if (Thread.currentThread().isInterrupted()) {
                    return new Geldbetrag(0, getKontostand().getWaehrung());
                }

                // Kurs ist über oder gleich dem Mindestpreis. Jetzt verkaufen!
                Geldbetrag aktuellerVerkaufskurs = finalAktie.getKurs();
                Geldbetrag gesamtpreisBerechnet = aktuellerVerkaufskurs.mal(anzahlZuVerkaufen);
                gesamterloes = gesamtpreisBerechnet.umrechnen(getKontostand().getWaehrung());


                // Lock für das KONTO erwerben, um den Kontostand zu ändern und das Depot zu aktualisieren
                synchronized (this) { // Synchronisiere auf dem Aktienkonto-Objekt
                    if (isGesperrt()) {
                        // Werfe eine Ausnahme statt zu drucken
                        throw new GesperrtException(this.getKontonummer());
                    }
                    // Aktien aus dem Depot entfernen
                    int removedAnzahl = removeAktienFromDepot(finalAktie, anzahlZuVerkaufen); // Sollte die volle Stückzahl sein
                    if (removedAnzahl > 0) {
                        // Geld einzahlen
                        einzahlen(gesamterloes); // Nutze deine einzahlen-Methode
                    } else {
                        // Dies sollte nicht passieren, wenn die anfängliche Prüfung korrekt war,
                        // aber es behandelt Randfälle.
                        return new Geldbetrag(0, getKontostand().getWaehrung()); // Keine Aktien zu entfernen, kein Erlös
                    }
                }
            } catch (GesperrtException e) {
                // Die geprüfte GesperrtException in eine ungeprüfte CompletionException für Callable verpacken
                throw new CompletionException(e);
            } catch (Exception e) {
                // Breite Ausnahmen vom Task abfangen und verpacken
                throw new CompletionException("Verkaufsauftrag für " + finalAktie.getWkn() + " fehlgeschlagen mit Ausnahme: " + e.getMessage(), e);
            } finally {
                aktienLock.unlock(); // Das Lock der Aktie MUSS freigegeben werden
            }
            return gesamterloes;
        };

        // Den Verkauf-Task an den ExecutorService übergeben und das Future zurückgeben
        return KaufauftragExecutor.submit(verkaufTask);
    }



    @Override
    public boolean abheben(Geldbetrag betrag) throws GesperrtException {
        if (betrag == null || betrag.isNegativ()) {
            throw new IllegalArgumentException("Betrag ungültig");
        }
        if (this.isGesperrt()) {
            throw new GesperrtException(this.getKontonummer());
        }

        setKontostand(this.getKontostand().minus(betrag));
        return true;
    }


    @Override
    public void einzahlen(Geldbetrag betrag) {
        if (betrag == null || betrag.isNegativ()) {
            throw new IllegalArgumentException("Betrag ungültig");
        }
        synchronized (this) { // Synchronisiere auf dem Aktienkonto-Objekt, um den Kontostand zu schützen
            setKontostand(getKontostand().plus(betrag));
        }
    }


    public static void shutdownExecutor() {
        KaufauftragExecutor.shutdown();
        try {
            if (!KaufauftragExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                KaufauftragExecutor.shutdownNow(); // Falls nicht alle Aufgaben beendet wurden
            }
        } catch (InterruptedException e) {
            KaufauftragExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }


    }


}
