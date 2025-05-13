package Übungsstunde;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.mockito.Mockito;
import org.testng.annotations.Test;

public class Bowl_Bestellung_Test {


    /**
     * Kommunikation mit den Benutzer kommnizieren
     * genutzt wird Interface Kommunikation und daraus fragen und ausgeben
      */
    @Test
    void bowlBestellung(){
        //SetUp:

        Kommunikation komm = Mockito.mock();
        when(komm.fragen("Wie soll die Bowl aussehen ?")).thenReturn("Karotten, Rucola und Kürbiskerne ");

        // hier kommen die Mock Objekte rein
       // BowlBestellung bowlB = new BowlBestellung();





}


}
