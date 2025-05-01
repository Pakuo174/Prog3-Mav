package bankprojekt.verarbeitung;

import java.time.LocalDate;

/**
 * Bereitstellung des heutigen Datums
 */
public class Kalender {
	/**
	 * liefert das heutige Datum
	 * @return das heutige Datum
	 */
	public LocalDate getHeutigesDatum() {
		return LocalDate.now();
	}
}
