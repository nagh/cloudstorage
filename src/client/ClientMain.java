package client;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;

import serv.Access;
import serv.AddSlaveHandler;
import edu.kit.aifb.dbe.hermes.Receiver;
import edu.kit.aifb.dbe.hermes.Request;
import edu.kit.aifb.dbe.hermes.RequestHandlerRegistry;
import edu.kit.aifb.dbe.hermes.Sender;
import edu.kit.aifb.dbe.hermes.SimpleFileLogger;

/*
 * Der Request-Objekt wird hier gebaut und an den Server geschickt.
 * Die put-methode schickt nur an PutMasterHandler und die get-Methode greift auf alle 3 Replika.
 * Die AddSlaveHandler sorgen dafür, dass der Client über die Existenz von 2 weiteren Servern weiß.
 */
// Das Request-Objekt besteht aus: Request(Messagepayload[Serializable oder List<Serializable>], Name des Handlers auf Serverseite, beliebiger String {auf diesen kann mit getOriginator() zugegriffen werden})
// Konvention bei Put Requests: List<Serializable>: 1. Eintrag key, 2. Eintrag data

public class ClientMain {
	
	private static int timeout = 1000; // Zeit [ms]
	private static int port = 5;       // Port-#
	private static String ipMaster = "127.0.0.1";
	private static String ipSlave1 = "127.0.0.1";
	private static String ipSlave2 = "127.0.0.1";
	
	public static void main(String args[]) {
		PropertyConfigurator.configure("log4j.properties");
		
		// Senderobjekte erstellen (fuer getRequest werden alle 3 Hosts angesprochen)
		Sender senderM = new Sender(ipMaster, port);
		Sender senderS1 = new Sender(ipSlave1, port);
		Sender senderS2 = new Sender(ipSlave2, port);
		
		// Receiverobjekt und starten
		Receiver receiver = null;
		try {
			receiver = new Receiver(port, 5, 5);
		} catch (IOException e) {
			e.printStackTrace();
		}
		receiver.start();
		
		// AddSlaveHandler aufrufen
		AddSlaveHandler addSlaveHandler1 = new AddSlaveHandler(ipSlave1, port);
		AddSlaveHandler addSlaveHandler2 = new AddSlaveHandler(ipSlave2, port);
		
		// putRequest absetzen
		String key = "sent_testfile.txt";
		String data = Access.get("input_testfile.txt");
		senderM.sendMessage(putRequest(key, data), timeout);
	}
	
	public static Request putRequest(String key, String data) {	
		List<Serializable> payload = new ArrayList<Serializable>();
		payload.add(key);
		payload.add(data);
		Request request = new Request(payload, "putMasterHandler", null);
		return request;
	}
	
	public void getRequest(String key) {
		// an alle drei
	}
}
