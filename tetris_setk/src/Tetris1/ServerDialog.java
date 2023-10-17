package Tetris1;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ServerDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel label = new JLabel("Port:");
	private JTextField text = new JTextField(10);
	private JButton okButton = new JButton("Ok");
	private JButton cancelButton = new JButton("Cancel");
	private int port = 0;
	public enum Choice {
        NONE, OK, CANCEL
    }
    public Choice userChoice = Choice.NONE;
	public ServerDialog() {
		setModal(true);
		setTitle("서버 생성");
		setLayout(new FlowLayout());
		add(label);
		add(text);
		add(okButton);
		add(cancelButton);
		pack();
		text.setText("5000");
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				userChoice = Choice.OK;
				port = Integer.parseInt(text.getText());
				dispose();
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				userChoice = Choice.CANCEL;
				dispose();
			}
		});
	}
	
	public int getPortNumber() {
		return this.port;
	}
}
