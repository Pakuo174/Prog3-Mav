package bankprojekt.verarbeitung;

import bankprojekt.verwaltung.Bank;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Aktientest_AktieKaufen {

    public static void main(String[] args) {


        Bank b1 = new Bank(1234567);
        Kunde k1 = new Kunde("Daniel", "Kujawa", "Bärlin", LocalDate.of(2000, 7, 12));
        Kunde k2 = new Kunde("Nico", "Froelich", "Rathenow", LocalDate.of(2000, 12, 13));
        Geldbetrag neuEinsteigerBonus = new Geldbetrag(500);


        System.out.println("--- Aktien-Testprogramm gestartet ---");


        // Aktien anlegen
        Aktie apple = new Aktie("APPLE",new Geldbetrag(120));
        Aktie tesla = new Aktie("TESLA", new Geldbetrag(89));

        System.out.println("Anfängliche Aktienkurse:");
        System.out.println("APPLE: " + apple.getKurs());
        System.out.println("TESLA: " + tesla.getKurs());
        System.out.println("------------------------------------");

        // Aktiendepo anlegen
        Aktienkonto a1 = new Aktienkonto(k1, 10000001L); // Kontonummer übergeben
        a1.einzahlen(neuEinsteigerBonus);


        System.out.println("\nAnfängliche Kontostände:");
        System.out.println(a1);
        System.out.println("------------------------------------");

        Thread kursAusgabeThread = new Thread(() -> {
            try {
                while (true) { // Läuft unendlich, bis das Programm stoppt
                    System.out.println("Aktueller APPLE-Kurs: " + apple.getKurs() + " | TESLA-Kurs: " + tesla.getKurs());
                    Thread.sleep(5000); // 5 Sekunden warten
                }
            } catch (InterruptedException e) {
                // Thread wurde unterbrochen (z.B. beim Beenden des JVMs)
                Thread.currentThread().interrupt(); // Interrupt-Status wiederherstellen
            }
        });
        kursAusgabeThread.setDaemon(true); // WICHTIG: Macht den Thread zu einem Daemon-Thread
        kursAusgabeThread.start(); // Thread starten

        // --- Dein Kaufauftrag ---
        System.out.println("\nPlatziere Kaufauftrag für APPLE (2 Stück, max. 117.0 EUR)...");
        Future<Geldbetrag> kaufResultat = a1.kaufauftrag(apple.getWkn(), 2, new Geldbetrag(117.0));

        // Warte auf Ergebnis des Kaufauftrags und behandle es
        try {
            Geldbetrag bezahlterBetrag = kaufResultat.get(); // Blockiert, bis der Kauf abgeschlossen ist
            if (bezahlterBetrag != null) {
                System.out.println("\nKauf abgeschlossen: Erfolgreich für " + bezahlterBetrag + " gekauft.");
            } else {
                System.out.println("\nKauf abgeschlossen: Kauf wurde nicht ausgeführt (Kurs nie erreicht oder andere Gründe).");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("\nKaufauftrag wurde unterbrochen.");
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            // Wichtig: ExecutorService und Kursgeneratoren immer beenden
            a1.shutdownExecutor();
            apple.anhalten();
            tesla.anhalten();
        }
    }
}