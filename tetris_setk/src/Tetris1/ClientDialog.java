package Tetris1;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ClientDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel label1 = new JLabel("Host:");
	private JTextField text1 = new JTextField(10);
	private JLabel label2 = new JLabel("Port:");
	private JTextField text2 = new JTextField(10);
	private JButton okButton = new JButton("Ok");
	private JButton cancelButton = new JButton("Cancel");
	private int port = 0;
	private String host;
	public enum Choice {
        NONE, OK, CANCEL
    }
    public Choice userChoice = Choice.NONE;
	public ClientDialog() {
		setModal(true);
		setTitle("서버 접속");
		setLayout(new GridLayout(3, 1));
		JPanel hostPanel = new JPanel();
		add(hostPanel);
		hostPanel.setLayout(new FlowLayout());
		hostPanel.add(label1);
		hostPanel.add(text1);
		
		JPanel portPanel = new JPanel();
		add(portPanel);
		portPanel.setLayout(new FlowLayout());
		portPanel.add(label2);
		portPanel.add(text2);
		
		JPanel buttonPanel = new JPanel();
		add(buttonPanel);
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		pack();
		text1.setText("127.0.0.1");
		text2.setText("5000");
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				userChoice = Choice.OK;
				host = text1.getText();
				port = Integer.parseInt(text2.getText());
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
	
	public String getHost() {
		return this.host;
	}
}
