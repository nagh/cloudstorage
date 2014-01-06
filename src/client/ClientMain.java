package client;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;

import serv.Access;
import serv.AddSlaveHandler;
import edu.kit.aifb.dbe.hermes.*;

/*
 * Der Request-Objekt wird hier gebaut und an den Server geschickt.
 * Die put-methode schickt nur an PutMasterHandler und die get-Methode greift auf alle 3 Replika.
 * Die AddSlaveHandler sorgen dafür, dass der Client über die Existenz von 2 weiteren Servern weiß.
 */
// Das Request-Objekt besteht aus: Request(Messagepayload[Serializable oder List<Serializable>], Name des Handlers auf Serverseite, beliebiger String {auf diesen kann mit getOriginator() zugegriffen werden})
// Konvention bei Put Requests: List<Serializable>: 1. Eintrag key, 2. Eintrag data

public class ClientMain {
	// Configuration
	private static int timeout = 1000; // Zeit [ms]
	private static int port = 5;       // Port-#
	private static String ipMaster = "127.0.0.1";
	private static String ipSlave1 = "127.0.0.1";
	private static String ipSlave2 = "127.0.0.1";
	// Initiatlization
	private static Sender senderM = null;
	private static Sender senderS1 = null;
	private static Sender senderS2 = null;
	private static Receiver receiver = null;
	public static AddSlaveHandler addSlaveHandler1 = null;
	public static AddSlaveHandler addSlaveHandler2 = null;
		
	public static void main(String args[]) {
		PropertyConfigurator.configure("log4j.properties");
		
		// Senderobjekte erstellen (fuer einen getRequest werden alle 3 Hosts angesprochen)
		senderM = new Sender(ipMaster, port);
		senderS1 = new Sender(ipSlave1, port);
		senderS2 = new Sender(ipSlave2, port);
		
		// Receiverobjekt starten
		try {
			receiver = new Receiver(port, 5, 5);
		} catch (IOException e) {
			e.printStackTrace();
		}
		receiver.start();
		
		// AddSlaveHandler aufrufen
		addSlaveHandler1 = new AddSlaveHandler(ipSlave1, port);
		addSlaveHandler2 = new AddSlaveHandler(ipSlave2, port);
		
		// putRequest senden
		String key = "sent_testfile.txt";
		String data = Access.get("input_testfile.txt");
		putRequest(key, data);
		
		// getRequest senden
		String key1 = "input_testfile.txt";
		getRequest(key1);
	}
	
	public static void putRequest(String key, String data) {
		// Request erstellen		
		List<Serializable> payload = new ArrayList<Serializable>();
		payload.add(key);
		payload.add(data);
		Request request = new Request(payload, "putMasterHandler", null);
		
		// Request an den Master senden
		Response responseM = senderM.sendMessage(request, timeout);
		if (responseM.responseCode()) {
			System.out.println("Der Put-Request war erfolgreich.");
		}
		else {
			System.out.println("Der Put-Request ist fehlgeschlagen.");
		}
	}
	
	public static void getRequest(String key) {
		// Request erstellen
		List<Serializable> payload = new ArrayList<Serializable>();
		payload.add(key);
		Request request = new Request(payload, "getHandler", null);
		
		// Request an den Master und die beiden Slaves senden
		Response responseM = senderM.sendMessage(request, timeout);
		Response responseS1 = senderS1.sendMessage(request, timeout);
		Response responseS2 = senderS2.sendMessage(request, timeout);
		
		if (responseM.responseCode() && responseS1.responseCode() && responseS2.responseCode()) {  // check if get-operation was successful
			if (responseM.getItems().get(2).equals(responseS1.getItems().get(2)) && responseS1.getItems().get(2).equals(responseS2.getItems().get(2))) {  // check if replicas are in consistent state
				Access.put((String) responseM.getItems().get(1), (String) responseM.getItems().get(2));
				System.out.println("Der Get-Request war erfolgreich. Das Datum wurde lokal gespeichert.");
			}
			else {
				System.out.println("Die Replika sind nicht konsistent.");
			}
		}
		else {
			System.out.println("Der Get-Request ist fehlgeschlagen.");
		}	
	}
}
