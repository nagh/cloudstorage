package serv;

import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;

import edu.kit.aifb.dbe.hermes.*;

public class ServerMain {
	
	private static int port = 32563;
	private static boolean isMaster = true;
	private static boolean hasSlave = true; // nur relevant, falls isMaster = false
	public static Receiver receiver = null;
	public static AddSlaveHandler addSlaveHandler = new AddSlaveHandler();
	// public static RequestHandlerRegistry reg = null;
	// public static PutSlaveHandler putSlaveHandler = null;
	// public static PutMasterHandler putMasterHandler = null;
	
	public static void main(String args[]) {
		// Argumente args[] auswerten
		System.out.println("Aufruf: ServerMain <master/slave> <slave/noslave>");
		if (args != null) {
			isMaster = args[0].equals("true");
			hasSlave = args[1].equals("true");
		}
		System.out.println("Server started with following configuration:");
		System.out.println("Server is Master: " + isMaster);
		System.out.println("Server has Slave: " + hasSlave);
		
		// Basic Setup
		PropertyConfigurator.configure("log4j.properties");
		SimpleFileLogger.getInstance();
		RequestHandlerRegistry reg = RequestHandlerRegistry.getInstance();

		// Generate and register Handlers
		reg.registerHandler("getHandler", new GetHandler());
		if (isMaster == true) {
			reg.registerHandler("putMasterHandler", new PutMasterHandler());
			reg.registerHandler("addSlaveHandler", addSlaveHandler);
		}
		else if(hasSlave == true) {
			reg.registerHandler("putSlaveHandler", new PutSlaveHandler(hasSlave));
			reg.registerHandler("addSlaveHandler", addSlaveHandler);
		}
		else {
			reg.registerHandler("putSlaveHandler", new PutSlaveHandler(!hasSlave));
		}
		
		// Start receiver
		
		try {
			receiver = new Receiver(port, 5, 5);
		} catch (IOException e) {
			e.printStackTrace();
		}
		receiver.start();

	}

}
