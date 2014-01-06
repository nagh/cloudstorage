package serv;

import java.io.*;
import java.util.Date;

public class Access {
	// private String data;
	// private String key;
	public static boolean free = true;
	
	public Access() {
		// empty constructor
	}
	// Lesen und Schreiben aus/in Unterordner Data.
	// Synchronisieren: Idee: Boolean-Variable zur Steuerung.
	// C: Wir k�nnten noch einen if/else-Block in die Methoden put() und get() packen. Sowas wie: if free = true { execute... } else {throw exception}. 
	// C: Bin mir aber noch nicht ganz sicher ob wir das so brauchen oder nicht.
	// A: Check.
	
	public static String get(String key){
		
		String data = null;
		while (!free) { // optional noch eine weitere Abbruchbedingung. for-Schleife mit Zaehler?
			try {
				Thread.sleep(50);
			}
			catch (InterruptedException iex) {
				Thread.currentThread().interrupt();
			}
		}
			free = false;
			try {
				data = readFile("data/" + key);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			free = true;
			
	 return data;
}

	public static void put(String key, String data){		
		while (!free) { // Auch hier: optional noch eine weitere Abbruchbedingung.
			try {
				Thread.sleep(50);
			}
			catch (InterruptedException iex) {
				Thread.currentThread().interrupt();
			}
		}
		free = false;
		PrintStream out = null;
		PrintStream log = null;
		Date date = new Date();
		try {
			// write to file
			out = new PrintStream(new FileOutputStream("data/" + key));
			out.print(data);
			// save log (Task 2.1)
			long time = date.getTime();
			log = new PrintStream(new FileOutputStream("log/" + key));
			log.print(time);
		}
		catch (IOException ioe) {
			System.out.println(ioe);
		}
		finally {
			if (out != null) out.close();
			if (log != null) log.close();
		}		
		free = true;
	}
	
	public static String readFile(String fileName) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(fileName));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}
}
