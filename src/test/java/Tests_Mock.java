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
    void kontostandExistiert() {

        // Set UP
        UeberweisungsfaehigesKonto kontoMock = mock();
        Geldbetrag mockGeldbetrag = new Geldbetrag(100, Waehrung.EUR);
        when(kontoMock.getKontostand()).thenReturn(mockGeldbetrag);


        long kontonummer = bank.mockEinfuegen(kontoMock);
        when(kontoMock.getKontonummer()).thenReturn(kontonummer);


        // Exercise - MockKonto einfügen und MockKonto auf existierends Geld testen
        Geldbetrag mockGeld = bank.getKontostand(kontonummer);

        // Verify - kontrollieren
        assertEquals(mockGeldbetrag, mockGeld);
    }

    @Test
    void kontostandExistiertNicht() {
        // Set UP
        UeberweisungsfaehigesKonto kontoMock = mock();

        long kontonummer = bank.mockEinfuegen(kontoMock);
        when(kontoMock.getKontonummer()).thenReturn(kontonummer);

        // Exercise - MockKonto einfügen und MockKonto auf existierends Geld testen
        Geldbetrag mockGeld = bank.getKontostand(kontonummer);

        // Verify - kontrollieren ob auch nichts funktioniert
        assertEquals(null, mockGeld);
    }


    @Test
    void überweisenNormal() throws GesperrtException {


        // Set UP
        UeberweisungsfaehigesKonto kontoMock1 = mock(Girokonto.class);
        UeberweisungsfaehigesKonto kontoMock2 = mock(Girokonto.class);


        long kontonummer1 = bank.mockEinfuegen(kontoMock1);
        when(kontoMock1.getKontonummer()).thenReturn(kontonummer1);
        Geldbetrag mockGeldbetrag1 = new Geldbetrag(100, Waehrung.EUR);
        when(kontoMock1.getKontostand()).thenReturn(mockGeldbetrag1);

        long kontonummer2 = bank.mockEinfuegen(kontoMock2);
        when(kontoMock2.getKontonummer()).thenReturn(kontonummer2);
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
        Geldbetrag uebBetrag = new Geldbetrag(50, Waehrung.EUR);
        bank.geldUeberweisen(kontonummer1, kontonummer2, uebBetrag, "Schulden");


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
        UeberweisungsfaehigesKonto kontoMock1 = mock(Girokonto.class);
        UeberweisungsfaehigesKonto kontoMock2 = mock(Girokonto.class);

        long kontonummer1 = bank.mockEinfuegen(kontoMock1);
        when(kontoMock1.getKontonummer()).thenReturn(kontonummer1);

        long kontonummer2 = bank.mockEinfuegen(kontoMock2);
        when(kontoMock2.getKontonummer()).thenReturn(kontonummer2);

        when(kontoMock1.getInhaber()).thenReturn(
                new Kunde("Max", "Mustermann", "Straße", LocalDate.now()));
        when(kontoMock2.getInhaber()).thenReturn(
                new Kunde("Erika", "Musterfrau", "Weg", LocalDate.now()));


        assertThrows(IllegalArgumentException.class, () ->  bank.geldUeberweisen(kontonummer1, kontonummer2, null, "Zweck"));

        // Verify: Überweisungsmethoden dürfen nicht aufgerufen -> never als VerifivationMode bestätigt dies
        verify(kontoMock1, atMost(0)).ueberweisungAbsenden(any(), anyString(), anyLong(), anyLong(), anyString());
        verify(kontoMock2, never()).ueberweisungEmpfangen(any(), anyString(), anyLong(), anyLong(), anyString());
    }

    @Test
    public void testUeberweisungMitNegativBetrag() throws Exception {

        // Set Up
        UeberweisungsfaehigesKonto kontoMock1 = mock(Girokonto.class);
        UeberweisungsfaehigesKonto kontoMock2 = mock(Girokonto.class);

        long kontonummer1 = bank.mockEinfuegen(kontoMock1);
        when(kontoMock1.getKontonummer()).thenReturn(kontonummer1);

        long kontonummer2 = bank.mockEinfuegen(kontoMock2);
        when(kontoMock2.getKontonummer()).thenReturn(kontonummer2);

        when(kontoMock1.getInhaber()).thenReturn(
                new Kunde("Max", "Mustermann", "Straße", LocalDate.now()));
        when(kontoMock2.getInhaber()).thenReturn(
                new Kunde("Erika", "Musterfrau", "Weg", LocalDate.now()));


        // Exercise
        Geldbetrag uebBetrag = new Geldbetrag(-50, Waehrung.EUR);


        assertThrows(IllegalArgumentException.class, () ->  bank.geldUeberweisen(kontonummer1, kontonummer2, uebBetrag, "Zweck"));


        // Verify: Überweisungsmethoden dürfen nicht aufgerufen -> never als VerifivationMode bestätigt dies
        verify(kontoMock1, atMost(0)).ueberweisungAbsenden(any(), anyString(), anyLong(), anyLong(), anyString());
        verify(kontoMock2, atMost(0)).ueberweisungEmpfangen(any(), anyString(), anyLong(), anyLong(), anyString());
    }

    @Test
    void kontoExisitiertNicht() throws GesperrtException {
        // Set Up
        UeberweisungsfaehigesKonto kontoMock1 = mock(Girokonto.class);
        UeberweisungsfaehigesKonto kontoMock2 = mock(Girokonto.class);


        // nur kontonummer 1 wird der Bank hinzugefügt
        long kontonummer1 = bank.mockEinfuegen(kontoMock1);
        when(kontoMock1.getKontonummer()).thenReturn(kontonummer1);
        when(kontoMock2.getKontonummer()).thenReturn(2L);

        when(kontoMock1.getInhaber()).thenReturn(
                new Kunde("Max", "Mustermann", "Straße", LocalDate.now()));
        when(kontoMock2.getInhaber()).thenReturn(
                new Kunde("Erika", "Musterfrau", "Weg", LocalDate.now()));




        // Exercise
        Geldbetrag uebBetrag = new Geldbetrag(50, Waehrung.EUR);


        assertThrows(IllegalArgumentException.class, () ->  bank.geldUeberweisen(kontonummer1, 2L, uebBetrag, "Zweck"));

        verify(kontoMock1, atMost(0)).ueberweisungAbsenden(any(), anyString(), anyLong(), anyLong(), anyString());
    }
    @Test
    void überweisungZweckIstNull() throws GesperrtException {
        // Set Up
        UeberweisungsfaehigesKonto kontoMock1 = mock(Girokonto.class);
        UeberweisungsfaehigesKonto kontoMock2 = mock(Girokonto.class);

        long kontonummer1 = bank.mockEinfuegen(kontoMock1);
        when(kontoMock1.getKontonummer()).thenReturn(kontonummer1);

        long kontonummer2 = bank.mockEinfuegen(kontoMock2);
        when(kontoMock2.getKontonummer()).thenReturn(kontonummer2);

        when(kontoMock1.getInhaber()).thenReturn(
                new Kunde("Max", "Mustermann", "Straße", LocalDate.now()));
        when(kontoMock2.getInhaber()).thenReturn(
                new Kunde("Erika", "Musterfrau", "Weg", LocalDate.now()));




        // Exercise
        Geldbetrag uebBetrag = new Geldbetrag(50, Waehrung.EUR);


        assertThrows(IllegalArgumentException.class, () ->  bank.geldUeberweisen(kontonummer1, kontonummer2, uebBetrag, null));

        // Verify
        verify(kontoMock1, atMost(0)).ueberweisungAbsenden(any(), anyString(), anyLong(), anyLong(), anyString());
    }

    // Es ist es möglich mit 0 Euro zu überweisen
    @Test
    void überweisungMit0Euro() throws GesperrtException {
        // Set Up
        UeberweisungsfaehigesKonto kontoMock1 = mock(Girokonto.class);
        UeberweisungsfaehigesKonto kontoMock2 = mock(Girokonto.class);

        long kontonummer1 = bank.mockEinfuegen(kontoMock1);
        when(kontoMock1.getKontonummer()).thenReturn(kontonummer1);

        long kontonummer2 = bank.mockEinfuegen(kontoMock2);
        when(kontoMock2.getKontonummer()).thenReturn(kontonummer2);

        when(kontoMock1.getInhaber()).thenReturn(
                new Kunde("Max", "Mustermann", "Straße", LocalDate.now()));
        when(kontoMock2.getInhaber()).thenReturn(
                new Kunde("Erika", "Musterfrau", "Weg", LocalDate.now()));


        // Exercise
        Geldbetrag uebBetrag = new Geldbetrag(0, Waehrung.EUR);
        bank.geldUeberweisen(kontonummer1, kontonummer2, uebBetrag, "Konto 2 will sich eine Schoki holen");

       // assertThrows(IllegalArgumentException.class, () ->  bank.geldUeberweisen(kontonummer1, kontonummer2, uebBetrag, "Konto 2 will sich eine Schoki holen"));

        // Verify
        verify(kontoMock1, atMost(1)).ueberweisungAbsenden(
                eq(uebBetrag), anyString(), anyLong(), anyLong(), anyString());

        verify(kontoMock2, atMost(1)).ueberweisungEmpfangen(
                eq(uebBetrag), anyString(), anyLong(), anyLong(), anyString());

    }

    @Test
    void gesperrtesKonto() throws GesperrtException {
        // Set Up
        UeberweisungsfaehigesKonto kontoMock1 = mock(Girokonto.class);
        UeberweisungsfaehigesKonto kontoMock2 = mock(Girokonto.class);

        long kontonummer1 = bank.mockEinfuegen(kontoMock1);
        when(kontoMock1.getKontonummer()).thenReturn(kontonummer1);

        long kontonummer2 = bank.mockEinfuegen(kontoMock2);
        when(kontoMock2.getKontonummer()).thenReturn(kontonummer2);

        when(kontoMock1.getInhaber()).thenReturn(
                new Kunde("Max", "Mustermann", "Straße", LocalDate.now()));
        when(kontoMock2.getInhaber()).thenReturn(
                new Kunde("Erika", "Musterfrau", "Weg", LocalDate.now()));


        doThrow(new GesperrtException(kontonummer1)).when(kontoMock1).ueberweisungAbsenden(
                any(Geldbetrag.class),
                anyString(),
                anyLong(),
                anyLong(),
                anyString()
        );

        // Exercise
        Geldbetrag uebBetrag = new Geldbetrag(50, Waehrung.EUR);

        assertThrows(GesperrtException.class, () ->
                bank.geldUeberweisen(kontonummer1, kontonummer2, uebBetrag, "Konto 2 will sich eine Schoki holen")
        );

        // Verify
        verify(kontoMock1, atMost(1)).ueberweisungAbsenden(any(), anyString(), anyLong(), anyLong(), anyString());
        verify(kontoMock2, atMost(0)).ueberweisungEmpfangen(any(), anyString(), anyLong(), anyLong(), anyString());
    }
    @Test
    void überweisenAbsendenFalse() throws GesperrtException {
        // Set Up
        UeberweisungsfaehigesKonto kontoMock1 = mock(Girokonto.class);
        UeberweisungsfaehigesKonto kontoMock2 = mock(Girokonto.class);

        long kontonummer1 = bank.mockEinfuegen(kontoMock1);
        when(kontoMock1.getKontonummer()).thenReturn(kontonummer1);

        long kontonummer2 = bank.mockEinfuegen(kontoMock2);
        when(kontoMock2.getKontonummer()).thenReturn(kontonummer2);

        when(kontoMock1.getInhaber()).thenReturn(
                new Kunde("Max", "Mustermann", "Straße", LocalDate.now()));
        when(kontoMock2.getInhaber()).thenReturn(
                new Kunde("Erika", "Musterfrau", "Weg", LocalDate.now()));


        Geldbetrag kontoStandKonto1 = new Geldbetrag(-9999,Waehrung.EUR);
        // Exercise - Konto1 soll -9999 Euro als Kontostand haben --> nicht überweisungsfähig
        when(kontoMock1.getKontostand()).thenReturn(kontoStandKonto1);

        Geldbetrag uebBetrag = new Geldbetrag(50, Waehrung.EUR);

        boolean result = bank.geldUeberweisen(kontonummer1, kontonummer2, uebBetrag, "Zweck");
        assertFalse(result); // Überweisung war nicht erfolgreich

        // Verify
        verify(kontoMock1, atMost(1)).ueberweisungAbsenden(eq(kontoStandKonto1), anyString(), anyLong(), anyLong(), anyString());
        verify(kontoMock2, atMost(0)).ueberweisungEmpfangen(any(), anyString(), anyLong(), anyLong(), anyString());

    }
    @Test
    void nichtÜberweisungsfähigeKonten() throws GesperrtException {
        // Set Up
        Sparbuch kontoMock1 = mock(Sparbuch.class);
        Sparbuch kontoMock2 = mock(Sparbuch.class);

        long kontonummer1 = bank.mockEinfuegen(kontoMock1);
        when(kontoMock1.getKontonummer()).thenReturn(kontonummer1);

        long kontonummer2 = bank.mockEinfuegen(kontoMock2);
        when(kontoMock2.getKontonummer()).thenReturn(kontonummer2);

        when(kontoMock1.getInhaber()).thenReturn(
                new Kunde("Max", "Mustermann", "Straße", LocalDate.now()));
        when(kontoMock2.getInhaber()).thenReturn(
                new Kunde("Erika", "Musterfrau", "Weg", LocalDate.now()));


        // Exercise
        Geldbetrag uebBetrag = new Geldbetrag(50, Waehrung.EUR);

        boolean result = bank.geldUeberweisen(kontonummer1, kontonummer2, uebBetrag, "Zweck");
        assertFalse(result); // Überweisung war nicht erfolgreich

        // Verify - kein versehentlicher Methodenaufruf auf den Sparbuch-Konten gemacht wurde
        verifyNoInteractions(kontoMock1);

    }


}
