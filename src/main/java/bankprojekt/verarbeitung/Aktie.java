package bankprojekt.verarbeitung;

import bankprojekt.geld.Waehrung; // Sicherstellen, dass dies importiert ist
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
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
public class Aktie implements Serializable {

	private static Map<String, Aktie> alleAktien = new HashMap<>();
	private String wkn;
	private Geldbetrag kurs;

	private transient Condition kursHoch;
	private transient Condition kursRunter;
	private transient Lock aktienlock;
	private transient Future<?> kursAenderung; // <- Hier bereits transient

	private transient final Random random = new Random(); // <- Hier bereits transient und final

	// Globaler ScheduledExecutorService für alle Aktien, nur einmal initialisiert.
	// Dieser ist statisch und transient, d.h. er wird nicht serialisiert und ist global.
	private static transient ScheduledExecutorService kursAenderungService;




	public static Aktie getAktie(String wkn) {
		return alleAktien.get(wkn);
	}

	public Aktie(String wkn, Geldbetrag k) {
		Geldbetrag nullBetrag = new Geldbetrag(0);

		if(wkn == null || k.compareTo(nullBetrag) < 0 || alleAktien.containsKey(wkn)) {
			throw new IllegalArgumentException();
		}
		this.wkn = wkn;
		this.kurs = k;

		// Initialisierung der transienten Felder im Konstruktor
		this.aktienlock = new ReentrantLock();
		this.kursHoch = this.aktienlock.newCondition();
		this.kursRunter = this.aktienlock.newCondition();

		alleAktien.put(wkn, this); // Fügt die neue Aktie der statischen Map hinzu

		// Starten der Kursänderung

	}


	private void kursAendern() {
		double veraenderungProzent = (random.nextDouble() * 6 - 3) / 100.0;
		double faktor = 1.0 + veraenderungProzent;

		aktienlock.lock();
		try {
			kurs = kurs.mal(faktor);
			if(veraenderungProzent > 0)
				kursHoch.signalAll();
			if(veraenderungProzent < 0)
				kursRunter.signalAll();
		} finally {
			aktienlock.unlock();
		}
	}

	public String getWkn() {
		return wkn;
	}

	public Geldbetrag getKurs() {
		aktienlock.lock();
		try {
			return kurs;
		} finally {
			aktienlock.unlock();
		}
	}

	public Condition getKursHoch() {
		return kursHoch;
	}

	public Condition getKursRunter() {
		return kursRunter;
	}

	public Lock getAktienlock() {
		return aktienlock;
	}

	public void anhalten() {
		if (kursAenderung != null) {
			kursAenderung.cancel(false);
		}
	}


	public static void clearAlleAktien() {
		// Stellen Sie sicher, dass keine laufenden Scheduled Tasks das stören
		// oder dass der ExecutorService vor dem Leeren gestoppt wird,
		// falls Tasks noch auf die 'alleAktien' zugreifen.
		// Hier ist es wichtiger, die Map zu leeren.
		alleAktien.clear();
	}


	public static void shutdownKursAenderungService() {
		if (kursAenderungService != null && !kursAenderungService.isShutdown()) {
			kursAenderungService.shutdown();
			try {
				if (!kursAenderungService.awaitTermination(1, TimeUnit.SECONDS)) { // Kurze Wartezeit
					kursAenderungService.shutdownNow();
				}
			} catch (InterruptedException e) {
				kursAenderungService.shutdownNow();
				Thread.currentThread().interrupt();
			}
		}
		// Re-initialisieren für den nächsten Testlauf, falls benötigt (optional, je nach Teststruktur)
		// Wenn Sie sicher sind, dass es in den Tests immer nur einmal pro JVM-Lauf gebraucht wird,
		// können Sie es lassen. Aber bei vielen Tests kann das Problem verursachen.
		// Für Unit-Tests ist es oft besser, den Service zu mocken oder zu kontrollieren.
		// Für Integrationstests lassen Sie ihn laufen.
		// Wenn Sie eine saubere Trennung pro Test wollen:
		// kursAenderungService = Executors.newScheduledThreadPool(1);
	}


}