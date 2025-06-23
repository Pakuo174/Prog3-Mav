package Formatierungen;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Scanner; // Für Benutzereingaben

public class FormaterMain_tests {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Eigener_Formater formater = new Eigener_Formater();

        try {
            System.out.print("Bitte geben Sie eine ganze Zahl ein: ");
            long eingegebeneGanzeZahl = scanner.nextLong(); // Bleibt long für ganze Zahlen

            System.out.print("Bitte geben Sie eine Zahl mit Nachkommaanteil ein: ");
            double eingegebeneNachkommaZahl = scanner.nextDouble(); // <<<<<< NEU: Eingabe für Gleitkommazahl

            // Beispiel für eine Gleitkommazahl und ein Datum
            double beispielGleitkommazahl = 123.45678;
            LocalDate aktuellesDatum = LocalDate.now();
            LocalTime jetzt = LocalTime.now();

            formater.schreibeFormatierteDatenInDatei("formatierte_daten.txt", eingegebeneGanzeZahl, aktuellesDatum, eingegebeneNachkommaZahl, jetzt);

        } catch (Exception e) {
            System.err.println("Ein Fehler ist aufgetreten: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}