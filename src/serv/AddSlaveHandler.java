package serv;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.kit.aifb.dbe.hermes.IRequestHandler;
import edu.kit.aifb.dbe.hermes.Request;
import edu.kit.aifb.dbe.hermes.Response;

/*
 * Als Übergabewerte kommen in den Konstruktor ein String und ein int. (ip und port)
 * Beide müssen public sein.
 */

public class AddSlaveHandler implements IRequestHandler {
	
	public String ipAddress;
	public int port;
	
	public AddSlaveHandler() {
		// empty constructor
	}

	@Override
	public Response handleRequest(Request req) {
		// read request
		List<Serializable> items = new ArrayList<Serializable>();
		items = req.getItems();
		String ip = (String) items.get(0);
		int port = (Integer) items.get(1);
		// save to global variables
		this.ipAddress = ip;
		this.port = port;
		// Response erstellen und zurueckgeben
		Response resp = new Response("IP-Adresse und Port vom Slave erfolgreich �bergeben.", true, req);
		return resp;
	}

	@Override
	public boolean hasPriority() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean requiresResponse() {
		// TODO Auto-generated method stub
		return false;
	}

}
