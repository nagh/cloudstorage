package serv;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import client.ClientMain;
import edu.kit.aifb.dbe.hermes.AsyncCallbackRecipient;
import edu.kit.aifb.dbe.hermes.IRequestHandler;
import edu.kit.aifb.dbe.hermes.Request;
import edu.kit.aifb.dbe.hermes.Response;
import edu.kit.aifb.dbe.hermes.Sender;

/* 
 * Kopiert von PutSlaveHandler
 * Aenderungen: PutMasterHandler besitzt immer einen Slave. Als zusaetzlichen Payload-Parameter soll dieser Handler daher
 * ein Boolean erwarten und in seiner Methode handleRequest abhaengig von dieser Booleanvar entweder asynchron (sendMessageAsync)
 * oder synchron (sendMessage) arbeiten.
 * 
 * If(sync){sendMessage(); persist(); return...;} else {persist();sendAsync(); return...;}
*/

public class PutMasterHandler implements IRequestHandler, AsyncCallbackRecipient {
		
		private boolean sync;
		private static int timeout = 1000; // Timeout in [ms]
		
	public PutMasterHandler(boolean sync) {
		this.sync = sync;
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
		// Slave ist immer vorhanden. Request asynchron weitersenden falls gewuenscht.
		Sender sender = new Sender(ClientMain.addSlaveHandler1.ipAddress, ClientMain.addSlaveHandler1.port);
		if(!sync)
			sender.sendMessageAsync(req, null);
		
		else 
			sender.sendMessage(req, timeout);
	
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
	public void callback(Response arg0) { // kann leer gelassen werden
		// TODO Auto-generated method stub
		
	}

}
