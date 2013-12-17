package serv;

import java.io.*;

public class Access {
	// private String data;
	// private String key;
	public static boolean free = true;
	
	public Access() {
		// empty constructor
	}
	// Lesen und Schreiben aus/in Unterordner Data.
	// Synchronisieren: Idee: Boolean-Variable zur Steuerung.
	
	public String get(){
		free = false;
		String data = null; // lesen	
		String key = null;
		try {
			data = readFile("data/" + key);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		free = true;
	 return data;
}

	public void put(String key, String data){
		free = false;
		PrintStream out = null;
		try {
			out = new PrintStream(new FileOutputStream("data/" + key));
			out.print(data);
		}
		catch (IOException ioe) {
			System.out.println(ioe);
		}
		finally {
			if (out != null) out.close();
		}		
		free = true;
	}
	public String readFile(String fileName) throws IOException {
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
