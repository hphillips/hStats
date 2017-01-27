package hStats;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class StatQueue {

	private StatList head;
	private StatList tail;
	
	private Object tailLock = new Object();
	
	public StatQueue(){
		head=null;
		tail=null;
	}
	
	public synchronized void push(List<String> stats){
		//System.out.println("Pushing Stats");
		StatList list = new StatList(stats,tail);
		if(tail==null){
			tail=list;
			head=list;
		}else{
			synchronized(tail){
				tail.next=list;
				tail=list;
			}
		}
		//System.out.println(this);
	}
	
	public List<String> pop(){		
		//System.out.println("Removing Stats");
		if(head==null) return null;
		synchronized(head){
			StatList next;
			if(head==tail) next=head;
			else next=head.next;
			synchronized(next){
				StatList ret = head;
				head = head.next;
				if(head!=null)head.prior=null;
				if(ret==tail) tail=null;
				return ret.stats;
			}
		}
	}
	
	public synchronized List<String> synch_pop(){
		return null;
	}
	
	public boolean isEmpty(){
		return head==null;
	}
	
	private class StatList {
		
		List<String> stats;
		
		private StatList prior;
		private StatList next;
		
		private StatList(List<String> stats, StatList prior){
			this.stats=stats;
			this.prior=prior;
		}
		
		public String toString(){
			String ret = "[";
			
			for(String i:stats){
				ret+=i+",";
			}
			
			return ret.substring(0, ret.length()-1)+"]";
		}
		
		public String detailedPrint(){
			return "["+prior+","+stats+","+next+"]";
		}
				
	}
	
	public String toString(){
		String ret = "";
		StatList cur = head;
		while(cur!=null){
			ret+=cur.toString()+"\n";
			cur=cur.next;
		}
		return ret.substring(0,ret.length()-1);
	}
	
	public String detailedPrint(){
		if(head==null)return "null";
		String ret = head+"\n";
		StatList cur = head;
		while(cur!=null){
			ret+=cur.detailedPrint()+(cur==tail?"\n"+tail+"\n":"\n");
			cur=cur.next;
		}
		return ret.substring(0,ret.length()-1);
	}
		
	public static void main(String[] args) throws Exception{
		//push pop and print
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		String input;
		StatQueue sq = new StatQueue();
		int pusher=0;
		do{
			input = in.readLine();
			if(input.equals("push")){
				List<String> to_add = new ArrayList<String>();
				to_add.add(pusher+"");
				sq.push(to_add);
				pusher++;
			}else if(input.equals("pop")){
				sq.pop();
			}else if(input.equals("print")){
				System.out.println(sq);
			}else if(input.equals("detail")){
				System.out.println(sq.detailedPrint());
			}
		}while(input!="done");
		
	}
	
}
