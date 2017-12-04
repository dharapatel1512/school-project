package client;

import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import server.Member;

/**
 * @author Dharaben Patel, Nidhi Patel
 */
public class Client {

	ArrayList<Member> members;
	Client(ArrayList<Member> members){
		this.members = members;
	}

	public ArrayList<Member> startClient() {
		Socket socketToServer = null;
		ObjectOutputStream outToServer = null;
		ObjectInputStream inFromServer = null;

		try {
			//socketToServer = new Socket("localhost", 3007); 
			socketToServer = new Socket("afs4.njit.edu", 3007);
			outToServer = new ObjectOutputStream(socketToServer.getOutputStream());
			inFromServer = new ObjectInputStream(socketToServer.getInputStream()); 
			outToServer.writeObject(this.members);
			this.members = (ArrayList<Member>) inFromServer.readObject();
			outToServer.close(); 
			inFromServer.close(); 
			socketToServer.close();
		} catch (ClassNotFoundException | IOException e) {
			
			System.out.println("Server Connection is closed now. Thank you :)" );
		}

		return this.members;
	}
	
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run()
			{
				LoginView frame=new LoginView();
				frame.setSize(350,100);
				frame.setVisible(true);
				frame.setAlwaysOnTop(true);

				frame.addWindowListener(new java.awt.event.WindowAdapter() {
					public void windowClosing(WindowEvent winEvt) {
						ClientUtil.closeTheWindow();
					}
				});

			}

		});
	}

}
