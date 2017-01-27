package hStats;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class FakeClient2 extends Thread{
	
	private int num;
	
	public FakeClient2(int num){
		this.num=num;
	}
	
	
	public void run(){
		System.setProperty("java.net.useSystemProxies", "true");
				
		Socket thisComputer = null;
		PrintWriter socket_out = null;
		try {
			thisComputer = new Socket("127.0.0.1", 57475);
			System.out.println(num+": Starting");
			socket_out = new PrintWriter(thisComputer.getOutputStream(), true);
			
			for(int i = 0; i<100; i++){
				socket_out.println(i);
				//System.out.println(i);
			}
			socket_out.println("Done");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		socket_out.close();
	    System.out.println(num+": All Done!");
	    
	}
	
	public static void main(String[] args) throws IOException{
		for(int i = 0; i<100; i++){
			new FakeClient2(i).start();
		}
		System.out.println("Done creating threads");
	}
	
}
