package Nullstelle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.function.DoubleUnaryOperator; // funktionelles Interface Eingabe : double , Ausgabe : double

/**
 * Aufrufabfolge
 * - Erstellung eines Objekt vom Typ DoubleUnaryOperator
 * - Dieses Objekt bekommt beim erzeugen den LambdaAusdruck zugewiesen
 * - später innerhalb der Methode für die NullstellenSuche f.applyAsDouble aufrufen
 *      , um die Funktion anzuwenden mit z.B. a,b oder mid
 */
public class Tests_NullstellenSuche {

    @Test
    void testEinfacheFunktion(){

        DoubleUnaryOperator f = x -> x * x -25; // mittels Lamda das Interface erweitern obwohl eigentlich nur ein double übergeben und ausgegben wird
        System.out.println(NullstellenSuche.findeNullstelle(f,0,10));
        double nullstelle = NullstellenSuche.findeNullstelle(f, 0, 10);
        assertEquals(5.0, nullstelle, 0.01);
    }
    @Test
    void testEinfacheFunktion2(){

        DoubleUnaryOperator f = x -> x * x +x -100; // mittels Lamda das Interface erweitern obwohl eigentlich nur ein double übergeben und ausgegben wird
        System.out.println(NullstellenSuche.findeNullstelle(f,0,10));

    }


    @Test
    void testFunktion2(){
        DoubleUnaryOperator f = x -> Math.exp(3* x) -7;
        System.out.println(NullstellenSuche.findeNullstelle(f,0,10));
        double nullstelle = NullstellenSuche.findeNullstelle(f, 0, 10);
        assertEquals(Math.log(7)/3, nullstelle, 0.01);
    }


    @Test
    public void testFunktion3() {
        DoubleUnaryOperator f = x -> Math.sin(x * x) - 0.5;
        double nullstelle = NullstellenSuche.findeNullstelle(f, 0.5, 1.5);
        assertEquals(0.75, nullstelle, 0.05); // grobe Annäherung
    }
}
