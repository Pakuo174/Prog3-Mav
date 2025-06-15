package bankprojekt.verwaltung;

import bankprojekt.geld.Waehrung;
import bankprojekt.verarbeitung.Aktienkonto;
import bankprojekt.verarbeitung.Geldbetrag;
import bankprojekt.verarbeitung.Kunde;

import java.io.*;
import java.time.LocalDate;

import static java.io.FileDescriptor.out;

public class Bank_StreamIO_main {

    public static void main(String[] args) throws IOException {

        Bank bank = new Bank(12345);

        Kunde k1 = new Kunde("Daniel", "Kujawa", "Bärlin", LocalDate.of(2000, 7, 12));
        Kunde k2 = new Kunde("Nico", "Froelich", "Rathenow", LocalDate.of(2000, 12, 13));


        bank.AktienKontoErstellen(k1);
        bank.geldEinzahlen(1,new Geldbetrag(500));

        bank.AktienKontoErstellen(k2);
        bank.geldEinzahlen(2,new Geldbetrag(504240));




        // Dateiname für die Serialisierung
        String dateiname = "C:/Users/Pakuo/IdeaProjects/Prog3_Mav/src/bankdaten.ser";



        try (FileOutputStream fos = new FileOutputStream(dateiname)) {
            // Den FileOutputStream an die speichern-Methode der Bank übergeben
            bank.speichern(fos);
            System.out.println("Bankdaten erfolgreich in '" + dateiname + "' gespeichert.");
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern der Bankdaten: " + e.getMessage());
            e.printStackTrace();
        }
    }

    }

