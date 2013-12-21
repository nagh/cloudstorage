package serv;

/*
 * Als Übergabewerte kommen in den Konstruktor ein String und ein int. (ip und port)
 * Beide müssen public sein.
 */

public class AddSlaveHandler {
	
	public String ipAddress;
	public int port;
	
	public AddSlaveHandler(String ipAddress, int port) {
		this.ipAddress = ipAddress;
		this.port = port;
	}

}
