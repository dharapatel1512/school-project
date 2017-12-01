package server;
import java.io.*; 
import java.net.*;
import java.util.ArrayList;

public class ThreadedDataObjectServer { 
	public static void main(String[] args ) {
		try {
			ServerSocket s = new ServerSocket(3003); 
			while(true) {
				Socket incoming = s.accept( );
				new ThreadedDataObjectHandler(incoming).start();
			} 
		}
		catch (Exception e) {
			System.out.println(e);
		} 
	}
}
class ThreadedDataObjectHandler extends Thread {
	ArrayList<Member> members = null;
	private Socket incoming; 
	public ThreadedDataObjectHandler(Socket i) {
		incoming = i; 
	}
	@SuppressWarnings("unchecked")
	public void run() { 
		try {
			ObjectInputStream in =
					new ObjectInputStream(incoming.getInputStream());
			ObjectOutputStream out =
					new ObjectOutputStream(incoming.getOutputStream());
			members = (ArrayList<Member>)in.readObject(); 

			DbInteraction dbCall = new DbInteraction(members);
			ArrayList<Member> dbResp = dbCall.buildQuery();
			
			out.writeObject(dbResp);
			in.close();
			out.close();
			incoming.close();
		}
		catch (Exception e) { 
			e.printStackTrace();
		} 
	}
}
