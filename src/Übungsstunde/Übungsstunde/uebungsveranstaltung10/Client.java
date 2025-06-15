package Übungsstunde.uebungsveranstaltung10;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
* Client für einen ganz einfachen Chat
*/
public class Client {



    /**
	* baut die Verbindung zum Server an der angegebenen Adresse auf
        * und chattet mit ihm
	* @param ip IP-Adresse des Servers
	*/
	public void chatten(String ip)
	{
		InetAddress ipAdresse;
		Socket so = null;
		try {
			ipAdresse = InetAddress.getByName(ip);
			so = new Socket(ipAdresse, Server.ANMELDEPORT);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		try {
			OutputStream aus = so.getOutputStream();		
			InputStream ein = so.getInputStream();

			Nachricht n1 = new Nachricht("looool");

			Kommunikation.nachrichtSenden(aus,n1);
			System.out.println(Kommunikation.nachrichtEmpfangen(ein));
						
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
			so.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	* startet den Client
	* @param args wird nicht verwendet
	*/
	public static void main(String[] args) {
		Client client = new Client();
		client.chatten("localhost");
	}
}
