package uk.ac.babraham.trainflag;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;

import uk.ac.babraham.trainflag.client.ServerSelector;
import uk.ac.babraham.trainflag.server.TrainFlagServer;

public class TrainFlagApplication extends JFrame implements ActionListener {

	/**
	 * This is the main application which is just a selector say whether 
	 * you should run the server or the client.
	 */
	
	public TrainFlagApplication () {
		super("Welcome to TrainFlag");
		setIconImage(new ImageIcon(ClassLoader.getSystemResource("images/trainflag_logo.png")).getImage());

		getContentPane().setLayout(new GridLayout(1, 2));
		
		JButton serverButton = new JButton("Start Server");
		serverButton.setActionCommand("server");
		serverButton.addActionListener(this);
		getContentPane().add(serverButton);
		
		JButton clientButton = new JButton("Start Client");
		clientButton.setActionCommand("client");
		clientButton.addActionListener(this);
		getContentPane().add(clientButton);
		
		setSize(500,200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
	}
	
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}

		new TrainFlagApplication();
	}


	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("server")) {
			new TrainFlagServer();
		}
		else if (ae.getActionCommand().equals("client")) {
			new ServerSelector();
		}
		else {
			throw new IllegalArgumentException("Unknown action "+ae.getActionCommand());
		}
		
		setVisible(false);
	}

}
