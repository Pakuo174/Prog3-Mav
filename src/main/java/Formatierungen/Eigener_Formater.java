package Formatierungen;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Eigener_Formater {


    public void schreibeFormatierteDatenInDatei(String dateiName, long ganzeZahl, LocalDate datum, double eingabeNachkommaZahl, LocalTime jetzt) {

        try (FileWriter fw = new FileWriter(dateiName);
             PrintWriter pw = new PrintWriter(fw)) {

            // 1. Ganze Zahl (vom Benutzer eingegeben) mit Standardformatierung

            pw.println(ganzeZahl);

            //2. Ganze Zahl mit insg. 12 Stellen geschrieben werden, falls zu kurz mit 0 auffüllen
            pw.printf("Zahl mit insgesammt 12 Stellen %012d%n", ganzeZahl);

            //3. Ganze Zahl mit Vorzeichen und Tausendertrennzeichen geschrieben werden
            pw.printf("%+,d%n",ganzeZahl);

            //4. die Zahl hexadezimal geschreiben werden, wobei A bis F großgeschrieben werden soll
            pw.printf("Hexadezimal : %X%n", ganzeZahl);

            // 5. Nachkommazahl im Standart
            pw.println("Nachkommazahl"+ eingabeNachkommaZahl);

            //6. Zahl mit Vorzeichen und mit 4 Nachkommastellen
            pw.printf("Nachkommazahl mit Vorzeichen und 4 Dezimalstellen: %+.4f%n",eingabeNachkommaZahl);

            //7. wissenschaftliche Darstellung
            pw.printf("Wissenschaftliche Darstellung: %e%n", eingabeNachkommaZahl);

            // 8. Achte Zeile: Zahl mit 2 Nachkommastellen und Punkt als Dezimaltrennzeichen (USA-Format)
            pw.printf(Locale.US, "Nachkommazahl (US-Format, 2 Dezimalstellen): %.2f%n", eingabeNachkommaZahl);

            //9 aktuelle Datum mit zweistelliger Tages und Monats und Jahreszahl und dem voll ausgeschriebenen Wochentagsnamen in französischer Sprache
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d. MMMM yyyy (EE)", Locale.GERMAN);
            pw.println("Aktuelles Datum : " + datum.format(formatter)); // Datum formatieren und ausgeben

            // 10 zweistelliger Tages- und Monats- und Jahreszahl und dem voll ausgeschriebenen Wochentagsnamen in französischer Sprache.
            DateTimeFormatter frenchFormatter = DateTimeFormatter.ofPattern("dd.MM.yy EEEE", Locale.FRENCH);
            pw.println("Aktuelles Datum (Französisch): " + datum.format(frenchFormatter));

            // 11. Aktuelle Uhrzeit im englischen Format (Stunde:Minute am/pm ohne führende Nullen bei der Stundenzahl).
            DateTimeFormatter englishTimeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.US);
            pw.println("Aktuelle Uhrzeit (Englisch AM/PM): " + jetzt.format(englishTimeFormatter));


            System.out.println("Daten erfolgreich in '" + dateiName + "' geschrieben.");

        } catch (IOException e) {
            // Eine RuntimeException werfen, falls ein Fehler beim Schreiben auftritt
            throw new RuntimeException("Fehler beim Schreiben in die Datei: " + e.getMessage(), e);
        }
    }
}