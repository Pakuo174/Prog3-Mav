package Übungsstunde.uebungsveranstalung11;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * bietet die Oberfläche zu einem Getränkeautomaten an
 */
public abstract class Automat {
	
	public abstract AutomatenGetraenk erzeugen(int auswahl);
	
	/**
	 * bietet die Oberfläche zu einem Getränkeautomaten an
	 * @throws InputMismatchException wenn der Benutzer eine falsche Eingabe macht
	 * @throws NoSuchElementException wenn der Benutzer gar keine Eingabe macht
	 */
	public void getraenkKochen() 
	{
		int auswahl;
		Scanner console = new Scanner(System.in);
		System.out.println("Wollen Sie Luxuskaffee (1) oder LuxusTee (2) LuxusKakao (3) WeisserKakao(4)?");
		//System.out.println("Wollen Sie Kaffee (1) oder Tee (2)?");
		auswahl = console.nextInt();
		
		AutomatenGetraenk get = erzeugen(auswahl);
		if(get == null) {
			System.out.println("Dann eben nix zu trinken...");
			System.exit(0);
		}
		get.kochen();
	}

}
