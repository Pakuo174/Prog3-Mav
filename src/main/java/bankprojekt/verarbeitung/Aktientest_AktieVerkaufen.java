package bankprojekt.verarbeitung;

import bankprojekt.geld.Waehrung;
import bankprojekt.verwaltung.Bank;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static bankprojekt.verarbeitung.Aktienkonto.shutdownExecutor;

public class Aktientest_AktieVerkaufen {

    public static void main(String[] args) throws GesperrtException {

        Bank b1 = new Bank(1234567);
        Kunde k1 = new Kunde("Daniel", "Kujawa", "Bärlin", LocalDate.of(2000, 7, 12));
        Geldbetrag startkapital = new Geldbetrag(500, Waehrung.EUR);


        System.out.println("--- Aktien-Testprogramm gestartet ---");

        // Aktien anlegen. Sie beginnen sofort, ihren Kurs zu ändern.
        Aktie apple = new Aktie("APPLE", new Geldbetrag(120)); // Startkurs für Apple
        Aktie tesla = new Aktie("TESLA", new Geldbetrag(89)); // Tesla-Aktie

        System.out.println("Anfängliche Aktienkurse:");
        System.out.println("APPLE: " + String.format("%.2f", apple.getKurs()));
        System.out.println("TESLA: " + String.format("%.2f", tesla.getKurs()));
        System.out.println("------------------------------------");

        // Aktiendepot anlegen
        Aktienkonto a1 = new Aktienkonto(k1, 10000001L);
        a1.einzahlen(startkapital);


        a1.addAktienToDepot(apple, 5); // Füge 5 Apple-Aktien direkt zum Depot hinzu
        a1.addAktienToDepot(tesla, 10); // Füge 10 Tesla-Aktien direkt zum Depot hinzu



        // Verkaufsauftrag platzieren
        System.out.println("\nPlatziere Verkaufsauftrag für APPLE (min. 125.0 EUR)...");
        // Der Verkaufsauftrag wartet, bis der Kurs 125 EUR oder höher erreicht.
        Future<Geldbetrag> appleVerkaufFuture = a1.verkaufauftrag(apple.getWkn(), new Geldbetrag(123.50, Waehrung.EUR));

        // --- Wartezeit für den VERKAUFAUFTRAG ---
        // WICHTIG: Gib dem Verkaufsauftrag Zeit, abgeschlossen zu werden.
        System.out.println("\nWarte 60 Sekunden, damit der Verkaufsauftrag ausgeführt werden kann...");
        try {
            Geldbetrag verkaufserloes = appleVerkaufFuture.get(60, TimeUnit.SECONDS); // Warte max. 60s auf Verkaufsabschluss
            if (verkaufserloes != null && verkaufserloes.getBetrag() > 0) {
                System.out.println("APPLE Verkaufsauftrag erfolgreich abgeschlossen! Erlös: " + verkaufserloes);
            } else {
                System.out.println("APPLE Verkaufsauftrag wurde nicht abgeschlossen oder brachte keinen Erlös (Timeout/Kurs nicht erreicht).");
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.err.println("Fehler beim Abrufen des APPLE Verkaufsauftragsergebnisses oder Timeout: " + e.getMessage());
            appleVerkaufFuture.cancel(true); // Versuch, den Auftrag abzubrechen
            Thread.currentThread().interrupt();
        }

        System.out.println("\nEndgültiger Kontostand nach Transaktionen:");
        System.out.println(a1);


        // --- Sauberes Herunterfahren ---
        System.out.println("\n--- Beende Hintergrundaufgaben ---");
        // Stoppe die Kursänderungen für alle Aktien
        apple.anhalten();
        tesla.anhalten();

        // Wichtig: Den ExecutorService, der für alle Aufträge verwendet wird, herunterfahren.
        // Dies muss NACHDEM alle Aufträge voraussichtlich abgeschlossen sind.
        shutdownExecutor();

        System.out.println("\n--- Aktien-Testprogramm beendet ---");
        System.out.printf("APPLE Endkurs: %.2f%n", apple.getKurs());
        System.out.printf("TESLA Endkurs: %.2f%n", tesla.getKurs());
    }
}