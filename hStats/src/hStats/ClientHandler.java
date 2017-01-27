package hStats;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

public class ClientHandler extends Thread {

	public static long TIMEOUT = 100*1000; /*time in milliseconds to disconnect from a nonresponsive connection*/
	public static long SLEEP_TIME = 1000; /*time to sleep if no message waiting*/
	
	private Socket connection;
	private int handlerID;
	private StatQueue sq;
	
	public ClientHandler(Socket connection, int handlerID, StatQueue sq){
		this.connection=connection;
		this.handlerID = handlerID;	
		this.sq=sq;
		this.setDaemon(true); /*will be changed with a real stats system - leaving as true for now so I don't keep JVMs up while I don't know if this works*/
		
	}
	
	public void run() {
		System.out.println(this.handlerID+": Starting");
		BufferedReader in = null;
		PrintWriter out = null;
		List<String> logging = new LinkedList<String>();
		try {
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			out= new PrintWriter(connection.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		long lastTime = System.currentTimeMillis();
		
		boolean quit=false;
		String currentComm = null;
		do{
			try {
				if(in.ready()){
					currentComm = in.readLine();
					//System.out.println(this.handlerID+": "+currentComm);
					if(currentComm.equals("Done")){
						//System.out.println(this.handlerID+": Done?");
						quit=true;
					}else{
						logging.add(currentComm);
						//out.println(currentComm);
						out.flush();
					}
					lastTime = System.currentTimeMillis();
				}else{
					if(System.currentTimeMillis()-lastTime>=TIMEOUT){
						quit=true;
					}else{
						//System.out.println(this.handlerID+": Sleeping");
						Thread.sleep(SLEEP_TIME);
					}
				}					
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println(this.handlerID+": Am I done?");
		}while(!quit);
		System.out.println(this.handlerID+": Closing Connection");
		
		sq.push(logging);
		//close the connection
		out.println(currentComm);
		out.close();
		
	}
	
	public static void main(String[] args){
		
	}

}
