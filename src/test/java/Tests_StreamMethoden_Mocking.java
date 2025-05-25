import bankprojekt.verarbeitung.Geldbetrag;
import bankprojekt.geld.Waehrung;
import bankprojekt.verarbeitung.*;
import bankprojekt.verwaltung.Bank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class Tests_StreamMethoden_Mocking {

    Bank bank;
    Girokonto kontoAlt1;
    Girokonto kontoAlt2;
    Girokonto kontoJung;
    Girokonto kontoLeer;

    @BeforeEach
    void setup() {
        bank = new Bank(12345678L);

        // Mock-Konten
        kontoAlt1 = mock(Girokonto.class);
        kontoAlt2 = mock(Girokonto.class);
        kontoJung = mock(Girokonto.class);
        kontoLeer = mock(Girokonto.class);

        // Älter als 18
        Kunde k1 = new Kunde("Daniel", "Kujawa", "Bärlin", LocalDate.of(2000, 7, 12));
        when(kontoAlt1.getInhaber()).thenReturn(k1);
        when(kontoAlt1.getKontostand()).thenReturn(new Geldbetrag(100,Waehrung.EUR));

        Kunde k2 = new Kunde("Nico", "Froelich", "Rathenow", LocalDate.of(2000, 12, 13));
        when(kontoAlt2.getInhaber()).thenReturn(k2);
        when(kontoAlt2.getKontostand()).thenReturn(new Geldbetrag(200,Waehrung.EUR));

        // Jünger als 18
        Kunde jung = new Kunde("Kein", "Name", "Adresse", LocalDate.of(2015, 12, 12));
        when(kontoJung.getInhaber()).thenReturn(jung);
        when(kontoJung.getKontostand()).thenReturn(new Geldbetrag(1000000,Waehrung.EUR));

        // leeres Konto
        Kunde leer = new Kunde("Erklär", "Bär", "Bärlin", LocalDate.of(1999, 8, 12));
        when(kontoLeer.getInhaber()).thenReturn(leer);


        // Konto-Map manipulieren (angenommen, du hast in Bank Zugriff auf konten-Map)
        long kontonummer1 = bank.mockEinfuegen(kontoAlt1);
        long kontonummer2 = bank.mockEinfuegen(kontoAlt2);
        long kontonummer3 = bank.mockEinfuegen(kontoJung);
        long kontonummer4 = bank.mockEinfuegen(kontoLeer);

    }





    @Test
    void testEinzahlenBetragÜber18(){
        Geldbetrag betrag = new Geldbetrag(50, Waehrung.EUR);

        // Exercise
        bank.schenkungAnNeuerwachsene(betrag);

        // Verify: Einzahlung bei Erwachsenen
        verify(kontoAlt1, times(1)).einzahlen(betrag);
        verify(kontoAlt2, times(1)).einzahlen(betrag);

        // Verify: Keine Einzahlung beim Kind
        verify(kontoJung, never()).einzahlen(any());

    }
    @Test
    void testKundenmitLeerenKonto_ThrowsIfKontostandNull(){

        // Act & Assert
        assertThrows(NullPointerException.class, () -> bank.getKundenMitLeeremKonto());
    }



}
