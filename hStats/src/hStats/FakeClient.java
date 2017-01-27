package hStats;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class FakeClient {
	
	public static void main(String[] args) throws IOException{
		System.setProperty("java.net.useSystemProxies", "true");

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
					
		Socket thisComputer = new Socket("127.0.0.1", 57475);
		PrintWriter socket_out = new PrintWriter(thisComputer.getOutputStream(), true);
	    BufferedReader socket_in = new BufferedReader(
	        new InputStreamReader(thisComputer.getInputStream()));
	    
	    String curString;
		do{
			curString = in.readLine();
			socket_out.println(curString);
			socket_out.flush();
			//System.out.println("Response: "+socket_in.readLine());
		}while(!curString.equals("Done"));
		socket_out.close();
	}
	
}
