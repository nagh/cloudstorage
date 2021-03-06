package client;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.log4j.PropertyConfigurator;

import serv.Access;
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
	private static int port = 32563;       // Port-#
	private static String ipMaster = "ec2-54-220-73-34.eu-west-1.compute.amazonaws.com";
	private static String ipSlave1 = "ec2-54-205-136-34.compute-1.amazonaws.com";
	private static String ipSlave2 = "ec2-54-241-213-45.us-west-1.compute.amazonaws.com";
	// Initialization
	private static Sender senderM = null;
	private static Sender senderS1 = null;
	private static Sender senderS2 = null;
	private static Receiver receiver = null;
		
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
		
		/** for test purposes 
		// putRequest senden
		String key = "sent_testfile10.txt";
		String data = Access.get("input_testfile.txt");
		putRequest(key, data, "true");
		
		// getRequest senden
		String key1 = "input_testfile.txt";
		getRequest(key1);
		**/
		
		// Task 2.2
		String[] key = new String[1000];
		double[] latency = new double[1000];
		String data = Access.get("input_testfile.txt");
		String sync = "true";
		for (int ii = 0; ii < 1000; ii++) {
			key[ii] = "key_" + sync + "_" + ii;
			Date start = new Date();
			putRequest(key[ii], data, sync);
			Date stop = new Date();
			latency[ii] = stop.getTime() - start.getTime();
		}
		

		// Task 2.4 - Latenzauswertung
		double lat_max = StatUtils.max(latency);
		double lat_min = StatUtils.min(latency);
		double lat_mean = StatUtils.mean(latency);
		double lat_med = StatUtils.percentile(latency, 50);
		System.out.println("Alle Angaben in ms:");
		System.out.println("Maximum: " + lat_max);
		System.out.println("Minimum: " + lat_min);
		System.out.println("Mittelwert: " + lat_mean);
		System.out.println("Median: " + lat_med);
	}
	
	public static void initialize() {
		List<Serializable> payloadM = new ArrayList<Serializable>();
		payloadM.add(ipSlave1);
		payloadM.add(port);
		Request requestM = new Request(payloadM, "AddSlaveHandler", null);
		Response responseM = senderM.sendMessage(requestM, timeout);
		if (responseM.responseCode()) {
			System.out.println("Die Initialisierung auf dem Master war erfolgreich.");
		}
		else {
			System.out.println("Die Initialisierung auf dem Master ist fehlgeschlagen.");
		}
		
		List<Serializable> payloadS1 = new ArrayList<Serializable>();
		payloadS1.add(ipSlave2);
		payloadS1.add(port);
		Request requestS1 = new Request(payloadS1, "AddSlaveHandler", null);
		Response responseS1 = senderM.sendMessage(requestS1, timeout);
		if (responseS1.responseCode()) {
			System.out.println("Die Initialisierung auf dem Slave war erfolgreich.");
		}
		else {
			System.out.println("Die Initialisierung auf dem Slave ist fehlgeschlagen.");
		}
	}
	
	
	public static void putRequest(String key, String data, String sync) {
		// Request erstellen		
		List<Serializable> payload = new ArrayList<Serializable>();
		payload.add(key);
		payload.add(data);
		payload.add(sync);
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
			if (responseM.getItems().get(1).equals(responseS1.getItems().get(1)) && responseS1.getItems().get(1).equals(responseS2.getItems().get(1))) {  // check if replicas are in consistent state
				Access.put((String) responseM.getItems().get(0), (String) responseM.getItems().get(1));
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
