package bankprojekt.verwaltung;

import java.io.FileInputStream;
import java.io.IOException;
// import java.io.FileDescriptor.out; // Diese Zeile bitte entfernen, sie wird nicht benötigt

public class Bank_Stream_Main_lesen {

    public static void main(String[] args) {

        // Verwende den gleichen Pfad, den du zum Speichern verwendet hast.
        // Beispiel: dateiname = "bankdaten.ser"; wenn es im Projektstamm gespeichert wurde
        // Oder den vollständigen Pfad, den du zuvor genutzt hast (z.B. "C:/Users/Pakuo/IdeaProjects/BankDatenTest_Aufgabe10/bankdaten.ser")
        String dateiname = "C:/Users/Pakuo/IdeaProjects/Prog3_Mav/src/bankdaten.ser";

        Bank geleseneBank = null; // Variable für die eingelesene Bank

        try (FileInputStream fis = new FileInputStream(dateiname)) { // Basis-Stream für das Lesen aus der Datei
            // Die statische Methode einlesen() der Bank-Klasse aufrufen
            geleseneBank = Bank.einlesen(fis);
            System.out.println("Bankdaten erfolgreich aus '" + dateiname + "' eingelesen.");

            // Überprüfe die eingelesenen Daten
            System.out.println("\n--- Eingelesene Bankdaten ---");
            System.out.println("Bankleitzahl: " + geleseneBank.getBankleitzahl());
            System.out.println("Alle Konten der eingelesenen Bank:");
            System.out.println(geleseneBank.getAlleKonten());

        } catch (IOException e) {
            // Fehler beim Öffnen oder Lesen der Datei (bevor einlesen() aufgerufen wird)
            System.err.println("Fehler beim Zugriff auf die Datei: " + e.getMessage());
            e.printStackTrace();
            // In diesem Fall wird keine Bank eingelesen, also bleibt geleseneBank null
            // oder du kannst hier eine leere Bank erstellen, falls gewünscht
        }
    }
}