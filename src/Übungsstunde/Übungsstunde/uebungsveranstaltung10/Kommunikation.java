package Übungsstunde.uebungsveranstaltung10;

import java.io.*;

/**
 * Klasse für die Kommunikation in einem Chat
 */
public class Kommunikation {
	/**
	 * sendet eine Nachricht an den Partner
	 * @param out OutputStream, der zum Chatpartner führt
	 * @param text zu sendende Nachricht
	 */
	public static void nachrichtSenden(OutputStream out, Nachricht text) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(out);
		oos.writeObject(text);

	}
	
	/**
	 * empfängt eine Nachricht vom Partner (blockierend)
	 * @param in dem InputStream vom Chatpartner
	 * @return empfangene Nachricht
	 */
	public static String nachrichtEmpfangen(InputStream in) throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(in);
		String s = (String) ois.readObject();
        return s;
    }
}
