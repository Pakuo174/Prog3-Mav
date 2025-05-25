package Übungsstunde.uebungsveranstaltung7;

import java.time.LocalDate;
import java.util.*;

import bankprojekt.verarbeitung.Geldbetrag;
import bankprojekt.verarbeitung.Girokonto;
import bankprojekt.verarbeitung.Konto;
import bankprojekt.verarbeitung.Kunde;
/**
 * Klasse mit Übungen zu Streams
 */
public class StreamsUebung {

    /**
     * Übungen zu Streams
     * @param args wird nicht verwendet
     */
    public static void main(String[] args) {
        Kunde hans = new Kunde("Hans", "Meier", "Unterm Regenbogen 19", LocalDate.of(1990, 1, 5));
        Kunde otto = new Kunde("Otto", "Kar", "Hoch über den Wolken 7", LocalDate.of(1992, 2, 25));
        Kunde sabrina = new Kunde("Sabrina", "August", "Im Wald 15", LocalDate.of(1988, 3, 21));
        Konto eins = new Girokonto(hans, 123, Geldbetrag.NULL_EURO);
        eins.einzahlen(new Geldbetrag(100));
        Konto zwei = new Girokonto(otto, 234, Geldbetrag.NULL_EURO);
        zwei.einzahlen(new Geldbetrag(200));
        Konto drei = new Girokonto(sabrina, 333, Geldbetrag.NULL_EURO);
        drei.einzahlen(new Geldbetrag(100));
        Konto vier = new Girokonto(sabrina, 432, Geldbetrag.NULL_EURO);
        vier.einzahlen(new Geldbetrag(500));
        Konto fuenf = new Girokonto(otto, 598, Geldbetrag.NULL_EURO);
        fuenf.einzahlen(new Geldbetrag(600));

        Map<Long, Konto> kontenmap = new HashMap<Long, Konto>();
        kontenmap.put(123L, eins);
        kontenmap.put(234L, zwei);
        kontenmap.put(333L, drei);
        kontenmap.put(432L, vier);
        kontenmap.put(598L, fuenf);

        //Liste aller Kunden ohne doppelte:

       /*
       List<Kunde> aEinfach = null;

        for (Konto k : kontenmap.values()){
            aEinfach.add(k.getInhaber());
        }
        */


        List<Kunde> a = kontenmap.values().stream()
                .map((konto) -> konto.getInhaber())
                .distinct()
                .toList();
        System.out.println("a: " + a + "\n");
        //System.out.println("-----------------");

        //Liste aller Kunden, sortiert nach ihrem Kontostand (d.h. ein Kunde kann mehrfach vorkommen):

/*
        //2 Konten miteinander vergelich und dann sortieren - Collection.sort verwenden
        List<Konto> kontenListe = new LinkedList<>(kontenmap.values());
        kontenListe.sort((k1, k2) -> k1.getKontostand().compareTo(k2.getKontostand()) );
        List<Kunde> aSortiert = new LinkedList<>();
        for (Konto k : kontenListe){
            aSortiert.add(k.getInhaber());
        }
        System.out.println("\n sortiert nach Kontostand \n"+aSortiert);
 */
        List<Kunde> b = kontenmap.values().stream()
                .sorted((k1, k2) -> k1.getKontostand().compareTo(k2.getKontostand()) )
               // .map((konto -> konto.getInhaber()))
                .map(konto -> new Kunde(konto.getInhaber().getVorname(),konto.getInhaber().getNachname(),konto.getInhaber().getAdresse(),konto.getInhaber().getGeburtstag()))
                        .toList();
        System.out.println("b: " + b);
        System.out.println("-----------------");

        //fängt mindestens ein Kunde mit 'A' an?
        //boolean c =
        //System.out.println("c: " + c);
        System.out.println("-----------------");

        //alle Kundennamen in einem String:
        //String d =
        //System.out.println("d: " + d);
        System.out.println("-----------------");

        //Haben alle Kunden im Jahr 1990 Geburtstag?
        //boolean e =
        //System.out.println("e: " + e);
        System.out.println("-----------------");

        //Wie viele verschiedene Kunden gibt es?
        //long f =
        //System.out.println("f: " + f);
        System.out.println("-----------------");

        //Konten aller Kunde über 35 sperren:
        //System.out.println("g: " + kontenmap);
        System.out.println("-----------------");

        //Map, die zu jedem Kunden alle seine Konten auflistet:
        //Map<Kunde, List<Konto>> h =
        //System.out.println("h: " + h);
        System.out.println("-----------------");

    }
}
