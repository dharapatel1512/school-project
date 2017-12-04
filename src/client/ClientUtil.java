package client;

import java.util.ArrayList;

import javax.swing.JButton;

import server.Member;

/**
 * @author Dharaben Patel, Nidhi Patel
 */
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

	public static int getIndex(ArrayList<Member> recordedMembers, Member newMember){

		int index = 0;
		if(recordedMembers.size() >0){

			Member oldMember = recordedMembers.get(index);
			while (newMember.compareTo(oldMember) > 0 && index < recordedMembers.size()){
				oldMember = recordedMembers.get(index);
				index++;
			}
			if (newMember.compareTo(oldMember) <0 && index != 0){
				index--;
			}
			if (newMember.compareTo(oldMember) == 0){
				return -1;
			}
		}
		return index;
	}

	public static void enableButton(JButton button, boolean alreadyEnabled) {
		if (!alreadyEnabled) {
			button.setEnabled(true);
			button.setFocusPainted(true);
		}
	}
}
