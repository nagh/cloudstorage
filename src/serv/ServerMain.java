package serv;

import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;

import edu.kit.aifb.dbe.hermes.*;

public class ServerMain {
	
	private static int port = 5;
	private static boolean isMaster = true;
	private static boolean hasSlave = true; // nur relevant, falls isMaster = false
	private static boolean sync = true;
	
	public static void main(String args[]) {
		
			System.out.println("Aufruf: ServerMain <master/slave> <slave/noslave> <sync/async>");
		if (args != null) {
			isMaster = args[0].equals("true");
			hasSlave = args[1].equals("true");
			sync = args[2].equals("true");
		}
		System.out.println("Server started with following configuration:");
		System.out.println("Server is Master: " + isMaster);
		System.out.println("Server has Slave: " + hasSlave);
		System.out.println("Synchronous: " + sync);
		// Basic Setup
		PropertyConfigurator.configure("log4j.properties");
		SimpleFileLogger.getInstance();
		RequestHandlerRegistry reg = RequestHandlerRegistry.getInstance();
						
		// Generate and register Handlers
		reg.registerHandler("getHandler", new GetHandler());
		if (isMaster == true) {
			reg.registerHandler("putMasterHandler", new PutMasterHandler(sync));  // PutMasterHandler.java mit sync-Variable
		}
		else if(hasSlave == true) {
			reg.registerHandler("putSlaveHandler", new PutSlaveHandler(hasSlave));
		}
		else {
			reg.registerHandler("putSlaveHandler", new PutSlaveHandler(!hasSlave));
		}
		
		// Start receiver
		Receiver receiver = null;
		try {
			receiver = new Receiver(port, 5, 5);
		} catch (IOException e) {
			e.printStackTrace();
		}
		receiver.start();
		
		// irgendwas mit Threads -> Receiver auswerten  -> FORUM
		// request auswerten -> angefragten Handlernamen als id
		// IRequestHandler Handler = reg.getHandlerForID(id);

	}

}
