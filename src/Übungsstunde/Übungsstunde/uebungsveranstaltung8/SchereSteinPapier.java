package Übungsstunde.uebungsveranstaltung8;

/**
 * Das Schere-Stein-Papier-Spiel
 * @author Doro
 *
 */
public class SchereSteinPapier {
	
	/**
	 * ermittelt die Spielernummer des Gewinners,
	 * @return 0 oder 1
	 */
	public int gewinnerErmitteln(Auswahl spieler1, Auswahl spieler2) {
		if(spieler1 == spieler2){
			return -1; // Unentschieden
		}
		if (
			spieler1 == Auswahl.PAPIER && spieler2 == Auswahl.STEIN ||
					spieler1 == Auswahl.PAPIER && spieler2 == Auswahl.Spock||
			spieler1 == Auswahl.STEIN && spieler2 == Auswahl.SCHERE ||
			spieler1 == Auswahl.STEIN && spieler2 == Auswahl.Echse ||
			spieler1 == Auswahl.SCHERE && spieler2 == Auswahl.PAPIER ||
			spieler1 == Auswahl.SCHERE && spieler2 == Auswahl.Echse ||
			spieler1 == Auswahl.Echse && spieler2 == Auswahl.Spock ||
			spieler1 == Auswahl.Echse && spieler2 == Auswahl.PAPIER ||
			spieler1 == Auswahl.Spock && spieler2 == Auswahl.STEIN ||
			spieler1 == Auswahl.Spock && spieler2 == Auswahl.SCHERE
		){
			return 0; // Spieler 1 gewinn
		}else{
			return 1; // Spieler 2 gewinnt
		}

	}

	/**
	 * startet das Spiel
	 * Thread werden für Spieler gestartet
	 *
	 */
	public void spielStarten()
	{
		// erstellt Objekte die den Thread zugewiesen werden
		Spieler spieler1 = new Spieler();
		Spieler spieler2 = new Spieler();
		Signalgeber signalG = new Signalgeber(spieler1,spieler2);


		Thread tSpieler1 = new Thread(spieler1);
		Thread tSpieler2 = new Thread(spieler2);
		Thread tSignal = new Thread(signalG);

		// startet die Threads
		tSpieler1.start();
		tSpieler2.start();
		tSignal.start();

		//
        try {
            tSpieler1.join();
			tSpieler2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

		int gewinner = gewinnerErmitteln(spieler1.getWahl(),spieler2.getWahl());
		System.out.println("Gewinner ist Spieler :" + (gewinner +1));


    }
	
	/**
	 * startet das Spiel
	 * @param args wird nicht verwendet
	 */
	public static void main(String[] args) {
		SchereSteinPapier ssp = new SchereSteinPapier();

		for(int i=0; i< 10; i++)
			ssp.spielStarten();
	}
}

