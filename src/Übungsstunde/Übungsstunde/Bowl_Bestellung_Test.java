package Übungsstunde;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.util.List;

public class Bowl_Bestellung_Test {


    /**
     * Kommunikation mit den Benutzer kommnizieren
     * genutzt wird Interface Kommunikation und daraus fragen und ausgeben
      */
    @Test
    void bowlBestellung(){
        //SetUp:

        // Setup: Mocks
        Kommunikation komm = mock(Kommunikation.class);
        EingabeParser parser = mock(EingabeParser.class);
        BowlBuilder builder = mock(BowlBuilder.class);
        Bowl bowl = mock(Bowl.class);


        when(komm.fragen(BowlBestellung.FRAGE)).thenReturn("Karotten, Rucola, Kürbiskerne");
        when(parser.bestellungParsen(anyString())).thenReturn(List.of("Karotten", "Rucola", "Kürbiskerne"));
        when(builder.build()).thenReturn(bowl);
        when(bowl.getPreis()).thenReturn(5.99);

        // BowlBestellung instanziieren
        BowlBestellung bestellung = new BowlBestellung(komm, builder, parser);

        // Methode testen
        Bowl result = bestellung.bestellungDurchfuehren();

        // Überprüfen, ob die Kommunikation korrekt verwendet wurde
        verify(komm).fragen(BowlBestellung.FRAGE);
        verify(komm).ausgeben("Deine Bowl kostet 5.99");

        // Bowl darf nicht null sein
        assertEquals(bowl, result); // Erwartung: diese Bowl wurde zurückgegeben
        assertNotNull(result);

}

    @Test
    void testUnverstaendlicheBestellung() {
        Kommunikation komm = Mockito.mock(Kommunikation.class);
        EingabeParser parser = Mockito.mock(EingabeParser.class);
        BowlBuilder builder = Mockito.mock(BowlBuilder.class);

        when(komm.fragen(BowlBestellung.FRAGE)).thenReturn("blablabla");
        when(parser.bestellungParsen("blablabla")).thenReturn(List.of());

        BowlBestellung bestellung = new BowlBestellung(komm, builder, parser);

        Bowl result = bestellung.bestellungDurchfuehren();

        assertNull(result); // Erwartung: keine Bowl gebaut
        Mockito.verify(komm).ausgeben(BowlBestellung.SORRY); // Erwartung: Sorry wurde ausgegeben
    }
    @Test
    void testVerstaendlicheBestellung() {
        Kommunikation komm = Mockito.mock(Kommunikation.class);
        EingabeParser parser = Mockito.mock(EingabeParser.class);
        BowlBuilder builder = Mockito.mock(BowlBuilder.class);

        when(komm.fragen(BowlBestellung.FRAGE)).thenReturn("Karotten, Rucola");
        when(parser.bestellungParsen("Karotten, Rucola")).thenReturn(List.of("Karotten", "Rucola"));

        Bowl bowl = Mockito.mock(Bowl.class);
        when(bowl.getPreis()).thenReturn(4.50);
        when(builder.build()).thenReturn(bowl);

        BowlBestellung bestellung = new BowlBestellung(komm, builder, parser);

        Bowl result = bestellung.bestellungDurchfuehren();

        assertEquals(bowl, result); // Erwartung: diese Bowl wurde zurückgegeben
        Mockito.verify(builder).add("Karotten");
        Mockito.verify(builder).add("Rucola");
        Mockito.verify(komm).ausgeben("Deine Bowl kostet 4.5");

        assertEquals(bowl, result); // Erwartung: diese Bowl wurde zurückgegeben
    }
    @Test
    void testBuildWirdNurEinmalAufgerufen() {
        Kommunikation komm = Mockito.mock(Kommunikation.class);
        EingabeParser parser = Mockito.mock(EingabeParser.class);
        BowlBuilder builder = Mockito.mock(BowlBuilder.class);
        Bowl bowl = Mockito.mock(Bowl.class);

        when(komm.fragen(BowlBestellung.FRAGE)).thenReturn("Karotten, Rucola");
        when(parser.bestellungParsen("Karotten, Rucola")).thenReturn(List.of("Karotten", "Rucola"));
        when(builder.build()).thenReturn(bowl);
        when(bowl.getPreis()).thenReturn(5.0);

        BowlBestellung bestellung = new BowlBestellung(komm, builder, parser);
        bestellung.bestellungDurchfuehren();

        // Hier kommt der entscheidende Check:
        Mockito.verify(builder, Mockito.times(1)).build(); // ❗ Test schlägt fehl, wenn build() öfter aufgerufen wird

        // richtige Lösung :
        /*

        for (String ing : ingredients) {
            builder.add(ing);  // Zutaten alle zuerst hinzufügen
        }
        bowl = builder.build(); // und dann genau EINMAL build() aufrufen

         */
    }






}
