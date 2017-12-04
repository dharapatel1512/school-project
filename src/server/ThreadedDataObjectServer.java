package server;
import java.io.*; 
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Dharaben Patel, Nidhi Patel
 */
public class ThreadedDataObjectServer { 
	public static void main(String[] args ) {
		try {
			ServerSocket serverSocket = new ServerSocket(3007); 
			System.out.println("Server started");
			while(true) {
				Socket incoming = serverSocket.accept( );
				new ThreadedDataObjectHandler(incoming).start();
			}
		}
		catch (IOException e) {
			System.out.println("Exception in the Server class: " +e.getMessage());
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

		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		try {
			in = new ObjectInputStream(incoming.getInputStream());
			out = new ObjectOutputStream(incoming.getOutputStream());
			members = (ArrayList<Member>)in.readObject();
			if(members.get(0).getMessage().equals("close")) {
				System.exit(0);
			}
			else{
				DbInteraction dbCall = new DbInteraction(members);
				ArrayList<Member> dbResp = dbCall.buildQuery();
				out.writeObject(dbResp);
				in.close();
				out.close();
				incoming.close();
			}
		} catch (ClassNotFoundException | SQLException | IOException e) {
			System.out.println("Exception in the Server class: " +e.getMessage());
		}
	}
}
