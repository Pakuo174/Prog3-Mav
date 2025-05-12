import mathematik.Intervall;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

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

    @Test
    void intervallSchnitt1(){
        Intervall <Integer> i = new Intervall(0,5);
        Intervall <Integer> i2 = new Intervall<>(2,7);

        Intervall<Integer> schnitt = i.schnitt(i2);

        assertFalse(schnitt.enthaelt(1));
        assertTrue(schnitt.enthaelt(3));
        assertTrue(schnitt.enthaelt(4));
        assertTrue(schnitt.enthaelt(5));

    }

    // Intervall sollte von Kuerbis bis Mango gehen
    @Test
    void intervallSchnitt2(){
        Intervall <String> i = new Intervall("Apfel","Mango");
        Intervall <String> i2 = new Intervall("Kuerbis","Mango");

        Intervall<String> schnitt = i.schnitt(i2);

        assertFalse(schnitt.enthaelt("Zitrone"));
        assertTrue(schnitt.enthaelt("Mango"));
        assertTrue(schnitt.enthaelt("Kuerbis"));

    }
    @Test
    void intervallSchnitt3() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse("2000-01-01");
        Date date2 = sdf.parse("2030-12-31");
        Date date3 = sdf.parse("2010-01-01");

        Intervall<Date> i = new Intervall<>(date1, date2);
        Intervall<Date> i2 = new Intervall<>(date3, date2);

        // Führe den Schnitt durch
        Intervall<Date> schnitt = i.schnitt(i2);

        // Teste, ob das Intervall den 1. Januar 2000 enthält
        assertFalse(schnitt.enthaelt(sdf.parse("2000-01-01")));
        assertTrue(schnitt.enthaelt(sdf.parse("2015-01-01")));

    }
    @Test
    void intervallSchnitt4() throws ParseException {

        Time time1 = Time.valueOf("05:00:00");
        Time time2 = Time.valueOf("10:00:00");
        Time time3 = Time.valueOf("08:00:00");
        Time time4 = Time.valueOf("15:00:00");

        Intervall<Time> i1 = new Intervall<>(time1, time2);
        Intervall<Time> i2 = new Intervall<>(time3, time4);

        Intervall<Time> schnitt = i1.schnitt(i2);

        assertNotNull(schnitt);
        assertEquals(Time.valueOf("08:00:00"), schnitt.getUntereGrenze());
        assertEquals(Time.valueOf("10:00:00"), schnitt.getObereGrenze());

        assertFalse(schnitt.enthaelt(Time.valueOf("07:00:00")));
        assertTrue(schnitt.enthaelt(Time.valueOf("08:30:00")));


    }

}
