package Übungsstunde.uebungsveranstaltung10;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
* Server eines ganz einfachen Chats
*/
public class Server {
	/**
	 * erstellt ein Objekt der Klasse
	 */
	public Server() {}
/**
* Port, unter dem der Server auf eingehenden Verbindungen wartet
*/
	public static int ANMELDEPORT = 7777;
/**
* startet den Server, wartet auf eingehende Verbindungen vom Client
* und chattet mit ihm
 * @throws ClassNotFoundException 
*/
	private void verbindungAnnehmenUndChatten()
	{
		ServerSocket seso = null;
		Socket so = null;
		
		try {
			seso = new ServerSocket(ANMELDEPORT);
			so = seso.accept();
		} catch (IOException e) {
				e.printStackTrace();		
		} 
			
		try
		{
			InputStream ein = so.getInputStream();		
			OutputStream aus = so.getOutputStream();

			// nachricht ausgeben
			System.out.println(Kommunikation.nachrichtEmpfangen(ein));


			Nachricht n1 = new Nachricht("looool vom Serer");
			// eigene Nachricht senden
			Kommunikation.nachrichtSenden(aus,n1);

		}catch(IOException e)
		{
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
			seso.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
/**
* startet den Server
* @param args wird nicht verwendet
*/
	public static void main(String[] args)
	{
		Server server = new Server();
		server.verbindungAnnehmenUndChatten();
	}

}
