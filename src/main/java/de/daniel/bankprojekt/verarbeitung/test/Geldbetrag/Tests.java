package de.daniel.bankprojekt.verarbeitung.test.Geldbetrag;

import de.daniel.bankprojekt.verarbeitung.Geldbetrag;
import org.junit.jupiter.api.Test;

public class Tests {

    @Test
    void GeldbetragTest(){
        Geldbetrag g1 = new Geldbetrag(-2.0);
        System.out.println(g1);
    }
    @Test
    void GeldbetragTest2(){
        Geldbetrag g1 = new Geldbetrag(5.0);
        System.out.println(g1);
    }


}
