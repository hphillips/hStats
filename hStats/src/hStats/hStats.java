package hStats;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class hStats {

	public static void main(String[] args){
		StatQueue sq = new StatQueue();
		
		LogWriter lw = new LogWriter(sq);
		lw.start();
		List<ClientHandler> handlers = new ArrayList<ClientHandler>();
		boolean keepGoing = true;
		int connectionCount=0;
		ServerSocket serverSocket = null;
		try{
			serverSocket = new ServerSocket(57475);
			while(keepGoing){
				System.out.println("Main: Waiting for new Connection");
				Socket clientSocket = serverSocket.accept();
				connectionCount++;
				System.out.println("Main: Connection "+connectionCount+" received");
				ClientHandler ch = new ClientHandler(clientSocket, connectionCount, sq);
				ch.start();
				//handlers.add(ch);				
			}
			serverSocket.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			serverSocket.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
}
