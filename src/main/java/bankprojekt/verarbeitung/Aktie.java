package bankprojekt.verarbeitung;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Eine Aktie, die ständig ihren Kurs verändert
 * @author Doro
 *
 */
public class Aktie {
	
	private static Map<String, Aktie> alleAktien = new HashMap<>(); 	// Eine statische Map, die alle erstellten Aktie-Objekte speichert, indexiert nach ihrer Wertepapierkennnummer (WKN).
	private String wkn; 												// Wertepapierkennnummer
	private double kurs;												// Der aktuelle Kurs der Aktie. Dieser Wert wird sich dynamisch ändern.

																		// Dies sind Condition-Objekte, die mit dem aktienlock verbunden sind.
	private Condition kursHoch;											// Threads können hier warten, bis der Kurs steigt.
	private Condition kursRunter;										// Threads können hier warten, bis der Kurs fällt.
	private Lock aktienlock;											// Ein Lock-Objekt - Wird verwendet, um den Zugriff auf den kurs zu synchronisieren, wenn dieser verändert wird.
	private Future<?> kursAenderung;

	private final Random random = new Random();							//
	
	/**
	 * gibt die Aktie mit der gewünschten Wertpapierkennnummer zurück
	 * @param wkn Wertpapierkennnummer
	 * @return Aktie mit der angegebenen Wertpapierkennnummer oder null, wenn es diese WKN
	 * 			nicht gibt.
	 */
	public static Aktie getAktie(String wkn)
	{
		return alleAktien.get(wkn);
	}
	
	/**
	 * erstellt eine neu Aktie mit den angegebenen Werten
	 * @param wkn Wertpapierkennnummer
	 * @param k aktueller Kurs
	 * @throws IllegalArgumentException wenn einer der Parameter null bzw. negativ ist
	 * 		                            oder es eine Aktie mit dieser WKN bereits gibt
	 */
	public Aktie(String wkn, double k) {
		if(wkn == null || k <= 0 || alleAktien.containsKey(wkn))
			throw new IllegalArgumentException();	
		this.wkn = wkn;
		this.kurs = k;
		this.aktienlock = new ReentrantLock();
		this.kursHoch = this.aktienlock.newCondition();
		this.kursRunter = this.aktienlock.newCondition();
		alleAktien.put(wkn, this);

		// ScheduledExecutorService, der die Aufgabe im Hintergrund auf einem eigenen Thread erledigt - jede sekunde
		Runnable kursAendern = () -> this.kursAendern();
		ScheduledExecutorService service = Executors.newScheduledThreadPool(0);

		int zufallszeit = random.nextInt(5) + 1;
		kursAenderung = service.
				scheduleWithFixedDelay(kursAendern, 0, zufallszeit, TimeUnit.SECONDS);
	}

	/**
	 * verändert den aktuellen AKtienIndex bzw. simuliert es eher mit einer Zufallszahl wischen -3 und 3
	 * Race Condition verhindern - Zugriff auf den kurs wird gesperrt
	 *
	 * Zufallszahl der Aktie geändert
	 * abhängig von negativen oder postiven Veränderung wird kursHoch oder kursRUnter Conditon signalisiert
	 */
	private void kursAendern()
	{
		double veraenderung = Math.random()*6-3;
		aktienlock.lock();
		kurs = kurs * (100 + veraenderung)/100;
		if(veraenderung > 0)
			kursHoch.signalAll();
		if(veraenderung < 0)
			kursRunter.signalAll();
		aktienlock.unlock();
	}

	/**
	 * Wertpapierkennnummer
	 * @return WKN der Aktie
	 */
	public String getWkn() {
		return wkn;
	}

	/**
	 * aktueller Kurs
	 * @return Kurs der Aktie
	 */
	public double getKurs() {
		return kurs;
	}

	/**
	 * Condition-Objekt für Benachrichtigungen, wenn der Kurs steigt
	 * @return Benachrichtigung bei steigendem Kurs
	 */
	public Condition getKursHoch() {
		return kursHoch;
	}
	/**
	 * * Condition-Objekt für Benachrichtigungen, wenn der Kurs fällt
	 * @return Benachrichtigung bei fallendem Kurs
	 */
	public Condition getKursRunter() {
		return kursRunter;
	}

	/**
	 * Lock-Objekt
	 * @return Lock-Objekt für alle Conditions
	 */
	public Lock getAktienlock() {
		return aktienlock;
	}
	
	/**
	 * Wenn du aktie.anhalten() aufrufst, wird kursAenderung.cancel(false) ausgeführt. Das ist der Befehl an den ScheduledExecutorService, die weiter Ausführung der kursAendern()-Methode zu stoppen.
	 * stoppt die Kursänderung
	 */
	public void anhalten() {
		kursAenderung.cancel(false);
	}
}
