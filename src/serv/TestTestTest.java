package serv;

import edu.kit.aifb.dbe.hermes.Request;
import edu.kit.aifb.dbe.hermes.Response;

public class TestTestTest {
	
	
	public static void main(String args[]) {
	
		// String a = Access.get("input_testfile.txt");
		// System.out.println(a);
		// Access.put("output_testfile.txt", "Weihnachten ist mit Ostern und Pfingsten eines der drei Hauptfeste des Kirchenjahres. \n Die weihnachtliche Festzeit beginnt mit der ersten Vesper von Weihnachten am Heiligabend (siehe dazu auch Christvesper) und endet in der römisch-katholischen Kirche mit dem Fest Taufe des Herrn am Sonntag nach Erscheinung des Herrn. \n Der erste liturgische Höhepunkt der Weihnachtszeit ist die Mitternachtsmesse (siehe Christmette). \n Vor der Liturgiereform von 1963 erstreckte sich der Weihnachtsfestkreis, der den Advent als Vorbereitungszeit einschließt, bis zum Fest Darstellung des Herrn am 2. Februar, umgangssprachlich Maria Lichtmess oder Mariä Lichtmess genannt.");
		
		Request req = new Request("input_testfile.txt", "targetName", null);
		
		GetHandler GET = new GetHandler();
		Response resp = GET.handleRequest(req);
		System.out.println(resp.getResponseMessage());
		
	}
}
