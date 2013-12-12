package client;

import org.apache.log4j.PropertyConfigurator;

import edu.kit.aifb.dbe.hermes.RequestHandlerRegistry;
import edu.kit.aifb.dbe.hermes.SimpleFileLogger;

/*
 * Der Request-Objekt wird hier gebaut und an den Server geschickt.
 * Die put-methode schickt nur an PutMasterHandler und die get-Methode greift auf alle 3 Replika.
 * Die AddSlaveHandler sorgen dafür, dass der Client über die Existenz von 2 weiteren Servern weiß.
 */

public class ClientMain {
	public static void main(String args[]) {
		PropertyConfigurator.configure("log4j.properties");
		SimpleFileLogger.getInstance();
		RequestHandlerRegistry reg = RequestHandlerRegistry.getInstance();
	}
}
