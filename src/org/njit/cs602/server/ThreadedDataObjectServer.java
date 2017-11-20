package org.njit.cs602.server;
import java.io.*; 
import java.net.*;

public class ThreadedDataObjectServer { 
	public static void main(String[] args ) {
		try {
			ServerSocket s = new ServerSocket(3000); 
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
	Member member = null;
	private Socket incoming; 
	public ThreadedDataObjectHandler(Socket i) {
		incoming = i; 
	}
	public void run() { 
		try {
			ObjectInputStream in =
					new ObjectInputStream(incoming.getInputStream());
			ObjectOutputStream out =
					new ObjectOutputStream(incoming.getOutputStream());
			member = (Member)in.readObject(); 
			//System.out.println("Message read: " + member.getMessage());
			System.out.println("Message read: " + member.getUsername());
			System.out.println("Message read: " + member.getPassword());

			DbInteraction dbCall = new DbInteraction(member);
			String dbResp = dbCall.buildQuery();
			
			if(dbResp!=null) {
				//member.setMessage("success");
				//member.getMessage();
				System.out.println("Message written: " + member.getMessage()); 
			}
			//System.out.println("Message written: " + member.getMessage()); 
			out.writeObject(member);
			in.close();
			out.close();
			incoming.close();
		}
		catch (Exception e) { 
			e.printStackTrace();
		} 
	}
}
