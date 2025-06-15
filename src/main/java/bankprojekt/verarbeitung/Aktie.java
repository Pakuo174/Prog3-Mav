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

	// Statischer Initialisierungsblock: wird einmalig beim Laden der Klasse ausgeführt
	static {
		kursAenderungService = Executors.newScheduledThreadPool(1); // Ein Thread reicht hier oft
		// Optional: Shutdown Hook, um den ExecutorService beim Beenden der JVM sauber herunterzufahren
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			if (kursAenderungService != null && !kursAenderungService.isShutdown()) {
				kursAenderungService.shutdown();
				try {
					if (!kursAenderungService.awaitTermination(5, TimeUnit.SECONDS)) {
						kursAenderungService.shutdownNow(); // Versuche, sofort zu beenden, wenn nicht terminiert
					}
				} catch (InterruptedException e) {
					kursAenderungService.shutdownNow();
					Thread.currentThread().interrupt();
				}
			}
		}));
	}


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


}