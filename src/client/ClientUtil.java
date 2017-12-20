package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.JTextComponent;

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

class HintTextField extends JTextField implements FocusListener{

	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String hint;

    public HintTextField(final String hint){
        setHint(hint);
        super.addFocusListener(this);
    }

    public void setHint(String hint){
        this.hint = hint;
        HintTextFieldUI ui = new HintTextFieldUI(hint, true, getDisabledTextColor());
        setUI(ui);
    }

    public void focusGained(FocusEvent e){
        if(this.getText().length() == 0){
            super.setText("");
        }
    }

    public void focusLost(FocusEvent e){
        if(this.getText().length() == 0){
            setHint(hint);
        }
    }
}

class HintTextFieldUI extends BasicTextFieldUI implements FocusListener {

	private String hint;
	private boolean hideOnFocus;
	private Color color;

	public HintTextFieldUI(String hint, boolean hideOnFocus, Color color) {
		this.hint = hint;
		this.hideOnFocus = hideOnFocus;
		this.color = color;
	}

	@Override
	protected void paintSafely(Graphics g) {
		super.paintSafely(g);
		JTextComponent comp = getComponent();
		if(hint!=null && comp.getText().length() == 0 && (!(hideOnFocus && comp.hasFocus()))){
			if(color != null) {
				g.setColor(color);
			} else {
				g.setColor(comp.getForeground());              
			}
			int padding = (comp.getHeight() - comp.getFont().getSize())/2;
			g.drawString(hint, 2, comp.getHeight()-padding-1);          
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		if(hideOnFocus) {
			getComponent().repaint();
		}

	}
	@Override
	public void focusLost(FocusEvent e) {
		if(hideOnFocus) {
			getComponent().repaint();
		}
	}
	@Override
	protected void installListeners() {
		super.installListeners();
		getComponent().addFocusListener(this);
	}
	@Override
	protected void uninstallListeners() {
		super.uninstallListeners();
		getComponent().removeFocusListener(this);
	}
}
