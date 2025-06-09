package bankprojekt.verarbeitung;

import bankprojekt.geld.Waehrung;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Aktienkonto extends Konto {

    /**
     * Speichert das Aktiendepot: für jede Aktie die zugehörige Stückzahl.
     * Der Schlüssel ist das Aktie-Objekt, der Wert ist die Stückzahl (Integer).
     */
    public Map <Aktie,Integer> aktiendepot;


    //einen ExecutorService, um die Kaufaufträge asynchron auszuführen.
    // Ein CachedThreadPool ist gut für viele kurzlebige, unabhängige Aufgaben.
    private static final ExecutorService kaufauftragExecutor = Executors.newCachedThreadPool();


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
     * @param aktie Die Aktie.
     * @param anzahl Die Anzahl der hinzuzufügenden Stücke.
     */
    public void addAktienToDepot(Aktie aktie, int anzahl) {
        aktiendepot.merge(aktie, anzahl, Integer::sum);
    }

    /**
     * Entfernt eine Anzahl von Aktien aus dem Depot.
     * Wenn die Anzahl größer oder gleich dem Bestand ist, wird der Eintrag gelöscht.
     * @param aktie Die Aktie.
     * @param anzahl Die Anzahl der zu entfernenden Stücke.
     * @return Die tatsächlich entfernte Stückzahl.
     */
    private int removeAktienFromDepot(Aktie aktie, int anzahl) {
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
    }



    public Future<Geldbetrag> kaufauftrag(String wkn, int anzahl, Geldbetrag hoechstpreis){
        Aktie aktie = Aktie.getAktie(wkn);

        if (anzahl <= 0 || hoechstpreis == null || hoechstpreis.isNegativ()) {
            return kaufauftragExecutor.submit(() -> { throw new IllegalArgumentException("Ungültige Parameter für Kaufauftrag."); });
        }

        //warten
        // Die eigentliche Kauflogik als Callable, die im ExecutorService ausgeführt wird
        Callable<Geldbetrag> kaufTask = () -> {
            Lock aktienLock = aktie.getAktienlock();
            Condition kursRunter = aktie.getKursRunter();
            Geldbetrag bezahlterPreis;

            aktienLock.lock(); // Lock für die Aktie erwerben
            try {
                // Schleife, um auf den passenden Kurs zu warten.
                while (aktie.getKurs() > hoechstpreis.getBetrag() && !Thread.currentThread().isInterrupted()) {
                    System.out.println(Thread.currentThread().getName() + " --> Warte auf Kursfall bei " + aktie.getWkn() + ". Aktuell: " + String.format("%.2f", aktie.getKurs()) + ", Max: " + String.format("%.2f", hoechstpreis.getBetrag()));
                    kursRunter.await(10, TimeUnit.SECONDS); // Warte max. 10 Sekunden, bevor erneut geprüft wird
                }
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Kaufauftrag für " + aktie.getWkn() + " unterbrochen.");
                    return null; // Auftrag abgebrochen
                }


                // Kurs ist unter oder gleich dem Höchstpreis. Jetzt kaufen!
                double aktuellerKurs = aktie.getKurs();
                double gesamtpreisDouble = aktuellerKurs * anzahl;
                Geldbetrag kaufbetrag = new Geldbetrag(gesamtpreisDouble, getKontostand().getWaehrung());

                // Lock für das KONTO erwerben, um den Kontostand zu ändern und Depot zu aktualisieren
                synchronized (this) { // Synchronisiere auf dem Aktienkonto-Objekt
                    if (getKontostand().compareTo(kaufbetrag) >= 0) {
                        // Geld abziehen
                        abheben(kaufbetrag);
                        // Aktien zum Depot hinzufügen
                        addAktienToDepot(aktie, anzahl);
                        bezahlterPreis = kaufbetrag;
                        System.out.printf("Aktie %s gekauft: %d Stück zu %.2f (Gesamt: %.2f). Neuer Kontostand: %s.\n",
                                aktie.getWkn(), anzahl, aktuellerKurs, gesamtpreisDouble, getKontostand().toString());
                    } else {
                        System.out.println("Kaufauftrag für " + aktie.getWkn() + " fehlgeschlagen: Unzureichender Kontostand. Aktueller Kurs: " + String.format("%.2f", aktuellerKurs) + ", Benötigt: " + kaufbetrag + ", Vorhanden: " + getKontostand());
                        return null;
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("Kaufauftrag für " + aktie.getWkn() + " unterbrochen beim Warten.");
                Thread.currentThread().interrupt(); // Interrupt-Flag wieder setzen
                return null;
            } finally {
                // Das Lock der Aktie MUSS freigegeben werden, egal was passiert ist
                aktienLock.unlock();
            }
            return bezahlterPreis;
        };
        // Den Kauf-Task an den ExecutorService übergeben und das Future zurückgeben
        return kaufauftragExecutor.submit(kaufTask);
    }


    public Future<Geldbetrag> verkaufauftrag(String wkn, Geldbetrag minimalpreis){
        Aktie aktie = Aktie.getAktie(wkn);
        final int[] stueckzahlWrapper = {0}; // Wrapper für Integer, da Lambda nur final/effectively final Variablen nutzt


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
                System.out.println("Verkaufsauftrag für WKN " + wkn + " fehlgeschlagen: Aktie nicht im Depot oder Bestand 0.");
                // Wenn Aktie nicht im Depot oder Bestand 0, sofort Future mit 0 zurückgeben
                return kaufauftragExecutor.submit(() -> new Geldbetrag(0, Waehrung.EUR));
            }
            aktie = aktieImDepot; // Stelle sicher, dass wir die Depot-Aktien-Instanz verwenden
        }

        if (aktie == null) { // Fall, dass Aktie zwar im Depot aber Aktie.getAktie(wkn) null liefert.
            System.out.println("Verkaufsauftrag für WKN " + wkn + " fehlgeschlagen: Aktie nicht gefunden (obwohl im Depot).");
            return kaufauftragExecutor.submit(() -> new Geldbetrag(0, Waehrung.EUR));
        }
        if (minimalpreis == null || minimalpreis.isNegativ()) {
            return kaufauftragExecutor.submit(() -> { throw new IllegalArgumentException("Ungültiger Minimalpreis für Verkaufsauftrag."); });
        }


        // Die eigentliche Verkaufslogik als Callable
        final Aktie finalAktie = aktie; // Aktie-Objekt ist nun final
        final int anzahlZuVerkaufen = stueckzahlWrapper[0]; // Die vollständige Stückzahl aus dem Depot

        Callable<Geldbetrag> verkaufTask = () -> {
            Lock aktienLock = finalAktie.getAktienlock();
            Condition kursHoch = finalAktie.getKursHoch(); // Brauchen Condition für Kursanstieg
            Geldbetrag gesamterloes ;

            aktienLock.lock(); // Lock für die Aktie erwerben
            try {
                // Schleife, um auf den passenden Kurs zu warten
                while (finalAktie.getKurs() < minimalpreis.getBetrag() && !Thread.currentThread().isInterrupted()) {
                    System.out.println(Thread.currentThread().getName() + " --> Warte auf Kursanstieg bei " + finalAktie.getWkn() + ". Aktuell: " + String.format("%.2f", finalAktie.getKurs()) + ", Min: " + String.format("%.2f", minimalpreis.getBetrag()));
                    kursHoch.await(10, TimeUnit.SECONDS); // Warte max. 10 Sekunden
                }
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Verkaufsauftrag für " + finalAktie.getWkn() + " unterbrochen.");
                    return new Geldbetrag(0, getKontostand().getWaehrung()); // Auftrag abgebrochen, Erlös 0
                }

                // Kurs ist über oder gleich dem Minimalpreis. Jetzt verkaufen!
                double aktuellerVerkaufskurs = finalAktie.getKurs();
                double gesamtpreisDouble = aktuellerVerkaufskurs * anzahlZuVerkaufen;
                gesamterloes = new Geldbetrag(gesamtpreisDouble, getKontostand().getWaehrung());

                // Lock für das KONTO erwerben, um den Kontostand zu ändern und Depot zu aktualisieren
                synchronized (this) { // Synchronisiere auf dem Aktienkonto-Objekt
                    if (isGesperrt()) {
                        System.out.println(Thread.currentThread().getName() + ": Verkaufsauftrag für " + finalAktie.getWkn() + " fehlgeschlagen: Konto gesperrt.");
                        return new Geldbetrag(0, getKontostand().getWaehrung());
                    }
                    // Aktien aus dem Depot entfernen
                    int removedAnzahl = removeAktienFromDepot(finalAktie, anzahlZuVerkaufen); // Sollte anzahlZuVerkaufen sein
                    if (removedAnzahl > 0) {
                        // Geld einzahlen
                        einzahlen(gesamterloes); // Nutze deine einzahlen-Methode
                        System.out.printf("Aktie %s verkauft: %d Stück zu %.2f (Gesamt: %.2f). Neuer Kontostand: %s.\n",
                                finalAktie.getWkn(), removedAnzahl, aktuellerVerkaufskurs, gesamtpreisDouble, getKontostand().toString());
                    } else {
                        // Dies sollte nicht passieren, wenn die Prüfung am Anfang korrekt war
                        System.out.println("Verkaufsauftrag für " + finalAktie.getWkn() + " fehlgeschlagen: Aktie nicht mehr im Depot oder Stückzahl 0.");
                        return new Geldbetrag(0, getKontostand().getWaehrung());
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("Verkaufsauftrag für " + finalAktie.getWkn() + " unterbrochen beim Warten.");
                Thread.currentThread().interrupt();
                return new Geldbetrag(0, getKontostand().getWaehrung()); // Erlös 0 bei Abbruch
            } catch (Exception e) {
                System.err.println("Verkaufsauftrag für " + finalAktie.getWkn() + " fehlgeschlagen mit Exception: " + e.getMessage());
                return new Geldbetrag(0, getKontostand().getWaehrung());
            } finally {
                aktienLock.unlock(); // Das Lock der Aktie MUSS freigegeben werden
            }
            return gesamterloes;
        };

        // Den Verkauf-Task an den ExecutorService übergeben und das Future zurückgeben
        return kaufauftragExecutor.submit(verkaufTask);
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
        kaufauftragExecutor.shutdown();
        try {
            if (!kaufauftragExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                kaufauftragExecutor.shutdownNow(); // Falls nicht alle Aufgaben beendet wurden
            }
        } catch (InterruptedException e) {
            kaufauftragExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }


    }


}
