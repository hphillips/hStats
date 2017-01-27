package hStats;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * 
 * Gonna have to figure out how to exit cleanly
 *
 */
public class LogWriter extends Thread{

	private StatQueue sq;
		
	public LogWriter(StatQueue sq){
		this.sq=sq;
		this.setDaemon(true);
	}
	
	private long firstWrite = System.currentTimeMillis();
	private boolean anyWritten=false;
	private boolean quit = false;
	
	public void run(){
		//System.out.println("Log Starting");
		File stats = new File("stats_"+System.currentTimeMillis()+".txt");
		FileWriter fw = null;
		try {
			fw = new FileWriter(stats);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		while(!quit){
			if(System.currentTimeMillis()-firstWrite>=1000*15 && anyWritten){
				try{
					fw.close();
					System.out.println("File Written: "+stats.getAbsolutePath());
					stats = new File("stats_"+System.currentTimeMillis()+".txt");
					fw = new FileWriter(stats);
					//System.out.println("New File: "+stats.getAbsolutePath());
					anyWritten = false;
				}catch(IOException e){
					e.printStackTrace();
				}
				
			}
			if(sq.isEmpty()){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				System.out.println("Grabbing Stats");
				try{
					for(String s:sq.pop()){
						//System.out.println(s);
						fw.write(s);
						fw.write(System.lineSeparator());
					}				
					fw.flush();
				}catch(IOException e){
					e.printStackTrace();
				}
				if(!anyWritten)firstWrite = System.currentTimeMillis();
				anyWritten=true;
				
			}
		}
		try{
			fw.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
}
