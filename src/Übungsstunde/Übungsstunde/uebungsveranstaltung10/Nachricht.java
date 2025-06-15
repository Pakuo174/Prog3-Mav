package Übungsstunde.uebungsveranstaltung10;

import java.time.LocalDate;

public class Nachricht {

    private String textDerNachricht;
    private LocalDate zeitStempel;

    public Nachricht(String nachricht){
        this.textDerNachricht = nachricht;
        zeitStempel = LocalDate.now();
    }

}
