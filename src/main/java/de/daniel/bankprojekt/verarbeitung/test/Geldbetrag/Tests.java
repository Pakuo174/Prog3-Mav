package de.daniel.bankprojekt.verarbeitung.test.Geldbetrag;

import de.daniel.bankprojekt.geld.Waehrung;
import de.daniel.bankprojekt.verarbeitung.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {

    @Test
    void GeldbetragTest(){
        Geldbetrag g1 = new Geldbetrag(-2.0);
        System.out.println(g1);
    }
    @Test
    void GeldbetragTest2(){
        Geldbetrag g1 = new Geldbetrag(1.0,Waehrung.EUR);
        Geldbetrag newG = g1.umrechnen(Waehrung.ESCUDO);
        System.out.println(newG);
    }

    @Test
    void minusTest(){
        Geldbetrag g1 = new Geldbetrag(5.0);
        Geldbetrag g2 = new Geldbetrag(2.0);

        double newG = g1.minus(g2).getBetrag();
        assertEquals(newG,3.0);

    }
    @Test
    void malTest(){
        Geldbetrag g1 = new Geldbetrag(5.0);

        Geldbetrag newG = g1.mal(2.0);
        System.out.println(newG.toString());
    }

    //___________________Tests für Konto________________________________

    @Test
    void kontoErstellung_GeldEinzahlen(){

        // Konto wird erstellt ohne Geld aber mit 100 Euro dispo
        Girokonto k1 = new Girokonto(new Kunde("Daniel","Kujawa","Bärlin", LocalDate.ofEpochDay(2000-07-12)),10552,new Geldbetrag(100,Waehrung.EUR));
        /**
         * k1.setKontostand geht nicht da protectet !!!
         * deshalb Methode einzahlen verwenden der Konto Klasse
         * * diese brauchht ein Geldbetrag Objekt zum einzahlen und ruft dann "plus" der Klasse Geldbetrag auf
         * * neuer Geldbetrag wird dann mittels return zurückgegeben und den Konto zugewiesen als neuer Wert
          */
        k1.einzahlen(new Geldbetrag(52,Waehrung.EUR));
        System.out.println(k1);
    }

    @Test
    void kontoErstellung_GeldAbhebenÜberDispoEURO(){
        // Konto wird erstellt ohne Geld aber mit 100 Euro dispo
        Girokonto k1 = new Girokonto(new Kunde("Daniel","Kujawa","Bärlin", LocalDate.ofEpochDay(2000-07-12)),10552,new Geldbetrag(100,Waehrung.EUR));
        k1.einzahlen(new Geldbetrag(52,Waehrung.EUR));
        System.out.println(k1);


        System.out.println("_____________-55 Euro _______________");

        try {
            k1.abheben(new Geldbetrag(55,Waehrung.EUR));
        } catch (GesperrtException e) {
            throw new RuntimeException(e);
        }
        System.out.println(k1);
    }

    //KLAPPPT NICHT !
    @Test
    void kontoErstellung_GeldAbhebenÜberDispoEscudo(){
        // Konto wird erstellt ohne Geld aber mit 100 Euro dispo
        Girokonto k1 = new Girokonto(new Kunde("Daniel","Kujawa","Bärlin", LocalDate.ofEpochDay(2000-07-12)),10552,new Geldbetrag(100,Waehrung.EUR));
        k1.einzahlen(new Geldbetrag(52,Waehrung.EUR));
        System.out.println(k1);

        try {
            k1.abheben(new Geldbetrag (109.8269,Waehrung.ESCUDO));
        } catch (GesperrtException e) {
            throw new RuntimeException(e);
        }


        System.out.println("____________- 109.8269 Escudo________________");
        System.out.println(k1);


    }


}
