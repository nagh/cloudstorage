package serv;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.kit.aifb.dbe.hermes.IRequestHandler;
import edu.kit.aifb.dbe.hermes.Request;
import edu.kit.aifb.dbe.hermes.Response;

public class GetHandler implements IRequestHandler {
	
	public GetHandler() {
		// empty constructor
	}

	@Override
	public Response handleRequest(Request req) {	
		// Request auslesen
		List<Serializable> items = new ArrayList<Serializable>();
		items = req.getItems();
		String key = (String) items.get(0); 
		
		// Datei "key" von der Festplatte lesen
		String data = Access.get(key);
		items.add(data);
		System.out.println("Datum erfolgreich von der Festplatte gelesen.");
		
		// Response erstellen
		List<Serializable> respitems = new ArrayList<Serializable>();
		respitems.add(key);
		respitems.add(data);
		
		String message = "Get-Request erfolgreich.";
		Response resp = new Response(respitems, message, true, req);
		return resp;
	}

	@Override
	public boolean hasPriority() {   // M�ssen wir EHER NICHT implementieren.
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean requiresResponse() { // M�ssen wir VIELLEICHT implementieren.
		// TODO Auto-generated method stub
		return false;
	}

}
