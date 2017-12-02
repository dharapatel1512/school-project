package client;

import java.util.ArrayList;
import server.Member;

public class ClientUtil {
	public static void closeTheWindow() {
		Member tempMember = new Member();
		tempMember.setMessage("close");
		ArrayList<Member> members = new ArrayList<Member>();
		members.add(0, tempMember);
		Client client = new Client(members);
		client.startClient();
		System.exit(0);
	}
	
	public static ArrayList<Member> actionPerform(ArrayList<Member> members, String action){
		members.get(0).setMessage(action);
		Client client = new Client(members);
		ArrayList<Member> memberFromServer = client.startClient();
		memberFromServer.remove(0);

		return memberFromServer;
	}
}
