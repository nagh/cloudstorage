package serv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import edu.kit.aifb.dbe.hermes.Request;
import edu.kit.aifb.dbe.hermes.Response;

import org.apache.commons.math3.stat.StatUtils;

public class TestTestTest {
	
	
	public static void main(String args[]) {
	
		// String a = Access.get("input_testfile.txt");
		// System.out.println(a);
		// Access.put("output_testfile.txt", "Weihnachten ist mit Ostern und Pfingsten eines der drei Hauptfeste des Kirchenjahres. \n Die weihnachtliche Festzeit beginnt mit der ersten Vesper von Weihnachten am Heiligabend (siehe dazu auch Christvesper) und endet in der römisch-katholischen Kirche mit dem Fest Taufe des Herrn am Sonntag nach Erscheinung des Herrn. \n Der erste liturgische Höhepunkt der Weihnachtszeit ist die Mitternachtsmesse (siehe Christmette). \n Vor der Liturgiereform von 1963 erstreckte sich der Weihnachtsfestkreis, der den Advent als Vorbereitungszeit einschließt, bis zum Fest Darstellung des Herrn am 2. Februar, umgangssprachlich Maria Lichtmess oder Mariä Lichtmess genannt.");
		
		/*
		Request req = new Request("input_testfile.txt", "targetName", null);
		
		GetHandler GET = new GetHandler();
		Response resp = GET.handleRequest(req);
		System.out.println(resp.getResponseMessage());
		*/
		
		String[] key = new String[100];
		double[] latency = new double[100];
		String sync = "true";
		Date start = null;
		Date stop = null;
		
		for (int ii = 0; ii < 100; ii++) {
			key[ii] = "key_" + sync + "_" + ii;
			start = new Date();
			try {
				Thread.sleep(90);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//putRequest(key[ii], data, sync);
			stop = new Date();
			latency[ii] = stop.getTime() - start.getTime();
		}
		
		System.out.format("%20d%n",stop.getTime());

		System.out.println(start.getTime());
		
		double lat_max = StatUtils.max(latency);
		double lat_min = StatUtils.min(latency);
		double lat_mean = StatUtils.mean(latency);
		double lat_med = StatUtils.percentile(latency, 50);
		
		// Access.put("auswertung_keys_" + sync, key.toString());
		
		// Access.put("auswertung_lat_" + sync, latency.toString());
		
		System.out.println(lat_max);
		System.out.println(lat_min);
		System.out.println(lat_mean);
		System.out.println(lat_med);
	}
}
