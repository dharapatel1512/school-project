package client;

import javax.swing.*;

import server.Member;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;


public class LoginView extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	JButton btnLogIn, btnClear;
	JPanel panel;
	JLabel lblUserName,lblPassword;
	final JTextField  txtUserName,txtPassword;

	public LoginView(){
		super("Member Administration System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		lblUserName = new JLabel();
		txtUserName = new JTextField(15);
		lblPassword = new JLabel();
		txtPassword = new JPasswordField(15);
		btnLogIn=new JButton("LogIn");
		btnClear=new JButton("Clear");
		initializeLoginScreen();
		btnLogIn.addActionListener(this);
		btnLogIn.setActionCommand("Open");
		btnClear.addActionListener(new Clearistner());
	}
	
	public void initializeLoginScreen() {
		lblUserName.setText("Username:");
		lblPassword.setText("Password:");
		
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
	public void actionPerformed(ActionEvent event)
	{
		String valueUserName=txtUserName.getText();
		String valuePassword=txtPassword.getText();
		System.out.println(valueUserName);
		System.out.println(valuePassword);
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

			if(cmd.equals("Open"))
			{
				dispose();
				MainScreen design=null;
				try {
					design = new MainScreen(members, memberFromServer.get(0));
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
				design.addWindowListener(new java.awt.event.WindowAdapter() {
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





