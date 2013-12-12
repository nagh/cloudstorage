package serv;

import edu.kit.aifb.dbe.hermes.AsyncCallbackRecipient;
import edu.kit.aifb.dbe.hermes.IRequestHandler;
import edu.kit.aifb.dbe.hermes.Request;
import edu.kit.aifb.dbe.hermes.Response;
// Klasse Sender importieren.

/* Wenn fertig, Klasse als PutMasterHandler.java kopieren.
Der Request, der übergeben werden muss, soll auch einen zusätzlichen Parameter auswerten, der sagt, ob der Request asynchron oder synchron weitergeleitet werden soll.

*/

public class PutSlaveHandler implements IRequestHandler, AsyncCallbackRecipient {

	public PutSlaveHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response handleRequest(Request arg0) {
		// TODO Auto-generated method stub
		return null;
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
