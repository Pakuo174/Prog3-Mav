import mathematik.Intervall;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Intervall_Test {

    @Test
    void intervallErstellen(){
        Intervall <Integer> i = new Intervall(0,5);
        System.out.println(i.getUntereGrenze());
        System.out.println(i.getObereGrenze());
    }

    @Test
    void intervallErstellen2(){
        Intervall <String> i = new Intervall("Apfel","Birne");
        System.out.println(i.getUntereGrenze());
        System.out.println(i.getObereGrenze());
    }
    @Test
    void intervallEnthaelt1(){
        Intervall <Integer> i = new Intervall(0,5);

        System.out.println(i.enthaelt(7));

    }

    @Test
    void intervallEnthaelt2(){
        Intervall <Integer> i = new Intervall(0,5);

        assertFalse(i.enthaelt(6));

    }
    @Test
    void intervallEnthaelt3(){
        Intervall <String> i = new Intervall("Apfel","Mango");

        assertTrue(i.enthaelt("Mandarine"));

    }

    @Test
    void intervallEnthaelt4(){
        Intervall <String> i = new Intervall("Apfel","Mango");

        assertFalse(i.enthaelt("Zitrone"));

    }

}
