package client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
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

        if(action.equals("getUser")){
            String email = memberFromServer.get(memberFromServer.size()-1).getEmail();
            String username = memberFromServer.get(memberFromServer.size()-1).getFullname();
            String password = memberFromServer.get(memberFromServer.size() -1).getPassword();



            Socket smtpSocket = null;
            DataOutputStream os = null;
            BufferedReader is = null;

            try {
                System.out.println("Start");
                smtpSocket = new Socket("mail.njit.edu", 25);
                System.out.println("Connected to mail.njit.edu");
                os = new DataOutputStream(smtpSocket.getOutputStream());
                is = new BufferedReader(new InputStreamReader(smtpSocket.getInputStream()));

            } catch (UnknownHostException e) {
                System.err.println("Don't know about host: hostname");
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection to: hostname");
            }

            if (smtpSocket != null && os != null && is != null) {
                try {

                    String response;

                    String command = "HELO mail.njit.edu\n";
                    //System.out.print(command);
                    os.write(command.getBytes("US-ASCII"));
                    response = is.readLine();
                    System.out.println("Server " + response);

                    command = "MAIL FROM: np397@njit.edu\n";
                    // System.out.print(command);
                    os.write(command.getBytes("US-ASCII"));
                    response = is.readLine();
                    System.out.println("Server " + response);

                    command = "RCPT TO: "+ email + "\n";
                    //System.out.print(command);
                    os.write(command.getBytes("US-ASCII"));
                    response = is.readLine();
                    System.out.println("Server " + response);

                    command = "DATA\n";
                    //System.out.print(command);
                    os.write(command.getBytes("US-ASCII"));
                    response = is.readLine();
                    System.out.println("Server " + response);

                    command = "Hi "+ username +", your password is " + password +".\n";
                    //System.out.print(command);
                    os.write(command.getBytes("US-ASCII"));
                    response = is.readLine();
                    System.out.println("Server " + response);

                    command = ".\n";
                    //System.out.print(command);
                    os.write(command.getBytes("US-ASCII"));


                    command = "QUIT\n";
                    //System.out.print(command);
                    os.write(command.getBytes("US-ASCII"));

                    response = is.readLine();
                    System.out.println("Server " + response);


                    os.close();
                    is.close();
                    smtpSocket.close();
                } catch (UnknownHostException e) {
                    System.err.println("Trying to connect to unknown host: " + e);
                } catch (IOException e) {
                    System.err.println("IOException:  " + e);
                }
            }

        }

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