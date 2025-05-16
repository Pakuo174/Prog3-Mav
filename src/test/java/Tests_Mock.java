import bankprojekt.geld.Waehrung;
import bankprojekt.verarbeitung.Geldbetrag;
import bankprojekt.verarbeitung.Konto;
import bankprojekt.verwaltung.Bank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Tests_Mock {


    private Bank bank;

    @BeforeEach
    void setUp() {
        bank = new Bank(12345678L);
    }

    @Test
    void MockKontoEinfügen(){

        // Set UP
        Konto kontoMock = mock();
        when(kontoMock.getKontonummer()).thenReturn(5L);

        // Exercise - erstellt ein mockKonto
        long kontonummer = bank.mockEinfuegen(kontoMock);

        // Verify - testen ob MockKonto existiert
        assertTrue(bank.getAlleKontonummern().contains(kontonummer), "Kontonummer sollte enthalten sein");

    }

    @Test
    void KontostandExistiert(){

        // Set UP
        Konto kontoMock = mock();
        Geldbetrag mockGeldbetrag = new Geldbetrag(100,Waehrung.EUR);
        when(kontoMock.getKontostand()).thenReturn(mockGeldbetrag);
        when(kontoMock.getKontonummer()).thenReturn(10L);

        // Exercise - MockKonto einfügen und MockKonto auf existierends Geld testen
        long kontonummer = bank.mockEinfuegen(kontoMock);

        Geldbetrag mockGeld = bank.getKontostand(kontonummer);

        // Verify - kontrollieren
        assertEquals(mockGeldbetrag,mockGeld);
    }

    @Test
    void KontostandExistiertNicht(){

        // Set UP
        Konto kontoMock = mock();
        when(kontoMock.getKontonummer()).thenReturn(10L);


        // Exercise - MockKonto einfügen und MockKonto auf existierends Geld testen
        long kontonummer = bank.mockEinfuegen(kontoMock);
        Geldbetrag mockGeld = bank.getKontostand(kontonummer);

        // Verify - kontrollieren ob auch nichts funktioniert
        assertEquals(null,mockGeld);
    }

}
