package org.njit.cs602.client;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.njit.cs602.server.Member;

/**
 * @author 
 */

public class MainScreen extends JFrame implements ListSelectionListener, Serializable{

	private static final long serialVersionUID=1L;
	public static final String storeDir = "Resources";
	public static final String storeFile = "MemberRecord.txt";



	private JButton button_add, button_edit, button_delete, button_cancel, button_done, button_logout;
	private JLabel label_full_name, label_email, label_phone_num, label_dob, label_member_list;
	private static JTextField text_full_name, text_email, text_phone_num, text_dob;
	private static JTextArea text_log;
	private static JTextArea text_details;
	private ArrayList<Member> members;
	private Member member;

	private JList list;
	private DefaultListModel listModel;

	public static ArrayList<Member> recordList;

	public MainScreen(ArrayList<Member> members, Member member) throws IOException, ClassNotFoundException{
		super();
		this.members=members;
		this.member = member;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout(FlowLayout.LEFT,10,10));

		recordList = readApp(this.members);
		if(recordList == null){
			recordList = new ArrayList<Member>();
		}

		label_full_name = new JLabel("Enter Full Name:");
		label_email = new JLabel("Enter Email Address:");
		label_phone_num = new JLabel("Enter Phone No:");
		label_dob = new JLabel("Enter Date Of Birth:");
		label_member_list = new JLabel("Member List");
		label_member_list.setFont(new Font("Serif", Font.BOLD, 14));
		//label_member_list.setAlignmentY(BOTTOM_ALIGNMENT);
		label_member_list.setAlignmentY(LEFT_ALIGNMENT);


		text_full_name = new JTextField("");
		text_email = new JTextField("");
		text_phone_num = new JTextField("");
		text_dob = new JTextField("");

		button_add = new JButton("Add");
		button_edit = new JButton("Edit");
		button_delete = new JButton("Delete");
		button_cancel = new JButton("Cancel");
		button_done = new JButton("Done");
		button_logout = new JButton("Logout");
		button_logout.setAlignmentX(RIGHT_ALIGNMENT);


		HireListener hireListener = new HireListener(button_add);

		button_add.addActionListener(hireListener);
		button_add.setEnabled(false);

		text_full_name.getDocument().addDocumentListener(hireListener);

		text_email.getDocument().addDocumentListener(hireListener);
		text_phone_num.addActionListener(hireListener);
		text_dob.addActionListener(hireListener);


		button_delete.addActionListener(new FireListener());
		button_edit.addActionListener(new EditListener());
		button_cancel.addActionListener(new CancelListener());
		button_done.addActionListener(new DoneListener());
		button_logout.addActionListener(new LogoutListner());

		button_done.setEnabled(false);
		button_cancel.setEnabled(false);

		if(recordList.size() ==0){
			button_delete.setEnabled(false);
			button_edit.setEnabled(false);
		}


		text_details = new JTextArea(4,28);
		text_details.setEditable(false);
		JPanel topRight = new JPanel();
		topRight.setBorder(BorderFactory.createTitledBorder("Member Details"));
		text_details.setFont(new Font("Serif", Font.ITALIC, 14));
		text_details.setBackground(getBackground());
		topRight.add(text_details);


		text_log = new JTextArea(1,28);
		text_details.setEditable(false);
		JPanel bottomRight = new JPanel();
		bottomRight.setBorder(BorderFactory.createTitledBorder("Log"));
		text_log.setBackground(getForeground());
		text_log.setFont(new Font("Serif", Font.ITALIC, 14));
		text_log.setText("");
		text_log.setEditable(false);
		bottomRight.add(text_log);


		/* Panel on the right half of the base container*/

		JPanel rightPanel = new JPanel(new GridLayout(0,2));

		rightPanel.add(label_full_name);
		rightPanel.add(text_full_name);
		rightPanel.add(label_email);
		rightPanel.add(text_email);
		rightPanel.add(label_phone_num);
		rightPanel.add(text_phone_num);
		rightPanel.add(label_dob);
		rightPanel.add(text_dob);

		rightPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));		

		JPanel mainRightPanel = new JPanel();
		mainRightPanel.setLayout(new BoxLayout(mainRightPanel,BoxLayout.Y_AXIS));
		mainRightPanel.add(topRight);
		if(this.member.getType().equalsIgnoreCase("admin")) {
			mainRightPanel.add(rightPanel);
		}
		mainRightPanel.add(bottomRight);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane,BoxLayout.LINE_AXIS));
		if(this.member.getType().equalsIgnoreCase("admin")) {
			buttonPane.add(button_cancel);
			buttonPane.add(Box.createHorizontalStrut(5));
			buttonPane.add(button_done);
			buttonPane.add(Box.createHorizontalStrut(5));
			buttonPane.add(button_add);
		}
		buttonPane.add(button_logout);
		buttonPane.setBorder(BorderFactory.createEmptyBorder(10,5,5,5));

		mainRightPanel.add(buttonPane);

		/* Panel on the left half of the base container*/

		JPanel leftPanel = new JPanel();
		leftPanel.add(label_member_list);	
		leftPanel.setLayout(new BoxLayout(leftPanel,BoxLayout.Y_AXIS));



		listModel = new DefaultListModel();

		// inflate listModel
		for(int j =0; j<recordList.size(); j++){
			listModel.addElement(recordList.get(j).getFullname());
		}
		if(recordList.size() !=0){
			text_details.setText(" Full Name: " + recordList.get(0).getFullname() +  "\n Email Adddress: " + recordList.get(0).getEmail() + "\n  PhoneNo: " + recordList.get(0).getPhoneNo() + "\n  DOB: " + recordList.get(0).getDob());
		}


		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addListSelectionListener(this);
		list.setVisibleRowCount(5);

		JScrollPane listScrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		listScrollPane.setPreferredSize(new Dimension(200,280));


		leftPanel.add(listScrollPane);


		JPanel leftButtonPane = new JPanel();
		leftButtonPane.setLayout(new BoxLayout(leftButtonPane,
				BoxLayout.LINE_AXIS));

		leftButtonPane.add(button_delete);
		leftButtonPane.add(Box.createHorizontalStrut(5));
		leftButtonPane.add(new JSeparator(SwingConstants.VERTICAL));
		leftButtonPane.add(button_edit);
		leftButtonPane.add(Box.createHorizontalStrut(5));
		leftButtonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		if(this.member.getType().equalsIgnoreCase("admin")) {
			leftPanel.add(leftButtonPane);
		}

		add(leftPanel);
		add(mainRightPanel);


		pack();
		setVisible(true);

	}

	public int getIndex(Member newMember){

		int index = 0;
		if(recordList.size() >0){

			Member oldMember = recordList.get(index);

			while (newMember.compareTo(oldMember) > 0 && index < recordList.size()){
				oldMember = recordList.get(index);
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


	public static void writeApp(ArrayList<Member> gapp) throws IOException{
		ObjectOutputStream oos = new ObjectOutputStream(
				new FileOutputStream(storeDir + File.separator + storeFile));
		oos.writeObject(gapp);
	}


	public static ArrayList<Member> readApp(ArrayList<Member> members)
			throws IOException, ClassNotFoundException {

		//		File f = new File(storeDir + File.separator + storeFile);
		//		ArrayList<Member> gapp = null;
		//
		//		try {
		//			if( f.length() == 0 ){
		//				f.createNewFile();
		//			} else {
		//				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
		//				gapp = (ArrayList<Member>) ois.readObject();
		//			}
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		}
		members.get(0).setMessage("load");
		Client client = new Client(members);
		ArrayList<Member> memberFromServer = client.startClient();
		memberFromServer.remove(0);

		return memberFromServer;
	}

	public static ArrayList<Member> addMember(ArrayList<Member> members)
			throws IOException, ClassNotFoundException {
		members.get(0).setMessage("add");
		Client client = new Client(members);
		ArrayList<Member> memberFromServer = client.startClient();
		memberFromServer.remove(0);

		return memberFromServer;
	}

	public static ArrayList<Member> deleteMember(ArrayList<Member> members)
			throws IOException, ClassNotFoundException {
		members.get(0).setMessage("delete");		
		Client client = new Client(members);
		ArrayList<Member> memberFromServer = client.startClient();
		memberFromServer.remove(0);

		return memberFromServer;
	}

	public static ArrayList<Member> updateMember(ArrayList<Member> members)
			throws IOException, ClassNotFoundException {
		members.get(0).setMessage("update");
		Client client = new Client(members);
		ArrayList<Member> memberFromServer = client.startClient();
		memberFromServer.remove(0);

		return memberFromServer;
	}


	//This method is required by ListSelectionListener.
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {

			if (list.getSelectedIndex() == -1) {
				//No selection, disable fire button.
				// button_delete.setEnabled(false);
				text_details.setText("");
				//System.out.println("shoot" + list.getSelectedIndex());

			} else {
				//Selection, enable the fire button.
				//fireButton.setEnabled(true);

				//System.out.println("shootfsafa" + list.getSelectedIndex());
				String member,email,phone,dob;
				int memberId;

				member = recordList.get(list.getSelectedIndex()).getFullname();
				email = recordList.get(list.getSelectedIndex()).getEmail();
				phone = recordList.get(list.getSelectedIndex()).getPhoneNo();
				dob =recordList.get(list.getSelectedIndex()).getDob();

				text_details.setText(" Full Name: " + member +  "\n Email: " + email + "\n PhoneNo : " + phone + "\n DOB: " + dob);


			}
		}
	}

	class LogoutListner implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dispose();
			new LoginView().main(null);
		}
	}

	class DoneListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {


			if(listModel.size() ==0){
				return;
			}


			Member newMember = new Member(text_full_name.getText().trim(),
					text_email.getText().trim(),text_phone_num.getText().trim(),
					text_dob.getText().trim());

			//User didn't type in a unique name...
			if (newMember.getFullname().equals("")) {// truncate it as well
				Toolkit.getDefaultToolkit().beep();
				text_full_name.requestFocusInWindow();
				text_log.setText("Please enter the name of the member.");
				text_full_name.selectAll();
				return;
			}

			if(newMember.getEmail().equals("")){// truncate it 
				Toolkit.getDefaultToolkit().beep();
				text_email.requestFocusInWindow();
				text_email.selectAll();
				text_log.setText("Please enter the email of the member.");
				return;
			}

			int indexOfEditedMember = list.getSelectedIndex();

			System.out.println("####"+newMember.compareTo(recordList.get(indexOfEditedMember)));
			if(newMember.compareTo(recordList.get(indexOfEditedMember)) == 0){

				newMember.setMemberId(recordList.get(indexOfEditedMember).getMemberId());
				recordList.get(indexOfEditedMember).setPhoneNo(newMember.getPhoneNo());
				recordList.get(indexOfEditedMember).setDob(newMember.getDob());

				ArrayList<Member> members = new ArrayList<Member>();

				members.add(recordList.get(indexOfEditedMember));
				try {
					updateMember(members);
				} catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}


			}

			else{
				int index = getIndex(newMember);

				if (index ==-1) {
					Toolkit.getDefaultToolkit().beep();
					text_full_name.requestFocusInWindow();
					text_full_name.selectAll();
					//text_log.setText("member already exists in the db.");
					return;
				}
				else{
					if(index <= indexOfEditedMember){
						newMember.setMemberId(recordList.get(indexOfEditedMember).getMemberId());
						recordList.remove(indexOfEditedMember);
						listModel.remove(indexOfEditedMember);

						recordList.add(index, newMember);

						ArrayList<Member> members = new ArrayList<Member>();

						members.add(recordList.get(index));
						try {
							updateMember(members);
						} catch (ClassNotFoundException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						listModel.add(index, newMember.getFullname());
						list.setSelectedIndex(index);
					}
					else{
						newMember.setMemberId(recordList.get(indexOfEditedMember).getMemberId());
						recordList.add(index, newMember);
						listModel.add(index, newMember.getFullname());

						ArrayList<Member> members = new ArrayList<Member>();

						members.add(recordList.get(index));
						try {
							updateMember(members);
						} catch (ClassNotFoundException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						recordList.remove(indexOfEditedMember);
						listModel.remove(indexOfEditedMember);
						list.setSelectedIndex(index-1);
					}
				}
			}


			text_details.setText(" Full Name: " + newMember.getFullname()
			+  "\n Email Address: " + newMember.getEmail() 
			+ "\n PhoneNo: " + newMember.getPhoneNo()
			+ "\n DOB: " + newMember.getDob());

			text_full_name.setText("");
			text_email.setText("");
			text_phone_num.setText("");
			text_dob.setText("");	 

			button_cancel.setEnabled(false);
			button_done.setEnabled(false);
			button_edit.setEnabled(true);
			text_log.setText("Successfully edited!");
			list.enable();

		}
	}

	class CancelListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {


			text_full_name.setText("");
			text_email.setText("");
			text_phone_num.setText("");
			text_dob.setText("");	 

			button_cancel.setEnabled(false);
			button_done.setEnabled(false);
			button_edit.setEnabled(true);
			text_log.setText("");
			list.enable();
		}
	}


	class EditListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			text_log.setText("");
			if(listModel.size() ==0){
				return;
			}

			int index = list.getSelectedIndex();
			list.disable();
			text_full_name.setText(recordList.get(index).getFullname());
			text_email.setText(recordList.get(index).getEmail());
			text_phone_num.setText(recordList.get(index).getPhoneNo());
			text_dob.setText(recordList.get(index).getDob());	 

			button_cancel.setEnabled(true);
			button_done.setEnabled(true);
			button_edit.setEnabled(false);
			//getRootPane().setDefaultButton(button_done);
			text_full_name.requestFocusInWindow();

			//text_log.setText("Select Done to save changes.");
		}
	}


	class FireListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//This method can be called only if
			//there's a valid selection
			//so go ahead and remove whatever's selected.

			if(listModel.size() ==0){
				return;
			}

			list.enable();
			int index = list.getSelectedIndex();

			listModel.remove(index);

			ArrayList<Member> members = new ArrayList<Member>();
			members.add(recordList.get(index));
			try {
				deleteMember(members);
			} catch (ClassNotFoundException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			recordList.remove(index);

			int size = listModel.getSize();

			if (size == 0) { //Nobody's left, disable firing.

				button_delete.setEnabled(false);
				button_edit.setEnabled(false);

			} else { //Select an index.
				if (index == listModel.getSize()) {
					//removed item in last position
					index--;
				}

				list.setSelectedIndex(index);
				list.ensureIndexIsVisible(index);
			}
			text_log.setText("Successfully deleted!");
			text_full_name.setText("");
			text_email.setText("");
			text_phone_num.setText("");
			text_dob.setText("");	 
			button_cancel.setEnabled(false);
			button_done.setEnabled(false);

		}
	}




	//This listener is shared by the text field and the hire button.
	class HireListener implements ActionListener, DocumentListener {
		private boolean alreadyEnabled = false;
		private JButton button;

		public HireListener(JButton button) {
			this.button = button;
		}

		//Required by ActionListener.
		public void actionPerformed(ActionEvent e) {


			Member newMember = new Member(text_full_name.getText().trim(),
					text_email.getText().trim(),text_phone_num.getText().trim(),
					text_dob.getText().trim());

			//User didn't type in a unique name...
			if (newMember.getFullname().equals("")) {// truncate it as well
				Toolkit.getDefaultToolkit().beep();
				text_full_name.requestFocusInWindow();
				text_log.setText("Please enter the name of the member.");
				text_full_name.selectAll();
				return;
			}

			if(newMember.getEmail().equals("")){// truncate it 
				Toolkit.getDefaultToolkit().beep();
				text_email.requestFocusInWindow();
				text_email.selectAll();
				text_log.setText("Please enter the email of the member.");
				return;
			}

			int index = getIndex(newMember);

			if (index ==-1) {
				Toolkit.getDefaultToolkit().beep();
				text_full_name.requestFocusInWindow();
				text_full_name.selectAll();
				//text_log.setText("member already exists in the db.");
				return;
			}

			ArrayList<Member> members = new ArrayList<Member>();
			newMember.setMemberId(new Random().nextInt(1000)+1);

			members.add(newMember);
			try {
				addMember(members);
			} catch (ClassNotFoundException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			recordList.add(index, newMember);

			listModel.insertElementAt(newMember.getFullname(), index);
			//If we just wanted to add to the end, we'd do this:
			//listModel.addElement(employeeName.getText());

			//Reset the text field.
			text_full_name.requestFocusInWindow();
			text_full_name.setText("");
			text_email.setText("");
			text_phone_num.setText("");
			text_dob.setText("");
			text_log.setText("Successfully added! ");
			list.enable();
			//Select the new item and make it visible.
			list.setSelectedIndex(index);
			list.ensureIndexIsVisible(index);

			button_cancel.setEnabled(false);
			button_done.setEnabled(false);
			button_edit.setEnabled(true);
			if (!button_delete.isEnabled()) {
				button_delete.setEnabled(true);
				button_edit.setEnabled(true);
			}
		}

		//Required by DocumentListener.
		public void insertUpdate(DocumentEvent e) {
			enableButton();
		}

		//Required by DocumentListener.
		public void removeUpdate(DocumentEvent e) {
			handleEmptyTextField(e);
		}

		//Required by DocumentListener.
		public void changedUpdate(DocumentEvent e) {
			if (!handleEmptyTextField(e)) {
				enableButton();
			}
		}

		private void enableButton() {
			if (!alreadyEnabled) {
				button.setEnabled(true);
				button.setFocusPainted(true);
			}
		}

		private boolean handleEmptyTextField(DocumentEvent e) {
			if (e.getDocument().getLength() <= 0) {
				button.setEnabled(false);
				alreadyEnabled = false;
				return true;
			}
			return false;
		}
	}
	/**
	 * @return the member
	 */
	public ArrayList<Member> getMembers() {
		return members;
	}

	/**
	 * @param member the member to set
	 */
	public void setMembers(ArrayList<Member> members) {
		this.members = members;
	}
}
