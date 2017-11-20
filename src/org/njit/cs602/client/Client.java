package org.njit.cs602.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.njit.cs602.server.Member;

public class Client {

	Member member;
	Client(Member member){
		this.member = member;
	}

	public Member startClient() {
		try{
			//System.out.println("Message sent : " + this.member.getMessage()); 
			//Socket socketToServer = new Socket("afs4.njit.edu", 3000);
			Socket socketToServer = new Socket("localhost", 3000); 

			ObjectOutputStream outToServer =
					new ObjectOutputStream(socketToServer.getOutputStream()); 
			ObjectInputStream inFromServer =
					new ObjectInputStream(socketToServer.getInputStream()); 
			outToServer.writeObject(this.member);
			this.member = (Member) inFromServer.readObject();
			System.out.println("Messaged received : " + this.member.getMessage());
			outToServer.close(); 
			inFromServer.close(); 
			socketToServer.close();
		}
		catch(Exception e){
			System.out.println(e);
		}
		return this.member;
	}

}
