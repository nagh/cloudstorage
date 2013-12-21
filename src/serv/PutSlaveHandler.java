package serv;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.kit.aifb.dbe.hermes.AsyncCallbackRecipient;
import edu.kit.aifb.dbe.hermes.IRequestHandler;
import edu.kit.aifb.dbe.hermes.Request;
import edu.kit.aifb.dbe.hermes.Response;
import edu.kit.aifb.dbe.hermes.Sender;
// Klasse Sender importieren.

/* Wenn fertig, Klasse als PutMasterHandler.java kopieren.
Der Request, der übergeben werden muss, soll auch einen zusätzlichen Parameter auswerten, der sagt, ob der Request asynchron oder synchron weitergeleitet werden soll.

*/

public class PutSlaveHandler implements IRequestHandler, AsyncCallbackRecipient {
		
	private boolean hasSlave;
	
	public PutSlaveHandler(boolean hasSlave) {
		this.hasSlave = hasSlave;
	}

	@Override
	public Response handleRequest(Request req) {
		// Request auslesen
		List<Serializable> items = new ArrayList<Serializable>();
		items = req.getItems();
		String key = (String) items.get(0);
		String data = (String) items.get(1);
		// Datum auf Festplattte schreiben
		Access.put(key, data);
		// Falls weiterer Slave vorhanden, Request asynchron weitersenden
		if  (hasSlave == true) {
			Sender sender = new Sender(/*String*/ host, /*int*/ port);
			sender.sendMessageAsync(req, null);
		}
		// Response erstellen und zurueckgeben
		Response resp = new Response("Daten erfolgreich persistiert", true, req);
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

	@Override
	public void callback(Response arg0) {
		// TODO Auto-generated method stub
		
	}

}
