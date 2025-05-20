import bankprojekt.geld.Waehrung;
import bankprojekt.verarbeitung.*;
import bankprojekt.verwaltung.Bank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class Tests_Mock {


    Bank bank;
    Kunde kunde, kundeGesperrt, kundeEmpfang;

    long nrGiro, nrGiroGesperrt, nrGiroNichtGedeckt, nrGiroEmpfang;
    UeberweisungsfaehigesKonto kontoGiro, kontoGiroGesperrt, kontoGiroNichtGedeckt;


    @BeforeEach
    void setUp() {
        bank = new Bank(12345678L);
    }

    @Test
    void MockKontoEinfügen() {

        // Set UP
        Konto kontoMock = mock();
        when(kontoMock.getKontonummer()).thenReturn(5L);

        // Exercise - erstellt ein mockKonto
        long kontonummer = bank.mockEinfuegen(kontoMock);

        // Verify - testen ob MockKonto existiert
        assertTrue(bank.getAlleKontonummern().contains(kontonummer), "Kontonummer sollte enthalten sein");
    }

    @Test
    void KontostandExistiert() {

        // Set UP
        Konto kontoMock = mock();
        Geldbetrag mockGeldbetrag = new Geldbetrag(100, Waehrung.EUR);
        when(kontoMock.getKontostand()).thenReturn(mockGeldbetrag);
        when(kontoMock.getKontonummer()).thenReturn(10L);

        // Exercise - MockKonto einfügen und MockKonto auf existierends Geld testen
        long kontonummer = bank.mockEinfuegen(kontoMock);

        Geldbetrag mockGeld = bank.getKontostand(kontonummer);

        // Verify - kontrollieren
        assertEquals(mockGeldbetrag, mockGeld);
    }

    @Test
    void KontostandExistiertNicht() {
        // Set UP
        Konto kontoMock = mock();
        when(kontoMock.getKontonummer()).thenReturn(10L);


        // Exercise - MockKonto einfügen und MockKonto auf existierends Geld testen
        long kontonummer = bank.mockEinfuegen(kontoMock);
        Geldbetrag mockGeld = bank.getKontostand(kontonummer);

        // Verify - kontrollieren ob auch nichts funktioniert
        assertEquals(null, mockGeld);
    }


    @Test
    void ÜberweisenNormal() throws GesperrtException {


        // Set UP
        Girokonto kontoMock1 = mock(Girokonto.class);
        Girokonto kontoMock2 = mock(Girokonto.class);


        when(kontoMock1.getKontonummer()).thenReturn(1L);
        Geldbetrag mockGeldbetrag1 = new Geldbetrag(100, Waehrung.EUR);
        when(kontoMock1.getKontostand()).thenReturn(mockGeldbetrag1);

        when(kontoMock2.getKontonummer()).thenReturn(2L);
        Geldbetrag mockGeldbetrag2 = new Geldbetrag(100, Waehrung.EUR);
        when(kontoMock2.getKontostand()).thenReturn(mockGeldbetrag2);


        when(kontoMock1.getInhaber()).thenReturn(new Kunde("Max", "Mustermann", "Dies ist kein Wert null Straße", LocalDate.now()));
        when(kontoMock2.getInhaber()).thenReturn(new Kunde("Erika", "Musterfrau", "nullte Weg 000", LocalDate.now()));

        when(kontoMock1.ueberweisungAbsenden(
                ArgumentMatchers.any(Geldbetrag.class),
                anyString(),
                anyLong(),
                anyLong(),
                anyString()
        )).thenReturn(true);


        // Exercise - MockKonto einfügen und MockKonto auf existierends Geld testen
        long kontonummer1 = bank.mockEinfuegen(kontoMock1);
        long kontonummer2 = bank.mockEinfuegen(kontoMock2);


        Geldbetrag uebBetrag = new Geldbetrag(50, Waehrung.EUR);
        bank.geldUeberweisen(kontonummer1, kontonummer2, uebBetrag, "Schulden");


        //Verify
        verify(kontoMock1).ueberweisungAbsenden(
                eq(uebBetrag),
                eq("Musterfrau, Erika"),
                eq(kontonummer2),
                eq(bank.getBankleitzahl()),
                eq("Schulden")
        );

        verify(kontoMock2).ueberweisungEmpfangen(
                eq(uebBetrag),
                eq("Mustermann, Max"),
                eq(kontonummer1),
                eq(bank.getBankleitzahl()),
                eq("Schulden")
        );
    }

    @Test
    public void testUeberweisungMitNullBetrag() throws Exception {
        // Set Up
        Girokonto kontoMock1 = mock(Girokonto.class);
        Girokonto kontoMock2 = mock(Girokonto.class);

        when(kontoMock1.getKontonummer()).thenReturn(1L);
        when(kontoMock2.getKontonummer()).thenReturn(2L);

        when(kontoMock1.getInhaber()).thenReturn(
                new Kunde("Max", "Mustermann", "Straße", LocalDate.now()));
        when(kontoMock2.getInhaber()).thenReturn(
                new Kunde("Erika", "Musterfrau", "Weg", LocalDate.now()));

        long kontonummer1 = bank.mockEinfuegen(kontoMock1);
        long kontonummer2 = bank.mockEinfuegen(kontoMock2);

        // Exercise & Assert (klassisch)
        try {
            bank.geldUeberweisen(kontonummer1, kontonummer2, null, "Zweck");
            fail("Exception ist nicht aufgetreten");
        } catch (IllegalArgumentException ex) {

        }
        assertThrows(IllegalArgumentException.class, () ->  bank.geldUeberweisen(kontonummer1, kontonummer2, null, "Zweck"));

        // Verify: Überweisungsmethoden dürfen nicht aufgerufen -> never als VerifivationMode bestätigt dies
        verify(kontoMock1, atMost(0)).ueberweisungAbsenden(any(), anyString(), anyLong(), anyLong(), anyString());
        verify(kontoMock2, never()).ueberweisungEmpfangen(any(), anyString(), anyLong(), anyLong(), anyString());
    }

    @Test
    public void testUeberweisungMitNegativBetrag() throws Exception {

        // Set Up
        Girokonto kontoMock1 = mock(Girokonto.class);
        Girokonto kontoMock2 = mock(Girokonto.class);

        when(kontoMock1.getKontonummer()).thenReturn(1L);
        when(kontoMock2.getKontonummer()).thenReturn(2L);

        when(kontoMock1.getInhaber()).thenReturn(
                new Kunde("Max", "Mustermann", "Straße", LocalDate.now()));
        when(kontoMock2.getInhaber()).thenReturn(
                new Kunde("Erika", "Musterfrau", "Weg", LocalDate.now()));

        long kontonummer1 = bank.mockEinfuegen(kontoMock1);
        long kontonummer2 = bank.mockEinfuegen(kontoMock2);

        // Exercise
        Geldbetrag uebBetrag = new Geldbetrag(-50, Waehrung.EUR);
        try {
            bank.geldUeberweisen(kontonummer1, kontonummer2, uebBetrag, "Zweck");
        } catch (IllegalArgumentException ex) {

        }


        // Verify: Überweisungsmethoden dürfen nicht aufgerufen -> never als VerifivationMode bestätigt dies
        verify(kontoMock1, atMost(0)).ueberweisungAbsenden(any(), anyString(), anyLong(), anyLong(), anyString());
        verify(kontoMock2, atMost(0)).ueberweisungEmpfangen(any(), anyString(), anyLong(), anyLong(), anyString());
    }

    @Test
    void KontoExisitiertNicht() throws GesperrtException {
        // Set Up
        Girokonto kontoMock1 = mock(Girokonto.class);
        Girokonto kontoMock2 = mock(Girokonto.class);

        when(kontoMock1.getKontonummer()).thenReturn(1L);
        when(kontoMock2.getKontonummer()).thenReturn(2L);

        when(kontoMock1.getInhaber()).thenReturn(
                new Kunde("Max", "Mustermann", "Straße", LocalDate.now()));
        when(kontoMock2.getInhaber()).thenReturn(
                new Kunde("Erika", "Musterfrau", "Weg", LocalDate.now()));

        // nur kontonummer 1 wird der Bank hinzugefügt
        long kontonummer1 = bank.mockEinfuegen(kontoMock1);


        // Exercise
        Geldbetrag uebBetrag = new Geldbetrag(50, Waehrung.EUR);
        try {
            bank.geldUeberweisen(kontonummer1, 2L, uebBetrag, "Zweck");
        } catch (IllegalArgumentException ex) {
            // Exception ist da
        }

        verify(kontoMock1, atMost(0)).ueberweisungAbsenden(any(), anyString(), anyLong(), anyLong(), anyString());
    }
    @Test
    void ÜberweisungZweckIstNull() throws GesperrtException {
        // Set Up
        Girokonto kontoMock1 = mock(Girokonto.class);
        Girokonto kontoMock2 = mock(Girokonto.class);

        when(kontoMock1.getKontonummer()).thenReturn(1L);
        when(kontoMock2.getKontonummer()).thenReturn(2L);

        when(kontoMock1.getInhaber()).thenReturn(
                new Kunde("Max", "Mustermann", "Straße", LocalDate.now()));
        when(kontoMock2.getInhaber()).thenReturn(
                new Kunde("Erika", "Musterfrau", "Weg", LocalDate.now()));

        long kontonummer1 = bank.mockEinfuegen(kontoMock1);
        long kontonummer2 = bank.mockEinfuegen(kontoMock2);

        // Exercise
        Geldbetrag uebBetrag = new Geldbetrag(50, Waehrung.EUR);
        try {
            bank.geldUeberweisen(kontonummer1,kontonummer2,uebBetrag,null);
        } catch (IllegalArgumentException e) {

        }

        // Verify
        verify(kontoMock1, atMost(0)).ueberweisungAbsenden(any(), anyString(), anyLong(), anyLong(), anyString());
    }

    // ist es möglich 0 Euro zu überweisen
    @Test
    void ÜberweisungMit0Euro() throws GesperrtException {
        // Set Up
        Girokonto kontoMock1 = mock(Girokonto.class);
        Girokonto kontoMock2 = mock(Girokonto.class);

        when(kontoMock1.getKontonummer()).thenReturn(1L);
        when(kontoMock2.getKontonummer()).thenReturn(2L);

        when(kontoMock1.getInhaber()).thenReturn(
                new Kunde("Max", "Mustermann", "Straße", LocalDate.now()));
        when(kontoMock2.getInhaber()).thenReturn(
                new Kunde("Erika", "Musterfrau", "Weg", LocalDate.now()));

        long kontonummer1 = bank.mockEinfuegen(kontoMock1);
        long kontonummer2 = bank.mockEinfuegen(kontoMock2);

        // Exercise
        Geldbetrag uebBetrag = new Geldbetrag(0, Waehrung.EUR);
        try {
            bank.geldUeberweisen(kontonummer1,kontonummer2,uebBetrag,"Konto 2 will sich eine Schoki holen");
        } catch (IllegalArgumentException e) {

        }

        // Verify
        verify(kontoMock1, atMost(1)).ueberweisungAbsenden(any(), anyString(), anyLong(), anyLong(), anyString());
        verify(kontoMock2, atMost(1)).ueberweisungEmpfangen(any(), anyString(), anyLong(), anyLong(), anyString());

    }

    @Test
    void GesperrtesKonto() throws GesperrtException {
// Set Up
        Girokonto kontoMock1 = mock(Girokonto.class);
        Girokonto kontoMock2 = mock(Girokonto.class);

        when(kontoMock1.getKontonummer()).thenReturn(1L);
        when(kontoMock2.getKontonummer()).thenReturn(2L);

        when(kontoMock1.getInhaber()).thenReturn(
                new Kunde("Max", "Mustermann", "Straße", LocalDate.now()));
        when(kontoMock2.getInhaber()).thenReturn(
                new Kunde("Erika", "Musterfrau", "Weg", LocalDate.now()));

        long kontonummer1 = bank.mockEinfuegen(kontoMock1);
        long kontonummer2 = bank.mockEinfuegen(kontoMock2);

        doThrow(new GesperrtException(kontonummer1)).when(kontoMock1).ueberweisungAbsenden(
                any(Geldbetrag.class),
                anyString(),
                anyLong(),
                anyLong(),
                anyString()
        );

        // Exercise
        Geldbetrag uebBetrag = new Geldbetrag(50, Waehrung.EUR);
        try {
            bank.geldUeberweisen(kontonummer1,kontonummer2,uebBetrag,"Konto 2 will sich eine Schoki holen");
        } catch (GesperrtException e) {

        }
        // Verify
        verify(kontoMock1, atMost(1)).ueberweisungAbsenden(any(), anyString(), anyLong(), anyLong(), anyString());
        verify(kontoMock2, atMost(0)).ueberweisungEmpfangen(any(), anyString(), anyLong(), anyLong(), anyString());


    }



}
