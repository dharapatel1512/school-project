package client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.ArrayList;

import server.Member;

/**
 * @author Dharaben Patel, Nidhi Patel
 */
public class LoginView extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	JButton btnLogIn, btnClear;
	JPanel panel;
	JLabel lblUserName,lblPassword;
	final JTextField  txtUserName,txtPassword;

	public LoginView(){
		super("Member Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		lblUserName = new JLabel();
		lblUserName.setText("Username:");
		txtUserName = new JTextField(15);
		lblPassword = new JLabel();
		lblPassword.setText("Password:");
		txtPassword = new JPasswordField(15);
		btnLogIn=new JButton("LogIn");
		btnClear=new JButton("Clear");

		btnLogIn.addActionListener(this);
		btnLogIn.setActionCommand("Open");
		btnClear.addActionListener(new Clearistner());

		panel=new JPanel(new GridLayout(3,1));
		panel.add(lblUserName);
		panel.add(txtUserName);
		panel.add(lblPassword);
		panel.add(txtPassword);
		panel.add(btnLogIn);
		panel.add(btnClear);

		this.add(panel,BorderLayout.CENTER);
		pack();
	}

	@Override
	public void actionPerformed(ActionEvent event){
		String valueUserName=txtUserName.getText();
		String valuePassword=txtPassword.getText();
		ArrayList<Member> members = new ArrayList<Member>();
		Member member = new Member();
		member.setMessage("login");
		member.setUsername(valueUserName);
		member.setPassword(valuePassword);
		members.add(member);
		Client client = new Client(members);
		ArrayList<Member> memberFromServer = client.startClient();
		if(memberFromServer.get(0).getMessage().equals("success")) {
			String cmd = event.getActionCommand();
			if(cmd.equals("Open")) {
				dispose();
				MainScreen mainscreen=null;
				mainscreen = new MainScreen(members, memberFromServer.get(0));
				mainscreen.setSize(570,335);
				mainscreen.addWindowListener(new java.awt.event.WindowAdapter() {
					public void windowClosing(WindowEvent winEvt) {			            
						ClientUtil.closeTheWindow();
					}
				});
			}
		}
	}

	class Clearistner implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			txtUserName.setText(null);
			txtPassword.setText(null);
		}
	}
}





