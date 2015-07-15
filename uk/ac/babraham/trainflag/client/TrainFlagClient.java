package uk.ac.babraham.trainflag.client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.InetAddress;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import uk.ac.babraham.trainflag.client.network.ClientThreadListener;
import uk.ac.babraham.trainflag.client.ui.BlockButton;
import uk.ac.babraham.trainflag.resources.ColourScheme;
import uk.ac.babraham.trainflag.server.ClientInstance;

public class TrainFlagClient extends JFrame implements ActionListener, KeyListener, ClientThreadListener {

	private ServerInstaceForClient server;
	private JTextField nameField;
	
	private BlockButton [] buttons = new BlockButton[4];
	
	private int currentState = ClientInstance.STATE_READY;
		
	public TrainFlagClient (ServerInstaceForClient server) {
		
		this.server = server;
		
		// This object will already have been registered with the ClientThread by
		// the server selector so we don't need to do that here.
		
		getContentPane().setLayout(new BorderLayout());
		
		JPanel namePanel = new JPanel();
		namePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		
		namePanel.setLayout(new BorderLayout());
		namePanel.add(new JLabel("Your name: "),BorderLayout.WEST);
		nameField = new JTextField("[Enter your name please]");
		nameField.addKeyListener(this);
		namePanel.add(nameField,BorderLayout.CENTER);
		
		
		JPanel buttonPanel = new JPanel();
		
		buttonPanel.setLayout(new GridLayout(1,4));
		
		BlockButton readyButton = new BlockButton("<html><center>I'm<br>Ready</center></html>",ColourScheme.READY_COLOUR);
		readyButton.setActionCommand("ready");
		readyButton.addActionListener(this);
		buttonPanel.add(readyButton);
		buttons[0] = readyButton;
		
		BlockButton workingButton = new BlockButton("<html><center>I'm<br>Working</center></html>",ColourScheme.WORKING_COLOUR);
		workingButton.setActionCommand("working");
		workingButton.addActionListener(this);
		buttonPanel.add(workingButton);
		buttons[1] = workingButton;

		BlockButton finishedButton = new BlockButton("<html><center>I've<br>Finished</center></html>",ColourScheme.FINISHED_COLOUR);
		finishedButton.setActionCommand("finished");
		finishedButton.addActionListener(this);
		buttonPanel.add(finishedButton);
		buttons[2] = finishedButton;

		BlockButton helpButton = new BlockButton("<html><center>I'd like<br>some help</center></html>",ColourScheme.HELP_COLOUR);
		helpButton.setActionCommand("help");
		helpButton.addActionListener(this);
		buttonPanel.add(helpButton);
		buttons[3] = helpButton;
		
		getContentPane().add(namePanel,BorderLayout.NORTH);
		getContentPane().add(buttonPanel,BorderLayout.CENTER);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700,175);

		// Don't set a start location so we start in the top left corner, which is a pretty good place to be.
		buttons[0].mouseClicked(new MouseEvent(this, 1, 1, 0, 0, 0, 0, 0, 0, false, 0));
		setVisible(true);
		
	}
		
	public int currentState () {
		return currentState;
	}
	

	public void actionPerformed(ActionEvent ae) {
		try {

			// This must come from a button, so clear the selections from the ones
			// it's not from
			for (int b=0;b<buttons.length;b++) {
				if (!buttons[b].equals(ae.getSource())) {
					buttons[b].deselect();
				}
			}
			
			if (ae.getActionCommand().equals("help")) {
				server.sendCommand(new String [] {"CHANGE_STATE",""+ClientInstance.STATE_HELP});
				currentState = ClientInstance.STATE_HELP;
			}
			else if (ae.getActionCommand().equals("finished")) {
				server.sendCommand(new String [] {"CHANGE_STATE",""+ClientInstance.STATE_COMPLETE});
				currentState = ClientInstance.STATE_COMPLETE;
			}
			else if (ae.getActionCommand().equals("ready")) {
				server.sendCommand(new String [] {"CHANGE_STATE",""+ClientInstance.STATE_READY});
				currentState = ClientInstance.STATE_READY;
			}
			else if (ae.getActionCommand().equals("working")) {
				server.sendCommand(new String [] {"CHANGE_STATE",""+ClientInstance.STATE_WORKING});
				currentState = ClientInstance.STATE_WORKING;
			}
			else {
				throw new IllegalStateException("Unknown action "+ae.getActionCommand());
			}
			
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void keyPressed(KeyEvent ke) {}

	public void keyReleased(KeyEvent ke) {
		try {
			server.sendCommand(new String [] {"CHANGE_NAME",nameField.getText()});
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void keyTyped(KeyEvent ke) {}

	public void serverAnswered(InetAddress address, String courseName) {}

	public void changeState(int newState) {
		if (currentState == ClientInstance.STATE_HELP) return;
		
		if (newState == ClientInstance.STATE_READY) {
			actionPerformed(new ActionEvent(buttons[0], 0, "ready"));
			buttons[0].select();
		}

		else if (newState == ClientInstance.STATE_COMPLETE) {
			actionPerformed(new ActionEvent(buttons[2], 0, "complete"));
			buttons[2].select();
		}
		
		if (newState == ClientInstance.STATE_WORKING) {
			actionPerformed(new ActionEvent(buttons[1], 0, "working"));
			buttons[1].select();
		}

		if (newState == ClientInstance.STATE_HELP) {
			actionPerformed(new ActionEvent(buttons[3], 0, "help"));
			buttons[3].select();
		}

	
	}

	
	

}
