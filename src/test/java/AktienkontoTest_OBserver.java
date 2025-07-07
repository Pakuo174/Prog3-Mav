import bankprojekt.geld.Waehrung;
import bankprojekt.verarbeitung.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class AktienkontoTest_OBserver {

    private Kunde testKunde1;
    private Kunde testKunde2;
    private Aktie apple;
    private Aktie tesla;
    private Aktienkonto aktienkonto1;

    private AktienkontoBeobachter mockBeobachter;

    @BeforeEach
    void setUp(){
        testKunde1 = new Kunde("Daniel", "Kujawa", "Bärlin", LocalDate.of(2000, 7, 12));
        testKunde2 = new Kunde("Nico", "Froelich", "Rathenow", LocalDate.of(2000, 12, 13));

        apple = new Aktie("APPLE",new Geldbetrag(120));
        tesla = new Aktie("TESLA", new Geldbetrag(800));

        aktienkonto1 = new Aktienkonto(testKunde1,12345);

        // Ein bisschen Geld einzahlen, damit Käufe möglich sind
        aktienkonto1.einzahlen(new Geldbetrag(10000, Waehrung.EUR));

        // Erstelle den Mock des Beobachters
        mockBeobachter = mock(AktienkontoBeobachter.class);
        // registriere den Mock-Beobachter bei dem Aktienkonto
        aktienkonto1.anmeldenBeobachter(mockBeobachter);
    }

    // In Test_AktienkontoObserver.java
    @AfterEach
    void tearDown() {
        // Sicherstellen, dass die Executors heruntergefahren werden
        Aktienkonto.shutdownExecutor();
        Aktie.shutdownKursAenderungService(); // Diese sollte existieren, siehe Problem 2

        // Statische Map in Aktie leeren, damit andere Tests nicht fehlschlagen
        Aktie.clearAlleAktien(); //
    }


    @Test
    void testLoggerErstellen() throws GesperrtException {

        System.out.println("Kontostand: " + aktienkonto1.getKontostand());
        System.out.println("Depot: " + aktienkonto1.aktiendepot);

        AktienkontoLogger logger1 = new AktienkontoLogger("Depot Logger 1");
        // AktienkontoLogger logger2 = new AktienkontoLogger("Depot Logger 2");
        aktienkonto1.anmeldenBeobachter(logger1);
       // aktienkonto1.anmeldenBeobachter(logger2);

        aktienkonto1.addAktienToDepot(apple,4);
        aktienkonto1.addAktienToDepot(tesla,2);
    }
    @Test
    void testBeobachterAufrufBeiAddAktienToDepot() throws GesperrtException {
        int anzahlApple = 10;

        // Aktion: Aktien direkt zum Depot hinzufügen

        aktienkonto1.addAktienToDepot(apple, anzahlApple);

        // Verifiziere, dass die update-Methode des Mocks korrekt aufgerufen wurde
        verify(mockBeobachter, times(1)).aktualisieren(
                eq(aktienkonto1),
                eq(apple),
                eq(anzahlApple),
                eq(anzahlApple) // Neuer Bestand sollte 10 sein
        );

        // Führe eine weitere Hinzufügung aus, um zu sehen, ob der Zähler funktioniert
        int anzahlApple2 = 5;
        aktienkonto1.addAktienToDepot(apple, anzahlApple2);

        // Verifiziere den zweiten Aufruf
        verify(mockBeobachter, times(1)).aktualisieren(
                eq(aktienkonto1),
                eq(apple),
                eq(anzahlApple2),
                eq(anzahlApple + anzahlApple2) // Neuer Bestand sollte 15 sein
        );

    }

    @Test
    void testBeobachterAufrufBeiKaufauftrag() throws InterruptedException, ExecutionException, TimeoutException {


        int anzahlTesla = 5;
        // Wähle einen Höchstpreis, der garantiert über dem aktuellen Kurs liegt, damit der Kauf sofort erfolgt
        Geldbetrag hoechstpreis = new Geldbetrag(10000, Waehrung.EUR); // Weit über dem erwarteten Kurs

        // Aktion: Kaufauftrag ausführen
        Future<Geldbetrag> kaufFuture = aktienkonto1.kaufauftrag("TESLA", anzahlTesla, hoechstpreis);

        // Hier warten bis der asynchrone Kauf abgeschlossen ist
        Geldbetrag bezahlterPreis = kaufFuture.get(5, TimeUnit.SECONDS); // Warte maximal 5 Sekunden

        // Verifiziere, dass die update-Methode des Mocks korrekt aufgerufen wurde
        verify(mockBeobachter, times(1)).aktualisieren(
                eq(aktienkonto1),
                eq(tesla), // Dies ist die referenzierte Aktie, die du erstellt hast
                eq(anzahlTesla),
                eq(anzahlTesla) // Neuer Bestand nach dem Kauf
        );
    }




}
