package bankprojekt.verarbeitung;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Konkrete Fabrik zur Erstellung von Mock-Konto-Objekten für Tests.
 */
public class MockKontofabrik extends Kontofabrik {

    @Override
    public Konto erstelleKonto(String kontoTyp, Kunde inhaber, long kontonummer, Geldbetrag betrag) {


        switch (kontoTyp.toLowerCase()) {
            case "sparbuch":
                // Erstellt einen Mock von Sparbuch.class
                Sparbuch mockSparbuch = mock(Sparbuch.class);

                // Standardverhalten:
                when(mockSparbuch.getInhaber()).thenReturn(inhaber);
                when(mockSparbuch.getKontonummer()).thenReturn(kontonummer);
                return mockSparbuch;

            case "girokonto":
                // Erstellt einen Mock von Girokonto.class
                Girokonto mockGirokonto = mock(Girokonto.class);

                // Standardverhalten:
                when(mockGirokonto.getInhaber()).thenReturn(inhaber);
                when(mockGirokonto.getKontonummer()).thenReturn(kontonummer);
                when(mockGirokonto.getKontostand()).thenReturn(betrag);

                return mockGirokonto;

            case "aktienkonto":
                // Erstellt einen Mock von Aktienkonto.class
                Aktienkonto mockAktienkonto = mock(Aktienkonto.class);

                // Standardverhalten:
                when(mockAktienkonto.getInhaber()).thenReturn(inhaber);
                when(mockAktienkonto.getKontonummer()).thenReturn(kontonummer);

                return mockAktienkonto;

            default:
                throw new IllegalArgumentException("Unbekannter Kontotyp für MockKontofabrik: " + kontoTyp);
        }
    }
}