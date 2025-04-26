package de.daniel.bank.bankprojekt.verarbeitung.test.Geldbetrag;

import bankprojekt.geld.Waehrung;
import de.daniel.bank.bankprojekt.verarbeitung.Geldbetrag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestGeldbetrag {

    @Test
    void String_EuroINFranc(){

        Geldbetrag g1 = new Geldbetrag(100, Waehrung.EUR);

        //assertEquals(g1.getBetrag(),120);

        Geldbetrag g2 = g1.umrechnen(Waehrung.FRANC);
        System.out.println(g2);
    }
    @Test
    void EuroInEscudo() {
        Geldbetrag g1 = new Geldbetrag(1, Waehrung.EUR);
        Geldbetrag g2 = g1.umrechnen(Waehrung.ESCUDO);
        assertEquals(109.8269, g2.getBetrag(), 0.0001);
    }
    @Test
    void EscudoInDobra() {
        Geldbetrag g1 = new Geldbetrag(109.8269, Waehrung.ESCUDO); // entspricht 1 EUR
        Geldbetrag g2 = g1.umrechnen(Waehrung.DOBRA);
        assertEquals(24304.7429, g2.getBetrag(), 0.01);
    }
    @Test
    void DobraInFranc() {
        Geldbetrag g1 = new Geldbetrag(24304.7429, Waehrung.DOBRA); // entspricht 1 EUR
        Geldbetrag g2 = g1.umrechnen(Waehrung.FRANC);
        assertEquals(490.92, g2.getBetrag(), 0.01);
    }




}
