package serv;

import edu.kit.aifb.dbe.hermes.IRequestHandler;
import edu.kit.aifb.dbe.hermes.Request;
import edu.kit.aifb.dbe.hermes.Response;

public class GetHandler implements IRequestHandler {
	
	private Request req;
	private Response resp;
	

	public GetHandler() {
		// TODO Auto-generated constructor stub
		req = this.req;
		resp = this.resp;
	}

	@Override
	public Response handleRequest(Request req) {
		// TODO Auto-generated method stub
		
		
		resp.setResponseMessage(req.getItems().toString());
		
		
		
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
