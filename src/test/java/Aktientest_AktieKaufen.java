
import bankprojekt.verarbeitung.Aktie;
import bankprojekt.verarbeitung.Aktienkonto;
import bankprojekt.verarbeitung.Geldbetrag;
import bankprojekt.verarbeitung.Kunde;
import bankprojekt.verwaltung.Bank;

import java.time.LocalDate;

public class Aktientest_AktieKaufen {

    public static void main(String[] args) {


        Bank b1 = new Bank(1234567);
        Kunde k1 = new Kunde("Daniel", "Kujawa", "Bärlin", LocalDate.of(2000, 7, 12));
        Kunde k2 = new Kunde("Nico", "Froelich", "Rathenow", LocalDate.of(2000, 12, 13));
        Geldbetrag neuEinsteigerBonus = new Geldbetrag(500);


        System.out.println("--- Aktien-Testprogramm gestartet ---");


        // Aktien anlegen
        Aktie apple = new Aktie("APPLE",120.50);
        Aktie tesla = new Aktie("TESLA", 89.60);

        System.out.println("Anfängliche Aktienkurse:");
        System.out.println("APPLE: " + String.format("%.2f", apple.getKurs()));
        System.out.println("TESLA: " + String.format("%.2f", tesla.getKurs()));
        System.out.println("------------------------------------");

        // Aktiendepo anlegen
        Aktienkonto a1 = new Aktienkonto(k1, 10000001L); // Kontonummer übergeben
        a1.einzahlen(neuEinsteigerBonus);


        System.out.println("\nAnfängliche Kontostände:");
        System.out.println(a1);
        System.out.println("------------------------------------");

        // 2 Aktien sollen gekauft werden bei einen Höchstwert von 118
        System.out.println("\nPlatziere Kaufauftrag für APPLE (2 Stück, max. 116.0 EUR)...");
        a1.kaufauftrag(apple.getWkn(),2,new Geldbetrag(117.0));



        a1.shutdownExecutor();


        // Stoppe die Kursänderungen für alle Aktien
        apple.anhalten();
        tesla.anhalten();
    }
}