package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import server.Member;

public class Client {

	ArrayList<Member> members;
	Client(ArrayList<Member> members){
		this.members = members;
	}

	public ArrayList<Member> startClient() {
		try{
			//System.out.println("Message sent : " + this.member.getMessage()); 
			Socket socketToServer = new Socket("afs4.njit.edu", 3003);
			//Socket socketToServer = new Socket("localhost", 3000); 

			ObjectOutputStream outToServer =
					new ObjectOutputStream(socketToServer.getOutputStream()); 
			ObjectInputStream inFromServer =
					new ObjectInputStream(socketToServer.getInputStream()); 
			outToServer.writeObject(this.members);
			this.members = (ArrayList<Member>) inFromServer.readObject();
			outToServer.close(); 
			inFromServer.close(); 
			socketToServer.close();
		}
		catch(Exception e){
			System.out.println(e);
		}
		return this.members;
	}

}
