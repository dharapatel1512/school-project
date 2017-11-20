package org.njit.cs602.client;

import javax.swing.*;

import org.njit.cs602.server.Member;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;


public class LoginView extends JFrame implements ActionListener
{
	JButton btnLogIn;
	JPanel panel;
	JLabel lblUserName,lblPassword;
	final JTextField  txtUserName,txtPassword;

	public LoginView()
	{
		super("Member Administration System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		lblUserName = new JLabel();
		lblUserName.setText("Username:");
		txtUserName = new JTextField(10);

		lblPassword = new JLabel();
		lblPassword.setText("Password:");
		txtPassword = new JPasswordField(10);

		btnLogIn=new JButton("LogIn");

		panel=new JPanel(new GridLayout(3,1));
		panel.add(lblUserName);
		panel.add(txtUserName);
		panel.add(lblPassword);
		panel.add(txtPassword);
		panel.add(btnLogIn);

		btnLogIn.addActionListener(this);
		btnLogIn.setActionCommand("Open");
		add(panel,BorderLayout.CENTER);
		pack();

	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String valueUserName=txtUserName.getText();
		String valuePassword=txtPassword.getText();
		System.out.println(valueUserName);
		System.out.println(valuePassword);
		Member member = new Member();
		member.setMessage("login");
		member.setUsername(valueUserName);
		member.setPassword(valuePassword);
		Client client = new Client(member);
		Member memberFromServer = client.startClient();
		if(memberFromServer.getMessage().equals("success")) {
			String cmd = e.getActionCommand();

			if(cmd.equals("Open"))
			{
				dispose();
				Design design=null;
				try {
					design = new Design();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				design.addWindowListener(new java.awt.event.WindowAdapter() {
			        public void windowClosing(WindowEvent winEvt) {
			            try {
							Design.writeApp(Design.recordList);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			            System.exit(0);
			        }
				});
			        
			}
		}
	}

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run()
			{
				LoginView frame=new LoginView();
				frame.setSize(300,100);
				frame.setVisible(true);

			}

		});
	}
}





