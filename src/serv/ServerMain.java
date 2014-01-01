package serv;

import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;

import edu.kit.aifb.dbe.hermes.*;

public class ServerMain {
	
	private static int port = 5;
	private static boolean isMaster = true;
	private static boolean hasSlave = true; // nur relevant, falls isMaster = false
	
	public static void main(String[] args) {
		// Basic Setup
		PropertyConfigurator.configure("log4j.properties");
		SimpleFileLogger.getInstance();
		RequestHandlerRegistry reg = RequestHandlerRegistry.getInstance();
		
		// Generate Handlers
		GetHandler getHandler = new GetHandler();
		PutMasterHandler masterSlaveHandler = new PutMasterHandler();
		PutSlaveHandler putSlaveHandler1 = new PutSlaveHandler(hasSlave);
		PutSlaveHandler putSlaveHandler2 = new PutSlaveHandler(!hasSlave);
		
		// Register Handlers
		reg.registerHandler("getHandler",getHandler);
		if (isMaster == true) {
			reg.registerHandler("putMasterHandler", putMasterHandler);
		}
		else if(hasSlave == true) {
			reg.registerHandler("putSlaveHandler1", putSlaveHandler1);
		}
		else {
			reg.registerHandler("putSlaveHandler2", putSlaveHandler2);
		}
		
		// Start receiver
		Receiver receiver = null;
		try {
			receiver = new Receiver(port, 5, 5);
		} catch (IOException e) {
			e.printStackTrace();
		}
		receiver.start();
		
		// irgendwas mit Threads -> Receiver auswerten
		// request auswerten -> angefragten Handlernamen als id
		IRequestHandler Handler = reg.getHandlerForID(id);

	}

}
