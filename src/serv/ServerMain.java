package serv;

import org.apache.log4j.PropertyConfigurator;

import edu.kit.aifb.dbe.hermes.*;

public class ServerMain {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PropertyConfigurator.configure("log4j.properties");
		SimpleFileLogger.getInstance();
		RequestHandlerRegistry reg = RequestHandlerRegistry.getInstance();
		// Fragen im Forum.
		// IRequestHandler handlerImplementationObject = new IRequestHandler();
		GetHandler getHandler = new GetHandler();
		PutSlaveHandler putSlaveHandler = new PutSlaveHandler();
		reg.registerHandler("targetHandler",getHandler);
		
		Receiver receiver = new Receiver(port, 5,5);
		receiver.start();
	}

}
