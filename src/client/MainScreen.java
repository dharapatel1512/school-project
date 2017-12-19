package client;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Random;

import server.Member;

/**
 * @author Dharaben Patel, Nidhi Patel
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
	private int selectedIndex;
	private JList<String> list;
	private DefaultListModel<String> listModel;
	public static ArrayList<Member> recordList;
	private JPanel rightPanel, bottomRight;


	public MainScreen(ArrayList<Member> members, Member member){
		super("Member Administration System");
		this.members=members;
		this.member = member;
		this.selectedIndex = 0;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout(FlowLayout.LEFT,10,10));

		recordList = ClientUtil.actionPerform(this.members, "load");
		if(recordList == null){
			recordList = new ArrayList<Member>();
		}

		label_full_name = new JLabel("Enter Full Name:");
		label_email = new JLabel("Enter Email Address:");
		label_phone_num = new JLabel("Enter Phone No:");
		label_dob = new JLabel("Enter Date Of Birth:");
		label_member_list = new JLabel("Member List");
		label_member_list.setFont(new Font("Serif", Font.BOLD, 14));
		label_member_list.setAlignmentY(LEFT_ALIGNMENT);

		text_full_name = new JTextField("");
		text_email = new JTextField("");
		text_phone_num = new JTextField("");
		text_dob = new JTextField("");

		button_add = new JButton("Add");
		button_add.setEnabled(false);
		button_edit = new JButton("Edit");
		button_delete = new JButton("Delete");
		button_cancel = new JButton("Cancel");
		button_done = new JButton("Done");
		button_logout = new JButton("Logout");
		button_logout.setAlignmentX(RIGHT_ALIGNMENT);

		AddListener hireListener = new AddListener(button_add);
		button_add.addActionListener(hireListener);
		text_full_name.getDocument().addDocumentListener(hireListener);
		text_email.getDocument().addDocumentListener(hireListener);
		text_phone_num.addActionListener(hireListener);
		text_dob.addActionListener(hireListener);

		button_delete.addActionListener(new DeleteListener());
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

		JPanel topRight = createTopRight();
		bottomRight = createBottomRight();
		rightPanel = createRightPanel();

		JPanel mainRightPanel = createMainRightPanel(topRight, rightPanel, bottomRight);
		JPanel leftButtonPane = createLeftButtonPane();
		JPanel leftPanel = createLeftPanel(leftButtonPane);

		add(leftPanel);
		add(mainRightPanel);

		hideIfMember();

		pack();
		setVisible(true);
	}

	private JPanel createTopRight() {
		JPanel topRight = new JPanel();
		topRight.setBorder(BorderFactory.createTitledBorder("Member Details"));
		text_details = new JTextArea(4,28);
		text_details.setEditable(false);
		text_details.setFont(new Font("Serif", Font.ITALIC, 14));
		text_details.setBackground(getBackground());
		text_details.setEditable(false);
		topRight.add(text_details);
		return topRight;
	}

	private JPanel createBottomRight() {
		JPanel bottomRight = new JPanel();
		bottomRight.setBorder(BorderFactory.createTitledBorder("Log"));
		text_log = new JTextArea(1,28);
		text_log.setBackground(getForeground());
		text_log.setFont(new Font("Serif", Font.ITALIC, 14));
		text_log.setText("");
		text_log.setEditable(false);
		bottomRight.add(text_log);
		return bottomRight;
	}

	/* Panel on the right half of the base container*/
	private JPanel createRightPanel() {
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
		return rightPanel;		
	}

	private JPanel createButtonPane() {
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane,BoxLayout.LINE_AXIS));

		buttonPane.add(button_cancel);
		buttonPane.add(Box.createHorizontalStrut(5));
		buttonPane.add(button_done);
		buttonPane.add(Box.createHorizontalStrut(5));
		if(this.member.getType().equals("admin")) {
			buttonPane.add(button_add);
		}

		buttonPane.add(button_logout);
		buttonPane.setBorder(BorderFactory.createEmptyBorder(10,5,5,5));

		return buttonPane;
	}

	private JPanel createLeftPanel(JPanel leftButtonPane) {
		JPanel leftPanel = new JPanel();
		//leftPanel.add(label_member_list);	
		leftPanel.setLayout(new BoxLayout(leftPanel,BoxLayout.Y_AXIS));

		listModel = new DefaultListModel<String>();

		// inflate listModel
		for(int j =0; j<recordList.size(); j++){
			listModel.addElement(recordList.get(j).getFullname());
		}
		if(recordList.size() !=0){
			text_details.setText(" Full Name: " + recordList.get(0).getFullname() +  "\n Email Adddress: " + recordList.get(0).getEmail() + "\n  PhoneNo: " + recordList.get(0).getPhoneNo() + "\n  DOB: " + recordList.get(0).getDob());
		}

		list = new JList<String>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(this.selectedIndex);
		list.addListSelectionListener(this);
		list.setVisibleRowCount(5);
		list.setToolTipText("Member List");

		JScrollPane listScrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		listScrollPane.setPreferredSize(new Dimension(200,250));

		leftPanel.add(listScrollPane);

		leftPanel.add(leftButtonPane);

		return leftPanel;
	}

	/* Panel on the left half of the base container*/
	private JPanel createLeftButtonPane() {
		JPanel leftButtonPane = new JPanel();
		leftButtonPane.setLayout(new BoxLayout(leftButtonPane,
				BoxLayout.LINE_AXIS));

		leftButtonPane.add(button_delete);
		leftButtonPane.add(Box.createHorizontalStrut(5));
		leftButtonPane.add(new JSeparator(SwingConstants.VERTICAL));
		leftButtonPane.add(button_edit);
		leftButtonPane.add(Box.createHorizontalStrut(5));
		leftButtonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		return leftButtonPane;
	}

	private JPanel createMainRightPanel(JPanel topRight, JPanel rightPanel, JPanel bottomRight) {
		JPanel mainRightPanel = new JPanel();
		mainRightPanel.setLayout(new BoxLayout(mainRightPanel,BoxLayout.Y_AXIS));
		mainRightPanel.add(topRight);

		mainRightPanel.add(rightPanel);

		mainRightPanel.add(bottomRight);

		JPanel buttonPane = createButtonPane();

		mainRightPanel.add(buttonPane);
		return mainRightPanel;
	}

	@Override	//This method is required by ListSelectionListener.
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {

			if (list.getSelectedIndex() == -1) {
				text_details.setText("");

			} else if(this.member.getMemberId()!=recordList.get(list.getSelectedIndex()).getMemberId() && !(this.member.getType().equals("admin"))) {
				hideIfMember();
				//pack();
			}
			else {
				button_cancel.setVisible(true);
				button_done.setVisible(true);
				button_edit.setVisible(true);
				if(this.member.getMemberId()!=recordList.get(list.getSelectedIndex()).getMemberId() && !(this.member.getType().equals("admin"))) {
					button_add.setVisible(true);
				}
				button_delete.setVisible(true);
				rightPanel.setVisible(true);
				bottomRight.setVisible(true);
				String member, email, phone, dob;
				member = recordList.get(list.getSelectedIndex()).getFullname();
				email = recordList.get(list.getSelectedIndex()).getEmail();
				phone = recordList.get(list.getSelectedIndex()).getPhoneNo();
				dob =recordList.get(list.getSelectedIndex()).getDob();
				text_details.setText(" Full Name: " + member +  "\n Email: " + email + "\n PhoneNo : " + phone + "\n DOB: " + dob);
				//pack();
			}
		}
	}

	class LogoutListner implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dispose();
			Client.main(null);
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

			if(newMember.compareTo(recordList.get(indexOfEditedMember)) == 0){

				newMember.setMemberId(recordList.get(indexOfEditedMember).getMemberId());
				recordList.get(indexOfEditedMember).setPhoneNo(newMember.getPhoneNo());
				recordList.get(indexOfEditedMember).setDob(newMember.getDob());

				ArrayList<Member> members = new ArrayList<Member>();

				members.add(recordList.get(indexOfEditedMember));

				ClientUtil.actionPerform(members, "update");

			}

			else{
				int index = ClientUtil.getIndex(recordList, newMember);

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

						ClientUtil.actionPerform(members, "update");


						listModel.add(index, newMember.getFullname());
						list.setSelectedIndex(index);
					}
					else{
						newMember.setMemberId(recordList.get(indexOfEditedMember).getMemberId());
						recordList.add(index, newMember);
						listModel.add(index, newMember.getFullname());

						ArrayList<Member> members = new ArrayList<Member>();

						members.add(recordList.get(index));

						ClientUtil.actionPerform(members, "update");

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

			resetTextFields();	 
			button_cancel.setEnabled(false);
			button_done.setEnabled(false);
			button_edit.setEnabled(true);
			text_log.setText("Successfully edited!");
			list.setEnabled(true);

		}
	}

	class CancelListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			resetTextFields(); 
			button_cancel.setEnabled(false);
			button_done.setEnabled(false);
			button_edit.setEnabled(true);
			text_log.setText("");
			list.setEnabled(true);
		}
	}

	class EditListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			text_log.setText("");
			if(listModel.size() ==0){
				return;
			}

			int index = list.getSelectedIndex();
			list.setEnabled(false);
			text_full_name.setText(recordList.get(index).getFullname());
			text_email.setText(recordList.get(index).getEmail());
			text_phone_num.setText(recordList.get(index).getPhoneNo());
			text_dob.setText(recordList.get(index).getDob());	 

			button_cancel.setEnabled(true);
			button_done.setEnabled(true);
			button_edit.setEnabled(false);

			text_full_name.requestFocusInWindow();

			text_log.setText("Select Done to save changes.");
		}
	}

	//This method can be called only if there's a valid selection to remove whatever's selected.
	class DeleteListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(listModel.size() ==0){
				return;
			}

			list.setEnabled(true);
			int index = list.getSelectedIndex();

			listModel.remove(index);

			ArrayList<Member> members = new ArrayList<Member>();
			members.add(recordList.get(index));

			ClientUtil.actionPerform(members, "delete");

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
			resetTextFields(); 
			button_cancel.setEnabled(false);
			button_done.setEnabled(false);
		}
	}

	//This listener is shared by the text field and the hire button.
	class AddListener implements ActionListener, DocumentListener {
		private JButton button;
		private boolean alreadyEnabled = false;

		public AddListener(JButton button) {
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

			int index = ClientUtil.getIndex(recordList, newMember);

			if (index ==-1) {
				Toolkit.getDefaultToolkit().beep();
				text_full_name.requestFocusInWindow();
				text_full_name.selectAll();
				text_log.setText("Member already exists in the db.");
				return;
			}

			ArrayList<Member> members = new ArrayList<Member>();
			newMember.setMemberId(new Random().nextInt(1000)+1);

			members.add(newMember);

			ClientUtil.actionPerform(members, "add");

			recordList.add(index, newMember);

			listModel.insertElementAt(newMember.getFullname(), index);
			//If we just wanted to add to the end, we'd use below statement: 
			//listModel.addElement(newMember.getFullname());

			//Reset the text field.
			text_full_name.requestFocusInWindow();
			resetTextFields();
			text_log.setText("Successfully added! ");
			list.setEnabled(true);
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

		@Override	//Required by DocumentListener.
		public void insertUpdate(DocumentEvent e) {
			ClientUtil.enableButton(button, alreadyEnabled);
		}

		@Override	//Required by DocumentListener.
		public void removeUpdate(DocumentEvent e) {
			handleEmptyTextField(e);
		}

		@Override	//Required by DocumentListener.
		public void changedUpdate(DocumentEvent e) {
			if (!handleEmptyTextField(e)) {
				ClientUtil.enableButton(button, alreadyEnabled);
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

	private void resetTextFields() {
		text_full_name.setText("");
		text_email.setText("");
		text_phone_num.setText("");
		text_dob.setText("");
	}

	private void hideIfMember() {
		if(this.member.getMemberId()!=recordList.get(list.getSelectedIndex()).getMemberId() && !(this.member.getType().equals("admin"))) {
			button_cancel.setVisible(false);
			button_done.setVisible(false);
			button_edit.setVisible(false);
			button_add.setVisible(false);
			button_delete.setVisible(false);
			rightPanel.setVisible(false);
			bottomRight.setVisible(false);

			String  member1= recordList.get(list.getSelectedIndex()).getFullname();
			String email = recordList.get(list.getSelectedIndex()).getEmail();
			String phone = recordList.get(list.getSelectedIndex()).getPhoneNo();
			String dob =recordList.get(list.getSelectedIndex()).getDob();
			text_details.setText(" Full Name: " + member1 +  "\n Email: " + email + "\n PhoneNo : " + phone + "\n DOB: " + dob);
			
		}
	}
}
