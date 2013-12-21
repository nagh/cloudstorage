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
		
		// Response erstellen
		Response resp = new Response(data, false, req); // ???: Wofür ist der boolean-Wert?

		return resp;
	}

	@Override
	public boolean hasPriority() {   // Müssen wir EHER NICHT implementieren.
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean requiresResponse() { // Müssen wir VIELLEICHT implementieren.
		// TODO Auto-generated method stub
		return false;
	}

}
